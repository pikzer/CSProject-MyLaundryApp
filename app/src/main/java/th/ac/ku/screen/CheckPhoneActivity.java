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

public class CheckPhoneActivity extends AppCompatActivity {

    EditText phoneFieldCheck ;
    Button checkPhoneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone);

        phoneFieldCheck = findViewById(R.id.phoneFieldCheck);
        checkPhoneBtn = findViewById(R.id.checkPhoneBtn) ;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        checkPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneFieldCheck.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกเบอร์โทร",
                            Toast.LENGTH_LONG).show();
                }
                else if(!Validator.isPhoneNumber(phoneFieldCheck.getText().toString())){
                    Toast.makeText(getApplicationContext(),"เบอร์โทรไม่ถูกต้อง",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    getOldCustomerId(phoneFieldCheck.getText().toString(),view);
                }
            }
        });

    }

    public void getOldCustomerId(String phone, View view){
        JSONObject params = new JSONObject();
        try{
            params.put("phone",phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = "auth/getOldCustomerId";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(CheckPhoneActivity.this,url);
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
                                Intent intent = new Intent(CheckPhoneActivity.this,SignupActivity.class);
                                intent.putExtra("name",respond.getString("name"));
                                intent.putExtra("phone",phone);
                                intent.putExtra("id",respond.getInt("user"));
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(code == 201){
                            Intent intent = new Intent(CheckPhoneActivity.this,SignupActivity.class);
                            intent.putExtra("phone",phone);
                            startActivity(intent);
                            finish();
                        }
                        else if(code == 202){
                            Toast.makeText(getApplicationContext(),"เบอร์โทรนี้ถูกใช้งานแล้ว",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"เบอร์โทรไม่ถูกต้อง",
                                    Toast.LENGTH_LONG).show();
                            hideKeyboardFrom(CheckPhoneActivity.this,view);
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