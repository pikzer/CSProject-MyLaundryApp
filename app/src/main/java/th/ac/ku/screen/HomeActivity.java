package th.ac.ku.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import th.ac.ku.R;
import th.ac.ku.service.Http;
import th.ac.ku.service.LocalStorage;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    OrderFragment orderFragment = new OrderFragment();
    AddressFragment addressFragment = new AddressFragment();
    MoreFragment moreFragment = new MoreFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        LocalStorage localStorage = new LocalStorage(HomeActivity.this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

//        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.order);
//        badgeDrawable.setVisible(true);
//        badgeDrawable.setNumber(10);



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.order:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, orderFragment).commit();
                        return true;
                    case R.id.address:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, addressFragment).commit();
                        return true;
                    case R.id.more:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container, moreFragment).commit();
                        new AlertDialog.Builder(HomeActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("ออกจากระบบ").setMessage("คุณต้องการที่จะออกจากระบบหรือไม่?")
                                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendLogout();
                                        Toast.makeText(HomeActivity.this, "ออกจากระบบสำเร็จ",Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("ยกเลิก", null).show();

                    default:
                        return false ;
                }
            }
        });
    }

    public  void updateNavigationBarState(int actionId){
        bottomNavigationView.setSelectedItemId(actionId);
    }

    static final long THRESHOLD = 2000;
    long backLastPressed;

    boolean doubleBackToExitPressedOnce = false;



    public void sendLogout(){
        JSONObject params = new JSONObject();
        try{
            params.put("message",null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "auth/logout";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(HomeActivity.this,url);
                http.setMethod("POST");
                http.setData(null);
                http.send();
                Log.e("xxx",http.getRespond());
                Integer code = http.getStatusCode();
                if(code == 200){
                    try{
                        JSONObject jsonObject = new JSONObject(http.getRespond());
                        LocalStorage localStorage = new LocalStorage(HomeActivity.this);
                        localStorage.setToken(null);
                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                        finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(HomeActivity.this,"Faild TO",
                            Toast.LENGTH_LONG).show();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backLastPressed < THRESHOLD) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "กดกลับอีกครั้งเพื่อออกจาก MyLaundry", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
            backLastPressed = 0;
            return;
        }
        backLastPressed = System.currentTimeMillis();
        // Otherwise, ignore this BACK press
    }
}