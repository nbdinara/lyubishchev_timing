package com.example.lyubishchevtiming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeTrackingSummaryActivity extends AppCompatActivity {

    private long mTimeAmount;
    private Task mTask;
    private TextView mTimeAmountTextView;
    private TextView mTaskNameTextView;
    private Button mContinueButton;
    private static final String TAG = "TimeTracking";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracking_summary);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mTimeAmount = intentThatStartedThisActivity.getLongExtra("time_amount", 0L);
            mTask = intentThatStartedThisActivity.getParcelableExtra("task");
        }

        mTaskNameTextView = findViewById(R.id.task_label);
        mTaskNameTextView.setText(mTask.getName());
        Log.d(TAG, "onCreate: " + mTimeAmount);

        mTimeAmountTextView = findViewById(R.id.time_amount_summary);
        mTimeAmountTextView.setText(convertTimeAmountToString(mTimeAmount));

        mContinueButton = findViewById(R.id.continue_button);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeTrackingSummaryActivity.this, TaskActivity.class);
                intent.putExtra("task", mTask);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public String convertTimeAmountToString(long timeAmount){
        Date date = new Date(timeAmount);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }
}
