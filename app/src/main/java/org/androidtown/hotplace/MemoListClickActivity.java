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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MemoListClickActivity extends AppCompatActivity {

    ImageView memo_photo_imageview;
    TextView memo_date_textview, memo_location_textview;
    EditText memo_contents_edittext;

    private int memo_photo_exist_int;
    private String memo_date;
    private String memo_contents;
    private String memo_date_for_save;
    private String memo_location;

    private String memo_contents_text;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference databaseReference = database.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hot-place-231908.appspot.com/");
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list_click);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        memo_photo_imageview = (ImageView) findViewById(R.id.memo_photo_imageview);
        memo_photo_imageview.setVisibility(View.GONE);
        memo_date_textview = (TextView) findViewById(R.id.memo_date_textview);
        memo_contents_edittext = (EditText) findViewById(R.id.memo_contents_edittext);
        memo_location_textview = (TextView)findViewById(R.id.memo_location_textview);

        Intent intent = getIntent();
        memo_date = intent.getStringExtra("memo date");

        database.getInstance().getReference("Memo").child(user.getUid()).child(memo_date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Memo_ get = dataSnapshot.getValue(Memo_.class);
                memo_photo_exist_int = get.photo_exist;
                memo_date_for_save = get.date;
                memo_contents = get.contents;
                memo_location = get.location;
                memo_date = memo_date_for_save.substring(0,4)+"."+memo_date_for_save.substring(4,6)+"."+memo_date_for_save.substring(6,8)+" "
                        +memo_date_for_save.substring(9,11)+":"+memo_date_for_save.substring(11,13);
                memo_date_textview.setText(memo_date);
                memo_contents_edittext.setText(memo_contents);
                memo_location_textview.setText(memo_location);

                if(memo_photo_exist_int == 1) {
                    memo_photo_imageview.setVisibility(View.VISIBLE);
                    pathReference = storageReference.child(user.getUid() + "/Memo_photo/" + memo_date_for_save);
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageurl = uri.toString();
                            Glide.with(getApplicationContext())
                                    .load(imageurl)
                                    .into(memo_photo_imageview);
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

    //툴바 메뉴 출력
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_memolistclick, menu);
        return true;
    }

    //툴바 메뉴 선택 시 실행
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                //뒤로가기 버튼 클릭시
                memo_contents_text = memo_contents_edittext.getText().toString();
                if(!memo_contents_text.equals(memo_contents)) {
                    AlertDialog.Builder backpress_builder = new AlertDialog.Builder(this);
                    backpress_builder.setMessage("수정된 메모는 저장되지 않습니다. 돌아가시겠습니까?").setCancelable(false)
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //뒤로가기 '네' 클릭시
                                    finish();
                                }
                            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //뒤로가기 '아니요' 클릭시
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = backpress_builder.create();
                    alert.show();
                } else {
                    finish();
                }
                return true;
            }
            case R.id.action_memo_modify_complete: {
                //메모 수정 완료 클릭시
                memo_contents_text = memo_contents_edittext.getText().toString();
                if(!memo_contents_text.equals(memo_contents)) {
                    AlertDialog.Builder compelete_builder = new AlertDialog.Builder(this);
                    compelete_builder.setMessage("메모를 수정하시겠습니까?").setCancelable(false)
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //메모 수정 완료 '네' 클릭시
                                    database.getInstance().getReference("Memo").child(user.getUid()).child(memo_date_for_save).child("contents").setValue(memo_contents_text);
                                    Toast.makeText(getApplicationContext(), "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //메모 수정 완료 '아니요' 클릭시
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = compelete_builder.create();
                    alert.show();
                } else {
                    Toast.makeText(this, "메모 내용을 수정해주세요.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            case R.id.action_memo_delete: {
                AlertDialog.Builder delete_builder = new AlertDialog.Builder(this);
                delete_builder.setMessage("메모를 삭제하시겠습니까? 삭제된 메모는 다시 되돌릴 수 없습니다.").setCancelable(false)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //메모 수정 완료 '네' 클릭시
                                database.getInstance().getReference("Memo").child(user.getUid()).child(memo_date_for_save).removeValue();
                                Toast.makeText(getApplicationContext(), "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //메모 수정 완료 '아니요' 클릭시
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = delete_builder.create();
                alert.show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
