package com.example.dolbomi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

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
    }

}
