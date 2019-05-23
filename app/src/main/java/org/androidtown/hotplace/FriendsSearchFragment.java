package org.androidtown.hotplace;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsSearchFragment extends Fragment {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    EditText search_friend_text;
    ImageButton search_friend_button;
    String search_friend_email;
    TextView search_result_count;

    private String result_friend_uid;
    private String result_friend_name;
    private String result_friend_email;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    ArrayList<String> Array = new ArrayList<String>();

    public FriendsSearchFragment() {
        // Required empty public constructor
    }

    public static FriendsSearchFragment newInstance(String param1, String param2) {
        FriendsSearchFragment fragment = new FriendsSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_friends_search, container, false);
        search_friend_text = layout.findViewById(R.id.friends_search_text);
        search_friend_button = layout.findViewById(R.id.friends_search_button);
        listView = layout.findViewById(R.id.friends_search_listview);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);
        search_result_count = (TextView) layout.findViewById(R.id.search_result_count);
        search_result_count.setVisibility(View.GONE);
        search_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_friend_email = search_friend_text.getText().toString();
                if(search_friend_email == null)
                    Toast.makeText(getActivity(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                search_friends(search_friend_email);
            }
        });
        return layout;
    }

    public void search_friends(final String search_friend_email) {
        database.getInstance().getReference("user_info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for(DataSnapshot friendsSnapshot : dataSnapshot.getChildren()) {
                    User get = friendsSnapshot.getValue(User.class);
                    result_friend_email = get.userEmail;
                    result_friend_name = get.userName;

                    if(!(user.getEmail().equals(result_friend_email)) && result_friend_email.equals(search_friend_email)) {
                        result_friend_uid = friendsSnapshot.getKey();
                        Array.add(result_friend_uid);
                        adapter.add(result_friend_name +" ("+ result_friend_email +")");
                    }
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1);
                if(adapter.getCount() == 0) {
                    search_result_count.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FriendsSearchClickActivity.class);
                intent.putExtra("search friend uid", Array.get(position));
                startActivity(intent);
            }
        });
    }
}
