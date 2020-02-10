package com.example.lyubishchevtiming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TimeTrackingSummaryActivity extends AppCompatActivity {

    private long mTimeAmount;
    private TextView mTimeAmountTextView;
    private static final String TAG = "TimeTracking";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracking_summary);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mTimeAmount = intentThatStartedThisActivity.getLongExtra("time_amount", 0L);
        }

        Log.d(TAG, "onCreate: " + mTimeAmount);

        //Log.d("timeAmount", mTimeAmount);
        mTimeAmountTextView = findViewById(R.id.time_amount_summary);
        mTimeAmountTextView.setText(Long.toString(mTimeAmount));
    }
}
