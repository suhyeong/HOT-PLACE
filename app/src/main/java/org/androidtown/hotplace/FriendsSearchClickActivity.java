package org.androidtown.hotplace;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsSearchClickActivity extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hot-place-231908.appspot.com/");
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference;

    CircleImageView friends_profile_photo_imageview;
    TextView friend_name_textview, friend_email_textview, friend_birth_textview, friend_memo_not_open, friend_memo_friend_open, friend_memo_count;

    private String search_friend_uid;
    private String search_friend_name;
    private String search_friend_email;
    private String search_friend_birth;
    private int search_friend_profile;
    private int search_friend_memo_open_range;
    long count;

    boolean alreadyfriends_friend;
    boolean alreadyfriends_user;

    private String memo_info;
    private String memo_contents;
    private String memo_date;
    private String memo_date_for_save;
    private String memo_photo_exist_str;
    private int memo_photo_exist_int;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    ArrayList<String> Array = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_search_click);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        friends_profile_photo_imageview = (CircleImageView) findViewById(R.id.search_friend_profile_image);
        friend_name_textview = (TextView) findViewById(R.id.search_friend_name_text);
        friend_email_textview = (TextView) findViewById(R.id.search_friend_email_text);
        friend_birth_textview = (TextView) findViewById(R.id.search_friend_birth_text);
        friend_memo_not_open = (TextView) findViewById(R.id.search_friend_memo_not_open);
        friend_memo_friend_open = (TextView) findViewById(R.id.search_friend_memo_friend_open);
        friend_memo_count = (TextView) findViewById(R.id.search_friend_memo_count);
        friend_memo_count.setVisibility(View.GONE);
        friend_memo_friend_open.setVisibility(View.GONE);
        friend_memo_not_open.setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.search_friend_memo_listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        search_friend_uid = intent.getStringExtra("search friend uid");

        database.getInstance().getReference("user_info").child(search_friend_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User get = dataSnapshot.getValue(User.class);
                search_friend_name = get.userName;
                search_friend_email = get.userEmail;
                search_friend_birth = get.userBirth;
                search_friend_profile = get.userProfile;
                search_friend_memo_open_range = get.userMemoOpenRange;

                char birth[] = search_friend_birth.toCharArray();
                String birth_year = "";
                String birth_month = "";
                String birth_day = "";
                for(int i=0;i<search_friend_birth.length();i++) {
                    if(i >= 0 && i <= 3)
                        birth_year += Character.toString(birth[i]);
                    else if(i == 4 || i == 5)
                        birth_month += Character.toString(birth[i]);
                    else if(i == 6 || i == 7)
                        birth_day += Character.toString(birth[i]);
                }
                String final_birth = birth_year+"년 "+birth_month+"월 "+birth_day+"일";

                friend_name_textview.setText(search_friend_name);
                friend_email_textview.setText(search_friend_email);
                friend_birth_textview.setText(final_birth);

                if (search_friend_profile == 0)
                    friends_profile_photo_imageview.setImageResource(R.drawable.user_defaultprofile_gray);
                else {
                    pathReference = storageReference.child(search_friend_uid+"/ProfileImage/ProfilePhoto");
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageurl = uri.toString();
                            Glide.with(getApplicationContext())
                                    .load(imageurl)
                                    .into(friends_profile_photo_imageview);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }

                AlreadyFriends_friend();
                AlreadyFriends_user();

                if(search_friend_memo_open_range == 0) { //메모 : 0 (전체공개)
                    openMemo();
                } else if(search_friend_memo_open_range == 1 && alreadyfriends_user == true && alreadyfriends_friend == true) { //메모 : 1 (친구공개)
                    openMemo();
                } else if(search_friend_memo_open_range == 1 && alreadyfriends_friend == false && alreadyfriends_user == false) {
                    listView.setVisibility(ListView.GONE);
                    friend_memo_friend_open.setVisibility(View.VISIBLE);
                } else if(search_friend_memo_open_range == 2){ //메모 : 2 (비공개)
                    listView.setVisibility(ListView.GONE);
                    friend_memo_not_open.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_friendssearchclick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.request_to_friend:
                //request to friend icon selected : click event for action_request_to_friend item
                if(alreadyfriends_friend == true && alreadyfriends_user == true) {
                    Toast.makeText(this, "이미 친구인 사용자입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    request_to_friend();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void AlreadyFriends_user() {
        database.getInstance().getReference("Friends_info").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot friendsSnapshot : dataSnapshot.getChildren()) {
                    if(friendsSnapshot.getValue().toString().equals(search_friend_uid)) {
                        alreadyfriends_user = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }

    public void AlreadyFriends_friend() {
        database.getInstance().getReference("Friends_info").child(search_friend_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot friendsSnapshot : dataSnapshot.getChildren()) {
                    if(friendsSnapshot.getValue().toString().equals(user.getUid())) {
                        alreadyfriends_friend = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }

    public void openMemo() {
        database.getInstance().getReference("Memo").child(search_friend_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                if(dataSnapshot.getChildrenCount() == 0)
                    friend_memo_count.setVisibility(View.VISIBLE);
                for(DataSnapshot memoSnapshot : dataSnapshot.getChildren()) {
                    Memo_ memo = memoSnapshot.getValue(Memo_.class);
                    memo_date_for_save = memo.date;
                    memo_contents = memo.contents;

                    memo_date = memo_date_for_save.substring(0,4)+"."+memo_date_for_save.substring(4,6)+"."+memo_date_for_save.substring(6,8)+" "
                            +memo_date_for_save.substring(9,11)+":"+memo_date_for_save.substring(11,13);

                    Array.add(memo_date_for_save);
                    adapter.add(memo_date + "\n" + memo_contents);
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
                Intent intent = new Intent(getApplicationContext(), SearchFriendMemoListClickActivity.class);
                intent.putExtra("search_friend_uid", search_friend_uid);
                intent.putExtra("search_friend_memo_date", String.valueOf(Array.get(position)));
                startActivity(intent);
            }
        });
    }

    public void request_to_friend() {
        database.getInstance().getReference("Request Friends").child(search_friend_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();
                count++;
                RequestFriend requestFriend = new RequestFriend(search_friend_uid, user.getUid()); //to, from
                databaseReference.child("Request Friends").child(search_friend_uid).child("Request"+count).setValue(requestFriend);
                Toast.makeText(getApplicationContext(), search_friend_name+"님에게 친구요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
