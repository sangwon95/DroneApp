package com.example.firebaesactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class NewLoginActivity extends AppCompatActivity {

    private EditText email_edt, pass_edt;
    private LinearLayout login_btn,account_btn;

    //로그인 모듈
    private FirebaseAuth firebaseAuth;
    //현재 로그인 된 유저 정보
    private FirebaseUser currentUser;
    // google login result 상수
    private static final int RC_SIGN_IN=900; //startactivityforresult 응답 코드로 사용
    // google api Client
    private GoogleSignInClient googleSignInClient;
    //google login button
    private SignInButton google_btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        //editText
        email_edt = findViewById(R.id.email_edit);
        pass_edt = findViewById(R.id.password_edit);

        //button
        login_btn = findViewById(R.id.login_Linear_btn);
        account_btn = findViewById(R.id.account_Linear_btn);
        google_btn= findViewById(R.id.google_btn);

        //firebase 인증객체
        firebaseAuth = FirebaseAuth.getInstance();

        //googleSignInOption 객체를 구성할때 requestIdToken을 호출
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        //google login button
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_edt.getText().toString();
                String password = pass_edt.getText().toString();
                if(email!=null||password!=null){
                    joinLogin(email,password);
                }
                else{
                    Toast.makeText(NewLoginActivity.this,"이메일 또는 비밀번호를 작성해주세요." ,Toast.LENGTH_SHORT).show();
                }
            }
        });

        //회원 가입
        account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewLoginActivity.this, NewRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //google login result button
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    //사용자가 정삭적으로 로그인한 후에 GoogleSignInAccout 개체에서 id 토큰을 가져와
    //firebase 사용자 인증 정보로 교환하고 firebase 사용자 인증 정보를 사용해 firebase에 인증한다.
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //로그인 성공
                    Toast.makeText(NewLoginActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();

                    currentUser = firebaseAuth.getCurrentUser();
                    Intent intent = new Intent(NewLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(NewLoginActivity.this, "Google Login 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    //로그인 실패
                    Toast.makeText(NewLoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //Email login join
    private void joinLogin(String email, String password) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(pass_edt.getWindowToken(),0);

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        Toast.makeText(NewLoginActivity.this,"존재하지 않는 id 입니다." ,Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(NewLoginActivity.this,"회원가입 후 로그인 해주세요." ,Toast.LENGTH_SHORT).show();
                    } catch (FirebaseNetworkException e) {
                        Toast.makeText(NewLoginActivity.this,"네트워크 문제 발생" ,Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(NewLoginActivity.this,"Exception" ,Toast.LENGTH_SHORT).show();
                    }
                } else{

                    currentUser = firebaseAuth.getCurrentUser();
                    Intent intent = new Intent(NewLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(NewLoginActivity.this, "환영합니다", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });


    }

    //Check login status
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!= null){
            startActivity(new Intent(NewLoginActivity.this, MainActivity.class));
            finish();
        }

    }
}
