package com.example.dolbomi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolbomi.paging.LakuePagingButton;
import com.example.dolbomi.paging.OnPageSelectListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExerciseDetail extends AppCompatActivity implements LocationListener {
    BarChart barChart;
    int yesterday = 7300;
    int selectDay = 8000;
    int tomorrow = 5000;

    String yesterdayDate = "21/07/21";
    String selectedDate = "21/07/22";
    String tomorrowDate = "21/07/23";

    Intent manboService;
    BroadcastReceiver receiver;
    String serviceData;
    TextView countText;

    private LocationManager locationManager;
    private Location mLastlocation = null;
    private double totalLocation = 0;
    private double deltaTime = 0;
    private TextView tvTimeDif, tvDistDif, tvCalDif;
    private double speed;
    private double calorie = 0;

    public void BarChartGraph() {
        // BarChart 메소드
        barChart=findViewById(R.id.step_bar_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, yesterday));
        entries.add(new BarEntry(1, selectDay));
        entries.add(new BarEntry(2, tomorrow));

        int[] BARCHART_COLOR = {
                Color.rgb(89, 172, 155), Color.rgb(20, 106, 89), Color.rgb(89, 172, 155),
        };

        BarDataSet depenses = new BarDataSet (entries, "일일 걸음 수");
        depenses.setAxisDependency(YAxis.AxisDependency.LEFT);
        depenses.setColors(BARCHART_COLOR);

        BarData data = new BarData(depenses);
        data.setValueTextSize(15f);

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setEnabled(false);
        barChart.getDescription().setEnabled(false); //label description 삭제
        //barChart.getLegend().setEnabled(false); // 범례 삭제

        barChart.setData(data);
        barChart.animateXY(1000,1000);
        barChart.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("상세 데이터");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        BarChartGraph();
        barChart.setTouchEnabled(false);//확대하지못하게 막아버림

        TextView labelView = (TextView)findViewById(R.id.date_label);
        labelView.setText(yesterdayDate+"          "+selectedDate+"          "+tomorrowDate);

        manboService = new Intent(this, StepCheckService.class);
        receiver = new PlayingReceiver();
        countText = (TextView) findViewById(R.id.stepText);
        try {
            IntentFilter mainFilter = new IntentFilter("make.a.yong.manbo");
            registerReceiver(receiver, mainFilter);
            startService(manboService);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        tvTimeDif = (TextView)findViewById(R.id.tvTimeDif);
        tvDistDif = (TextView)findViewById(R.id.tvDistDif);
        tvCalDif = (TextView)findViewById(R.id.calorie);
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formatDate = sdf.format(new Date(lastKnownLocation.getTime()));
        }
        // GPS 사용 가능 여부 확인
        boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,0, this);

        LakuePagingButton lakuePagingButton = findViewById(R.id.lpb_buttonlist);
        lakuePagingButton.setPageItemCount(5);
        lakuePagingButton.addBottomPageButton(50, 1);
        int max_page = 50;
        lakuePagingButton.setOnPageSelectListener(new OnPageSelectListener() {
            //PrevButton Click
            @Override
            public void onPageBefore(int now_page) {
                //prev 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                lakuePagingButton.addBottomPageButton(max_page,now_page);
                Toast.makeText(ExerciseDetail.this, ""+now_page, Toast.LENGTH_SHORT).show();
                //해당 페이지에 대한 소스 코드 작성
                //...
            }

            @Override
            public void onPageCenter(int now_page) {
                Toast.makeText(ExerciseDetail.this, ""+now_page, Toast.LENGTH_SHORT).show();
                //해당 페이지에 대한 소스 코드 작성
                //TO DO: 페이지 이동에 따른 그래프와 textView값 변경
            }

            //NextButton Click
            @Override
            public void onPageNext(int now_page) {
                //next 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                lakuePagingButton.addBottomPageButton(max_page,now_page);
                Toast.makeText(ExerciseDetail.this, ""+now_page, Toast.LENGTH_SHORT).show();
                //해당 페이지에 대한 소스 코드 작성
                //...
            }
        });
    }


    class PlayingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (Intent.ACTION_DATE_CHANGED.equals(action)) {
                // 날짜가 변경된 경우 해야 될 작업
                unregisterReceiver(receiver);
                stopService(manboService);
                deltaTime = 0;
                calorie = 0;
                totalLocation = 0;
            }
            Log.i("PlayignReceiver", "IN");
            serviceData = intent.getStringExtra("stepService");
            countText.setText(serviceData);
            //Toast.makeText(getApplicationContext(), "Playing game", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onLocationChanged(Location location) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        //  getSpeed() 함수를 이용하여 속도를 계산
        double getSpeed = Double.parseDouble(String.format("%.3f", location.getSpeed()));
        String formatDate = sdf.format(new Date(location.getTime()));
        // 위치 변경이 두번째로 변경된 경우 계산에 의해 속도 계산
        if(mLastlocation != null) {
            //시간 간격
            deltaTime += (location.getTime() - mLastlocation.getTime()) / 1000.0;
            calorie += deltaTime/30.0;
            tvCalDif.setText(calorie + "kcal");
            tvTimeDif.setText((deltaTime/60) + " 분");  // Time Difference
            totalLocation += mLastlocation.distanceTo(location);
            tvDistDif.setText(totalLocation + " m");  // Time Difference
            // 속도 계산
            speed = mLastlocation.distanceTo(location) / deltaTime;
            String formatLastDate = sdf.format(new Date(mLastlocation.getTime()));
            double calSpeed = Double.parseDouble(String.format("%.3f", speed));
        }
        // 현재위치를 지난 위치로 변경
        mLastlocation = location;
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 위치정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,0, this);
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 위치정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,0, this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 위치정보 가져오기 제거
        locationManager.removeUpdates(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
    }
}
