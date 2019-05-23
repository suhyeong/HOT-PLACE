package org.androidtown.hotplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getInstance().getReference();

    RadioGroup radioGroup;
    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        radioGroup = (RadioGroup) findViewById(R.id.memo_open_range_radiogroup);
        switchCompat = (SwitchCompat) findViewById(R.id.location_open_range_switch);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.memo_open_all:
                        databaseReference.child("user_info").child(user.getUid()).child("userMemoOpenRange").setValue(0);
                        Toast.makeText(SettingsActivity.this, "메모를 전체 공개로 설정하였습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.memo_open_friends:
                        databaseReference.child("user_info").child(user.getUid()).child("userMemoOpenRange").setValue(1);
                        Toast.makeText(SettingsActivity.this, "메모를 친구 공개로 설정하였습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.memo_not_open:
                        databaseReference.child("user_info").child(user.getUid()).child("userMemoOpenRange").setValue(2);
                        Toast.makeText(SettingsActivity.this, "메모를 비공개로 설정하였습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //체크 상태시
                    databaseReference.child("user_info").child(user.getUid()).child("userLocationOpenRange").setValue(true);
                    Toast.makeText(SettingsActivity.this, "회원님의 위치를 공개로 설정하였습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    //체크 상태 해제시
                    databaseReference.child("user_info").child(user.getUid()).child("userLocationOpenRange").setValue(false);
                    Toast.makeText(SettingsActivity.this, "회원님의 위치를 비공개로 설정하였습니다.", Toast.LENGTH_SHORT).show();
                }
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
}
