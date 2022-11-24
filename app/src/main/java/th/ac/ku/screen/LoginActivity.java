package th.ac.ku.screen;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import javax.security.auth.login.LoginException;

import th.ac.ku.R;
import th.ac.ku.model.Customer;
import th.ac.ku.service.CommonUser;
import th.ac.ku.service.Http;
import th.ac.ku.service.LocalStorage;

public class LoginActivity extends AppCompatActivity {

    EditText emailField,passwordField ;
    Button loginBtn;
    LocalStorage localStorage ;
    ProgressBar progressBarLogin ;

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        localStorage = new LocalStorage(LoginActivity.this);
        emailField = findViewById(R.id.emailLoginField);
        passwordField = findViewById(R.id.pwdLoginField);
        loginBtn = findViewById(R.id.loginOnBtn);
        progressBarLogin = findViewById(R.id.progressBarlogin) ;
//
//        // TODO DEV ONLY
//        emailField.setText("jame@mail.com");
//        passwordField.setText("password");


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(emailField.getText().equals("") || passwordField.getText().equals("")){
                        Toast.makeText(getApplicationContext(),"Please insert email or password",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        sendLogin(emailField.getText().toString(),passwordField.getText().toString(),view);
                        progressBarLogin.setVisibility(ProgressBar.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void sendLogin(String email, String pwd, View view) throws JSONException {
        JSONObject params = new JSONObject();
        try{
            params.put("email",email);
            params.put("password",pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = "auth/login";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(LoginActivity.this,url);
                http.setMethod("POST");
                http.setData(data);
                http.send();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            try{
                                JSONObject respond = new JSONObject(http.getRespond());
                                String token = respond.getString("access_token");
                                localStorage.setToken(token);
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                if(!getRole()){
                                    getUser();
                                    startActivity(intent);
                                    progressBarLogin.setVisibility(ProgressBar.INVISIBLE);
                                    finish();
                                    Toast.makeText(getApplicationContext(),"เข้าสู่ระบบสำเร็จ",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            progressBarLogin.setVisibility(ProgressBar.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"อีเมลล์หรือรหัสผ่านไม่ถูกต้อง",
                                    Toast.LENGTH_LONG).show();
//                            hideKeyboardFrom(LoginActivity.this,view);
                        }
                    }
                });
            }
        }).start();
    }

    public void getUser(){
        String url = "auth/customerGetMe";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(LoginActivity.this,url);
                http.setMethod("POST");
                http.send();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            try{
                                JSONObject jsonObject = new JSONObject(http.getRespond());
                                localStorage.setUserId(jsonObject.getInt("id"));
                                CommonUser.currentCustomer = new Customer(jsonObject.getInt("id"),jsonObject.getString("name"),jsonObject.getString("phone"),
                                        jsonObject.getString("email"),jsonObject.getInt("isMembership"),jsonObject.getString("memService"),jsonObject.getInt("memCredit"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }).start();
    }

    public boolean getRole(){
        String url = "auth/me";
        Http http = new Http(LoginActivity.this,url);
        http.setMethod("POST");
        http.send();
        Integer code = http.getStatusCode();
        if(code >= 200 && code < 400){
            try {
                JSONObject jsonObject = new JSONObject(http.getRespond());
                if (!jsonObject.getString("realrole").equals("CUSTOMER")) {
                    getUser();
                    Intent intent = new Intent(LoginActivity.this, DeliListViewActivity.class);
                    startActivity(intent);
                    progressBarLogin.setVisibility(ProgressBar.INVISIBLE);
                    finish();
                    Toast.makeText(getApplicationContext(), "เข้าสู่ระบบรูปแบบพนักงาน", Toast.LENGTH_LONG).show();
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false ;
    }
}