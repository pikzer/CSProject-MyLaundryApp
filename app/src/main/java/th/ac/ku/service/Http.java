package th.ac.ku.service;

import android.content.Context;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Http {
    Context context ;
    private String url, method = "GET", data =null, respond = null ;
    private Integer statusCode = 0;
    private Boolean token = true ;
    private LocalStorage localStorage ;
    private String baseUrl = "http://10.0.2.2/api/" ;

    public Http(Context context, String url) {
        this.context = context;
        this.url = baseUrl+url;
        localStorage = new LocalStorage(context);
    }

    public void setMethod(String method) {
        this.method = method.toUpperCase(Locale.ROOT);
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setToken(Boolean token) {
        this.token = token;
    }

    public String getRespond() {
        return respond;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void send(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL sUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) sUrl.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Requested-With","XMLHttpRequest");
            if(token){
                connection.setRequestProperty("Authorization","Bearer "+ localStorage.getToken());
            }
            if(!method.equals("GET")){
                connection.setDoOutput(true);
            }
            if(data != null){
                connection.setDoInput(true);
                OutputStream os = connection.getOutputStream();
                os.write(data.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
            }
            statusCode = connection.getResponseCode();
            InputStreamReader isr ;
            if(statusCode == 200 || statusCode <= 299){
                isr = new InputStreamReader(connection.getInputStream());
            }else{
                isr = new InputStreamReader(connection.getErrorStream());
            }
            BufferedReader br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            String line ;
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();
            respond = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
