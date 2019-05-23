package org.androidtown.hotplace;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsListClickActivity extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hot-place-231908.appspot.com/");
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference;

    CircleImageView friends_profile_photo_imageview;
    TextView friend_name_textview, friend_email_textview, friend_birth_textview, friend_memo_not_open_range, friend_memo_count;

    private String put_friend_uid;
    private String friend_name;
    private String friend_email;
    private String friend_birth;
    private int friend_profile;
    private int friend_memo_open_range;

    private String memo_contents;
    private String memo_date;
    private String memo_date_for_save;
    private int memo_photo_exist_int;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    ArrayList<String> Array = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list_click);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        friends_profile_photo_imageview = (CircleImageView) findViewById(R.id.friend_profile_image);
        friend_name_textview = (TextView) findViewById(R.id.friend_name_text);
        friend_email_textview = (TextView) findViewById(R.id.friend_email_text);
        friend_birth_textview = (TextView) findViewById(R.id.friend_birth_text);
        friend_memo_not_open_range = (TextView) findViewById(R.id.friend_memo_not_open);
        friend_memo_count = (TextView) findViewById(R.id.friend_memo_count);
        friend_memo_count.setVisibility(View.GONE);
        friend_memo_not_open_range.setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.friends_memo_listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        put_friend_uid = intent.getStringExtra("friend");

        database.getInstance().getReference("user_info").child(put_friend_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User get = dataSnapshot.getValue(User.class);
                friend_name = get.userName;
                friend_email = get.userEmail;
                friend_birth = get.userBirth;
                friend_profile = get.userProfile;
                friend_memo_open_range = get.userMemoOpenRange;

                char birth[] = friend_birth.toCharArray();
                String birth_year = "";
                String birth_month = "";
                String birth_day = "";
                for(int i=0;i<friend_birth.length();i++) {
                    if(i >= 0 && i <= 3)
                        birth_year += Character.toString(birth[i]);
                    else if(i == 4 || i == 5)
                        birth_month += Character.toString(birth[i]);
                    else if(i == 6 || i == 7)
                        birth_day += Character.toString(birth[i]);
                }
                String final_birth = birth_year+"년 "+birth_month+"월 "+birth_day+"일";

                friend_name_textview.setText(friend_name);
                friend_email_textview.setText(friend_email);
                friend_birth_textview.setText(final_birth);

                if (friend_profile == 0)
                    friends_profile_photo_imageview.setImageResource(R.drawable.user_defaultprofile_gray);
                else {
                    pathReference = storageReference.child(put_friend_uid+"/ProfileImage/ProfilePhoto");
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

                if(friend_memo_open_range == 0 || friend_memo_open_range == 1) {
                    database.getInstance().getReference("Memo").child(put_friend_uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            adapter.clear();
                            if(dataSnapshot.getChildrenCount() == 0) {
                                friend_memo_count.setVisibility(View.VISIBLE);
                            }
                            for(DataSnapshot memoSnapshot : dataSnapshot.getChildren()) {
                                Memo_ friend_memo = memoSnapshot.getValue(Memo_.class);
                                memo_date_for_save = friend_memo.date;
                                memo_contents = friend_memo.contents;

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
                } else if(friend_memo_open_range == 2) {
                    listView.setVisibility(View.GONE);
                    friend_memo_not_open_range.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FriendMemoListClickActivity.class);
                intent.putExtra("friend_uid", put_friend_uid);
                intent.putExtra("friend_memo_date", String.valueOf(Array.get(position)));
                startActivity(intent);
            }
        });
    }

    //툴바 메뉴 선택 시 실행
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
