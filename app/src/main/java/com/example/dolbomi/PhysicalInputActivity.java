package com.example.dolbomi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class PhysicalInputActivity {

    private Context context;

    public PhysicalInputActivity(Context context) {
        this.context = context;
    }

    public void callFunction(final TextView height, final TextView weight) {

        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dlg.setContentView(R.layout.activity_physical_input);

        dlg.show();

        Button physicalregisterButtonReal = (Button) dlg.findViewById(R.id.physicalregisterButtonReal);
        EditText heightPicker = (EditText) dlg.findViewById(R.id.hv);
        EditText weightPicker = (EditText) dlg.findViewById(R.id.wv);

        physicalregisterButtonReal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                height.setText(heightPicker.getText().toString());
                weight.setText(weightPicker.getText().toString());

                dlg.dismiss();
            }
        });

    }
}