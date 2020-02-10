package com.example.lyubishchevtiming;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TimeTrackingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracking);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
