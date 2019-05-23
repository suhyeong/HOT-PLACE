package org.androidtown.hotplace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MemoListActivity extends AppCompatActivity {

    TextView memo_list_count;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference databaseReference = database.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hot-place-231908.appspot.com/");
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    ArrayList<String> Array = new ArrayList<String>();

    private String memo_contents;
    private String memo_date_for_save;
    private String memo_date;
    private int memo_photo_exist_int;
    private String memo_location;
    private double memo_latitude;
    private double memo_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        listView = (ListView) findViewById(R.id.memo_listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);
        memo_list_count = (TextView) findViewById(R.id.memo_list_count);
        memo_list_count.setVisibility(View.GONE);


        database.getInstance().getReference("Memo").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                if(dataSnapshot.getChildrenCount() == 0) {
                    memo_list_count.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
                for(DataSnapshot memoSnapshot : dataSnapshot.getChildren()) {
                    Memo_ memo = memoSnapshot.getValue(Memo_.class);
                    memo_date_for_save = memo.date;
                    memo_contents = memo.contents;
                    memo_location = memo.location;
                    memo_photo_exist_int = memo.photo_exist;
                    memo_latitude = memo.location_Latitude;
                    memo_longitude = memo.location_Longitude;

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
                Intent intent = new Intent(getApplicationContext(), MemoListClickActivity.class);
                intent.putExtra("memo date", String.valueOf(Array.get(position)));
                startActivity(intent);
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
