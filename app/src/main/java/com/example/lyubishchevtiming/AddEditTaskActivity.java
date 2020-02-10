package com.example.lyubishchevtiming;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddEditTaskActivity extends AppCompatActivity {

    private Task mTask;
    private NumberPicker hoursNumberPicker;
    private NumberPicker minutesNumberPicker;
    private FloatingActionButton mSaveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra("task")) {
            mTask = intentThatStartedThisActivity.getExtras().getParcelable("task");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        hoursNumberPicker  = findViewById(R.id.hours_number_picker);
        hoursNumberPicker.setMinValue(0);
        hoursNumberPicker.setMaxValue(23);
        hoursNumberPicker.setOnValueChangedListener(onHoursChangeListener);

        minutesNumberPicker  = findViewById(R.id.minutes_number_picker);
        minutesNumberPicker.setMinValue(0);
        minutesNumberPicker.setMaxValue(59);
        minutesNumberPicker.setOnValueChangedListener(onMinutesChangeListener);

        mSaveButton = findViewById(R.id.save_fab);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO save changes to database
                finish();

            }
        });
    }


    public void fillFields(){
        if (mTask != null){
            //TODO fill views with data from intent
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // do something here, such as start an Intent to the parent activity.
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    NumberPicker.OnValueChangeListener onHoursChangeListener =
            new NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(AddEditTaskActivity.this,
                            "selected number "+numberPicker.getValue(), Toast.LENGTH_SHORT);
                }
            };

    NumberPicker.OnValueChangeListener onMinutesChangeListener =
            new NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(AddEditTaskActivity.this,
                            "selected number "+numberPicker.getValue(), Toast.LENGTH_SHORT);
                }
            };
}
