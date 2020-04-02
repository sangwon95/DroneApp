package com.example.firebaesactivity.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.firebaesactivity.DataDTO;
import com.example.firebaesactivity.GpsActivity;
import com.example.firebaesactivity.NewLoginActivity;
import com.example.firebaesactivity.NewRegisterActivity;
import com.example.firebaesactivity.R;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.List;

public class GraphFragment extends Fragment implements View.OnClickListener {

    private View view;
    private LineChart Chart_1,Chart_2,Chart_3;
    public static final int red = -65536;
    public static final int bule =-16776961;
    public static final int green =-16711936;

    public static final String altitude = "고도";
    public static final String temperature = "기온";
    public static final String finedust = "미세먼지";


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebaseDatabase를 사용하기위한 instanec가져오기
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graph, container, false);

        view.findViewById(R.id.update_btn).setOnClickListener(this);

        Chart_1 = view.findViewById(R.id.chart_1);
        Chart_2 = view.findViewById(R.id.chart_2);
        Chart_3 = view.findViewById(R.id.chart_3);


        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.update_btn) {
            //고도
            databaseReference.child("GraphData").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @androidx.annotation.Nullable String s) {
                    DataDTO dataDTO = dataSnapshot.getValue(DataDTO.class);//firebase에서 Data가져오기

                    List<Entry> entries = dataDTO.getentries(); //x,y좌표값
                    addLineChart(entries, red, altitude, 1);
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

            //기온
            databaseReference.child("GraphData2").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @androidx.annotation.Nullable String s) {
                    DataDTO dataDTO = dataSnapshot.getValue(DataDTO.class);//firebase에서 Data가져오기

                    List<Entry> entries = dataDTO.getentries(); //x,y좌표값
                    addLineChart(entries, bule,temperature, 2);
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

            //미세먼지
            databaseReference.child("GraphData3").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @androidx.annotation.Nullable String s) {
                    DataDTO dataDTO = dataSnapshot.getValue(DataDTO.class);//firebase에서 Data가져오기

                    List<Entry> entries = dataDTO.getentries(); //x,y좌표값
                    addLineChart(entries,green,finedust, 3);
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
    }

    private void addLineChart(List<Entry> entries,int color,String label,int chartNumber) {


        LineDataSet lineDataSet = new LineDataSet(entries, label);
        lineDataSet.setLineWidth(2); //그래프 라인 굵기
        lineDataSet.setCircleRadius(3); //그래프 원크기
        lineDataSet.setCircleColor(color);
        lineDataSet.setCircleColorHole(color); //그래프라인 색상
        lineDataSet.setColor(color);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(true);
        LineData lineData = new LineData(lineDataSet);
        lineData.setValueTextSize(20);
        lineData.setValueTextSize(14);

        switch (chartNumber) {
            case 1: {
                Chart_1.setData(lineData);
                XAxis xAxis = Chart_1.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(Color.BLACK);
                //  xAxis.setLabelCount(10,true); //라벨 갯수
                xAxis.enableGridDashedLine(4, 4, 0);//수직점선
                xAxis.setDrawLabels(true);
                xAxis.setDrawAxisLine(true);
                xAxis.setDrawGridLines(true);

                YAxis yLAxis = Chart_1.getAxisLeft();
                yLAxis.setTextColor(Color.BLACK);// y축 글씨 색
                YAxis yRAxis = Chart_1.getAxisRight();
                yRAxis.setDrawLabels(true);
                yRAxis.setDrawAxisLine(true);
                yRAxis.setDrawGridLines(false);
                Chart_1.setDoubleTapToZoomEnabled(false);
                Chart_1.setDrawGridBackground(false);
                //lineChart.setDescription(description);
                Chart_1.animateY(200, Easing.EasingOption.EaseInCubic);
                Chart_1.invalidate();

                break;
            }
            case 2: {
                Chart_2.setData(lineData);
                XAxis xAxis = Chart_2.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(Color.BLACK);
                //  xAxis.setLabelCount(10,true); //라벨 갯수
                xAxis.enableGridDashedLine(4, 4, 0);//수직점선
                xAxis.setDrawLabels(true);
                xAxis.setDrawAxisLine(true);
                xAxis.setDrawGridLines(true);
                YAxis yLAxis = Chart_2.getAxisLeft();
                yLAxis.setTextColor(Color.BLACK);// y축 글씨 색
                YAxis yRAxis = Chart_2.getAxisRight();
                yRAxis.setDrawLabels(true);
                yRAxis.setDrawAxisLine(true);
                yRAxis.setDrawGridLines(false);
                Chart_2.setDoubleTapToZoomEnabled(false);
                Chart_2.setDrawGridBackground(false);
                //lineChart.setDescription(description);
                Chart_2.animateY(200, Easing.EasingOption.EaseInCubic);
                Chart_2.invalidate();
                break;
            }
            case 3: {
                Chart_3.setData(lineData);
                XAxis xAxis = Chart_3.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(Color.BLACK);
                //  xAxis.setLabelCount(10,true); //라벨 갯수
                xAxis.enableGridDashedLine(4, 4, 0);//수직점선
                xAxis.setDrawLabels(true);
                xAxis.setDrawAxisLine(true);
                xAxis.setDrawGridLines(true);
                YAxis yLAxis = Chart_3.getAxisLeft();
                yLAxis.setTextColor(Color.BLACK);// y축 글씨 색
                YAxis yRAxis = Chart_3.getAxisRight();
                yRAxis.setDrawLabels(true);
                yRAxis.setDrawAxisLine(true);
                yRAxis.setDrawGridLines(false);
                Chart_3.setDoubleTapToZoomEnabled(false);
                Chart_3.setDrawGridBackground(false);
                //lineChart.setDescription(description);
                Chart_3.animateY(200, Easing.EasingOption.EaseInCubic);
                Chart_3.invalidate();
                break;
            }

        }



    }

}
