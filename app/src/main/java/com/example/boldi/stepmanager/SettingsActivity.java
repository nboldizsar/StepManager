package com.example.boldi.stepmanager;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class SettingsActivity extends AppCompatActivity {

    Database db;
    EditText weightPicker;
    EditText goalPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = new Database(this);

        weightPicker = (EditText)findViewById(R.id.weightEdit);
        goalPicker = (EditText) findViewById(R.id.goalEdit);

        /*weightPicker.setText(db.getWeight());*/

        Button saveButton = (Button) findViewById(R.id.button3);
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //db.saveSettings(Integer.parseInt(weightPicker.getText().toString()), Integer.parseInt(goalPicker.getText().toString()));
                        finish();
                    }
                }
        );
    }
}
