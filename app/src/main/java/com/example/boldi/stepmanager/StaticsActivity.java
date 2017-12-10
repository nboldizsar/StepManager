package com.example.boldi.stepmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.github.mikephil.charting.charts.BarChart;

import java.util.Calendar;

/**
 * Created by Kristóf on 12/10/2017.
 */

public class StaticsActivity extends AppCompatActivity {
    BarChart barChart;
    Button btnStart;
    Button btnEnd;
    DatePickerDialog.OnDateSetListener dateSetListenerStart;;
    DatePickerDialog.OnDateSetListener dateSetListenerEnd;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.statics);

        dateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btnStart.setText(year+"."+month+"."+day+".");
            }
        };
        dateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btnEnd.setText(year+"."+month+"."+day+".");
            }
        };

        barChart = (BarChart) findViewById(R.id.barChart);
        btnStart = (Button) findViewById(R.id.startDate);
        btnStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PickDate(dateSetListenerStart);
                    }
                }
        );
        btnEnd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PickDate(dateSetListenerEnd);
                    }
                }
        );
        btnEnd = (Button) findViewById(R.id.endDate);

    }

    void barChartRefresh(){

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void PickDate(DatePickerDialog.OnDateSetListener onDateSetListener){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,
                year,month,day
        );
        dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dpd.show();
    }
}

























