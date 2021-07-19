package com.example.dolbomi;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import com.github.mikephil.charting.charts.BarChart;

public class MainActivity extends AppCompatActivity {
    Dialog dialog;
    BarChart barChart;
    int x = 7330;

    //public void BarChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
    public void BarChartGraph() {
        // BarChart 메소드

        barChart=findViewById(R.id.bar_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, x));
        entries.add(new BarEntry(0, 10000));

        /*
        for(int i=0; i < valList.size();i++){
            entries.add(new BarEntry((Integer) valList.get(i), i));
        }
        */

        BarDataSet depenses = new BarDataSet (entries, "권장 걸음 수 / 금일 걸음 수 "); // 변수로 받아서 넣어줘도 됨
        depenses.setAxisDependency(YAxis.AxisDependency.RIGHT);
        //barChart.setDescription();

        /*
        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0; i < labelList.size(); i++){
            labels.add((String) labelList.get(i));
        }
         */

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

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("83세 한유진");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        TextView textView = (TextView)findViewById(R.id.textView);

        Log.d(this.getClass().getName(), (String)textView.getText());


        textView.setText(x+"걸음\n"+(x*0.37)+"Kcal");

        BarChartGraph();
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

                /*
                Button call_guardian = dialog.findViewById(R.id.imageButton2);
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












}