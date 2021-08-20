package com.example.dolbomi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolbomi.paging.LakuePagingButton;
import com.example.dolbomi.paging.OnPageSelectListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class ExerciseDetail extends AppCompatActivity implements SensorEventListener, LocationListener {
    BarChart barChart;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    TextView tvStepDetector, tvGpsEnable, tvGpsLatitude, tvGpsLongitude, tvTimeDif, tvDistDif, tvEndLatitude, tvEndLongitude, tvNowLatitude, tvNowLongitude;
    private int mStepDetector = 0;
    private boolean isGPSEnable = false;
    private LocationManager locationManager;
    private Location lastKnownLocation = null;
    private Location nowLastlocation = null;

    int yesterday = 7300;
    int selectDay = 8000;
    int tomorrow = 5000;
    int calorie = 0;

    String yesterdayDate = "21/07/21";
    String selectedDate = "21/07/22";
    String tomorrowDate = "21/07/23";

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

        /*tvGpsEnable = (TextView)findViewById(R.id.tvGpsEnable);
        tvGpsLatitude = (TextView)findViewById(R.id.tvGpsLatitude);
        tvGpsLongitude = (TextView)findViewById(R.id.tvGpsLongitude);
        tvEndLatitude = (TextView)findViewById(R.id.tvEndLatitude);
        tvEndLongitude = (TextView)findViewById(R.id.tvEndLongitude);
        tvNowLatitude = (TextView)findViewById(R.id.tvNowLatitude);
        tvNowLongitude = (TextView)findViewById(R.id.tvNowLongitude);*/
        tvTimeDif = (TextView)findViewById(R.id.tvTimeDif);
        tvDistDif = (TextView)findViewById(R.id.tvDistDif);
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // GPS 사용 가능 여부 확인
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //tvGpsEnable.setText("GPS Enable: " + isGPSEnable);  //GPS Enable
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //step -----
        tvStepDetector = (TextView)findViewById(R.id.tvStepDetector);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(stepDetectorSensor == null) {
            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }
        //TO DO : 칼로리 계산
        //calorie = 
        TextView textView4 = (TextView)findViewById(R.id.textView15);
        textView4.setText(calorie+"Kcal");

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
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if(event.values[0] == 1.0f) {
                double longWork = getGPSLocation();
                if(longWork > 0.05) {
                    mStepDetector++;
                    tvStepDetector.setText("걸음 수 : " + String.valueOf(mStepDetector));
                    tvTimeDif.setText("Return 간격 : " + String.format("%.3f",longWork) + " m");  // Time Difference
                }
            }
        }
    }
    public double getGPSLocation() {
        double deltaTime = 0.0;
        double deltaDist = 0.0;
        //GPS Start
        if(isGPSEnable) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return 0.0;
            }
//            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation == null ) {
                lastKnownLocation = nowLastlocation;
            }
            if (lastKnownLocation != null && nowLastlocation != null) {
                double lat1 = lastKnownLocation.getLatitude();
                double lng1 = lastKnownLocation.getLongitude();
                double lat2 = nowLastlocation.getLatitude();
                double lng2 = nowLastlocation.getLongitude();
                deltaTime = (nowLastlocation.getTime() - lastKnownLocation.getTime()) / 1000.0;  //시간 간격
                //double distanceMeter = distance(37.52135327,  126.93035147,  37.52135057,  126.93036593);
                deltaDist = distance(lat1,  lng1,  lat2,  lng2);
                if(deltaDist > 0.05) {
                    /*tvGpsLatitude.setText("Start Latitude : " + lat1);
                    tvGpsLongitude.setText("Start Longitude : " + lng1);
                    tvEndLatitude.setText("End Latitude : " + lat2);
                    tvEndLongitude.setText("End Longitude : " + lng2);*/
                    tvDistDif.setText("거리 간격 : " +  Double.parseDouble(String.format("%.3f",deltaDist)) + " m");  // Dist Difference
                    lastKnownLocation = nowLastlocation;
                    return deltaDist;
                }
//                lastKnownLocation = nowLastlocation;
            }
        }
        return 0.0;
    }
    @Override
    public void onLocationChanged(Location location) {
        nowLastlocation = location;
        //Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();
        double lng = location.getLongitude();
        double lat = location.getLatitude();
        Log.d("Now Location ::::::", "longtitude=" + lng + ", latitude=" + lat);
        /*tvNowLatitude.setText("Now Latitude : " + lat);
        tvNowLongitude.setText("Now Longitude : " + lng);*/
    }
    @Override
    public void onProviderEnabled(String provider) {
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 위치정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_UI);
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 위치정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
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
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    // 거리계산
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344 * 1000; //미터 단위
        return dist;
    }
    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
