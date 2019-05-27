package org.androidtown.hotplace;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class FriendMarkerClickActivity extends AppCompatActivity {

    ImageView friend_memo_photo_imageview;
    TextView friend_memo_date_textview, friend_memo_location_textview, friend_memo_contents_textview;

    private String put_friend_marker_title;
    private String put_friend_uid;

    private String friend_memo_date_hh;
    private String friend_memo_date_mm;

    private int memo_photo_exist_int;
    private String memo_date;
    private String memo_contents;
    private String memo_date_for_save;
    private String memo_location;

    long mNow;
    Date mDate;
    SimpleDateFormat nFormat = new SimpleDateFormat("yyyyMMdd");

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference databaseReference = database.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hot-place-231908.appspot.com/");
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_marker_click);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        friend_memo_photo_imageview = (ImageView) findViewById(R.id.friend_marker_memo_photo_imageview);
        friend_memo_photo_imageview.setVisibility(View.GONE);
        friend_memo_date_textview = (TextView) findViewById(R.id.friend_marker_memo_date_textview);
        friend_memo_contents_textview = (TextView) findViewById(R.id.friend_marker_memo_contents_textview);
        friend_memo_location_textview = (TextView) findViewById(R.id.friend_marker_memo_location_textview);

        Intent intent = getIntent();
        put_friend_uid = intent.getStringExtra("marker friend uid");
        put_friend_marker_title = intent.getStringExtra("friend marker title");
        friend_memo_date_hh = put_friend_marker_title.substring(put_friend_marker_title.indexOf("서")+2, put_friend_marker_title.indexOf("시"));
        friend_memo_date_mm = put_friend_marker_title.substring(put_friend_marker_title.indexOf("시")+1, put_friend_marker_title.indexOf("분"));
        memo_date_for_save = TodayDate().concat("_").concat(friend_memo_date_hh).concat(friend_memo_date_mm);
        memo_date = memo_date_for_save.substring(0,4)+"."+memo_date_for_save.substring(4,6)+"."+memo_date_for_save.substring(6,8)+" "
                +memo_date_for_save.substring(9,11)+":"+memo_date_for_save.substring(11,13);
        friend_memo_date_textview.setText(memo_date);

        database.getInstance().getReference("Memo").child(put_friend_uid).child(memo_date_for_save).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Memo_ get = dataSnapshot.getValue(Memo_.class);
                memo_photo_exist_int = get.photo_exist;
                memo_contents = get.contents;
                memo_location = get.location;
                friend_memo_contents_textview.setText(memo_contents);
                friend_memo_location_textview.setText(memo_location);

                if(memo_photo_exist_int == 1) {
                    friend_memo_photo_imageview.setVisibility(View.VISIBLE);
                    pathReference = storageReference.child(put_friend_uid + "/Memo_photo/" + memo_date_for_save);
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageurl = uri.toString();
                            Glide.with(getApplicationContext())
                                    .load(imageurl)
                                    .into(friend_memo_photo_imageview);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //툴바 메뉴 선택 시 실행
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                //뒤로가기 버튼 클릭시
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //오늘 날짜 불러오기 (현재)
    private String TodayDate() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return nFormat.format(mDate);
    }
}