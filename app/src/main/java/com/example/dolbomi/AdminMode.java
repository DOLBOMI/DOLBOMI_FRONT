package com.example.dolbomi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class AdminMode extends AppCompatActivity {
    Dialog dialog01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mode);

        String name = "이준호";
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("관리자 " + name);

        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        dialog01 = new Dialog(AdminMode.this);       // Dialog 초기화
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog01.setContentView(R.layout.activity_change_status);             // xml 레이아웃 파일과 연결

        ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

        ArrayList<String> arrarr = new ArrayList<String>();
        arrarr.add("한유진");
        arrarr.add("위험");

        ArrayList<String> arrarr2 = new ArrayList<String>();
        arrarr2.add("한유진2");
        arrarr2.add("위험2");

        datas.add(arrarr);
        datas.add(arrarr2);

        getData(datas);

        //SimpleUser user = new SimpleUser();

//        List<SimpleUser> users = DolbomiService.service.listUsers();
//        Log.d("TESTNOW", users.get(0).getClass().getName());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DolbomiService service = retrofit.create(DolbomiService.class);

        Call<List<SimpleUser>> call = service.listUsers();

        call.enqueue(new Callback<List<SimpleUser>>() {

            @Override
            public void onResponse(Call<List<SimpleUser>> call, Response<List<SimpleUser>> response) {
                if(response.isSuccessful()) {
                    List<SimpleUser> users = response.body();
                    Log.d(TAG, "api 테스트" + users.get(0).getClass().getName());
                }
            }

            @Override
            public void onFailure(Call<List<SimpleUser>> call, Throwable t) {
                Log.d(TAG, "실패 실패 api" + t);
            }
        });

        /*
        // 버튼: 커스텀 다이얼로그 띄우기
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog01(); // 아래 showDialog01() 함수 호출
            }
        }); */

        // 전체
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
/*
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog01(); // 아래 showDialog01() 함수 호출
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog01(); // 아래 showDialog01() 함수 호출
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }); */

    }

    public void getData(ArrayList<ArrayList<String>> datas){
        TableLayout tbl = (TableLayout) findViewById(R.id.tablelayout1);

        tbl.removeAllViews();

        for (int i = 0; i < datas.size(); i++) {
            TableRow row= new TableRow(this);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);

            TextView tv = new TextView(this);
            //tv.setBackground(getDrawable(R.drawable.boarder));
            tv.setTextColor(Color.DKGRAY);
            tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            tv.setTextSize(13);
            tv.setWidth(200);

            TextView tv2 = new TextView(this);
            //tv2.setBackground(getDrawable(R.drawable.boarder));
            tv2.setTextColor(Color.DKGRAY);
            tv2.setGravity(Gravity.LEFT|Gravity.CENTER_HORIZONTAL);
            tv2.setTextSize(13);
            tv2.setWidth(550);

            Button button = new Button(this);
            button.setText("상태변경");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog01(); // 아래 showDialog01() 함수 호출
                }
            });


            tv.setText(datas.get(i).get(1));
            tv2.setText(datas.get(i).get(0));
            row.addView(tv);
            row.addView(tv2);
            row.addView(button);
            tbl.addView(row);
        }
    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add:
                // User chose the "Settings" item, show the app settings UI...
                //Toast.makeText(getApplicationContext(), "추가 버튼 클릭됨", Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "추가 버튼 클릭됨", Toast.LENGTH_LONG).show();
                show();
                return true;
            default:
                //return true;
                return super.onOptionsItemSelected(item);
        }
    }

    public void show() {
        final EditText edittext1 = new EditText(this);
        final EditText edittext2 = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사용자 추가 \n\n");
        builder.setMessage("노인 행정 등록번호 입력");
        builder.setView(edittext1);
        //builder.setMessage("성함");
        //builder.setView(edittext2);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), edittext1.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }

    // dialog01을 디자인하는 함수
    public void showDialog01(){
        dialog01.show(); // 다이얼로그 띄우기

        /* 이 함수 안에 원하는 디자인과 기능을 구현하면 된다. */

        // 위젯 연결 방식은 각자 취향대로~
        // '아래 아니오 버튼'처럼 일반적인 방법대로 연결하면 재사용에 용이하고,
        // '아래 네 버튼'처럼 바로 연결하면 일회성으로 사용하기 편함.
        // *주의할 점: findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.

        // 아니오 버튼
        Button noBtn = dialog01.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                dialog01.dismiss(); // 다이얼로그 닫기
            }
        });
        // 네 버튼
        dialog01.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                dialog01.dismiss(); // 일단 다이얼로그 닫기
                // finish();           // 앱 종료
            }
        });
    }

}