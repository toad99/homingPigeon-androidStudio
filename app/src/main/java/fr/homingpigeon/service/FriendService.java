package fr.homingpigeon.service;

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

public class FriendService {
    private Client client;

    public FriendService(Client client) {
        this.client = client;
    }

    public List<String> getFriends() {
        HttpURLConnection connexion;
        JSONObject json;
        List<String> friends = new ArrayList<>();
        try {
            connexion = client.doGet("account/friends");
            int code_retour = connexion.getResponseCode();

            if(code_retour == HttpURLConnection.HTTP_OK) {
                String payload = UsefullFunctions.readInputStream(connexion.getInputStream());
                if(payload.length() == 0)
                    return Collections.emptyList();
                JSONArray jsonArray = new JSONArray(payload);
                for(int i = 0; i < jsonArray.length(); i++)
                    friends.add(jsonArray.get(i).toString());
                return friends;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
