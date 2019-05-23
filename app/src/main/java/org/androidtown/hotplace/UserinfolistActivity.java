package org.androidtown.hotplace;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class UserinfolistActivity extends AppCompatActivity {

    private static final String TAG = "User info list Activity";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getInstance().getReference();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hot-place-231908.appspot.com/");
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference;

    Dialog DialogPopup;
    TextView takephotobtn, takealbumbtn, resetprofilebtn, cancelbtn;
    EditText passwd_modify, passwd_mod_chk;
    TextView profile_change, username_text, useremail_text, userbirth_text, dropoutbtn;
    CircleImageView userprofile;

    private String newPassword = "";
    private String newPasswordChk = "";

    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    private File tempFile;
    Uri photoUri;
    String currentPhotoPath;
    String mImageCaptureName;
    String profilefilename = "ProfilePhoto";

    String name_str;
    String email_str;
    String birth_str;
    int profile_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfolist);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        DialogPopup = new Dialog(this);
        userprofile = (CircleImageView) findViewById(R.id.user_profile_image);

        profile_change = (TextView) findViewById(R.id.profile_change);
        username_text = (TextView) findViewById(R.id.user_name_text);
        useremail_text = (TextView) findViewById(R.id.user_email_text);
        userbirth_text = (TextView) findViewById(R.id.user_birth_text);
        dropoutbtn = (TextView) findViewById(R.id.user_drop_out);

        passwd_modify = (EditText) findViewById(R.id.user_passwd_modify);
        passwd_mod_chk = (EditText) findViewById(R.id.user_passwd_modify_check);

        database.getInstance().getReference("user_info").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user_ = dataSnapshot.getValue(User.class);
                name_str = user_.userName;
                email_str = user_.userEmail;
                birth_str = user_.userBirth;
                profile_state = user_.userProfile;

                char birth[] = birth_str.toCharArray();
                String birth_year = "";
                String birth_month = "";
                String birth_day = "";
                for(int i=0;i<birth_str.length();i++) {
                    if(i >= 0 && i <= 3)
                        birth_year += Character.toString(birth[i]);
                    else if(i == 4 || i == 5)
                        birth_month += Character.toString(birth[i]);
                    else if(i == 6 || i == 7)
                        birth_day += Character.toString(birth[i]);
                }
                String final_birth = birth_year+"년 "+birth_month+"월 "+birth_day+"일";
                username_text.setText(name_str);
                useremail_text.setText(email_str);
                userbirth_text.setText(final_birth);

                if (profile_state == 0)
                    userprofile.setImageResource(R.drawable.user_defaultprofile_gray);
                else {
                    pathReference = storageReference.child(user.getUid()+"/ProfileImage/ProfilePhoto");
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageurl = uri.toString();
                            Glide.with(getApplicationContext())
                                    .load(imageurl)
                                    .into(userprofile);
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

        profile_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionDialog();
            }
        });

        dropoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropOut();
            }
        });
    }

    //프로필 사진 바꾸기 클릭시 다이어로그 띄우기
    public void showOptionDialog() {
        DialogPopup.setContentView(R.layout.userinfo_change_profile_dialog);
        takephotobtn = (TextView) DialogPopup.findViewById(R.id.dialog_takephoto);
        takealbumbtn = (TextView) DialogPopup.findViewById(R.id.dialog_takealbum);
        resetprofilebtn = (TextView) DialogPopup.findViewById(R.id.dialog_resetprofile);
        cancelbtn = (TextView) DialogPopup.findViewById(R.id.dialog_cancel);

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPopup.dismiss();
            }
        });

        DialogPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogPopup.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = DialogPopup.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        DialogPopup.getWindow().setAttributes(params);
        DialogPopup.show();

        tedPermission();
        takephotobtn.setOnClickListener(new View.OnClickListener() { //'사진 찍기' 클릭시
            @Override
            public void onClick(View view) {
                if(isPermission) {
                    DoTakePhotoAction();
                    DialogPopup.dismiss();
                }
                else
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_SHORT).show();
            }
        });
        takealbumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermission) {
                    DoTakeAlbumAction();
                    DialogPopup.dismiss();
                }
                else
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_SHORT).show();
            }
        });
        resetprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resetprofilephoto();
            }
        });
    }

    //'사진 찍기' 실행
    public void DoTakePhotoAction() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                tempFile = null;
                try {
                    tempFile = createImageFile();
                } catch (IOException et) {
                    Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                    et.printStackTrace();
                }
                if (tempFile != null) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, tempFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } else {
                        photoUri = Uri.fromFile(tempFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    }
                }
            } else {
                Toast.makeText(this, "외장 메모리를 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "저장소 권한 설정에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //'라이브러리에서 선택' 실행
    public void DoTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    //'현재 사진 삭제' 실행
    public void Resetprofilephoto() {
        if(profile_state != 0) {
            pathReference = storageReference.child(user.getUid()+"/ProfileImage/"+profilefilename);
            pathReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"Profile Photo Delete Success.");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Profile Photo Delete Fail.");
                    e.printStackTrace();
                }
            });
            databaseReference.child("user_info").child(user.getUid()).child("userProfile").setValue(0);
            userprofile.setImageResource(R.drawable.user_defaultprofile_gray);
            Toast.makeText(this,"현재 프로필 사진을 삭제하였습니다.",Toast.LENGTH_SHORT).show();
            DialogPopup.dismiss();
        } else if(profile_state == 0) {
            Toast.makeText(this,"현재 기본 프로필입니다.",Toast.LENGTH_SHORT).show();
            DialogPopup.dismiss();
        }
    }

    //툴바 메뉴 출력
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_userinfo, menu);
        return true;
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
            case R.id.action_modify_complete: {
                //개인정보 수정 버튼 클릭시
                newPassword = passwd_modify.getText().toString();
                newPasswordChk = passwd_mod_chk.getText().toString();

                if(isVaildnewPasswd()) {
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Log.d(TAG, "User Password Update.");
                                        Toast.makeText(UserinfolistActivity.this, "비밀번호가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            Toast.makeText(this,"취소되었습니다.",Toast.LENGTH_SHORT).show();
            if(tempFile != null) {
                if(tempFile.exists()) {
                    if(tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + "Delete Complete");
                        tempFile = null;
                    }
                }
            }

            return;
        }
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                photoUri = data.getData();
                Cursor cursor = null;
                try { //Uri 스키마를 content:///에서 file:///로 변경한다.
                    String[] proj = { MediaStore.Images.Media.DATA };
                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    tempFile = new File(cursor.getString((column_index)));
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
                setImage();
                pathReference = storageReference.child(user.getUid()+"/ProfileImage/"+profilefilename);
                UploadTask uploadTask;
                Uri file = null;
                file = photoUri;
                uploadTask = pathReference.putFile(file);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Photo Upload Fail.");
                        e.printStackTrace();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Photo Upload Success.");
                        profile_state += 1;
                        databaseReference.child("user_info").child(user.getUid()).child("userProfile").setValue(profile_state);
                        Toast.makeText(getApplicationContext(),"프로필 사진이 수정되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case PICK_FROM_CAMERA: {
                setImage();
                pathReference = storageReference.child(user.getUid()+"/ProfileImage/"+profilefilename);
                UploadTask uploadTask;
                Uri file = null;
                file = Uri.fromFile(new File(currentPhotoPath));
                uploadTask = pathReference.putFile(file);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Photo Upload Fail.");
                        e.printStackTrace();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Photo Upload Success.");
                        profile_state += 1;
                        databaseReference.child("user_info").child(user.getUid()).child("userProfile").setValue(profile_state);
                        Toast.makeText(getApplicationContext(),"프로필 사진이 수정되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
        }
    }

    //폴더 및 파일 만들기
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".jpg";

        File storageDir = Environment.getExternalStorageDirectory();
        if(!storageDir.exists())
            storageDir.mkdirs();

        File curFile = new File(storageDir, mImageCaptureName);
        currentPhotoPath = curFile.getAbsolutePath();

        return curFile;
    }

    //이미지 파일을 bitmap으로 변환 후 리사이즈 & 회전
    public void setImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(tempFile.getAbsolutePath());
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

        bitmap = resize(bitmap);
        bitmap = rotate(bitmap, exifDegree);
        //userprofile.setImageBitmap(rotate(bitmap,exifDegree));
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

    //비트맵 사진 리사이징
    public Bitmap resize(Bitmap original) {
        int resizeWidth = 400;
        double aspec = (double) original.getHeight() / (double)original.getWidth();
        int targetHeight = (int)(resizeWidth * aspec);
        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if(result != original) {
            original.recycle();
        }
        return result;
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

    //회원 탈퇴
    public void DropOut() {
        final android.app.AlertDialog.Builder dropout_builder = new android.app.AlertDialog.Builder(this);
        dropout_builder.setTitle("정말로 탈퇴하시겠습니까?")
                .setMessage("진행하신다면 가입 시 기재한 회원의 정보가 전부 사라집니다. 탈퇴 후 어플은 다시 실행됩니다.").setCancelable(false)
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //회원탈퇴 '네' 클릭시
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Log.d(TAG, "User account deleted.");
                                            databaseReference.child("user_info").child(user.getUid()).removeValue();
                                            Toast.makeText(getApplicationContext(),"회원탈퇴가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(UserinfolistActivity.this, WelcomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);
                                            //moveTaskToBack(true);
                                            //finish();
                                            //android.os.Process.killProcess(android.os.Process.myPid());
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "User account deletion was fail.");
                                Toast.makeText(getApplicationContext(),"회원탈퇴에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        });
                    }
                }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //회원탈퇴 '아니요' 클릭시
                dialogInterface.dismiss();
            }
        });
        android.app.AlertDialog alert = dropout_builder.create();
        alert.show();
    }

    //비밀번호 수정시 비밀번호 유효성 검사
    private boolean isVaildnewPasswd() {
        if (newPassword.isEmpty() || newPasswordChk.isEmpty()) {
            //새 비밀번호 입력 공백
            Toast.makeText(this, "수정할 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!newPassword.matches(newPasswordChk)) {
            Toast.makeText(this,"비밀번호가 알맞지 않습니다.",Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }
}
