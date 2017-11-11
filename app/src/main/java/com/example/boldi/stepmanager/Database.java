package com.example.boldi.stepmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void saveStep(int step){
        int date = DateToIntConverter.DateToInt(Calendar.getInstance().getTime());
        Cursor c = getReadableDatabase().query(DB_NAME,new String[]{"date","steps"},"date = ?", new String[]{String.valueOf(date)},null,null,null);
        if (c.getCount() == 0){
            getWritableDatabase().execSQL("INSERT INTO "+DB_NAME+" VALUES("+date+","+step+")");
        }else{
            c.moveToFirst();
            if (c.getInt(1) > step){
                step += c.getInt(1);
            }
            getWritableDatabase().execSQL("UPDATE "+DB_NAME+" SET steps = "+step+" WHERE date = "+date);
        }


    }
    public int getTodayStep(){
        int date = DateToIntConverter.DateToInt(Calendar.getInstance().getTime());
        Cursor c = getReadableDatabase().query(DB_NAME,new String[]{"date", "steps"},"date = ?", new String[]{String.valueOf(date)},null,null,null);
        c.moveToFirst();
        int ret = 0;
        if (c.getCount()!= 0){
            ret = c.getInt(1);
        }
        return ret;
    }


}
