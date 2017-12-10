package com.example.boldi.stepmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Kristóf on 11/11/2017.
 */

public class Database extends SQLiteOpenHelper {
    private static final int DB_VER = 1;
    private static final String DB_NAME = "steps";

    public Database getInstance(final Context c) {
        if (instance == null) {
            instance = new Database(c.getApplicationContext());
        }
        return instance;
    }

    private Database instance;

    public Database(Context context) {
        super(context, DB_NAME, null,DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_NAME + " (date INTEGER, steps INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //TODO a frissítést úgy kellene csinálni, hogy nem minden egyes lépésre, csak bizonos időközönként, vagy egy adott lépésszám után
    public void saveStep(int step){
        int stepsTillBoot = getTillBootStep();
        if (stepsTillBoot > step){
            saveTillBootStep(step);
        }else{
            saveTillBootStep(step);
            step -= stepsTillBoot;
        }
        int date = DateToIntConverter.DateToInt(Calendar.getInstance());
        Cursor c = getReadableDatabase().query(DB_NAME,new String[]{"date","steps"},"date = ?", new String[]{String.valueOf(date)},null,null,null);
        if (c.getCount() == 0){
            getWritableDatabase().execSQL("INSERT INTO "+DB_NAME+" VALUES("+date+","+step+")");
        }else{
            c.moveToFirst();
            step+= c.getInt(1);
            getWritableDatabase().execSQL("UPDATE "+DB_NAME+" SET steps = "+step+" WHERE date = "+date);
        }
        c.close();


    }
    //just for debugging
    public void saveRandomStep(int step, int date) {
        Cursor c = getReadableDatabase().query(DB_NAME, new String[]{"date", "steps"}, "date = ?", new String[]{String.valueOf(date)}, null, null, null);
        if (c.getCount() == 0) {
            getWritableDatabase().execSQL("INSERT INTO " + DB_NAME + " VALUES(" + date + "," + step + ")");
        } else {
            getWritableDatabase().execSQL("UPDATE " + DB_NAME + " SET steps = " + step + " WHERE date = " + date);
        }
        c.close();
    }

    public int getTodayStep(){
        int date = DateToIntConverter.DateToInt(Calendar.getInstance());
        Cursor c = getReadableDatabase().query(DB_NAME,new String[]{"date", "steps"},"date = ?", new String[]{String.valueOf(date)},null,null,null);
        c.moveToFirst();
        int ret = 0;
        if (c.getCount()!= 0){
            ret = c.getInt(1);
        }
        c.close();
        return ret;
    }
    public int getTillBootStep(){
        Cursor c = getReadableDatabase().query(DB_NAME,new String[]{"date", "steps"},"date = ?", new String[]{String.valueOf(-1)},null,null,null);
        c.moveToFirst();
        int ret = 0;
        if (c.getCount()!= 0){
            ret = c.getInt(1);
        }else{
            getWritableDatabase().execSQL("INSERT INTO " + DB_NAME + " VALUES(" + -1 + "," + (int)0 + ")");
        }
        c.close();
        return ret;
    }
    public void saveTillBootStep(int value){
        getWritableDatabase().execSQL("UPDATE " + DB_NAME + " SET steps = " + value + " WHERE date = " + -1);
    }
    public void UploadWithUnrealData(int[] stepsOfDay){
        int dayMinus = stepsOfDay.length;
        Calendar c = Calendar.getInstance();
               c.add(Calendar.DAY_OF_MONTH, -(dayMinus+1));
        for (int i = 0; i < dayMinus; i++){
            c.add(Calendar.DAY_OF_MONTH, i);
            saveRandomStep(stepsOfDay[i],DateToIntConverter.DateToInt(c));
        }
    }
    public void resetAllData(){
        getWritableDatabase().execSQL("UPDATE " + DB_NAME + " SET steps = 0 WHERE date IS NOT -1");
    }
    public int getAvarageSteps(){
        int all = 0;
        Cursor c = getReadableDatabase().query(DB_NAME,new String[]{"date", "steps"},"date IS NOT ?", new String[]{String.valueOf(-1)},null,null,null);
        c.moveToFirst();
        int days =0;
        for (int i = 0; i < c.getCount(); i++){
            if (c.getInt(1) > 0)
                days ++;
            all += c.getInt(1);
            c.moveToNext();
        }
        c.close();
        return (days > 0)? all / days : 0;
    }
    public int getTotalSteps(){
        int all = 0;
        Cursor c = getReadableDatabase().query(DB_NAME,new String[]{"date", "steps"},"date IS NOT ?", new String[]{String.valueOf(-1)},null,null,null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++){
            all += c.getInt(1);
            c.moveToNext();
        }
        c.close();
        return all;
    }
    public ArrayList<BarEntry> getLastSevenDaySteps(int date){
        date = 20171128;
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        Cursor c = getReadableDatabase().query(DB_NAME,new String[]{"date", "steps"},null, null,null,null,null);
        c.moveToFirst();
        if (c.getCount()!= 0){
            int j = 0;
            for (int i = date; i > date-7; i--){
                c.moveToFirst();
                do {
                    int proba = c.getInt(0);
                    if (c.getInt(0) == i){
                        j++;
                        int steps = c.getInt(1);
                        barEntries.add(new BarEntry((float) c.getInt(1),j));
                    }
                }while(c.moveToNext());
            }
        }
        c.close();
        return barEntries;
    }

}
