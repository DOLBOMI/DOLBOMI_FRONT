package com.example.dolbomi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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

import java.util.ArrayList;

public class ExerciseDetail extends AppCompatActivity {
    BarChart barChart;
    int yesterday = 7300;
    int selectDay = 8000;
    int tomorrow = 5000;

    String yesterdayDate = "21/07/21";
    String selectedDate = "21/07/22";
    String tomorrowDate = "21/07/23";

    Intent manboService;
    BroadcastReceiver receiver;
    boolean flag = true;
    String serviceData;
    TextView countText;
    Button playingBtn;

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
            }
            Log.i("PlayignReceiver", "IN");
            serviceData = intent.getStringExtra("stepService");
            countText.setText(serviceData);
            //Toast.makeText(getApplicationContext(), "Playing game", Toast.LENGTH_SHORT).show();
        }
    }
}
