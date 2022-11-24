package th.ac.ku.screen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import th.ac.ku.R;
import th.ac.ku.model.Customer;
import th.ac.ku.service.CommonUser;
import th.ac.ku.service.Http;
import th.ac.ku.service.LocalStorage;


public class HomeFragment extends Fragment {

    Button addOrderBtn,addAddressBtn,editProfile,myADSBtn;
    public View view ;

    TextView usernameText ;

    LocalStorage localStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        localStorage = new LocalStorage(getActivity());


        addOrderBtn =view.findViewById(R.id.addOrderBtn);
        addAddressBtn = view.findViewById(R.id.addAddressBtn);
        usernameText = view.findViewById(R.id.usernameText);
        editProfile = view.findViewById(R.id.editProfile);
        myADSBtn = view.findViewById(R.id.myADSBtn);
        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddOrderActivity.class) ;
                startActivity(intent);
            }
        });
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddAddressActivity.class) ;
                startActivity(intent);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class) ;
                startActivity(intent);
            }
        });
        myADSBtn.setOnClickListener(new View.OnClickListener() {
            AddressFragment addressFragment = new AddressFragment();
            @Override
            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, addressFragment).commit();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(container.getId(),addressFragment);
                fragmentTransaction.commit();
            }
        });

        getUser();

//        String url = "customers/"+localStorage.getUserID();
//        Http http = new Http(getActivity(),url);
//        http.setMethod("GET");
//        http.send();
//        try {
//            if(http.getStatusCode() >= 200 && http.getStatusCode() < 400){
//                JSONObject j = new JSONObject(http.getRespond());

//            }
//            else{
//                Log.d("xxx",http.getRespond().toString());
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


//        if (CommonUser.currentCustomer.getName()!=null){
//            usernameText.setText(CommonUser.currentCustomer.getName());
//        }

        return view ;
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void getUser(){
        String url = "auth/customerGetMe";
        Http http = new Http(getActivity(),url);
        http.setMethod("POST");
        http.send();
        Integer code = http.getStatusCode();
        if(code == 200){
            try{
                JSONObject jsonObject = new JSONObject(http.getRespond());
                localStorage.setUserId(jsonObject.getInt("id"));
                CommonUser.currentCustomer = new Customer(jsonObject.getInt("id"),jsonObject.getString("name"),jsonObject.getString("phone"),
                        jsonObject.getString("email"),jsonObject.getInt("isMembership"),jsonObject.getString("memService"),jsonObject.getInt("memCredit"));
                usernameText.setText(CommonUser.currentCustomer.getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}