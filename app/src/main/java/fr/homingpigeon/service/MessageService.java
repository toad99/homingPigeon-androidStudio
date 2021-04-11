package fr.homingpigeon.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.homingpigeon.backend.Client;
import fr.homingpigeon.common.Message;
import fr.homingpigeon.common.UsefullFunctions;

public class MessageService {
    private Client client;
    private String conversation_id;

    public MessageService(Client client,String conversation_id) {
        this.client = client;
        this.conversation_id = conversation_id;
    }

    public String sendMessage(String message) {
        HttpURLConnection connexion;
        JSONObject json = new JSONObject();
        try {
            json.put("content",message);
            json.put("conversation_id",conversation_id);
            connexion = client.doPost("message/send",json);
            int code_retour = connexion.getResponseCode();
            if(code_retour == HttpURLConnection.HTTP_OK)
                return UsefullFunctions.readInputStream(connexion.getInputStream());
            else if (code_retour == HttpURLConnection.HTTP_BAD_REQUEST){
                String payload = UsefullFunctions.readInputStream(connexion.getErrorStream());
                JSONArray jsonArray = new JSONArray(payload);
                return jsonArray.get(0).toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown error";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Message> getMessages() {
        HttpURLConnection connexion;
        JSONObject json;
        List<Message> messages = new ArrayList<>();
        try {
            connexion = client.doGet("conversation/"+ conversation_id +"/messages");
            int code_retour = connexion.getResponseCode();
            if(code_retour == HttpURLConnection.HTTP_OK) {
                String payload = UsefullFunctions.readInputStream(connexion.getInputStream());
                if(payload.length() == 0)
                    return Collections.emptyList();
                JSONArray jsonArray = new JSONArray(payload);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    Message message = new Message();
                    message.setContent(jsonObject.getString("content"));
                    message.setDate(LocalDateTime.parse(jsonObject.getString("date")));
                    message.setSender(jsonObject.getString("sender"));
                    messages.add(message);
                }
                return messages;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
