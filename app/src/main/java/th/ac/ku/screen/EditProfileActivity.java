package th.ac.ku.screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import th.ac.ku.R;
import th.ac.ku.model.Customer;
import th.ac.ku.service.CommonUser;
import th.ac.ku.service.Http;
import th.ac.ku.service.LocalStorage;
import th.ac.ku.service.Validator;

public class EditProfileActivity extends AppCompatActivity {


    EditText nameEditField, emailEditField, phoneEditField ;
    Button editProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        nameEditField = findViewById(R.id.nameEditField);
        emailEditField = findViewById(R.id.emailEditField);
        phoneEditField = findViewById(R.id.phoneEditField);
        editProfileBtn = findViewById(R.id.editProfileBtn);

        nameEditField.setText(CommonUser.currentCustomer.getName());
        emailEditField.setText(CommonUser.currentCustomer.getEmail());
        phoneEditField.setText(CommonUser.currentCustomer.getPhone());

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEditField.getText().equals("") || emailEditField.getText().equals("") || phoneEditField.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูล",
                            Toast.LENGTH_LONG).show();
                }
                if(!Validator.isPhoneNumber(phoneEditField.getText().toString())
                        || !Validator.isValidEmail(emailEditField.getText().toString())){
                    Toast.makeText(getApplicationContext(),"ข้อมูลไม่ถูกต้อง",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    updateUser(nameEditField.getText().toString(),
                            emailEditField.getText().toString(),phoneEditField.getText().toString());
                }
            }
        });
    }

    public void updateUser(String name,String email,String phone){
        JSONObject params = new JSONObject();
        try{
            params.put("name",name);
            params.put("email",email);
            params.put("phone",phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = "customers/"+CommonUser.currentCustomer.getId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(EditProfileActivity.this,url);
                http.setMethod("PATCH");
                http.setData(data);
                http.send();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        Log.e("xxx",http.getRespond());
                        if(code >= 200 && code < 400){
                            try{
                                JSONObject respond = new JSONObject(http.getRespond());
                                Toast.makeText(getApplicationContext(),"แก้ไขข้อมูลสำเร็จ",
                                        Toast.LENGTH_LONG).show();
                                updateLocalUser();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Something went wrong",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();
    }

    public void updateLocalUser(){
        LocalStorage localStorage ;
        localStorage = new LocalStorage(EditProfileActivity.this);
        String url = "auth/customerGetMe";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(EditProfileActivity.this,url);
                http.setMethod("POST");
                http.send();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            try{
                                JSONObject jsonObject = new JSONObject(http.getRespond());
                                CommonUser.currentCustomer = new Customer(jsonObject.getInt("id"),jsonObject.getString("name"),jsonObject.getString("phone"),
                                        jsonObject.getString("email"),jsonObject.getInt("isMembership"),jsonObject.getString("memService"),jsonObject.getInt("memCredit"));
                                localStorage.setUserId(jsonObject.getInt("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }).start();
    }
}