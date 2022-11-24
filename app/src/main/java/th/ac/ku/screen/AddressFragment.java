package th.ac.ku.screen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import th.ac.ku.R;
import th.ac.ku.adapter.Adapter;
import th.ac.ku.adapter.AddressAdapter;
import th.ac.ku.model.ADS;
import th.ac.ku.model.AddressRecycle;
import th.ac.ku.service.Http;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment implements AddressAdapter.OnNoteListener {

    View view;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddressFragment() {
        // Required empty public constructor
    }


    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
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

    RecyclerView recyclerView2;

    ArrayList<AddressRecycle> addressRecycles ;
    LinearLayoutManager layoutManager ;
    AddressAdapter adapter ;
    Button addAds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_address, container, false);

        addressRecycles = new ArrayList<>();
        getMyAddress();
        for (AddressRecycle ads: addressRecycles) {
            Log.e("xxx",ads.getNameAdsTv());
            Log.e("xxx",ads.getUcodeTv());
        }
        addAds = view.findViewById(R.id.addAddressBBtn);

        recyclerView2 = view.findViewById(R.id.recyclerView2);
        layoutManager = new LinearLayoutManager(getActivity()) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView2.setLayoutManager(layoutManager);
        adapter = new AddressAdapter(addressRecycles, this::onNoteClick);
        recyclerView2.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        addAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddAddressActivity.class) ;
                startActivity(intent);
            }
        });


        return view ;
    }

    public void getMyAddress(){
        String url = "address/getAddress";
        Http http = new Http(getActivity(),url);
        http.setMethod("POST");
        http.send();
        Integer code = http.getStatusCode();
            if(code >= 200 && code < 400){
                    try{
                        JSONArray respond = new JSONArray(http.getRespond());
                        for (int i = 0; i < respond.length(); i++) {
                            if (!respond.getJSONObject(i).get("name").toString().equals("null")){
                                addressRecycles.add(new AddressRecycle(respond.getJSONObject(i).get("name").toString(),
                                        respond.getJSONObject(i).getString("u_code")));
                            }
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

    @Override
    public void onNoteClick(int pos) {

    }
}