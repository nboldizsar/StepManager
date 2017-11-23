package com.example.boldi.stepmanager;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    PieChart pieChart;
    TextView average;
    TextView total;
    Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database(this);

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setHoleRadius(90f);
        pieChart.setCenterTextSize(30);
        pieChart.setDragDecelerationEnabled(false);
        pieChart.setTouchEnabled(false);
        addDataSet(0,1000);

        Button randomData = (Button) findViewById(R.id.randomData);
        Button reset = (Button) findViewById(R.id.reset);
        randomData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.UploadWithUnrealData( new int[]{120,150,960,6281});
                    }
                }
        );
        reset.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.resetAllData();
                    }
                }
        );

        average = (TextView) findViewById(R.id.average);
        total = (TextView) findViewById(R.id.total);
        //TextView stepcount = (TextView) findViewById(R.id.steps);
        TextView kcalcount = (TextView) findViewById(R.id.kcalText);
        Intent i = new Intent(this,StepListener.class);
        startService(i);
        Calendar.getInstance().getTime();
        String asd = String.valueOf(DateToIntConverter.DateToInt(Calendar.getInstance()));
        Toast.makeText(this, asd, Toast.LENGTH_SHORT).show();
        String steps = String.valueOf(db.getTodayStep());
        String kcal = String.valueOf(db.getTodayStep() / 27);
        //stepcount.setText(steps);
        kcalcount.setText(kcal + " kcal");
        //stepcount.setText(db.getTodayStep());

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                average.setText(String.valueOf(db.getAvarageSteps()));
                                total.setText(String.valueOf(db.getTotalSteps()));
                                String steps = String.valueOf(db.getTodayStep());
                                addDataSet(Integer.parseInt(steps),1000);
                                //stepcount.setText(steps);

                                TextView kcalcount = (TextView) findViewById(R.id.kcalText);
                                String kcal = String.valueOf(db.getTodayStep() / 27);
                                kcalcount.setText(kcal + " kcal");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

    }

    private void addDataSet(int steps, int goal) {

        pieChart.setCenterText((String.valueOf(steps)+" / "+String.valueOf(goal)));
        ArrayList<PieEntry> entry = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();

        entry.add(new PieEntry(steps,0));
        entry.add(new PieEntry(goal,1));

        PieDataSet dataset = new PieDataSet(entry,"Steps");
        dataset.setSliceSpace(2f);
        dataset.setValueTextSize(0f);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        dataset.setColors(colors);

        PieData pieData = new PieData(dataset);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar1, menu);
        return true;
    }
}
