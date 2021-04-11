package fr.homingpigeon.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import fr.homingpigeon.backend.Client;
import fr.homingpigeon.service.FriendService;

public class FriendFragment extends Fragment {

    FriendService friendService;
    private ListView listView;

    public FriendFragment () {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends,container,false);
        Bundle bundle = getArguments();
        String token = bundle.getString("token");
        Log.d("friendFragment's Token",token);
        friendService = new FriendService(new Client(token));

        listView = view.findViewById(R.id.listViewFriends);
        List<String> friends = friendService.getFriends();
        ArrayAdapter arrayAdapter = new ArrayAdapter(container.getContext(), android.R.layout.simple_list_item_1,friends);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String friend = friends.get(position);
                PopupMenu popup = new PopupMenu(parent.getContext(),view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int i = item.getItemId();
                        if (i == R.id.chat_with) {
                            //lancer messenger activity, mais checker si la conversation existe
                            Toast.makeText(container.getContext(),"TODO",Toast.LENGTH_LONG).show();
                            return true;
                        }
                        else if (i == R.id.delete){
                            String result = friendService.deleteFriend(friend);
                            refresh();
                            Toast.makeText(container.getContext(),result,Toast.LENGTH_LONG).show();
                            return true;
                        }
                        else {
                            return onMenuItemClick(item);
                        }
                    }
                });
                popup.inflate(R.menu.popup_friend);
                popup.show();
            }
        });
        return view;
    }

    public void refresh() {
        List<String> friends = friendService.getFriends();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,friends);
        listView.setAdapter(arrayAdapter);
    }

}
