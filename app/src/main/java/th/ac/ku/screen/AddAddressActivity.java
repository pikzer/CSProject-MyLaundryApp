package th.ac.ku.screen;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import th.ac.ku.service.CommonUser;
import th.ac.ku.service.Http;
import th.ac.ku.service.LocalStorage;

public class AddAddressActivity extends AppCompatActivity {


    Button chooseFromMapBtn, newAddressBtn ;
    EditText addressNameField, conNameField, conPhoneField,addressField,hintField;
    Double lat,lng;
    String ucode ;
    ProgressBar progressBar;




    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null && result.getResultCode()==RESULT_OK){
                if(result.getData() != null && result.getData().getStringExtra(MapsActivity.KEY_NAME) != null){
                    addressField.setFocusable(View.FOCUSABLE);
                    String[] s = result.getData().getStringExtra(MapsActivity.KEY_NAME).split("\\|");
                    addressField.setText(s[0]);
                    lat = Double.parseDouble(s[1]);
                    lng = Double.parseDouble(s[2]);
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LocalStorage localStorage = new LocalStorage(AddAddressActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        chooseFromMapBtn = findViewById(R.id.chooseFromMapBtn);
        newAddressBtn = findViewById(R.id.newAddressBtn);
        addressField = findViewById(R.id.addressField);
        conNameField = findViewById(R.id.conNameField);
        conPhoneField = findViewById(R.id.conPhoneField);
        addressNameField = findViewById(R.id.addressNameField);
        hintField = findViewById(R.id.hintField);
        progressBar= findViewById(R.id.progressBar);


//        addressField.setFocusable(View.NOT_FOCUSABLE);

        chooseFromMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAddressActivity.this,MapsActivity.class);
                startForResult.launch(intent);
            }
        });

        newAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addressField.getText().toString().equals("") || addressNameField.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please insert address name and detail.",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    addAddress(addressNameField.getText().toString(),
                            addressField.getText().toString(),hintField.getText().toString(),
                            conNameField.getText().toString(),conPhoneField.getText().toString());
                }
            }
        });
    }

    public void addAddress(String name, String address,String hint,String conName,String conPhone){
        JSONObject params = new JSONObject();
        try{
            params.put("name",name);
            params.put("u_code",address);
            params.put("lat",lat);
            params.put("lng",lng);
            params.put("hint",hint);
            params.put("contact",conName + " " + conPhone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = "address";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(AddAddressActivity.this,url);
                http.setMethod("POST");
                http.setData(data);
                http.send();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        Log.d("xxx",http.getRespond());
                        if(code >= 200 && code < 400){
                            try{
                                JSONObject respond = new JSONObject(http.getRespond());
                                Intent intent = new Intent(AddAddressActivity.this,HomeActivity.class);
                                progressBar.setVisibility(ProgressBar.INVISIBLE);

                                Toast.makeText(AddAddressActivity.this,"เพิ่มที่อยู่ใหม่สำเร็จ",Toast.LENGTH_LONG).show();
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"มีบางอย่างผิดพลาด",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();
    }


}