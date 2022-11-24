package th.ac.ku.screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import th.ac.ku.R;
import th.ac.ku.adapter.Adapter;
import th.ac.ku.adapter.ClothListAdapter;
import th.ac.ku.model.ADS;
import th.ac.ku.model.ClothListRecycle;
import th.ac.ku.model.Order;
import th.ac.ku.model.PreviewClothList;
import th.ac.ku.service.CommonUser;
import th.ac.ku.service.Http;

public class OrderInfoActivity extends AppCompatActivity implements ClothListAdapter.OnNoteListener {
    Integer orId ;

    TextView orderIdTv, serviceTv, statusTv,pickAtTv,deliAtTv, piecesTv,totalTv ;
    EditText editTextTextMultiLine;
    RecyclerView clothListRecycle;
    ImageView imageView ;
//    Button authenQRBtn;
    Order order;
    LinearLayoutManager layoutManager ;
    int pieces = 0;



    ArrayList<ClothListRecycle> clothListRecycles ;
    ArrayList<Integer> clIdLists ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        Bundle extra = getIntent().getExtras();
        orId  = extra.getInt("orderId") ;
        orderIdTv = findViewById(R.id.orderIdTv);
        serviceTv = findViewById(R.id.serviceTv);
        statusTv = findViewById(R.id.statusTv);
        pickAtTv = findViewById(R.id.pickAtTv);
        deliAtTv = findViewById(R.id.deliAtTv);
        piecesTv = findViewById(R.id.piecesTv);
        totalTv = findViewById(R.id.totalTv);
        imageView = findViewById(R.id.imageView6);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
        editTextTextMultiLine.setFocusable(View.NOT_FOCUSABLE);
        clothListRecycle = findViewById(R.id.clothListRecycle);
//        authenQRBtn = findViewById(R.id.authenQRBtn);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        clIdLists = new ArrayList<>();
        clothListRecycles = new ArrayList<>();
        getOrder();
        DecimalFormat f = new DecimalFormat("#0.00");
        totalTv.setText(f.format(order.getTotal())+ "฿");
        orderIdTv.setText(order.getName());
        serviceTv.setText(order.getService());
        statusTv.setText(order.getStatus());
        piecesTv.setText(String.valueOf(pieces)+" ชิ้น");
        if(order.getPickDate() != null){
            pickAtTv.setText(order.getPickDate() + "  " + order.getPickTime());
            if(pickAtTv.getText().equals("null  ")){
                pickAtTv.setText("");
            }
        }
        if(order.getDeliDate() != null){
            deliAtTv.setText(order.getDeliDate() + "  " + order.getDeliTime());
            if(deliAtTv.getText().equals("null  ")){
                deliAtTv.setText("");
            }
        }
        if(order.getAddress() != null){
            editTextTextMultiLine.setText(order.getAddress());
        }
        if(order.getStatus().equals("เพิ่มรายการ")){
            imageView.setImageResource(R.drawable.order_in_icon);
        }
        if(order.getStatus().equals("เพิ่มการนัดหมาย")){
            imageView.setImageResource(R.drawable.order_add_icon);
        }
        if(order.getStatus().equals("กำลังดำเนินการ")){
            imageView.setImageResource(R.drawable.in_progress_icon);
        }

        if(order.getStatus().equals("ยืนยันการนัดหมาย")){
            imageView.setImageResource(R.drawable.orde_con);
        }

        if(order.getStatus().equals("รับผ้า")){
            imageView.setImageResource( R.drawable.pickup_ic);
        }
        if(order.getStatus().equals("ส่งผ้า")){
            imageView.setImageResource( R.drawable.go_out_deli);
        }

        if(order.getStatus().equals("เสร็จสิ้น")){
            imageView.setImageResource( R.drawable.complete_ic);
        }

        if(order.getStatus().equals("ยกเลิก")){
            imageView.setImageResource( R.drawable.cancel_icon);
        }





        Collections.reverse(clothListRecycles);
        clothListRecycle = findViewById(R.id.clothListRecycle) ;
        layoutManager = new LinearLayoutManager(this) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        clothListRecycle.setLayoutManager(layoutManager);
        ClothListAdapter clothListAdapter = new ClothListAdapter(clothListRecycles,this::onNoteClick);
        clothListRecycle.setAdapter(clothListAdapter);
        clothListAdapter.notifyDataSetChanged();

//        authenQRBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }




    public void getOrder(){
        String url = "orders/"+ orId ;
        Http http = new Http(OrderInfoActivity.this,url);
        http.setMethod("GET");
        http.send();
        Integer code = http.getStatusCode();
        Log.e("xxx",http.getRespond());
        if(code >=200 && code < 400){
            try{
                JSONObject jsonObject = new JSONObject(http.getRespond());
                order = new Order(jsonObject.getInt("id"),
                        jsonObject.getString("cus_phone"),
                        jsonObject.getString("service"),
                        jsonObject.getString("name"),
                        jsonObject.get("pick_date").toString(),
                        jsonObject.get("pick_time").toString(),
                        jsonObject.get("deli_date").toString(),
                        jsonObject.get("deli_time").toString(),
                        jsonObject.get("address").toString(),
                        jsonObject.get("responder").toString(),
                        jsonObject.get("deliver").toString(),
                        jsonObject.getInt("pay_status"),
                        jsonObject.getString("pay_method"),
                        jsonObject.getDouble("pick_ser_charge"),
                        jsonObject.getDouble("deli_ser_charge"),
                        jsonObject.getDouble("total"),
                        jsonObject.getString("status"),
                        jsonObject.getInt("is_membership_or"));
                JSONArray jsonArray = jsonObject.optJSONArray("cloth_lists");
                for (int i = 0; i < jsonArray.length(); i++) {
                    clIdLists.add(jsonArray.getJSONObject(i).getInt("id"));
                    getClothListPreview(clIdLists.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else{
            Toast.makeText(getApplicationContext(),"Something went wrong",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void getClothListPreview(int clid){
        JSONObject params = new JSONObject();
        try{
            params.put("cl_id",clid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = "orders/getPreviewClothList";

        Http http = new Http(OrderInfoActivity.this,url);
        http.setMethod("POST");
        http.setData(data);
        http.send();
        Integer code = http.getStatusCode();
        DecimalFormat f = new DecimalFormat("#0.00");
        if(code >= 200 && code < 400){
            try{
                JSONObject respond = new JSONObject(http.getRespond());
                pieces += respond.getInt("quantity");
                clothListRecycles.add(new ClothListRecycle(clid,
                        respond.getString("service"),
                        respond.getString("clothType"),
                        String.valueOf(respond.getInt("quantity")),
                        f.format(respond.getDouble("amount"))
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Something went wrong",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNoteClick(int pos) {

    }
}