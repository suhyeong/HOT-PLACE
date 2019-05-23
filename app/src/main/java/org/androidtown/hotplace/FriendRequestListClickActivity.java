package org.androidtown.hotplace;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class FriendRequestListClickActivity extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hot-place-231908.appspot.com/");
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference;

    CircleImageView friends_profile_photo_imageview;
    TextView friend_name_textview, friend_email_textview, friend_birth_textview, friend_memo_not_open, friend_memo_friend_open, friend_memo_count;

    private String request_friend_info;
    private String request_friend_uid;
    private String request_friend_num;
    private String request_friend_name;
    private String request_friend_email;
    private String request_friend_birth;
    private int request_friend_profile;
    private int request_friend_memo_open_range;

    private String memo_info;
    private String memo_contents;
    private String memo_date;
    private String memo_photo_exist_str;
    private int memo_photo_exist_int;

    private long user_friends_num;
    private long friend_friends_num;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    ArrayList<String> Array = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request_list_click);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        friends_profile_photo_imageview = (CircleImageView) findViewById(R.id.request_friend_profile_image);
        friend_name_textview = (TextView) findViewById(R.id.request_friend_name_text);
        friend_email_textview = (TextView) findViewById(R.id.request_friend_email_text);
        friend_birth_textview = (TextView) findViewById(R.id.request_friend_birth_text);
        friend_memo_not_open = (TextView) findViewById(R.id.request_friend_memo_not_open);
        friend_memo_friend_open = (TextView) findViewById(R.id.request_friend_memo_friend_open);
        friend_memo_count = (TextView) findViewById(R.id.request_friend_memo_count);
        friend_memo_count.setVisibility(View.GONE);
        friend_memo_friend_open.setVisibility(View.GONE);
        friend_memo_not_open.setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.request_friend_memo_listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        request_friend_info = intent.getStringExtra("request friend uid & num");
        request_friend_uid = request_friend_info.substring(0, request_friend_info.indexOf(","));
        request_friend_num = request_friend_info.substring(request_friend_info.indexOf(",")+1, request_friend_info.length());

        database.getInstance().getReference("user_info").child(request_friend_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User get = dataSnapshot.getValue(User.class);
                request_friend_name = get.userName;
                request_friend_email = get.userEmail;
                request_friend_birth = get.userBirth;
                request_friend_profile = get.userProfile;
                request_friend_memo_open_range = get.userMemoOpenRange;

                char birth[] = request_friend_birth.toCharArray();
                String birth_year = "";
                String birth_month = "";
                String birth_day = "";
                for(int i=0;i<request_friend_birth.length();i++) {
                    if(i >= 0 && i <= 3)
                        birth_year += Character.toString(birth[i]);
                    else if(i == 4 || i == 5)
                        birth_month += Character.toString(birth[i]);
                    else if(i == 6 || i == 7)
                        birth_day += Character.toString(birth[i]);
                }
                String final_birth = birth_year+"년 "+birth_month+"월 "+birth_day+"일";

                friend_name_textview.setText(request_friend_name);
                friend_email_textview.setText(request_friend_email);
                friend_birth_textview.setText(final_birth);

                if (request_friend_profile == 0)
                    friends_profile_photo_imageview.setImageResource(R.drawable.user_defaultprofile_gray);
                else {
                    pathReference = storageReference.child(request_friend_uid+"/ProfileImage/ProfilePhoto");
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

                if(request_friend_memo_open_range == 0) { //메모 : 0 (전체공개)
                    openMemo();
                } else if(request_friend_memo_open_range == 1) { //메모 : 1 (친구공개)
                    listView.setVisibility(ListView.GONE);
                    friend_memo_friend_open.setVisibility(View.VISIBLE);
                } else if(request_friend_memo_open_range == 2){ //메모 : 2 (비공개)
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
        menuInflater.inflate(R.menu.actionbar_friendrequestlistclick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.request_accept:
                //친구 요청 수락
                AlertDialog.Builder accept_builder = new AlertDialog.Builder(this);
                accept_builder.setMessage("친구 요청을 수락할까요?").setCancelable(false)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //친구 요청 수락 완료 '네' 클릭시
                                request_accept();
                            }
                        }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //친구 요청 수락 '아니요' 클릭시
                        dialog.dismiss();
                    }
                });
                AlertDialog alert_accept = accept_builder.create();
                alert_accept.show();
                return true;
            case R.id.request_refuse:
                //친구 요청 거절
                AlertDialog.Builder refuse_builder = new AlertDialog.Builder(this);
                refuse_builder.setMessage("친구 요청을 정말로 거절할까요?").setCancelable(false)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //친구 요청 거절 완료 '네' 클릭시
                                request_refuse();
                            }
                        }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //친구 요청 거절 '아니요' 클릭시
                        dialog.dismiss();
                    }
                });
                AlertDialog alert_refuse = refuse_builder.create();
                alert_refuse.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openMemo() {
        database.getInstance().getReference("Memo").child(request_friend_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                if(dataSnapshot.getChildrenCount() == 0)
                    friend_memo_count.setVisibility(View.VISIBLE);
                for(DataSnapshot memoSnapshot : dataSnapshot.getChildren()) {
                    memo_info = memoSnapshot.getValue().toString();
                    memo_date = memo_info.substring(6,19);
                    memo_contents = memo_info.substring(30,memo_info.lastIndexOf(','));
                    memo_photo_exist_str = memo_info.substring(memo_info.lastIndexOf('=')+1, memo_info.lastIndexOf('}'));
                    memo_photo_exist_int = Integer.parseInt(memo_photo_exist_str);
                    memo_date = memo_date.substring(0,4)+"."+memo_date.substring(4,6)+"."+memo_date.substring(6,8)+" "+memo_date.substring(9,11)+":"+memo_date.substring(11,13);
                    Array.add(memo_photo_exist_int + "\n" + memo_date + "\n" + memo_contents);
                    adapter.add(memo_date + "\n" + memo_contents);
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void request_accept() {
        databaseReference.child("Request Friends").child(user.getUid()).child("Request"+request_friend_num).removeValue();
        databaseReference.child("Friends_info").child(request_friend_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friend_friends_num = dataSnapshot.getChildrenCount();
                databaseReference.child("Friends_info").child(request_friend_uid).child("friends"+(friend_friends_num+1)).setValue(user.getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
        databaseReference.child("Friends_info").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_friends_num = dataSnapshot.getChildrenCount();
                databaseReference.child("Friends_info").child(user.getUid()).child("friends"+(user_friends_num+1)).setValue(request_friend_uid);
                Toast.makeText(getApplicationContext(), "친구 요청을 수락하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });

    }

    public void request_refuse() {
        databaseReference.child("Request Friends").child(user.getUid()).child("Request"+request_friend_num).removeValue();
        Toast.makeText(this, "친구 요청을 거절하였습니다.", Toast.LENGTH_SHORT).show();
    }
}
