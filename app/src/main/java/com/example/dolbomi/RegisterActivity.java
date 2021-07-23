package com.example.dolbomi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class RegisterActivity extends AppCompatActivity {

    Dialog physicalDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 신체 정보 입력 버튼 클릭시 다이얼로그 띄우기
        Button physicalMoreButton = (Button) findViewById(R.id.physicalMoreButton);
        Button signupButton = (Button) findViewById(R.id.signupButton);
        EditText heightValue = (EditText) findViewById(R.id.heightValue);
        EditText weightValue = (EditText) findViewById(R.id.weightValue);

        physicalMoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                PhysicalInputActivity PhysicalInputActivity = new PhysicalInputActivity(RegisterActivity.this);

                PhysicalInputActivity.callFunction(heightValue, weightValue);

            }

        });

        signupButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

