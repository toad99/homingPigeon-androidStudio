package fr.homingpigeon.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.NoSuchPaddingException;

import fr.homingpigeon.backend.Client;
import fr.homingpigeon.common.AsymmetricCryptography;
import fr.homingpigeon.common.Message;
import fr.homingpigeon.service.MessageService;

public class ChatActivity extends AppCompatActivity {

    private MessageService messageService;
    private ListView listView;
    private String username;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent i = getIntent();
        String token = i.getStringExtra("token");
        username = i.getStringExtra("username");
        String conversation_id = i.getStringExtra("conversation_id");
        messageService = new MessageService(new Client(token),conversation_id);
        listView = findViewById(R.id.listViewMessages);
        refresh();

        EditText editText = findViewById(R.id.messageBox);
        ImageButton imageButton = findViewById(R.id.buttonSendMessage);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = messageService.sendMessage(editText.getText().toString());
                editText.setText("");
                Toast.makeText(ChatActivity.this,result,Toast.LENGTH_LONG).show();
                refresh();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refresh() {
        try {
            List<Message> messages = messageService.getMessages();
            PrivateKey privateKey = AsymmetricCryptography.getPrivate(loadPrivateKey(username));
            for (Message message : messages) {
                message.setContent(new String(AsymmetricCryptography.do_RSADecryption(Base64.getDecoder().decode(message.getContent()), privateKey)));
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(ChatActivity.this, android.R.layout.simple_list_item_1,messages.stream()
                    .map(x->x.getContent()+" from:"+x.getSender()).collect(Collectors.toList()));
            listView.setAdapter(arrayAdapter);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadPrivateKey(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String s = sharedPreferences.getString(username, "");
        Log.d("pvt",s);
        return s;
    }
}