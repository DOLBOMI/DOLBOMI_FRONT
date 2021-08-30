package com.example.dolbomi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExerciseDetail extends AppCompatActivity implements LocationListener {
    BarChart barChart;
    int yesterday = 7300;
    int selectDay = 8000;
    int tomorrow = 5000;

    Intent manboService;
    BroadcastReceiver receiver;
    String serviceData;
    TextView countText;

    private DatabaseReference mDatabase;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference = database.getReference();
    private LocationManager locationManager;
    private Location mLastlocation = null;
    private double totalLocation = 0;
    private double deltaTime = 0;
    private TextView tvTimeDif, tvDistDif, tvCalDif;
    private double speed;
    private double calorie = 0;

    //TableLayout tbl;
    ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //setValue();

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("상세 데이터");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        BarChartGraph();
        barChart.setTouchEnabled(false);//확대하지못하게 막아버림

        /*TextView labelView = (TextView)findViewById(R.id.date_label);
        labelView.setText(yesterdayDate+"          "+selectedDate+"          "+tomorrowDate);*/

        manboService = new Intent(this, StepCheckService.class);
        receiver = new PlayingReceiver();
        countText = (TextView) findViewById(R.id.stepText);
        try {
            IntentFilter mainFilter = new IntentFilter("make.a.yong.manbo");
            registerReceiver(receiver, mainFilter);
            startService(manboService);
            //setValue();
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

        final Calendar c = Calendar.getInstance();///////////////
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        TextView textView = (TextView)findViewById(R.id.textView7);
        //textView.setText(year + "년 " + month + "월 " + day + "일");
        textView.setText("날짜를 선택하세요");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getValue(year, month, day);
        System.out.println("--------------------------------------------------------------------------");
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragmentDetail();
        newFragment.show(getSupportFragmentManager(),"datePicker");
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
            //countText.setText(serviceData);
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
            //tvCalDif.setText(calorie + "kcal");
            //tvTimeDif.setText((deltaTime/60) + " 분");  // Time Difference
            totalLocation += mLastlocation.distanceTo(location);
            tvDistDif.setText(totalLocation + " m");  // Time Difference
            // 속도 계산
            speed = mLastlocation.distanceTo(location) / deltaTime;
            String formatLastDate = sdf.format(new Date(mLastlocation.getTime()));
            double calSpeed = Double.parseDouble(String.format("%.3f", speed));
        }
        // 현재위치를 지난 위치로 변경
        mLastlocation = location;
        //setValue();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getValue(int year, int month, int day) {
        datas.clear();
        Log.d("getValue", "1");
        String month_string;
        if(month<10)
            month_string = "0" + Integer.toString(month+1);
        else
            month_string = Integer.toString(month+1);

        String day_string;
        if(day<10)
            day_string = "0" + Integer.toString(day);
        else
            day_string=Integer.toString(day);

        String year_string = Integer.toString(year);

        String date = (year_string + "-" + month_string + "-" + day_string);
        Log.d("date", date);


        //tbl = (TableLayout) findViewById(R.id.tableLayout);

        /*final DatabaseReference stepValue = mDatabase.child("Home1").child(date).child("Step").child(serviceData);
        final DatabaseReference exerciseValue  = mDatabase.child("Home1").child(date).child("ExerciseTime").child(String.valueOf(deltaTime));
        final DatabaseReference distanceValue  = mDatabase.child("Home1").child(date).child("Distance").child(String.valueOf(totalLocation));
        final DatabaseReference calorieValue  = mDatabase.child("Home1").child(date).child("Calorie").child(String.valueOf(calorie));
*/
        final DatabaseReference stepValue = mDatabase.child("Home1").child("2021-08-23").child("Step");
        final DatabaseReference exerciseValue  = mDatabase.child("Home1").child("2021-08-23").child("ExerciseTime");
        final DatabaseReference distanceValue  = mDatabase.child("Home1").child("2021-08-23").child("Distance");
        final DatabaseReference calorieValue  = mDatabase.child("Home1").child("2021-08-23").child("Calorie");

        //setContentView(R.layout.activity_firebase_base);
        TextView stepButt = (TextView) findViewById(R.id.stepText);

        stepValue.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Long stepValue = (Long) snapshot.getValue();

                    stepButt.setText(stepValue.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //setContentView(R.layout.activity_firebase_base);
        TextView timeButt = (TextView) findViewById(R.id.tvTimeDif);

        exerciseValue.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Long exerciseValue = (Long) snapshot.getValue();

                    timeButt.setText(exerciseValue.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //setContentView(R.layout.activity_firebase_base);
        TextView disButt = (TextView) findViewById(R.id.tvDistDif);

        distanceValue.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Long distanceValue = (Long) snapshot.getValue();

                    disButt.setText(distanceValue.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //setContentView(R.layout.activity_firebase_base);
        TextView calButt = (TextView) findViewById(R.id.calorie);

        calorieValue.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Long calorieValue = (Long) snapshot.getValue();

                    calButt.setText(calorieValue.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setValue(){
        databaseReference.child("Home1").child("2021-08-23").child("Step").setValue(serviceData);
        databaseReference.child("Home1").child("2021-08-23").child("ExerciseTime").setValue(deltaTime);
        databaseReference.child("Home1").child("2021-08-23").child("Distance").setValue(totalLocation);
        databaseReference.child("Home1").child("2021-08-23").child("Calorie").setValue(calorie);
    }
    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "년 " + month_string + "월 " + day_string + "일");

        TextView textView = (TextView)findViewById(R.id.textView7);
        textView.setText(dateMessage);
    }
}
