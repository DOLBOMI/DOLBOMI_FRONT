package com.example.dolbomi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.graphics.Color;
import android.graphics.fonts.Font;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeStatus extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_home);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        getValue();

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("내 집 상태");

        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
    }

    private void getValue() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = simpleDate.format(date);

        final DatabaseReference temperature = mDatabase.child("Home1").child(getTime).child("temperature");
        final DatabaseReference humidity = mDatabase.child("Home1").child(getTime).child("humidity");
        final DatabaseReference gas = mDatabase.child("Home1").child(getTime).child("gas");

        setContentView(R.layout.status_home);
        TextView temp_text = (TextView) findViewById(R.id.temptext);
        TextView humid_text = (TextView) findViewById(R.id.humidtext);
        TextView gas_text = (TextView) findViewById(R.id.gastext);

        temperature.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String temp = "온도 " + snapshot.getValue().toString() + "°C";
                temp_text.setText(temp);

                //if (snapshot.exists()) { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        humidity.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String humidity = "습도 " + snapshot.getValue().toString() + "%";
                humid_text.setText(humidity);

                //if (snapshot.exists()) { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        gas.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gas = "가스 농도 " + snapshot.getValue().toString() ;
                gas_text.setText(gas);

                //if (snapshot.exists()) { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}