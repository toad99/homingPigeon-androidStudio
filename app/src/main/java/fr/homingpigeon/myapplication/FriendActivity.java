package fr.homingpigeon.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fr.homingpigeon.backend.Client;
import fr.homingpigeon.service.FriendService;

public class FriendActivity extends AppCompatActivity {

    private FriendService friendService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Intent i = getIntent();
        String token = i.getStringExtra("token");
        friendService = new FriendService(new Client(token));

        List<String> friends = friendService.getFriends();
        createFriendsViews(friends);
    }

    public void createFriendsViews(List<String> friends) {
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,friends);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendActivity.this,FriendActivity.class);
                //intent.putExtra();
                startActivity(intent);
            }
        });
    }
}