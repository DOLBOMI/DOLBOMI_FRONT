package com.example.dolbomi;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.github.mikephil.charting.charts.BarChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements LocationListener {
    Dialog dialog;
    BarChart barChart;
    //int x = 7330;

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

    //public void BarChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
    public void BarChartGraph() {
        // BarChart 메소드

        barChart=findViewById(R.id.bar_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, com.example.dolbomi.StepValue.Step));
        entries.add(new BarEntry(0, 10000));

        /*for(int i=0; i < valList.size();i++){
            entries.add(new BarEntry((Integer) valList.get(i), i));
        }*/

        BarDataSet depenses = new BarDataSet (entries, "권장 걸음 수 / 금일 걸음 수 "); // 변수로 받아서 넣어줘도 됨
        depenses.setAxisDependency(YAxis.AxisDependency.RIGHT);
        //barChart.setDescription();

        /*ArrayList<String> labels = new ArrayList<String>();
        for(int i=0; i < labelList.size(); i++){
            labels.add((String) labelList.get(i));
        }*/

        BarData data = new BarData(depenses);
        //BarData data = new BarData(labels,depenses); // 라이브러리 v3.x 사용하면 에러 발생함
        depenses.setColors(ColorTemplate.LIBERTY_COLORS); //

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setEnabled(false);

        barChart.setData(data);
        barChart.animateXY(1000,1000);
        barChart.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        manboService = new Intent(this, StepCheckService.class);
        receiver = new PlayingReceiver();
        countText = (TextView) findViewById(R.id.stepText);
        try {
            IntentFilter mainFilter = new IntentFilter("make.a.yong.manbo");
            registerReceiver(receiver, mainFilter);
            startService(manboService);
            setValue();
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

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("83세 한유진");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        TextView textView = (TextView)findViewById(R.id.textView);

        Log.d(this.getClass().getName(), (String)textView.getText());

        textView.setText(serviceData+"걸음\n"+(calorie*0.37)+"Kcal");

        BarChartGraph();

        Button lifePatternButton = (Button) findViewById(R.id.lifePatternButton);
        Button tagHistoryButton = (Button) findViewById(R.id.tagHistoryButton);
        Button MyHomeButton = (Button) findViewById(R.id.MyHomeButton);
        Button SensorButton = (Button) findViewById(R.id.SensorButton);

        lifePatternButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), LifePatternActivity.class);
                startActivity(intent);
            }
        });

        tagHistoryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), TagHistory.class);
                startActivity(intent);
            }
        });

        MyHomeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), HomeStatus.class);
                startActivity(intent);
            }
        });

        SensorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SensorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu) ;

        return true ;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_call:
                // User chose the "Settings" item, show the app settings UI...

                dialog = new Dialog(MainActivity.this);       // Dialog 초기화
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                dialog.setContentView(R.layout.activity_call);             // xml 레이아웃 파일과 연결

                dialog.show();

                Button cancel = dialog.findViewById(R.id.button8);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 원하는 기능 구현
                        dialog.dismiss(); // 다이얼로그 닫기
                    }
                });

                /*Button call_guardian = dialog.findViewById(R.id.imageButton2);
                call_guardian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 원하는 기능 구현
                        TextView a=dialog.findViewById(R.id.textView8);
                        show(a.getText().toString());
                    }
                });

                Button call_admin = dialog.findViewById(R.id.imageButton3);
                call_admin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 원하는 기능 구현
                        TextView a=dialog.findViewById(R.id.textView11);
                        show(a.getText().toString());
                    }
                });
                 */
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(getApplicationContext(), "메뉴 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);

        }
    }

    public void show(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("전화 걸기\n");
        builder.setMessage(str + "에게 전화를 거시겠습니까?");

        AlertDialog b_alert=builder.create();

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), str + "에게 전화 걸기", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                b_alert.dismiss();
            }
        });
        b_alert.show();
    }

    public void ClickSensor(View view) {
        Intent intent = new Intent( this, SensorActivity.class);
        startActivity(intent);
    }

    public void ClickHomeStatus(View view) {
        Intent intent = new Intent( this, HomeStatus.class);
        startActivity(intent);
    }

    public void ClickPattern(View view) {
        Intent intent = new Intent( this, LifePatternActivity.class);
        startActivity(intent);
    }

    public void ClickTagHistory(View view) {
        Intent intent = new Intent( this, TagHistory.class);
        startActivity(intent);
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
            //sttvDistDif.setText(totalLocation + " m");  // Time Difference
            // 속도 계산
            speed = mLastlocation.distanceTo(location) / deltaTime;
            String formatLastDate = sdf.format(new Date(mLastlocation.getTime()));
            double calSpeed = Double.parseDouble(String.format("%.3f", speed));
        }
        // 현재위치를 지난 위치로 변경
        mLastlocation = location;
        setValue();
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

    private void getValue() {
        final DatabaseReference stepValue = mDatabase.child("Home1").child("2021-8-24").child("Step");
        final DatabaseReference exerciseTimeValue = mDatabase.child("Home1").child("2021-8-24").child("ExerciseTime");
        final DatabaseReference distanceValue = mDatabase.child("Home1").child("2021-8-24").child("Distance");
        final DatabaseReference calorieValue = mDatabase.child("Home1").child("2021-8-24").child("Calorie");

        setContentView(R.layout.activity_exercise_detail);
        TextView butt = (TextView) findViewById(R.id.textView53);

        stepValue.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Long bathValue = (Long) snapshot.getValue();

                    butt.setText(bathValue.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setValue(){
        databaseReference.child("Home1").child("2021-8-24").child("Step").setValue(serviceData);
        databaseReference.child("Home1").child("2021-8-24").child("ExerciseTime").setValue(deltaTime);
        databaseReference.child("Home1").child("2021-8-24").child("Distance").setValue(totalLocation);
        databaseReference.child("Home1").child("2021-8-24").child("Calorie").setValue(calorie);
    }
}