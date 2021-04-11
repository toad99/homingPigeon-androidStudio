package fr.homingpigeon.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.List;

import fr.homingpigeon.backend.Client;
import fr.homingpigeon.service.ConversationService;


public class ConversationFragment extends Fragment {

    private ConversationService conversationService;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversations,container,false);
        Bundle bundle = getArguments();
        String token = bundle.getString("token");
        String username = bundle.getString("username");
        Log.d("ConvFragment's Token",token);
        conversationService = new ConversationService(new Client(token)); //mieux d'initier le client dans service
        ListView listView = view.findViewById(R.id.listViewConversation);
        List<String> list = conversationService.getConversationsByMembers();
        ArrayAdapter arrayAdapter = new ArrayAdapter(container.getContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("conversation_id",conversationService.getConversations().get(position));
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
        return view;
    }
}
