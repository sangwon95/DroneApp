package com.example.firebaesactivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GpsActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebaseDatabase를 사용하기위한 instanec가져오기
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private Button gps_send_btn, gps_get,gps_return_btn;//xml에서 가져온 button
    private TextView edit_text_1, edit_text_2;//xml에서 가져온 textView

    static double longitude = 0.0,latitude=0.0 ;// 위도,경도 전역변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //textView
        edit_text_1 = findViewById(R.id.text_view1);
        edit_text_2 = findViewById(R.id.text_view2);
        //button
        gps_send_btn = findViewById(R.id.gps_send_btn);
        gps_get = findViewById(R.id.gps_get);

        gps_return_btn=findViewById(R.id.gps_return_btn);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //자신의 위치 서비스 선언

       //get 버튼리스너의경우 get버튼을 누르면 자신의 위치(위도,경도)를 가져와 longitude,latitude 각 전역변수에 대입
        gps_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GpsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                } else {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    latitude= location.getLongitude(); //위도
                    longitude = location.getLatitude();//경도

                }
            }
        });

        //send 버튼 리스너의 경우 firebase 에 보내기위한 버튼
        gps_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsDTO GPS = new GpsDTO(longitude,latitude); // GpsDTO 클래스를 따로 만들어주고  2개의 변수를 넘겨준다.
                databaseReference.child("GPS").push().setValue(GPS);// firebase에 데이터 푸쉬

            }
        });

        //return 버튼 리스너의 경우 firebase를 실시간으로 변화된 데이터를 가져와 각 editText에 send로 보냈던 위도 경도값이 보여주게 된다.
        gps_return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("GPS").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        GpsDTO gpsDTO= dataSnapshot.getValue(GpsDTO.class);//firebase에서 위도 경도값 가지고 오기

                        String gps_Change_1= Double.toString(gpsDTO.getlatitude()); //editext는 String형만 보여주기때문에 Double형을 String형으로 변환
                        String gps_Change_2 = Double.toString(gpsDTO.getlongitude());

                        edit_text_1.setText(gps_Change_1); //txet를  보여준다.
                        edit_text_2.setText(gps_Change_2);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });





    }
}
