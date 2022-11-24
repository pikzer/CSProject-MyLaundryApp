package th.ac.ku.screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import th.ac.ku.CustomSpinner;
import th.ac.ku.R;
import th.ac.ku.adapter.ServiceAdapter;
import th.ac.ku.inventory.ServiceData;
import th.ac.ku.model.ADS;
import th.ac.ku.model.Order;
import th.ac.ku.service.CommonUser;
import th.ac.ku.service.Http;

public class AddOrderActivity extends AppCompatActivity implements CustomSpinner.OnSpinnerEventsListener, AdapterView.OnItemSelectedListener {

    Spinner addressSpinner, payMethodSpinner,periodSpinner ;
    CustomSpinner serviceSpinner;
    Button makeOrderBtn ;
    EditText datePickField;
    ServiceAdapter adapter ;
    ProgressBar progressBar;

    String payMethodMem[] = {"เงินสด", "พร้อมเพย์","สมาชิก"};
    String payMethodDe[] = {"เงินสด", "พร้อมเพย์"} ;
;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        serviceSpinner = findViewById(R.id.serviceSpinner);
        addressSpinner = findViewById(R.id.addressSpinner);
        payMethodSpinner = findViewById(R.id.payMethodSpinner);
        periodSpinner = findViewById(R.id.periodSpinner);
        makeOrderBtn = findViewById(R.id.makeOrderBtn);
        datePickField = findViewById(R.id.datePickField);
        serviceSpinner.setSpinnerEventsListener(this);
        periodSpinner.setOnItemSelectedListener(this);
        addressSpinner.setOnItemSelectedListener(this);
        payMethodSpinner.setOnItemSelectedListener(this);
//        progressBar.findViewById(R.id.progressbarorder);

        if(CommonUser.currentCustomer.getIsMembership()==1){
            ArrayAdapter ad = new ArrayAdapter(AddOrderActivity.this,android.R.layout.simple_spinner_item,payMethodMem);
            ad.setDropDownViewResource(
                    android.R.layout
                            .simple_spinner_dropdown_item);
            payMethodSpinner.setAdapter(ad);
        }
        else{
            ArrayAdapter ad = new ArrayAdapter(AddOrderActivity.this,android.R.layout.simple_spinner_item,payMethodDe);
            ad.setDropDownViewResource(
                    android.R.layout
                            .simple_spinner_dropdown_item);
            payMethodSpinner.setAdapter(ad);
        }


        adapter = new ServiceAdapter(AddOrderActivity.this, ServiceData.getServiceList());
        serviceSpinner.setAdapter(adapter);
        datePickField.setFocusable(View.NOT_FOCUSABLE);
        datePickField.isCursorVisible();

        ArrayList<ADS> adsArrayList = getUserAddress();
        Log.e("xxx",String.valueOf(adsArrayList.size()));
        ArrayList<String> adsName = new ArrayList<>();
        if(adsArrayList.size()==0){
            Toast.makeText(AddOrderActivity.this,"กรุณาเพิ่มที่อยู่ก่อนทำรายการนัดหมาย",Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        else{
            for (int i = 0; i < adsArrayList.size(); i++) {
               adsName.add( adsArrayList.get(i).getName());
            }
            String adds [] = adsName.toArray(new String[0]);
            ArrayAdapter ad = new ArrayAdapter(AddOrderActivity.this,android.R.layout.simple_spinner_item,adds);
            ad.setDropDownViewResource(
                    android.R.layout
                            .simple_spinner_dropdown_item);
            addressSpinner.setAdapter(ad);
        }


        datePickField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DecimalFormat f = new DecimalFormat("00");
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddOrderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                datePickField.setText(year+"-"+(monthOfYear+1)+"-"+f.format(dayOfMonth));
                                String date = datePickField.getText().toString();
                                String[]s= getPeriodTime(date).toArray(new String[0]);
                                if(s.length==0){
                                    Toast.makeText(getApplicationContext(), "This date is fully reserve.",
                                            Toast.LENGTH_LONG).show();
                                    datePickField.setText(null);
                                }
                                if(datePickField.getText()!=null){
                                    getPeriodTime(date);
                                    ArrayAdapter ad = new ArrayAdapter(AddOrderActivity.this,android.R.layout.simple_spinner_item,s);
                                    ad.setDropDownViewResource(
                                            android.R.layout
                                                    .simple_spinner_dropdown_item);
                                    periodSpinner.setAdapter(ad);
                                 }
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(1));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(7));
                datePickerDialog.show();
            }
        });

        makeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String service = "";
                String period = "";
                String paymet = "";
                paymet = payMethodSpinner.getSelectedItem().toString();
                period = periodSpinner.getSelectedItem().toString();

