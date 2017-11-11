package com.example.boldi.stepmanager;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
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



        TextView stepcount = (TextView) findViewById(R.id.steps);
        Intent i = new Intent(this,StepListener.class);
        startService(i);
        Calendar.getInstance().getTime();
        String asd = String.valueOf(DateToIntConverter.DateToInt(Calendar.getInstance().getTime()));
        Toast.makeText(this, asd, Toast.LENGTH_SHORT).show();
        Database db = new Database(this);
        String steps = String.valueOf(db.getTodayStep());
        stepcount.setText(steps);
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
