package com.example.dolbomi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseBase extends AppCompatActivity {
    private DatabaseReference mDatabase;// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_base);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getValue();

    }

    private void getValue() {
        final DatabaseReference bathroomValue = mDatabase.child("2021-7-31").child("Home1").child("humidity");

        setContentView(R.layout.activity_firebase_base);
        TextView butt = (TextView) findViewById(R.id.textView53);

        bathroomValue.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Long bathValue = (Long) snapshot.getValue();

                    butt.setText(bathValue.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}