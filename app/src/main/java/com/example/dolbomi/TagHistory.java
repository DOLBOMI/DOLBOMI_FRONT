package com.example.dolbomi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;


import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class TagHistory extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TableLayout tbl;
    ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();
    int flag=0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_history);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("태그 기록");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tbl = (TableLayout) findViewById(R.id.tableLayout);
        ImageButton btn_all = (ImageButton)findViewById(R.id.imageButton);
        btn_all.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    getData(datas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton btn_fd = (ImageButton)findViewById(R.id.imageButton13);
        btn_fd.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    getValue_fd(datas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton btn_bed = (ImageButton)findViewById(R.id.imageButton2);
        btn_bed.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    getValue_bedroom(datas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton btn_bath = (ImageButton)findViewById(R.id.imageButton3);
        btn_bath.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    getValue_bathroom(datas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton btn_refri = (ImageButton)findViewById(R.id.imageButton7);
        btn_refri.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    getValue_refrigerator(datas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton btn_pill = (ImageButton)findViewById(R.id.imageButton5);
        btn_pill.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    getValue_pill(datas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button refresh = (Button)findViewById(R.id.button7);
        refresh.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    datas.clear();
                    TextView textView = (TextView)findViewById(R.id.textView7);
                    String data_str=textView.getText().toString();
                    String tmp="";
                    int year=0, month=0, day=0;
                    for(int i = 0;i<data_str.length();i++){
                        if(data_str.charAt(i)=='년'){
                            year=Integer.parseInt(tmp);
                        }
                        else if(data_str.charAt(i)=='월'){
                            month = Integer.parseInt(tmp);
                        }
                        else if(data_str.charAt(i)=='일'){
                            day = Integer.parseInt(tmp);
                        }
                        else if(data_str.charAt(i)==' '){
                            tmp="";
                        }
                        else{
                            tmp+=data_str.charAt(i);
                        }
                    }
                    Log.d("year", Integer.toString(year));
                    Log.d("month", Integer.toString(month));
                    Log.d("day", Integer.toString(day));

                    getValue(year, month-1, day);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        final Calendar c = Calendar.getInstance();
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
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "년 " + month_string + "월 " + day_string + "일");

        TextView textView = (TextView)findViewById(R.id.textView7);
        textView.setText(dateMessage);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getValue(year, month, day);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getData(ArrayList<ArrayList<String>> datas){
        tbl.removeAllViews();
        Comparator<ArrayList<String>> myComparator = new Comparator<ArrayList<String>>(){
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(1).compareTo(o2.get(1));
            }
        };
        Collections.sort(datas, myComparator);

        for (int i = 0; i < datas.size(); i++) {
            TableRow row= new TableRow(this);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);

            TextView tv = new TextView(this);
            tv.setBackground(getDrawable(R.drawable.boarder));
            tv.setTextColor(Color.DKGRAY);
            tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            tv.setTextSize(23);
            tv.setWidth(100);

            TextView tv2 = new TextView(this);
            tv2.setBackground(getDrawable(R.drawable.boarder));
            tv2.setTextColor(Color.DKGRAY);
            tv2.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            tv2.setTextSize(23);
            tv2.setWidth(100);

            tv.setText(datas.get(i).get(1));
            tv2.setText(datas.get(i).get(0));
            row.addView(tv);
            row.addView(tv2);
            tbl.addView(row);
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


        tbl = (TableLayout) findViewById(R.id.tableLayout);
        //tbl.removeViews(1, tbl.getChildCount());

        //dlist.clear();
        final DatabaseReference bathroomValue = mDatabase.child("Home1").child(date).child("Bathroom");
        final DatabaseReference bedroomValue = mDatabase.child("Home1").child(date).child("Bedroom");
        final DatabaseReference frontdoorValue = mDatabase.child("Home1").child(date).child("Frontdoor");
        final DatabaseReference refrigeratorValue = mDatabase.child("Home1").child(date).child("Refrigerator");
        final DatabaseReference pillValue = mDatabase.child("Home1").child(date).child("Pill");

        bathroomValue.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                }
                else {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("화장실");
                        data.add(child.getKey());
                        datas.add(data);
                    }
                }
            }
        });

        bedroomValue.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("침실");
                        data.add(child.getKey());
                        datas.add(data);
                    }
                }

            }
        });

        frontdoorValue.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("현관문");
                        data.add(child.getKey());
                        datas.add(data);
                    }
                }

            }
        });

        refrigeratorValue.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("냉장고");
                        data.add(child.getKey());
                        datas.add(data);
                    }
                }

            }
        });

        pillValue.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("약통");
                        data.add(child.getKey());
                        datas.add(data);
                    }
                }

            }
        });

        //Arrays.sort(datas, new Comparator<ArrayList<String>>()    );

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getValue_fd(ArrayList<ArrayList<String>> datas) {
        tbl.removeAllViews();
        Comparator<ArrayList<String>> myComparator = new Comparator<ArrayList<String>>(){
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(1).compareTo(o2.get(1));
            }
        };
        Collections.sort(datas, myComparator);

        for (int i = 0; i < datas.size(); i++) {
            if(datas.get(i).get(0)=="현관문"){
                TableRow row= new TableRow(this);

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);

                TextView tv = new TextView(this);
                tv.setBackground(getDrawable(R.drawable.boarder));
                tv.setTextColor(Color.DKGRAY);
                tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                tv.setTextSize(23);
                tv.setWidth(100);

                TextView tv2 = new TextView(this);
                tv2.setBackground(getDrawable(R.drawable.boarder));
                tv2.setTextColor(Color.DKGRAY);
                tv2.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                tv2.setTextSize(23);
                tv2.setWidth(100);

                tv.setText(datas.get(i).get(1));
                tv2.setText(datas.get(i).get(0));
                row.addView(tv);
                row.addView(tv2);
                tbl.addView(row);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getValue_bathroom(ArrayList<ArrayList<String>> datas) {
        tbl.removeAllViews();
        Comparator<ArrayList<String>> myComparator = new Comparator<ArrayList<String>>(){
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(1).compareTo(o2.get(1));
            }
        };
        Collections.sort(datas, myComparator);

        for (int i = 0; i < datas.size(); i++) {
            if(datas.get(i).get(0)=="화장실"){
                TableRow row= new TableRow(this);

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);

                TextView tv = new TextView(this);
                tv.setBackground(getDrawable(R.drawable.boarder));
                tv.setTextColor(Color.DKGRAY);
                tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                tv.setTextSize(23);
                tv.setWidth(100);

                TextView tv2 = new TextView(this);
                tv2.setBackground(getDrawable(R.drawable.boarder));
                tv2.setTextColor(Color.DKGRAY);
                tv2.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                tv2.setTextSize(23);
                tv2.setWidth(100);

                tv.setText(datas.get(i).get(1));
                tv2.setText(datas.get(i).get(0));
                row.addView(tv);
                row.addView(tv2);
                tbl.addView(row);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getValue_bedroom(ArrayList<ArrayList<String>> datas) {
        tbl.removeAllViews();
        Comparator<ArrayList<String>> myComparator = new Comparator<ArrayList<String>>(){
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(1).compareTo(o2.get(1));
            }
        };
        Collections.sort(datas, myComparator);

        for (int i = 0; i < datas.size(); i++) {
            if(datas.get(i).get(0)=="침실"){
                TableRow row= new TableRow(this);

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);

                TextView tv = new TextView(this);
                tv.setBackground(getDrawable(R.drawable.boarder));
                tv.setTextColor(Color.DKGRAY);
                tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                tv.setTextSize(23);
                tv.setWidth(100);

                TextView tv2 = new TextView(this);
                tv2.setBackground(getDrawable(R.drawable.boarder));
                tv2.setTextColor(Color.DKGRAY);
                tv2.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                tv2.setTextSize(23);
                tv2.setWidth(100);

                tv.setText(datas.get(i).get(1));
                tv2.setText(datas.get(i).get(0));
                row.addView(tv);
                row.addView(tv2);
                tbl.addView(row);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getValue_refrigerator(ArrayList<ArrayList<String>> datas) {
        tbl.removeAllViews();
        Comparator<ArrayList<String>> myComparator = new Comparator<ArrayList<String>>(){
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(1).compareTo(o2.get(1));
            }
        };
        Collections.sort(datas, myComparator);

        for (int i = 0; i < datas.size(); i++) {
            if(datas.get(i).get(0)=="냉장고"){
                TableRow row= new TableRow(this);

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);

                TextView tv = new TextView(this);
                tv.setBackground(getDrawable(R.drawable.boarder));
                tv.setTextColor(Color.DKGRAY);
                tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                tv.setTextSize(23);
                tv.setWidth(100);

                TextView tv2 = new TextView(this);
                tv2.setBackground(getDrawable(R.drawable.boarder));
                tv2.setTextColor(Color.DKGRAY);
                tv2.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                tv2.setTextSize(23);
                tv2.setWidth(100);

                tv.setText(datas.get(i).get(1));
                tv2.setText(datas.get(i).get(0));
                row.addView(tv);
                row.addView(tv2);
                tbl.addView(row);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getValue_pill(ArrayList<ArrayList<String>> datas) {
        tbl.removeAllViews();
        Comparator<ArrayList<String>> myComparator = new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(1).compareTo(o2.get(1));
            }
        };
        Collections.sort(datas, myComparator);

        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).get(0) == "약통") {
                TableRow row = new TableRow(this);

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);

                TextView tv = new TextView(this);
                tv.setBackground(getDrawable(R.drawable.boarder));
                tv.setTextColor(Color.DKGRAY);
                tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                tv.setTextSize(23);
                tv.setWidth(100);

                TextView tv2 = new TextView(this);
                tv2.setBackground(getDrawable(R.drawable.boarder));
                tv2.setTextColor(Color.DKGRAY);
                tv2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                tv2.setTextSize(23);
                tv2.setWidth(100);

                tv.setText(datas.get(i).get(1));
                tv2.setText(datas.get(i).get(0));
                row.addView(tv);
                row.addView(tv2);
                tbl.addView(row);
            }
        }
    }

}