//                Log.e("xxx",serviceSpinner.getSelectedItem().toString());
//                Log.e("xxx",datePickField.getText().toString());
//                Log.e("xxx",periodSpinner.getSelectedItem().toString());
//                Log.e("xxx",addressSpinner.getSelectedItem().toString());
//                Log.e("xxx",payMethodSpinner.getSelectedItem().toString());

                if ("0".equals(serviceSpinner.getSelectedItem().toString())) {
                    service = "ซักอบ";
                } else if ("1".equals(serviceSpinner.getSelectedItem().toString())) {
                    service = "ซักรีด";
                }else if("2".equals(serviceSpinner.getSelectedItem().toString())){
                    service = "ซักแห้ง";
                }else if("3".equals(serviceSpinner.getSelectedItem().toString())){
                    service = "รีด";
                }
//                if(periodSpinner.getSelectedItem().toString().equals("Morning")){
//                    period = "ช่วงเช้า";
//                }else if(periodSpinner.getSelectedItem().toString().equals("Afternoon")){
//                    period = "ช่วงบ่าย";
//                }else if(periodSpinner.getSelectedItem().toString().equals("Evening")){
//                    period = "ช่วงเย็น";
//                }
//                if(payMethodSpinner.getSelectedItem().toString().equals("Cash")){
//                    paymet = "เงินสด" ;
//                }else if(payMethodSpinner.getSelectedItem().toString().equals("Prompt pay")){
//                    paymet = "พร้อมเพย์";
//                }else if(payMethodSpinner.getSelectedItem().toString().equals("Membership")){
//                    paymet = "สมาชิก" ;
//                }
                Order order = new Order(service,datePickField.getText().toString(),
                        period,
                        adsArrayList.get(addressSpinner.getSelectedItemPosition()).getuCode(),
                        paymet);
                sendOrder(order);
            }
        });



    }


    public ArrayList<String> getPeriodTime(String date) {
        ArrayList<String> avaTime = new ArrayList<>();
        JSONObject params = new JSONObject();
        try {
            params.put("deli_date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = "delivery-time/getAvailableInDateTime";

        Http http = new Http(AddOrderActivity.this, url);
        http.setMethod("PUT");
        http.setData(data);
        http.send();
        Log.e("xxx",http.getRespond());
        Integer code = http.getStatusCode();
        if (code == 200) {
            try {
                JSONObject respond = new JSONObject(http.getRespond());
                Log.e("xxx", String.valueOf(respond.getBoolean("morning")));
                if (String.valueOf(respond.getBoolean("morning")).equals("true")) {
                    avaTime.add("ช่วงเช้า");
                }
                if (String.valueOf(respond.getBoolean("after")).equals("true")) {
                    avaTime.add("ช่วงบ่าย");
                }
                if (String.valueOf(respond.getBoolean("even")).equals("true")) {
                    avaTime.add("ช่วงเย็น");
                }
                return avaTime;
            } catch (JSONException e) {
                return avaTime;
            }

        }
        else {
            Toast.makeText(getApplicationContext(), "Invalid Date",
                    Toast.LENGTH_LONG).show();
            avaTime.add("else");
            return avaTime;
        }
    }

    public ArrayList<ADS> getUserAddress(){
        ArrayList<ADS> aDsArrayList = new ArrayList<ADS>();
        String url = "customers"+"/getCustomerAddressAuth";
        Http http = new Http(AddOrderActivity.this,url);
        http.setMethod("GET");
        http.send();
        Integer code = http.getStatusCode();
        if(code >=200 && code < 400){
            try{
                JSONArray respond = new JSONArray(http.getRespond());
                for (int i = 0; i < respond.length(); i++) {
                    if(!respond.getJSONObject(i).get("name").toString().equals("null")){
                        aDsArrayList.add(new ADS(respond.getJSONObject(i).getInt("id"),
                                respond.getJSONObject(i).getString("name"),
                                respond.getJSONObject(i).getString("u_code")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else{
            Toast.makeText(getApplicationContext(),"มีบางอย่างผิดพลาด",
                    Toast.LENGTH_LONG).show();
        }
        return aDsArrayList;
    }


    @Override
    public void onPopupWindowOpened(Spinner spinner) {

    }

    @Override
    public void onPopupWindowClosed(Spinner spinner) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void sendOrder(Order order){
        JSONObject params = new JSONObject();
        try{
            params.put("service",order.getService());
            params.put("pick_date",order.getPickDate());
            params.put("pick_time",order.getPickTime());
            params.put("address",order.getAddress());
            params.put("pay_method",order.getPayMethod());
            params.put("status","เพิ่มการนัดหมาย");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = "orders/storeWithPhone";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(AddOrderActivity.this,url);
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
                                Intent intent = new Intent(AddOrderActivity.this,HomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"เพิ่มรายการนัดหมายสำเร็จ",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"มีบางอย่างผิดพลาด",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();
    }
}