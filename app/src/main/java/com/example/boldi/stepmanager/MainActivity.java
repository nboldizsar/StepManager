package com.example.boldi.stepmanager;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database db = new Database(this);

        Button randomData = (Button) findViewById(R.id.randomData);
        Button reset = (Button) findViewById(R.id.reset);
        randomData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Database db = new Database(getApplicationContext());
                        db.UploadWithUnrealData( new int[]{120,150,960,6281});
                    }
                }
        );
        reset.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Database db = new Database(getApplicationContext());
                        db.resetAllData();
                    }
                }
        );
        TextView stepcount = (TextView) findViewById(R.id.steps);
        TextView kcalcount = (TextView) findViewById(R.id.kcalText);
        Intent i = new Intent(this,StepListener.class);
        startService(i);
        Calendar.getInstance().getTime();
        String asd = String.valueOf(DateToIntConverter.DateToInt(Calendar.getInstance()));
        Toast.makeText(this, asd, Toast.LENGTH_SHORT).show();
        String steps = String.valueOf(db.getTodayStep());
        String kcal = String.valueOf(db.getTodayStep() / 27);
        stepcount.setText(steps);
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
                                Database db = new Database(getApplicationContext());
                                TextView stepcount = (TextView) findViewById(R.id.steps);
                                String steps = String.valueOf(db.getTodayStep());
                                stepcount.setText(steps);

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

}
