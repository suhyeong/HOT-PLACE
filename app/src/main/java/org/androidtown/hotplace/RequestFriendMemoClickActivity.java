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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RequestFriendMemoClickActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hot-place-231908.appspot.com/");
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference;

    private String request_friend_memo_info;
    private String request_friend_uid;
    private String request_friend_memo_date_for_save;

    private String memo_contents;
    private String memo_date;
    private String memo_date_for_save;
    private int memo_photo_exist_int;
    private String memo_location;

    TextView friend_memo_date, friend_memo_location, friend_memo_contents;
    ImageView friend_memo_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_friend_memo_click);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        Intent intent = getIntent();
        request_friend_memo_info = intent.getStringExtra("request friend memo info");
        request_friend_uid = request_friend_memo_info.substring(0, request_friend_memo_info.indexOf(","));
        request_friend_memo_date_for_save = request_friend_memo_info.substring(request_friend_memo_info.indexOf(",")+1, request_friend_memo_info.length());

        friend_memo_date = (TextView) findViewById(R.id.request_friend_memo_date_textview);
        friend_memo_location = (TextView) findViewById(R.id.request_friend_memo_location_textview);
        friend_memo_contents = (TextView) findViewById(R.id.request_friend_memo_contents_textview);
        friend_memo_image = (ImageView) findViewById(R.id.request_friend_memo_photo_imageview);

        database.getInstance().getReference("Memo").child(request_friend_uid).child(request_friend_memo_date_for_save).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Memo_ get = dataSnapshot.getValue(Memo_.class);
                memo_photo_exist_int = get.photo_exist;
                memo_date_for_save = get.date;
                memo_contents = get.contents;
                memo_location = get.location;
                memo_date = memo_date_for_save.substring(0,4)+"."+memo_date_for_save.substring(4,6)+"."+memo_date_for_save.substring(6,8)+" "
                        +memo_date_for_save.substring(9,11)+":"+memo_date_for_save.substring(11,13);
                friend_memo_date.setText(memo_date);
                friend_memo_contents.setText(memo_contents);
                friend_memo_location.setText(memo_location);

                if(memo_photo_exist_int == 1) {
                    friend_memo_image.setVisibility(View.VISIBLE);
                    pathReference = storageReference.child(request_friend_uid + "/Memo_photo/" + memo_date_for_save);
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageurl = uri.toString();
                            Glide.with(getApplicationContext())
                                    .load(imageurl)
                                    .into(friend_memo_image);
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
                databaseError.getMessage();
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
