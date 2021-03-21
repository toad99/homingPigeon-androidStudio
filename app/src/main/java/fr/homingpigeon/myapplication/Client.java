package fr.homingpigeon.myapplication;

import android.preference.PreferenceActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


public class Client {
    private URL homingPigeonBackend;
    private String backendIp = "192.168.1.30";
    private String token;
    private String protocole;
    private String port;
    private String prefixeToken = "Bearer ";

    //*TRUE : Ne marche pas avec les certificat auto-signé*//
    public Client(boolean secure) {
        if(secure){
            protocole = "https";
            port = "8443";
        }
        else{
            protocole = "http";
            port = "8080";
        }
    }

    public String signUp(String username, String password, String public_key) {
        JSONObject json = new JSONObject();
        try {
            json.put("username",username);
            json.put("password",password);
            json.put("public_key",public_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return doPost("account/signup",json);
    }

    public String login(String username,String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("username",username);
            json.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return doPost("login",json);
    }

    public String doPost(String url, JSONObject json) {
        try {
            HttpURLConnection connexion;
            homingPigeonBackend = new URL(protocole + "://" + backendIp + ":" + port + "/" + url);
            connexion = (HttpURLConnection) homingPigeonBackend.openConnection();
            Log.d("doPost", "connexion is open");


            connexion.setRequestMethod("POST");
            connexion.setRequestProperty("Content-Type", "application/json; utf-8");
            connexion.setRequestProperty("Accept", "application/json");
            connexion.setDoOutput(true);


            OutputStream os = connexion.getOutputStream();
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
            Log.d("doPost", "request sent");

            int return_code = connexion.getResponseCode();
            Log.d(String.valueOf(return_code), "return code");

            if (return_code == HttpURLConnection.HTTP_OK && url.equals("login")) {
                String token = connexion.getHeaderField("Authorization");
                this.token = token.replace(prefixeToken, "");
                return "Connexion successfull";
            }else if(return_code == HttpURLConnection.HTTP_CREATED && url.equals("account/signup")){
                return "Account created";
            }else if(return_code == HttpURLConnection.HTTP_OK || return_code == HttpURLConnection.HTTP_CREATED)
                return connexion.getInputStream().toString();
            else {
                return connexion.getErrorStream().toString();
            }

        }catch (MalformedURLException malformed) {
            return "protocole, ip, port, ou url eronné";
        }catch (IOException e) {
            return "An error was encountered during the request";
        }
    }
}
