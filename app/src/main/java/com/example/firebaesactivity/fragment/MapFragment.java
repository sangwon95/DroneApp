package com.example.firebaesactivity.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.example.firebaesactivity.GpsActivity;
import com.example.firebaesactivity.MainActivity;
import com.example.firebaesactivity.NewLoginActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.firebaesactivity.GpsDTO;
import com.example.firebaesactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;


public class MapFragment extends Fragment implements OnMapReadyCallback,View.OnClickListener {

    private MapView mapView = null;
    private View view;
    private GoogleMap googleMap;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    //현재 로그인 된 유저 정보
    private FirebaseUser currentUser;
    //로그인 모듈
    private FirebaseAuth firebaseAuth;

    private GoogleApiClient mGoogleApiClient;

    public MapFragment() {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView =view.findViewById(R.id.map); //google map view
        mapView.getMapAsync(this);

        view.findViewById(R.id.Drone_location).setOnClickListener(this); //drone btn
        view.findViewById(R.id.logout_btn).setOnClickListener(this); //logout btn
        view.findViewById(R.id.test_view).setOnClickListener(this); //logout btn
        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        if(checkPermission()){
            googleMap.setMyLocationEnabled(true);
        }
        else{
            requsetPermission();
        }

        //현재 위치 가져와 초기 화면 설정
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                Double longitude = location.getLongitude();// 위도
                Double latitude = location.getLatitude();// 경도

                CameraUpdate zoom = CameraUpdateFactory.zoomTo(1);
                googleMap.animateCamera(zoom);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));

                LatLng position = new LatLng(latitude, longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    gpsLocationListener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    gpsLocationListener);
        } catch(SecurityException e){
            e.printStackTrace();
        }

       googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    //Drone Image check evnet
    @Override
    public void onClick(View v) {
        googleMap.clear();

        int id = v.getId();
        if(id == R.id.test_view){
            startActivity(new Intent(getActivity(), GpsActivity.class));
        }
        if(id == R.id.Drone_location) {
            addMarker();
        }
        if(id == R.id.logout_btn){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("로그 아웃");
            builder.setMessage("로그 아웃하시겠습까?");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                       FirebaseAuth.getInstance().signOut();
                  if (currentUser == null) {
                        startActivity(new Intent(getActivity(), NewLoginActivity.class));
                        dialog.dismiss();
                    }else{
                        dialog.dismiss();
                    }
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                    // Event
                }
            });
            // Create the AlertDialog
            builder.show();
        }

    }

    private void addMarker() {
        //Firebase loacation get
        databaseReference.child("GPS").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @androidx.annotation.Nullable String s) {
                GpsDTO gpsDTO = dataSnapshot.getValue(GpsDTO.class);

                Double Marker_latitude = gpsDTO.getlatitude();
                Double Marker_longitude = gpsDTO.getlongitude();

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Marker_latitude,Marker_longitude))
                        .title("Drone")
                        .snippet("(" + Marker_latitude + " , " + Marker_longitude + ")")
                        .draggable(true));


                CameraUpdate zoom = CameraUpdateFactory.zoomTo(1);
                googleMap.animateCamera(zoom);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Marker_latitude, Marker_longitude)));

                LatLng position = new LatLng(Marker_latitude, Marker_longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @androidx.annotation.Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @androidx.annotation.Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean checkPermission() {
        return(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void requsetPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPermission()) {
                        googleMap.setMyLocationEnabled(true);
                    } else {
                    }
                    break;
                }
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    //기존 데이터 유지
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    //액티비티가 처음 생성될 때 실행되는 함수
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }

    }
}

