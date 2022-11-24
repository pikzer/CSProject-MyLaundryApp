package th.ac.ku.screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import th.ac.ku.R;
import th.ac.ku.service.Http;
import th.ac.ku.service.Validator;

public class SignupActivity extends AppCompatActivity {

    EditText nameSignupField, emailSignupField, phoneSignupField, pwdSignupField, conPwdSignupField ;
    Button signupSBtn;
    ProgressBar progressBarSignup;

    Integer id ;
    String name ;
    String phone ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Bundle extras = getIntent().getExtras();

        nameSignupField = findViewById(R.id.nameSignupField);
        emailSignupField = findViewById(R.id.emailSignupField);
        phoneSignupField = findViewById(R.id.phoneSignupField);
        pwdSignupField = findViewById(R.id.pwdSignupField);
        conPwdSignupField = findViewById(R.id.conPwdSignupField);
        signupSBtn = findViewById(R.id.signupSBtn);
        progressBarSignup = findViewById(R.id.progressBarSignup);

        if(extras != null){
                id = extras.getInt("id");
                name = extras.getString("name");
                nameSignupField.setText(name);
                phone = extras.getString("phone");
                phoneSignupField.setText(phone);
                phoneSignupField.setFocusable(View.NOT_FOCUSABLE);
        }

        signupSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameSignupField.getText().toString().equals("") ||
                        emailSignupField.getText().toString().equals("") ||
                        phoneSignupField.getText().toString().equals("") ||
                        pwdSignupField.getText().toString().equals("")   ||
                        conPwdSignupField.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูล.",
                            Toast.LENGTH_LONG).show();
                }
                else if(!Validator.isValidEmail(emailSignupField.getText().toString())){
                    Toast.makeText(getApplicationContext(),"อีเมล์ไม่ถูกต้อง",
                            Toast.LENGTH_LONG).show();
                }
                else if(!Validator.isPhoneNumber(phoneSignupField.getText().toString())){
                    Toast.makeText(getApplicationContext(),"เบอร์โทรไม่ถูกต้อง",
                            Toast.LENGTH_LONG).show();
                }
                else if(!pwdSignupField.getText().toString().equals(conPwdSignupField.getText().toString())){
                    Toast.makeText(getApplicationContext(),"รหัสผ่านยืนยันไม่ตรงกัน",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    if(id == 0){
                        sendRegister(nameSignupField.getText().toString(), emailSignupField.getText().toString(),
                                phoneSignupField.getText().toString(), pwdSignupField.getText().toString(), view);
                    }
                    else{
                        sendRegisterOld(emailSignupField.getText().toString(),
                                pwdSignupField.getText().toString(),view);
                    }

                }
            }
        });
    }

    public void sendRegister(String name, String email, String phone, String pwd, View view){
        JSONObject params = new JSONObject();
        try{
            params.put("name",name);
            params.put("phone",phone);
            params.put("email",email);
            params.put("password",pwd);
            params.put("role","CUSTOMER");
            params.put("realrole","CUSTOMER");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = "auth/register";
        progressBarSignup.setVisibility(ProgressBar.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(SignupActivity.this,url);
                http.setMethod("POST");
                http.setData(data);
                http.send();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        Log.d("xxx",http.getRespond());
                        if(code == 201){
                            try{
                                JSONObject respond = new JSONObject(http.getRespond());
                                progressBarSignup.setVisibility(ProgressBar.INVISIBLE);
                                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                                Toast.makeText(getApplicationContext(),"ลงทะเบียนผู้ใช้งานสำเร็จ",
                                        Toast.LENGTH_LONG).show();
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"มีบางอย่างผิดพลาด",
                                    Toast.LENGTH_LONG).show();
                            progressBarSignup.setVisibility(ProgressBar.INVISIBLE);
                            hideKeyboardFrom(SignupActivity.this,view);
                        }
                    }
                });
            }
        }).start();
    }

    public void sendRegisterOld(String email, String pwd, View view){
        JSONObject params = new JSONObject();
        try{
            params.put("email",email);
            params.put("password",pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = "customers/"+id+"/registerOldCustomer";
        progressBarSignup.setVisibility(ProgressBar.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(SignupActivity.this,url);
                http.setMethod("PUT");
                http.setData(data);
                http.send();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(true){
                            Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                            Toast.makeText(getApplicationContext(),"ลงทะเบียนผู้ใช้งานสำเร็จ",
                                    Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"มีบางอย่างผิดพลาด",
                                    Toast.LENGTH_LONG).show();
                            progressBarSignup.setVisibility(ProgressBar.INVISIBLE);
                            hideKeyboardFrom(SignupActivity.this,view);
                        }
                    }
                });
            }
        }).start();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}