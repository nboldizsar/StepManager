package com.example.boldi.stepmanager;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFNAME = "setting";
    private static final String Weight_NAME = "weight";
    private static final String DAILYGOAL_NAME = "goal";

    Database db;
    EditText weightPicker;
    EditText goalPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        SharedPreferences settings = getSharedPreferences(PREFNAME, 0);
        int weight = settings.getInt(Weight_NAME, 0);
        int goal = settings.getInt(DAILYGOAL_NAME, 0);

        if(weight == 0)
            weight = 60;
        if(goal == 0)
            goal = 5000;

        weightPicker = (EditText)findViewById(R.id.weightEdit);
        goalPicker = (EditText) findViewById(R.id.goalEdit);

        weightPicker.setText(String.valueOf(weight));
        goalPicker.setText(String.valueOf(goal));

        Button saveButton = (Button) findViewById(R.id.button3);
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences settings = getSharedPreferences(PREFNAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt(Weight_NAME, Integer.parseInt(weightPicker.getText().toString()));
                        editor.putInt(DAILYGOAL_NAME, Integer.parseInt(goalPicker.getText().toString()));
                        editor.commit();
                        finish();
                    }
                }
        );

        Button resetButt = (Button) findViewById(R.id.resetButton);

        resetButt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.resetAllData();
                    }
                }
        );

    }
}
