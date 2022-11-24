package th.ac.ku.service;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    SharedPreferences sharedKeyPreferences  ;
    SharedPreferences.Editor editor ;
    Context context ;
    String token ;
    Integer userId;

    public LocalStorage(Context context){
        this.context = context ;
        sharedKeyPreferences = context.getSharedPreferences("STORAGE_LOGIN_API",Context.MODE_PRIVATE);
        editor = sharedKeyPreferences.edit();
    }

    public String getToken() {
        token = sharedKeyPreferences.getString("TOKEN","");
        return token;
    }

    public void setToken(String token) {
        editor.putString("TOKEN",token);
        editor.commit();
        this.token = token;
    }

    public void setUserId(Integer id){
        editor.putInt("USER_ID",id);
        editor.commit();
        this.userId = id;
    }
    public Integer getUserID(){
        userId = sharedKeyPreferences.getInt("USER_ID",0);
        return userId;
    }
}
