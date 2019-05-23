package org.androidtown.hotplace;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemoActivity extends AppCompatActivity {

    private static final String TAG = "Memo Activity";

    TextView memo_contents, memo_add_photo, memo_date, memo_location;
    EditText edit_memo_contents;
    ImageView add_to_take_photo, add_to_take_album, memo_photo, memo_photo_delete;

    private String memo_contents_text = "";
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");
    SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd_hhmm");
    private String memo_date_for_save;
    String memo_location_info;
    String memo_save_location;
    double memo_latitude;
    double memo_longitude;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hot-place-231908.appspot.com/");
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference;

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_TAKE_ALBUM = 2;
    private Uri photoUri;
    private String imageFilepath;
    private File photoFile;

    private Boolean isPermission = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        memo_contents =  (TextView) findViewById(R.id.memo_contents);
        memo_add_photo = (TextView) findViewById(R.id.add_photo);
        memo_date = (TextView) findViewById(R.id.memo_date);
        memo_date.setText(TodayDate());
        memo_location = (TextView) findViewById(R.id.memo_location);

        Intent location_intent = getIntent();
        memo_location_info = location_intent.getStringExtra("user location");
        memo_save_location = memo_location_info.substring(0, memo_location_info.indexOf(','));
        memo_latitude = Double.parseDouble(memo_location_info.substring(memo_location_info.indexOf(',')+1, memo_location_info.lastIndexOf(',')));
        memo_longitude = Double.parseDouble(memo_location_info.substring(memo_location_info.lastIndexOf(',')+1, memo_location_info.length()));
        memo_location.setText(memo_save_location);

        edit_memo_contents = (EditText) findViewById(R.id.edit_memo_contents);
        add_to_take_photo = (ImageView) findViewById(R.id.add_to_take_photo);
        add_to_take_album = (ImageView) findViewById(R.id.add_to_take_album);
        memo_photo = (ImageView) findViewById(R.id.memo_photo);
        memo_photo_delete = (ImageView) findViewById(R.id.memo_photo_delete);
        memo_photo_delete.setVisibility(View.INVISIBLE);

        tedPermission();
        add_to_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진을 찍어서 추가하기
                if(isPermission) {
                    sendTakePhotoIntent();
                }
                else
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_SHORT).show();
            }
        });
        add_to_take_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //갤러리에 있는 사진 추가하기
                if(isPermission) {
                    sendTakeAlbumIntent();
                }
                else
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_SHORT).show();
            }
        });
        memo_photo_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //추가한 사진 삭제
                memo_photo.setImageBitmap(null);
                imageFilepath = null;
                photoUri = null;
                memo_photo_delete.setVisibility(View.INVISIBLE);
            }
        });
    }

    //툴바 메뉴 출력
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_memo, menu);
        return true;
    }

    //툴바 메뉴 선택 시 실행
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                //뒤로가기 버튼 클릭시
                memo_contents_text = edit_memo_contents.getText().toString();
                if(!memo_contents_text.isEmpty()) {
                    AlertDialog.Builder compelete_builder = new AlertDialog.Builder(this);
                    compelete_builder.setMessage("작성된 메모는 저장되지 않습니다. 돌아가시겠습니까?").setCancelable(false)
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
                    AlertDialog alert = compelete_builder.create();
                    alert.show();
                } else
                    finish();
                return true;
            }
            case R.id.action_memo_complete: {
                //메모 작성 완료 버튼 클릭시
                AlertDialog.Builder compelete_builder = new AlertDialog.Builder(this);
                compelete_builder.setMessage("메모 작성을 완료하시겠습니까?").setCancelable(false)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //메모 작성 완료 '네' 클릭시
                                CompleteMemo();
                            }
                        }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //메모 작성 완료 '아니요' 클릭시
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = compelete_builder.create();
                alert.show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //메모 생성
    public void CompleteMemo() {
        memo_contents_text = edit_memo_contents.getText().toString();
        if(!isVaildContents()) {
            CreateMemo(memo_contents_text);
        }
    }

    //메모 내용값이 있는지 확인
    private boolean isVaildContents() {
        if(memo_contents_text.isEmpty()){
            Toast.makeText(MemoActivity.this, "메모의 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    //메모 작성 날짜 불러오기 (현재)
    private String TodayDate() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        memo_date_for_save = sFormat.format(mDate);
        return mFormat.format(mDate);
    }

    //데이터베이스에 메모 추가
    private void CreateMemo(String contents) {
        if(photoUri != null) { //만약 photoUri가 null이 아니면(추가한 사진이 있다면)
            //Memo new_memo = new Memo(memo_contents_text, memo_date_for_save, 1, memo_save_location);
            //databaseReference.child("Memo").child(user.getUid()).child(memo_date_for_save).setValue(new_memo);
            Memo_ memo = new Memo_(memo_contents_text, memo_date_for_save, 1, memo_save_location, memo_latitude, memo_longitude);
            databaseReference.child("Memo").child(user.getUid()).child(memo_date_for_save).setValue(memo);
            pathReference = storageReference.child(user.getUid()+"/Memo_photo/"+memo_date_for_save);
            UploadTask uploadTask;
            Uri file = null;
            file = photoUri;
            uploadTask = pathReference.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Photo Upload Fail.");
                    Toast.makeText(MemoActivity.this, "메모 사진 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "Photo Upload Success.");
                    Toast.makeText(MemoActivity.this, "메모가 정상적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else { //만약 photoUri가 null이면(추가한 사진이 없다면)
            //Memo new_memo = new Memo(memo_contents_text, memo_date_for_save, 0, memo_save_location);
            //databaseReference.child("Memo").child(user.getUid()).child(memo_date_for_save).setValue(new_memo);
            Memo_ memo = new Memo_(memo_contents_text, memo_date_for_save, 1, memo_save_location, memo_latitude, memo_longitude);
            databaseReference.child("Memo").child(user.getUid()).child(memo_date_for_save).setValue(memo);
            Toast.makeText(MemoActivity.this, "메모가 정상적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //사진찍기 이미지 선택시
    private void sendTakePhotoIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            if(photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //갤러리 이미지 선택시
    private void sendTakeAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timestamp + ".jpg";
        File storageDir = Environment.getExternalStorageDirectory();

        if(!storageDir.exists())
            storageDir.mkdirs();

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilepath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            memo_photo.setImageBitmap(setImage());
            memo_photo_delete.setVisibility(View.VISIBLE);
        }
        else if(requestCode == REQUEST_TAKE_ALBUM && resultCode == RESULT_OK) {
            photoUri = data.getData();
            Cursor cursor = null;
            try { //Uri 스키마를 content:///에서 file:///로 변경한다.
                String[] proj = { MediaStore.Images.Media.DATA };
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                photoFile = new File(cursor.getString((column_index)));
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            memo_photo.setImageBitmap(setImage());
            memo_photo_delete.setVisibility(View.VISIBLE);
        }
    }

    //이미지 파일을 bitmap으로 변환 후 리사이즈 & 회전
    public Bitmap setImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFile.getAbsolutePath());
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        int exifOrientation;
        int exifDegree;

        if(exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }

        bitmap = rotate(bitmap, exifDegree);

        return bitmap;
    }

    //프로필 이미지 회전시 필요한 각도 측정
    public static int exifOrientationToDegrees(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    //카메라로 찍은 사진이 회전되어 있을 경우 정상으로 회전 - 프로필 이미지 회전
    public Bitmap rotate(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //권한 설정
    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //권한 요청 성공
                isPermission = true;
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //권한 요청 실패
                isPermission = false;
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
}
