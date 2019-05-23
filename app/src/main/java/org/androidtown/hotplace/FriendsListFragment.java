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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsListFragment extends Fragment {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    //DatabaseReference databaseReference = database.getInstance().getReference();

    private ListView listView;
    private ArrayAdapter<String> adapter;
    ArrayList<String> Array = new ArrayList<String>();

    private String user_friend_uid;
    private String user_friend_name;
    private String user_friend_email;

    TextView friends_list_count;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    public static FriendsListFragment newInstance(String param1, String param2) {
        FriendsListFragment fragment = new FriendsListFragment();
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
        View layout = inflater.inflate(R.layout.fragment_friends_list, container, false);
        listView = layout.findViewById(R.id.friends_listview);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);

        friends_list_count = (TextView) layout.findViewById(R.id.friends_list_count);
        friends_list_count.setVisibility(View.GONE);

        database.getInstance().getReference("Friends_info").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                if(dataSnapshot.getChildrenCount() == 0) {
                    friends_list_count.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
                for(DataSnapshot friendsSnapshot : dataSnapshot.getChildren()) {
                    user_friend_uid = friendsSnapshot.getValue().toString();
                    database2.getInstance().getReference("user_info").child(user_friend_uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User friend = dataSnapshot.getValue(User.class);
                            user_friend_name = friend.userName;
                            user_friend_email = friend.userEmail;
                            adapter.add(user_friend_name +" ("+ user_friend_email+")");
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Array.add(user_friend_uid);
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FriendsListClickActivity.class);
                intent.putExtra("friend", Array.get(position));
                startActivity(intent);
            }
        });

        return layout;
    }
}
