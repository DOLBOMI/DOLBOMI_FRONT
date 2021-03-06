package com.example.dolbomi;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import java.util.TimeZone;

import com.github.mikephil.charting.charts.BarChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.messaging.FirebaseMessaging;



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

    private static final String TAG = "MainActivity ";
    //public void BarChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
    public void BarChartGraph() {
        // BarChart ?????????

        barChart=findViewById(R.id.bar_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, com.example.dolbomi.StepValue.Step));
        entries.add(new BarEntry(0, 10000));

        /*for(int i=0; i < valList.size();i++){
            entries.add(new BarEntry((Integer) valList.get(i), i));
        }*/

        BarDataSet depenses = new BarDataSet (entries, "?????? ?????? ??? / ?????? ?????? ??? "); // ????????? ????????? ???????????? ???
        depenses.setAxisDependency(YAxis.AxisDependency.RIGHT);
        //barChart.setDescription();

        /*ArrayList<String> labels = new ArrayList<String>();
        for(int i=0; i < labelList.size(); i++){
            labels.add((String) labelList.get(i));
        }*/

        BarData data = new BarData(depenses);
        //BarData data = new BarData(labels,depenses); // ??????????????? v3.x ???????????? ?????? ?????????
        depenses.setColors(ColorTemplate.LIBERTY_COLORS); //

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setEnabled(false);

        barChart.setData(data);
        barChart.animateXY(1000,1000);
        barChart.invalidate();
    }
/*
    public void push_notification() {
        //?????? ??????
        String channelId = "Channel ID";

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("????????? ??????!")
                        .setContentText("????????? ???????????? ???????????? ?????? ??? ?????????! ??? ????????? ????????? :)")
                        .setAutoCancel(true);
        //.setFullScreenIntent(pendingIntent, true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Channel Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        TimeZone tz;
        long now = System.currentTimeMillis();
        SimpleDateFormat time = new SimpleDateFormat("hh");
        tz = TimeZone.getTimeZone("Asia/Seoul");
        time.setTimeZone(tz);

        Date date = new Date(now);
        String getTime = time.format(date);
        // Log.d("?????? ??????", getTime);
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        String getDay = day.format(date);

        //final DatabaseReference calorieValue = mDatabase.child("Home1").child(getDay).child("Calorie");
        Log.d("??????", getTime);
        if (getTime.equals("08")) {
            Log.d("??????", getTime);

        }
        if (getTime == "08") {
            Log.d("???????????? ?????????????????? ??????", getTime);

        }*/
      //  RemoteMessage rm = new RemoteMessage()
        // The topic name can be optionally prefixed with "/topics/".
        //String topic = "highScores";


 /*       FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder("758940348113" + "@fcm.googleapis.com")
                //.setMessageId(Integer.toString(messageId))
                .addData("my_message", "????????? ??????")
                .addData("my_action","SAY_HELLO")
                .build());*/
/*
        RemoteMessage message = new RemoteMessage(bundle);

        RemoteMessage rm = RemoteMessage.setTitle("")
        MyFirebaseMessagingService fcm = new MyFirebaseMessagingService();
        fcm.onMessageReceived(fm);
*/

        // Send a message to the devices subscribed to the provided topic.
        //String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        //System.out.println("Successfully sent message: " + response);

        // Get token
        // [START log_reg_token]
        //MyFirebaseMessagingService fcm = new MyFirebaseMessagingService();
        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);

