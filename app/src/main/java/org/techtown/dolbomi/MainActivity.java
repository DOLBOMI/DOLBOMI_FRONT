package org.techtown.dolbomi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import com.github.mikephil.charting.charts.BarChart;

public class MainActivity extends AppCompatActivity {
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
        //barChart.setDrawValueAboveBar(true);
        barChart.getXAxis().setGranularity(10);


        barChart.setData(data);
        barChart.animateXY(1000,1000);
        barChart.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        TextView textView = (TextView)findViewById(R.id.textView);

        Log.d(this.getClass().getName(), (String)textView.getText());


        textView.setText(x+"걸음\n"+(x*0.37)+"Kcal");

        BarChartGraph();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu) ;

        return true ;
    }

}