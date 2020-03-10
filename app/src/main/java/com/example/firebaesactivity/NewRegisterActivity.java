package com.example.firebaesactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.util.regex.Pattern;

public class NewRegisterActivity extends AppCompatActivity {

    private EditText id_edt_acc, pass_edt_acc, name_edt_acc,code_edit_acc;
    private Button overlap_check_btn;
    private LinearLayout account_complete_btn,cancel_btn;

    private String email = "";
    private String password = "";
    private String name = "";

    private FirebaseAuth firebaseAuth;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
/*
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebaseDatabase를 사용하기위한 instanec가져오기
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);

        //Edit Text
        id_edt_acc = findViewById(R.id.email_edit_acc);
        pass_edt_acc = findViewById(R.id.pass_edit_acc);
        name_edt_acc = findViewById(R.id.name_edit_acc);
        code_edit_acc = findViewById(R.id.code_edit_acc);

        //Button
        overlap_check_btn = findViewById(R.id.overlap_check_btn);
        account_complete_btn = findViewById(R.id.account_Linear_btn);
        cancel_btn = findViewById(R.id.cancel_Linear_acc);

         firebaseAuth = FirebaseAuth.getInstance();


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewRegisterActivity.this, NewLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        account_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = id_edt_acc.getText().toString();
                password = pass_edt_acc.getText().toString();
                name = name_edt_acc.getText().toString();
                if(isValidEamil()&&isValidPasswd())
                createUser(email, password);
                //sendData(email,password,name);
            }
        });
    }

    private boolean isValidPasswd() {
        if (email.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"이메일이 비어 있습니다.",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(getApplicationContext(),"이메일 형식이 아닙니다.",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    private boolean isValidEamil() {
        if (password.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"비밀번호가 비어 있습니다.",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(password).matches())
        {
            Toast.makeText(getApplicationContext(),"비밀번호가 형식이 아닙니다.",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(NewRegisterActivity.this,NewLoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // 회원가입 실패
                            Toast.makeText(getApplicationContext(),"회원가입 실패 하였습니다.재시도 바랍니다.",Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }



}
