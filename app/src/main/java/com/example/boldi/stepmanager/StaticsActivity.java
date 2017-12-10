package com.example.boldi.stepmanager;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;

public class StaticsActivity extends AppCompatActivity {
    BarChart barChart;
    Database database;

    Button btnEnd;
    DatePickerDialog.OnDateSetListener dateSetListenerEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics);
        database = new Database(this);
        int today = DateToIntConverter.DateToInt(Calendar.getInstance());
        dateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btnEnd.setText(year+"."+month+"."+day+".");
            }
        };

        barChart = (BarChart) findViewById(R.id.barChart);
        setBarChart(barChart,DateToIntConverter.DateToInt(Calendar.getInstance()));
        btnEnd = (Button) findViewById(R.id.endDate);
        btnEnd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PickDate(dateSetListenerEnd);
                    }
                }
        );

    }
    void setBarChart(BarChart barChart, int date){
        ArrayList<BarEntry> BE = database.getLastSevenDaySteps(date);
        BarDataSet BDS = new BarDataSet(BE,"Dates");
        ArrayList<String> days = new ArrayList<>();
        for (int i = 0; i<7;i++){
            days.add(String.valueOf(date-i));
        }

        BarData BD = new BarData(BDS);
        barChart.setData(BD);
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
