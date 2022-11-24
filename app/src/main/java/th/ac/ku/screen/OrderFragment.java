package th.ac.ku.screen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import th.ac.ku.R;
import th.ac.ku.adapter.Adapter;
import th.ac.ku.model.ADS;
import th.ac.ku.model.AddressRecycle;
import th.ac.ku.model.Order;
import th.ac.ku.model.OrderRecycle;
import th.ac.ku.service.CommonUser;
import th.ac.ku.service.Http;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment implements Adapter.OnNoteListener {

    View view ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    ArrayList<OrderRecycle> orderRecycleArrayList ;

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    SwipeRefreshLayout swipeRefreshLayout ;
    RecyclerView recyclerView ;
    Adapter adapter ;
    LinearLayoutManager layoutManager ;
    Button showAllBtn, showInProBtn, showComBtn;
    int filter ;
    ArrayList<OrderRecycle> filteredRecycle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         view = inflater.inflate(R.layout.fragment_order, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        showAllBtn = view.findViewById(R.id.showAllBtn);
        showInProBtn = view.findViewById(R.id.showInProBtn);
        showComBtn = view.findViewById(R.id.showComBtn);
        sendRequestOrder();
        // TODO
        filterInPro();
        initRecyclerview(filteredRecycle);
        // -------------------------
        showInProBtn.setBackgroundColor(getResources().getColor(R.color.btnDis));
        filter = 0;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                sendRequestOrder();
                showInProBtn.performClick();
            }
        });

        showAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllBtn.setBackgroundColor(getResources().getColor(R.color.btnDis));
                showInProBtn.setBackgroundColor(getResources().getColor(R.color.bg));
                showComBtn.setBackgroundColor(getResources().getColor(R.color.bg));
                initRecyclerview(orderRecycles);
            }
        });

        showInProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllBtn.setBackgroundColor(getResources().getColor(R.color.bg));
                showInProBtn.setBackgroundColor(getResources().getColor(R.color.btnDis));
                showComBtn.setBackgroundColor(getResources().getColor(R.color.bg));
                filterInPro();
                initRecyclerview(filteredRecycle);
            }
        });

        showComBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllBtn.setBackgroundColor(getResources().getColor(R.color.bg));
                showInProBtn.setBackgroundColor(getResources().getColor(R.color.bg));
                showComBtn.setBackgroundColor(getResources().getColor(R.color.btnDis));
                filterCom();
                initRecyclerview(filteredRecycle);
            }
        });



        return view;
    }

    public void filterInPro(){
        filteredRecycle = new ArrayList<>();
        for (OrderRecycle or:orderRecycles) {
            if(or.getStatusNow_tv().equals("กำลังดำเนินการ")){
                filteredRecycle.add(or);
            }
            else if(or.getStatusNow_tv().equals("เพิ่มการนัดหมาย")){
                filteredRecycle.add(or);
            }
            else if(or.getStatusNow_tv().equals("ยืนยันการนัดหมาย")){
                filteredRecycle.add(or);
            }
            else if(or.getStatusNow_tv().equals("ส่งผ้า")){
                filteredRecycle.add(or);
            }
            else if(or.getStatusNow_tv().equals("รับผ้า")){
                filteredRecycle.add(or);
            }
            else if(or.getStatusNow_tv().equals("เพิ่มรายการ")){
                filteredRecycle.add(or);
            }
        }
    }

    public void filterCom(){
        filteredRecycle = new ArrayList<>();
        for (OrderRecycle or:orderRecycles) {
            if(or.getStatusNow_tv().equals("เสร็จสิ้น")){
                filteredRecycle.add(or);
            }
            if(or.getStatusNow_tv().equals("ยกเลิก")){
                filteredRecycle.add(or);
            }
        }
    }


    ArrayList<OrderRecycle>orderRecycles = new ArrayList<>();


    public void sendRequestOrder(){
        orderRecycles = new ArrayList<>();
        String url = "orders/getOrders";
        Http http = new Http(getActivity(),url);
        http.setMethod("POST");
        http.send();
        Integer code = http.getStatusCode();
        Log.d("xxx",http.getRespond());
        if(code >= 200 && code < 400){
            try{
                JSONArray respond = new JSONArray(http.getRespond());
                String adsName = "" ;
                String pickOrDeTv ="";
                String dateTime = "";
                int image = 0;
                for (int i = 0; i < respond.length(); i++) {

                    if(!respond.getJSONObject(i).get("address").toString().equals("")){
                        if(respond.getJSONObject(i).getString("name").contains("ORS")){
                            adsName = respond.getJSONObject(i).get("address").toString().subSequence(0,20).toString()+"...";
                            pickOrDeTv ="ส่งผ้าเมื่อ: ";
                            dateTime = respond.getJSONObject(i).getString("deli_date")+ "  " + respond.getJSONObject(i).getString("deli_time");
//                            for (ADS a: getUserAddress()) {
//                                if(respond.getJSONObject(i).getString("status").equals("กำลังดำเนินการ") ||
//                                        respond.getJSONObject(i).getString("status").equals("เพิ่มรายการ") ||
//                                        respond.getJSONObject(i).getString("status").equals("ส่งผ้า")){
//                                    pickOrDeTv = "ส่งผ้าเมื่อ: ";
//                                    dateTime = respond.getJSONObject(i).getString("deli_date")+ "  " + respond.getJSONObject(i).getString("deli_time");
//                                    if(dateTime.equals("null  null")){
//                                        dateTime = "";
//                                    }
//                                    if(dateTime.equals("null  ")){
//                                        dateTime = "";
//                                    }
//                                }
//                                if(a.getuCode().equals(respond.getJSONObject(i).getString("address"))) {
//                                    if(a.getName() != null){
//                                        adsName = a.getName();
//                                    }
//                                }
//                            }
                        }
                        else{
                            for (ADS a: getUserAddress()) {
                                if(respond.getJSONObject(i).getString("status").equals("รับผ้า") ||
                                        respond.getJSONObject(i).getString("status").equals("ยืนยันการนัดหมาย") ||
                                        respond.getJSONObject(i).getString("status").equals("เพิ่มการนัดหมาย") ){
                                    pickOrDeTv = "รับผ้าเมื่อ: ";
                                    dateTime = respond.getJSONObject(i).getString("pick_date")+ "  " + respond.getJSONObject(i).getString("pick_time");
                                    if(dateTime.equals("null  null")){
                                        dateTime = "";
                                    }
                                }
                                else{
                                    pickOrDeTv = "ส่งผ้าเมื่อ: ";
                                    dateTime = respond.getJSONObject(i).getString("deli_date")+ "  " + respond.getJSONObject(i).getString("deli_time");
                                    if(dateTime.equals("null  null")){
                                        dateTime = "";
                                    }
                                }
                                if(a.getuCode().equals(respond.getJSONObject(i).getString("address"))) {
                                    if(a.getName() != null){
                                        adsName = a.getName();
                                    }
                                }
                            }
                        }
                    }
                    else{
                        adsName = "รับผ้าที่ร้านซักรีด" ;
                        pickOrDeTv = "";
                        dateTime = "" ;
                    }

                    if(respond.getJSONObject(i).getString("status").equals("เพิ่มรายการ")){
                        image = R.drawable.order_in_icon;
                    }
                    if(respond.getJSONObject(i).getString("status").equals("เพิ่มการนัดหมาย")){
                        image = R.drawable.order_add_icon;
                    }
                    if(respond.getJSONObject(i).getString("status").equals("กำลังดำเนินการ")){
                        image = R.drawable.in_progress_icon;
                    }
                    if(respond.getJSONObject(i).getString("status").equals("ส่งผ้า")){
                        image = R.drawable.go_out_deli;
                    }
                    if(respond.getJSONObject(i).getString("status").equals("ยืนยันการนัดหมาย")){
                        image = R.drawable.orde_con;
                    }
                    if(respond.getJSONObject(i).getString("status").equals("รับผ้า")){
                        image = R.drawable.pickup_ic;
                    }
                    if(respond.getJSONObject(i).getString("status").equals("เสร็จสิ้น")){
                        image = R.drawable.complete_ic;
                    }
                    if(respond.getJSONObject(i).getString("status").equals("ยกเลิก")){
                        image = R.drawable.cancel_icon  ;
                    }

//                    Log.e("xxx", respond.getJSONObject(i).get("address").toString());

//                    if(respond.getJSONObject(i).get("address").toString().equals()){
//                        adsName = "มารับผ้าที่ร้าน";
//                        dateTime= "";
//                    }


                    // ------------------------------------------

                    orderRecycles.add(new OrderRecycle(respond.getJSONObject(i).getInt("id"),
                            respond.getJSONObject(i).getString("name"),
                            respond.getJSONObject(i).getString("service"),
                            adsName,
                            pickOrDeTv,
                            dateTime,
                            respond.getJSONObject(i).getString("status"),
                            image
                    ));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(getActivity(),"มีบางอย่างผิดพลาด",
                    Toast.LENGTH_LONG).show();
        }
    }


    public ArrayList<ADS> getUserAddress(){
        ArrayList<ADS> aDsArrayList = new ArrayList<ADS>();
        String url = "customers"+"/getCustomerAddressAuth";
        Http http = new Http(getActivity(),url);
        http.setMethod("GET");
        http.send();
        Integer code = http.getStatusCode();
        if(code >=200 && code < 400){
            try{
                JSONArray respond = new JSONArray(http.getRespond());
                for (int i = 0; i < respond.length(); i++) {
                    if (!respond.getJSONObject(i).get("name").toString().equals("null")){
                        aDsArrayList.add(new ADS(respond.getJSONObject(i).getInt("id"),
                                respond.getJSONObject(i).getString("name"),
                                respond.getJSONObject(i).getString("u_code")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else{
            Toast.makeText(getActivity(),"มีบางอย่างผิดพลาด",
                    Toast.LENGTH_LONG).show();
        }
        return aDsArrayList;
    }

    public void initRecyclerview(ArrayList<OrderRecycle> orderRecycles){
        Collections.reverse(orderRecycles);
        orderRecycleArrayList = orderRecycles ;
        recyclerView = view.findViewById(R.id.recyclerView) ;
        layoutManager = new LinearLayoutManager(getActivity()) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(orderRecycles, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(int pos) {
        Intent orderDashBoard = new Intent(getActivity(), OrderInfoActivity.class);
        orderDashBoard.putExtra("orderId",orderRecycleArrayList.get(pos).getId()) ;
        startActivity(orderDashBoard);
    }

//    @Override
//    public void onNoteClick(int pos) {
//        Intent orderDashBoard = new Intent(getActivity(), );
//        orderDashBoard.putExtra("orderKey123",orderRecycleArrayList.get(pos).getOrderNumTV()) ;
//        startActivity(orderDashBoard);
//    }
}