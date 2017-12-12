package com.example.boldi.stepmanager;

import android.app.ActionBar;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
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
    private static final String PREFNAME = "setting";
    private static final String Weight_NAME = "weight";
    private static final String DAILYGOAL_NAME = "goal";


    PieChart pieChart;
    TextView average;
    TextView total;
    Database db;
    TextView kcalcount;
    int weight;
    int goal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetSettings();

        db = new Database(this);

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setHoleRadius(90f);
        pieChart.setCenterTextSize(30);
        pieChart.setDragDecelerationEnabled(false);
        pieChart.setTouchEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        addDataSet(db.getTodayStep(),goal);

        Button randomData = (Button) findViewById(R.id.randomData);
        Button reset = (Button) findViewById(R.id.reset);
        randomData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.UploadWithUnrealData( new int[]{120,150,960,6281,342,1412,6234});
                    }
                }
        );
        reset.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //db.resetAllData();
                        staticsClick();
                    }
                }
        );

        average = (TextView) findViewById(R.id.average);
        total = (TextView) findViewById(R.id.total);
        kcalcount = (TextView) findViewById(R.id.kcalText);
        Intent i = new Intent(this,StepListener.class);
        startService(i);
        Calendar.getInstance().getTime();
        String kcal =  String.format("%.1f",(float)db.getTodayStep() / 27f);
        kcalcount.setText(kcal);

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
                                addDataSet(Integer.parseInt(steps),goal);
                                String kcal = String.format("%.1f",(float)db.getTodayStep() / 27f);
                                kcalcount.setText(kcal);
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
        boolean overstepped = false;
        int pair = 0;
        pieChart.setCenterText((String.valueOf(steps)+" / "+String.valueOf(goal)));
        ArrayList<PieEntry> entry = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        int overstep= 0;
        int goalHelp = goal - steps;
        if (steps > goal){
            overstep = steps - goal;
            goalHelp = 0;
            while(overstep-goal >0){
                overstep-= goal;
                pair ++;
            }
            steps = goal-(overstep);
            overstepped = true;
        }
        ArrayList<Integer> colors = new ArrayList<Integer>();
        if (overstepped){
            if(pair % 2 ==0) {
                entry.add(new PieEntry(overstep, 0));
                entry.add(new PieEntry(steps, 1));
                entry.add(new PieEntry(goalHelp, 2));
                colors.add(Color.rgb(114,136,226));
                colors.add(Color.rgb(115,250,90)); //lightgreen
                colors.add(Color.rgb(251,150,89));
            }else{
                entry.add(new PieEntry(overstep, 0));
                entry.add(new PieEntry(steps, 1));
                entry.add(new PieEntry(goalHelp, 2));
                colors.add(Color.rgb(115,250,90));//lightgreen
                colors.add(Color.rgb(114,136,226));
                colors.add(Color.rgb(251,150,89));
            }
        }
        else{
            entry.add(new PieEntry(overstep, 0));
            entry.add(new PieEntry(steps, 1));
            entry.add(new PieEntry(goalHelp, 2));
            colors.add(Color.rgb(114,136,226));
            colors.add(Color.rgb(115,250,90)); //lightgreen
            colors.add(Color.rgb(251,150,89));
        }
        PieDataSet dataset = new PieDataSet(entry,"Steps");
        dataset.setSliceSpace(0f);
        dataset.setValueTextSize(0f);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        dataset.setColors(colors);

        PieData pieData = new PieData(dataset);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    @Override
    protected void onResume() {
        super.onResume();

        SetSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar1, menu);
        return true;
    }

   public void actBarClick(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void staticsClick() {
        Intent intent = new Intent(this, StaticsActivity.class);
        startActivity(intent);
    }

    private void SetSettings()
    {
        SharedPreferences settings = getSharedPreferences(PREFNAME, 0);
        weight = settings.getInt(Weight_NAME, 0);
        goal = settings.getInt(DAILYGOAL_NAME, 0);

        if(weight == 0)
            weight = 60;
        if(goal == 0)
            goal = 5000;
    }
}
