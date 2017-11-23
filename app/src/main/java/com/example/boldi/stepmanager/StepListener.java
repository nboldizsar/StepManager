package com.example.boldi.stepmanager;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class StepListener extends Service implements SensorEventListener {
    int steps;
    int lastSavedSteps;
    long lastSavedTime;
    private static final int saveRateSteps = 0;
    private static final long saveRateTime =  AlarmManager.INTERVAL_HOUR;
    SensorManager sensorManager;
    boolean activityRunning;
    Database db;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("StepListener","on start");
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null)
        {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }else
        {
            Toast.makeText(this,"Sensor is not available",Toast.LENGTH_LONG).show();
        }
        db = new Database(this);
    }

    @Override
    public void onDestroy() {
        Log.i("StepListener","ondestroy");
        try {
            sensorManager.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] < Integer.MAX_VALUE) {
            if (event.values[0] > lastSavedSteps + saveRateSteps || System.currentTimeMillis() > lastSavedTime + saveRateTime) {
                db.saveStep((int) event.values[0]);
                lastSavedTime = System.currentTimeMillis();
                lastSavedSteps = (int) event.values[0];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
