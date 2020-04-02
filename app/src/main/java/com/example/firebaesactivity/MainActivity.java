package com.example.firebaesactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.firebaesactivity.fragment.FinedustFragment;
import com.example.firebaesactivity.fragment.GraphFragment;
import com.example.firebaesactivity.fragment.MapFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentmanager ;
    private FragmentTransaction transaction ;

    private MapFragment frag_map = new MapFragment();;
    private GraphFragment frag_graph = new GraphFragment();
    private FinedustFragment frag_finedust = new FinedustFragment();



    //로그인 모듈
    private FirebaseAuth firebaseAuth;

    //현재 로그인 된 유저 정보
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigationview);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {

                switch (menuItem.getItemId())
                {
                    case R.id.action_map:
                        setFrag(0);
                        break;
                    case R.id.action_graph:
                        setFrag(1);
                        break;
                    case R.id.action_finedust:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

       setFrag(0); // 첫 프래그먼트 화면 지정
    }

    // 프레그먼트 교체
    private void setFrag(int n)
    {
        fragmentmanager = getSupportFragmentManager();
        transaction = fragmentmanager.beginTransaction();
        switch (n)
        {
            case 0:
                transaction .replace(R.id.main_framelayout,frag_map);
                transaction .commit();
                break;

            case 1:
                transaction .replace(R.id.main_framelayout,frag_graph);
                transaction .commit();
                break;

            case 2:
                transaction .replace(R.id.main_framelayout,frag_finedust);
                transaction .commit();
                break;


        }
    }

    //로그인 되어있으면 currentUser 변수에 유저정보 할당. 아닌경우 login 페이지로 이동!
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, NewLoginActivity.class));
            finish();
        }
    }


}
