package com.example.firebaesactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewLoginActivity extends AppCompatActivity {

    private EditText email_edt, pass_edt;
    private LinearLayout login_btn,account_btn;


    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        //editText
        email_edt = findViewById(R.id.email_edit);
        pass_edt = findViewById(R.id.password_edit);

        //button
        login_btn = findViewById(R.id.login_Linear_btn);
        account_btn = findViewById(R.id.account_Linear_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_edt.getText().toString();
                String password = pass_edt.getText().toString();

                joinLogin(email, password);
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

    private void joinLogin(String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(NewLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(NewLoginActivity.this, CompleteActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(NewLoginActivity.this, "log failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

    }
}
