package th.ac.ku.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import th.ac.ku.model.Customer;
import th.ac.ku.service.CommonUser;
import th.ac.ku.service.Http;
import th.ac.ku.service.LocalStorage;

public class SplashScreen extends AppCompatActivity {

    LocalStorage localStorage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localStorage = new LocalStorage(SplashScreen.this);
        if(!localStorage.getToken().equals("")){
            if(!getRole()){
                String url = "auth/customerGetMe";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Http http = new Http(SplashScreen.this,url);
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
                                        Intent intent = new Intent(SplashScreen.this,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
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
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }
            }, 2000);
        }
    }

    public boolean getRole(){
        String url = "auth/me";
        Http http = new Http(SplashScreen.this,url);
        http.setMethod("POST");
        http.send();
        Integer code = http.getStatusCode();
        if(code >= 200 && code < 400){
            try {
                JSONObject jsonObject = new JSONObject(http.getRespond());
                if (!jsonObject.getString("realrole").equals("CUSTOMER")) {
                    Intent intent = new Intent(SplashScreen.this, DeliListViewActivity.class);
                    startActivity(intent);
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