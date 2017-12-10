package com.example.boldi.stepmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Kristóf on 11/11/2017.
 */

public class BootListener extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        SharedPreferences prefs = context.getSharedPreferences("stepmanager", Context.MODE_PRIVATE);
        context.startService(new Intent(context, StepListener.class));
    }
}