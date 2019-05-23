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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsRequestFragment extends Fragment {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getInstance().getReference();

    private ListView listView;
    private ArrayAdapter<String> adapter;
    ArrayList<String> Array = new ArrayList<String>();

    TextView request_count;

    private String request_info;
    private String request_from_user_uid;
    private String request_name;
    private String request_email;
    private String request_num;

    public FriendsRequestFragment() {
        // Required empty public constructor
    }

    public static FriendsRequestFragment newInstance(String param1, String param2) {
        FriendsRequestFragment fragment = new FriendsRequestFragment();
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
        View layout = inflater.inflate(R.layout.fragment_friends_request, container, false);
        listView = layout.findViewById(R.id.request_friends_listview);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);

        request_count = (TextView) layout.findViewById(R.id.request_list_count);
        request_count.setVisibility(View.GONE);

        database.getInstance().getReference("Request Friends").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0) {
                    request_count.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
                for(DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    request_num = requestSnapshot.getKey();
                    request_num = request_num.substring(request_num.length()-1, request_num.length());
                    request_info = requestSnapshot.getValue().toString();
                    request_from_user_uid = request_info.substring(request_info.indexOf("=")+1, request_info.indexOf(","));
                    Array.add(request_from_user_uid+","+request_num);
                    database.getInstance().getReference("user_info").child(request_from_user_uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User get = dataSnapshot.getValue(User.class);
                            request_name = get.userName;
                            request_email = get.userEmail;
                            adapter.add(request_name + " (" + request_email + ")");
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            databaseError.getMessage();
                        }
                    });
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
                Intent intent = new Intent(getActivity(), FriendRequestListClickActivity.class);
                intent.putExtra("request friend uid & num", Array.get(position));
                startActivity(intent);
            }
        });

        return layout;
    }
}
