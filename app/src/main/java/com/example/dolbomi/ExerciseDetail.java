package com.example.dolbomi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ExerciseDetail extends AppCompatActivity {
    BarChart barChart;
    int yesterday = 7300;
    int selectDay = 8000;
    int tomorrow = 5000;

    String stepAvg = "6000";
    String exerciseAvg = "48";
    String distanceAvg = "3.2";
    String kcalAvg = "161";

    public void BarChartGraph() {
        // BarChart 메소드
        barChart=findViewById(R.id.step_bar_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, yesterday));
        entries.add(new BarEntry(1, selectDay));
        entries.add(new BarEntry(2, tomorrow));

        BarDataSet depenses = new BarDataSet (entries, "일일 걸음 수 "); // 변수로 받아서 넣어줘도 됨
        depenses.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData data = new BarData(depenses);
        depenses.setColors(ColorTemplate.LIBERTY_COLORS);

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
        setContentView(R.layout.activity_exercise_detail);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("상세 데이터");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        BarChartGraph();
        barChart.setTouchEnabled(false);//확대하지못하게 막아버림

        TextView textView1 = (TextView)findViewById(R.id.textView9);
        textView1.setText(stepAvg+"걸음");
        TextView textView2 = (TextView)findViewById(R.id.textView11);
        textView2.setText(exerciseAvg+"분");
        TextView textView3 = (TextView)findViewById(R.id.textView13);
        textView3.setText(distanceAvg+"km");
        TextView textView4 = (TextView)findViewById(R.id.textView15);
        textView4.setText(kcalAvg+"Kcal");

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

}
