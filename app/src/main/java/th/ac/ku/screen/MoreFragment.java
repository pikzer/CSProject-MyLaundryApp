package th.ac.ku.screen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import th.ac.ku.R;
import th.ac.ku.service.Http;
import th.ac.ku.service.LocalStorage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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

    Button logoutBtn ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_more, container, false);
        View view =inflater.inflate(R.layout.fragment_more, container, false);

        logoutBtn = view.findViewById(R.id.logOutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject params = new JSONObject();
                try{
                    params.put("message",null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String data = params.toString();
                String url = "auth/logout";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Http http = new Http(getActivity(),url);
                        http.setMethod("POST");
                        http.setData(null);
                        http.send();
                        Log.e("xxx",http.getRespond());
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            try{
                                JSONObject jsonObject = new JSONObject(http.getRespond());
                                LocalStorage localStorage = new LocalStorage(getContext());
                                localStorage.setToken(null);
                                Intent intent = new Intent(getActivity(),MainActivity.class);
                                getActivity().finish();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(getActivity(),"Faild TO",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        });

        return  view;
    }
}