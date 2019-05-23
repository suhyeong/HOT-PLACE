package org.androidtown.hotplace;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "Sign Up Activity";

    ImageView I1;
    Animation showtext;
    TextView email, passwd, name, birth;
    EditText enteremail, enterpasswd, entername, enterbirth;
    Button backbtn, succbtn;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{8,20}$");
    private static final Pattern BIRTH_PATTERN = Pattern.compile("^[0-9]{8}$");

    private String userEmail ="";
    private String userPasswd ="";
    private String userName = "";
    private String userBirth="";
    private int userProfile = 0;
    private int userMemoOpenRange = 0;
    private boolean userLocationOpenRange = true;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        I1 = (ImageView) findViewById(R.id.signup_page);
        backbtn = (Button) findViewById(R.id.backButton);
        succbtn = (Button) findViewById(R.id.successButton);

        email = (TextView) findViewById(R.id.useremail);
        passwd = (TextView) findViewById(R.id.userpassword);
        name = (TextView) findViewById(R.id.username);
        birth = (TextView) findViewById(R.id.userbirth);
        enteremail = (EditText) findViewById(R.id.signupEmail);
        enterpasswd = (EditText) findViewById(R.id.signupPassword);
        entername = (EditText) findViewById(R.id.signupName);
        enterbirth = (EditText) findViewById(R.id.signupBirth);

        email.setTypeface(email.getTypeface(), Typeface.BOLD);
        passwd.setTypeface(passwd.getTypeface(), Typeface.BOLD);
        name.setTypeface(name.getTypeface(), Typeface.BOLD);
        birth.setTypeface(birth.getTypeface(), Typeface.BOLD);

        showtext = AnimationUtils.loadAnimation(this, R.anim.signup_showsignup);
        email.setAnimation(showtext);
        passwd.setAnimation(showtext);
        name.setAnimation(showtext);
        birth.setAnimation(showtext);
        enteremail.setAnimation(showtext);
        enterpasswd.setAnimation(showtext);
        entername.setAnimation(showtext);
        enterbirth.setAnimation(showtext);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        succbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(v);
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public void signUp(View view) {
        userEmail = enteremail.getText().toString();
        userPasswd = enterpasswd.getText().toString();
        userName = entername.getText().toString();
        userBirth = enterbirth.getText().toString();

        if(isVaildId() && isVaildPasswrd() && isVaildName() && isVaildBirth()) {
            createUser(userEmail, userPasswd, userName, userBirth);

            //databaseReference.child("user_info").child(userEmail).setValue(newuser); -> userEmail 때문에 에러가 난다.
            //child("userEmail").setValue(userEmail) 이런 식으로 하면 에러 안 남
        }
    }

    //이메일 유효성 검사
    private boolean isVaildId() {
        if(userEmail.isEmpty()) {
            //이메일 공백
            Toast.makeText(SignUpActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    //비밀번호 유효성 검사
    private boolean isVaildPasswrd() {
        if(userPasswd.isEmpty()) {
            //비밀번호 공백
            Toast.makeText(SignUpActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(userPasswd).matches()) {
            //비밀번호 형식 불일치
            Toast.makeText(SignUpActivity.this,"비밀번호는 8자 이상 20자 이하의 영어와 숫자 조합이여야 합니다.",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //이름 유효성 검사
    private boolean isVaildName() {
        if (userName.isEmpty()) {
            //이름 공백
            Toast.makeText(SignUpActivity.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    //생년월일 유효성 검사
    private boolean isVaildBirth() {
        if (userBirth.isEmpty()) {
            //생년월일 공백
            Toast.makeText(SignUpActivity.this, "생년월일을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!BIRTH_PATTERN.matcher(userBirth).matches()) {
            //생년월일 형식 불일치
            Toast.makeText(SignUpActivity.this, "생년월일의 형식이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //회원가입
    private void createUser(String email, String passwd, String name, String birth) {
        firebaseAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //회원가입 성공
                            FirebaseUser user = task.getResult().getUser();
                            User NewUser = new User(userEmail, userName, userBirth, userProfile, userMemoOpenRange, userLocationOpenRange);
                            databaseReference.child("user_info").child(user.getUid()).setValue(NewUser);
                            Toast.makeText(SignUpActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            SignUpActivity.super.onBackPressed();
                        } else {
                            //회원가입 실패
                            Toast.makeText(SignUpActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
