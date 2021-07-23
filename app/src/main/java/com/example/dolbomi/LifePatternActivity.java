package com.example.dolbomi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LifePatternActivity extends AppCompatActivity {

    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_pattern);

        ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        ProgressBar progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        ProgressBar progressBar4 = (ProgressBar) findViewById(R.id.progressBar4);
        ProgressBar progressBar5 = (ProgressBar) findViewById(R.id.progressBar5);

        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        TextView textView5 = (TextView) findViewById(R.id.textView5);

        progressBar1.setProgress(10);
        progressBar2.setProgress(30);
        progressBar3.setProgress(20);
        progressBar4.setProgress(60);
        progressBar5.setProgress(80);

        textView1.setText("10");
        textView2.setText("30");
        textView3.setText("20");
        textView4.setText("60");
        textView5.setText("80");


        Date date = new Date();
        String time = mFormat.format(date);

        TextView dateTextView = (TextView) findViewById(R.id.dataTextView);
        // 오늘 날짜 보이기
        dateTextView.setText(time);
    }
}