//                        fcm.onMessageReceived();
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });*/
        // [END log_reg_token]

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);



        manboService = new Intent(this, StepCheckService.class);
        receiver = new PlayingReceiver();
        //countText = (TextView) findViewById(R.id.stepText);

        try {
            IntentFilter mainFilter = new IntentFilter("make.a.yong.manbo");
            registerReceiver(receiver, mainFilter);
            startService(manboService);
            setValue();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        /*tvTimeDif = (TextView)findViewById(R.id.tvTimeDif);
        tvDistDif = (TextView)findViewById(R.id.tvDistDif);
        tvCalDif = (TextView)findViewById(R.id.calorie);*/
        //?????? ??????
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formatDate = sdf.format(new Date(lastKnownLocation.getTime()));
        }
        // GPS ?????? ?????? ?????? ??????
        boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,0, this);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("83??? ?????????");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        //TextView textView = (TextView)findViewById(R.id.textView);

        //Log.d(this.getClass().getName(), (String)textView.getText());

        //textView.setText(serviceData+"??????\n"+(calorie*0.37)+"Kcal");
        //countText.setText(serviceData);
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

                dialog = new Dialog(MainActivity.this);       // Dialog ?????????
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????? ??????
                dialog.setContentView(R.layout.activity_call);             // xml ???????????? ????????? ??????

                dialog.show();

                Button cancel = dialog.findViewById(R.id.button8);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // ????????? ?????? ??????
                        dialog.dismiss(); // ??????????????? ??????
                    }
                });

                /*Button call_guardian = dialog.findViewById(R.id.imageButton2);
                call_guardian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // ????????? ?????? ??????
                        TextView a=dialog.findViewById(R.id.textView8);
                        show(a.getText().toString());
                    }
                });

                Button call_admin = dialog.findViewById(R.id.imageButton3);
                call_admin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // ????????? ?????? ??????
                        TextView a=dialog.findViewById(R.id.textView11);
                        show(a.getText().toString());
                    }
                });
                 */
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(getApplicationContext(), "?????? ?????? ?????????", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);

        }
    }

    public void show(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("?????? ??????\n");
        builder.setMessage(str + "?????? ????????? ???????????????????");

        AlertDialog b_alert=builder.create();

        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), str + "?????? ?????? ??????", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
                // ????????? ????????? ?????? ?????? ??? ??????
                unregisterReceiver(receiver);
                stopService(manboService);
                deltaTime = 0;
                calorie = 0;
                totalLocation = 0;
            }
            Log.i("PlayignReceiver", "IN");
            serviceData = intent.getStringExtra("stepService");
            //countText.setText(serviceData);
            TextView textView = (TextView)findViewById(R.id.textView);
            textView.setText(serviceData+"??????\n");
            //Toast.makeText(getApplicationContext(), "Playing game", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onLocationChanged(Location location) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        //  getSpeed() ????????? ???????????? ????????? ??????
        double getSpeed = Double.parseDouble(String.format("%.3f", location.getSpeed()));
        String formatDate = sdf.format(new Date(location.getTime()));
        // ?????? ????????? ???????????? ????????? ?????? ????????? ?????? ?????? ??????
        if(mLastlocation != null) {
            //?????? ??????
            deltaTime += (location.getTime() - mLastlocation.getTime()) / 1000.0;
            calorie += deltaTime/30.0;
          //  TextView textView = (TextView)findViewById(R.id.calorie1);
            //textView.setText((calorie*0.37)+"Kcal");
            int rouneded_time = (int) Math.round(deltaTime);

            //tvCalDif.setText(calorie + "kcal");
            //tvTimeDif.setText((deltaTime/60) + " ???");  // Time Difference
            totalLocation += mLastlocation.distanceTo(location);
            //sttvDistDif.setText(totalLocation + " m");  // Time Difference
            // ?????? ??????
            speed = mLastlocation.distanceTo(location) / deltaTime;
            String formatLastDate = sdf.format(new Date(mLastlocation.getTime()));
            double calSpeed = Double.parseDouble(String.format("%.3f", speed));
        }
        // ??????????????? ?????? ????????? ??????
        mLastlocation = location;
        setValue();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
        //?????? ??????
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // ???????????? ????????????
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,0, this);
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        //?????? ??????
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // ???????????? ????????????
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,0, this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // ???????????? ???????????? ??????
        locationManager.removeUpdates(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //????????? ?????? ?????? ?????? ?????? ?????? ?????? ???????????? ?????? ????????? ??????
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // ?????? ?????????
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
        databaseReference.child("Home1").child("2021-08-24").child("Step").setValue(serviceData);
        databaseReference.child("Home1").child("2021-08-24").child("ExerciseTime").setValue(deltaTime);
        databaseReference.child("Home1").child("2021-08-24").child("Distance").setValue(totalLocation);
        databaseReference.child("Home1").child("2021-08-24").child("Calorie").setValue(calorie);
    }
}