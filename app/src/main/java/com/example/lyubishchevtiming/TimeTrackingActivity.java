package com.example.lyubishchevtiming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class TimeTrackingActivity extends AppCompatActivity {

    private Chronometer mChronometer;
    private Button mStartButton;
    private Button mCancelButton;
    private boolean isRunning;
    private Task mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracking);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mTask = intentThatStartedThisActivity.getExtras().getParcelable("task");
        }

        mChronometer = findViewById(R.id.chronometer);
        mStartButton = findViewById(R.id.stop_btn);
        mCancelButton = findViewById(R.id.cancel_btn);
        startChronometer();


        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning){
                    stopChronometer();
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void startChronometer(){
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        isRunning = true;
    }

    public void stopChronometer(){
        mChronometer.stop();
        long timeAmount = SystemClock.elapsedRealtime() - mChronometer.getBase();
        String mTaskName = mTask.getName();
        isRunning = false;
        //TODO save time to database
        Intent intent = new Intent(TimeTrackingActivity.this, TimeTrackingSummaryActivity.class);
        intent.putExtra("time_amount", timeAmount);
        intent.putExtra("task", mTask);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
