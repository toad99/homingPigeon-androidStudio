package fr.homingpigeon.backend;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import fr.homingpigeon.common.UsefullFunctions;


public class Client {
    private URL homingPigeonBackend;
    private String backendIp = "192.168.1.30";
    private String token;
    private String protocole = "http";
    private String port = "8080";
    private String prefixeToken = "Bearer ";

    //*HTTPS : Ne marche pas avec les certificat SSL auto-signé*//
    public Client(String token) {
        this.token = token;
    }

    public String signUp(String username, String password, String public_key) {
        JSONObject json = new JSONObject();
        try {
            json.put("username",username);
            json.put("password",password);
            json.put("public_key",public_key);
        } catch (JSONException e) {
            return "data could not be parsed into json";
        }

        HttpURLConnection connexion = null;
        try {
            connexion = doPost("account/signup",json);
            int code_retour = connexion.getResponseCode();
            if(code_retour == HttpURLConnection.HTTP_CREATED)
                return UsefullFunctions.readInputStream(connexion.getInputStream());
            else
                return UsefullFunctions.readInputStream(connexion.getErrorStream());
        } catch (IOException e) {
            return "connexion issues";
        }
    }

    public String login(String username,String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("username",username);
            json.put("password",password);
        } catch (JSONException e) {
            return "data could not be parsed into json";
        }

        try {
            HttpURLConnection connexion = doPost("login",json);
            int code_retour = connexion.getResponseCode();
            if(code_retour == HttpURLConnection.HTTP_OK) {
                token = connexion.getHeaderField("Authorization").replace(prefixeToken, "");
                return "Logged in";
            }
            else if(code_retour == HttpURLConnection.HTTP_FORBIDDEN)
                return "Bad credentials";//TODO : doit changer backend pour qu'il renvoie ça
            else
                return UsefullFunctions.readInputStream(connexion.getErrorStream());
        } catch (IOException e) {
            return "connexion issues";
        }

    }

    public HttpURLConnection doPost(String url, JSONObject json) throws IOException{
        homingPigeonBackend = new URL(protocole + "://" + backendIp + ":" + port + "/" + url);
        HttpURLConnection connexion = (HttpURLConnection) homingPigeonBackend.openConnection();

        connexion.setRequestMethod("POST");
        connexion.setRequestProperty("Content-Type", "application/json; utf-8");
        connexion.setRequestProperty("Accept", "application/json");
        connexion.setDoOutput(true);

        OutputStream os = connexion.getOutputStream();
        byte[] input = json.toString().getBytes("utf-8");
        os.write(input, 0, input.length);
        Log.d("doPost", "request sent");

        return connexion;
    }

    public HttpURLConnection doGet(String url) throws IOException {

        homingPigeonBackend = new URL(protocole + "://" + backendIp + ":" + port + "/" + url);
        HttpURLConnection connexion = (HttpURLConnection) homingPigeonBackend.openConnection();

        connexion.setRequestMethod("GET");
        connexion.setRequestProperty("Content-Type", "application/json; utf-8");
        connexion.setRequestProperty("Accept", "application/json");
        connexion.setRequestProperty ("Authorization",prefixeToken + token);
        connexion.setUseCaches(false);
        connexion.setAllowUserInteraction(false);
        connexion.setConnectTimeout(5000);
        connexion.setReadTimeout(5000);
        connexion.connect();

        Log.d("doGet", "request sent");
        return connexion;
    }

    public String getToken() {
        return token;
    }
}
