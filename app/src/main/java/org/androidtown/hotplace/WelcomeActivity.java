package org.androidtown.hotplace;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Handler;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;


public class WelcomeActivity extends AppCompatActivity {
    EditText ID, PASSWD;
    Button btnsignup, btnlogin;
    ImageView logo1, logo2;
    TextView signup_textview;
    Animation downtoup, logoupto, showlogin;

    private BackPressCloseHandler backPressCloseHandler;
    FirebaseAuth firebaseAuth;
    private String userId = "";
    private String userPasswd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        backPressCloseHandler = new BackPressCloseHandler(this);
        firebaseAuth = FirebaseAuth.getInstance();

        logo1 = (ImageView) findViewById(R.id.mainlogo1);
        logo2 = (ImageView) findViewById(R.id.mainlogo2);
        logo1.setVisibility(View.INVISIBLE);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.welcome_up_logo1);
        logoupto = AnimationUtils.loadAnimation(this,R.anim.welcome_up_logo2);
        logo2.setAnimation(downtoup);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logo1.setVisibility(View.VISIBLE);
                logo2.setVisibility(View.INVISIBLE);
                logo1.setAnimation(logoupto);
            }
        }, 3000);


        ID = (EditText) findViewById(R.id.loginID);
        PASSWD = (EditText) findViewById(R.id.loginPassword);
        signup_textview = (TextView) findViewById(R.id.signup_textview);
        btnsignup = (Button) findViewById(R.id.signupButton);
        btnlogin = (Button) findViewById(R.id.loginButton);

        showlogin = AnimationUtils.loadAnimation(this,R.anim.welcome_showlogin);
        ID.setAnimation(showlogin);
        PASSWD.setAnimation(showlogin);
        signup_textview.setAnimation(showlogin);
        btnsignup.setAnimation(showlogin);
        btnlogin.setAnimation(showlogin);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignupActivity(v);
            }
        });

    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void login(View view) {
        //데이터베이스에 아이디와 로그인이 맞다면 로그인 성공, 맞지 않다면 실패
        userId = ID.getText().toString();
        userPasswd = PASSWD.getText().toString();

        if(isVaildId() && isVaildPasswrd()) {
            loginUser(userId, userPasswd);
        }
    }
    public void loginUser(String id, String passwd) {
        firebaseAuth.signInWithEmailAndPassword(id, passwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //로그인 성공
                            finish();
                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            //로그인 실패
                            Toast.makeText(WelcomeActivity.this, "아이디 혹은 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void openSignupActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //이메일 유효성 검사
    private boolean isVaildId() {
        if(userId.isEmpty()) {
            //이메일 공백
            Toast.makeText(WelcomeActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    //비밀번호 유효성 검사
    private boolean isVaildPasswrd() {
        if(userPasswd.isEmpty()) {
            //비밀번호 공백
            Toast.makeText(WelcomeActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
