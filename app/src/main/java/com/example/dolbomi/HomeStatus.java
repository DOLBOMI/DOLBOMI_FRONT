package com.example.dolbomi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class HomeStatus extends AppCompatActivity {
    private LineChart chart;

    public void lineGraph() {
        chart = findViewById(R.id.chart);
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));
        }

        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        // set data
        chart.setData(data);
        chart.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_home);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("내 집 상태");

        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        Button temp_btn = (Button) findViewById(R.id.button1);
        Button moist_btn = (Button) findViewById(R.id.button2);
        Button gas_btn = (Button) findViewById(R.id.button3);

        temp_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lineGraph();
            }
        });
        moist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineGraph();
            }
        });
        gas_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //lineGraph();
            }
        });


    }


}