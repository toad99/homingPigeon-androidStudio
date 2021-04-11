package fr.homingpigeon.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.homingpigeon.backend.Client;
import fr.homingpigeon.common.UsefullFunctions;

public class ConversationService {

    private Client client;
    private List<String> conversations;

    public ConversationService(Client client) {
        this.client = client;
        loadConversationsIds();
    }

    public void loadConversationsIds() {
        HttpURLConnection connexion;
        JSONObject json;
        conversations = new ArrayList<>();

        try {
            connexion = client.doGet("account/conversations");
            int code_retour = connexion.getResponseCode();

            if(code_retour == HttpURLConnection.HTTP_OK) {
                String payload = UsefullFunctions.readInputStream(connexion.getInputStream());
                if(payload.length() == 0)
                    conversations = Collections.emptyList();
                JSONArray jsonArray = new JSONArray(payload);
                for(int i = 0; i < jsonArray.length(); i++)
                    conversations.add(jsonArray.get(i).toString());
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conversations = Collections.emptyList();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<String> getConversationsByMembers() {
        HttpURLConnection connexion;
        JSONObject json;
        List<String> groups = new ArrayList<>();
        for(String conversation : conversations) {
            try {
                connexion = client.doGet("conversation/" + conversation + "/members");
                int code_retour = connexion.getResponseCode();
                List<String> group = new ArrayList<>();
                if (code_retour == HttpURLConnection.HTTP_OK) {
                    String payload = UsefullFunctions.readInputStream(connexion.getInputStream());
                    if (payload.length() == 0)
                        group = Collections.emptyList();
                    else{
                        JSONArray jsonArray = new JSONArray(payload);
                        for (int i = 0; i < jsonArray.length(); i++)
                            group.add(jsonArray.get(i).toString());
                        this.conversations = conversations;
                        groups.add(String.join(",", group));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            } catch (JSONException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }
        return groups;
    }

    public List<String> getConversations() {
        return conversations;
    }
}
