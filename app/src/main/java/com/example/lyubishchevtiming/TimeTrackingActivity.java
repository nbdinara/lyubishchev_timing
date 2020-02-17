package com.example.lyubishchevtiming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.room.ColumnInfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.database.AppExecutors;
import com.example.lyubishchevtiming.model.Log;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.service.TimeTrackingService;

import java.util.Date;
import static android.content.ContentValues.TAG;


public class TimeTrackingActivity extends AppCompatActivity {

    private Chronometer mChronometer;
    private Button mStartButton;
    private Button mCancelButton;
    private boolean isRunning;
    private Task mTask;
    private AppDatabase mDb;
    private Log mLog;
    private long timeAmount;
    private long desiredTimeAmount;
    private long timeWhenStopped = 0;

    private static final String CHRONOMETER_TIME_KEY = "time";
    private static final String CHRONOMETER_RUNNING_KEY = "running";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracking);

        mDb = AppDatabase.getInstance(this);
        mChronometer = findViewById(R.id.chronometer);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mTask = intentThatStartedThisActivity.getExtras().getParcelable("task");
            desiredTimeAmount = intentThatStartedThisActivity.getLongExtra("todayDesiredTime", 0);
            if (intentThatStartedThisActivity.hasExtra("time") && intentThatStartedThisActivity.hasExtra("running")) {
                if (intentThatStartedThisActivity.hasExtra("time")) {
                    timeWhenStopped = intentThatStartedThisActivity.getLongExtra("time", 0);
                    setCurrentTime(timeWhenStopped);
                    android.util.Log.d(TAG, "onCreate: max " + timeWhenStopped);
                    timeWhenStopped = SystemClock.elapsedRealtime() - mChronometer.getBase();
                    android.util.Log.d(TAG, "onCreate:22 max " + timeWhenStopped);
                    if (intentThatStartedThisActivity.hasExtra("running")) {
                        isRunning = intentThatStartedThisActivity.getBooleanExtra("running", false);
                        if (isRunning) {
                            mChronometer.start();
                        }
                    }
                }

            } else {
                startChronometer();
            }
        }

        mStartButton = findViewById(R.id.stop_btn);
        mCancelButton = findViewById(R.id.cancel_btn);

        if (savedInstanceState!= null){
            android.util.Log.d(TAG, "onCreate: savedInstanceState != null");
            android.util.Log.d(TAG, "onCreate: SystemClock.elapsedRealtime() !=null " + SystemClock.elapsedRealtime());

            isRunning = savedInstanceState.getBoolean(CHRONOMETER_RUNNING_KEY);
            setCurrentTime(savedInstanceState.getLong(CHRONOMETER_TIME_KEY));
            timeWhenStopped = SystemClock.elapsedRealtime() - mChronometer.getBase();
            if (isRunning) {
                mChronometer.start();
            }

        } else if (savedInstanceState == null && !intentThatStartedThisActivity.hasExtra("time")){
            android.util.Log.d(TAG, "onCreate: savedInstanceState == null");
            android.util.Log.d(TAG, "onCreate: SystemClock.elapsedRealtime() == null " + SystemClock.elapsedRealtime());

            startChronometer();
        } else if (intentThatStartedThisActivity.hasExtra("time") && intentThatStartedThisActivity.hasExtra("running")){

            timeWhenStopped = intentThatStartedThisActivity.getLongExtra("time", 0);
            android.util.Log.d(TAG, "onCreate: timeWhenStopped is " + timeWhenStopped);
            isRunning = intentThatStartedThisActivity.getBooleanExtra("running", false);
            setCurrentTime(timeWhenStopped);

            if (isRunning) {
                mChronometer.start();
            }
        }

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

    public void setCurrentTime(long time) {
        timeWhenStopped = time;
        mChronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
    }

    public void startChronometer(){
        mChronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
        mChronometer.start();
        isRunning = true;
    }

    public void stopChronometer(){
       // stopService();
        mChronometer.stop();
        timeAmount = SystemClock.elapsedRealtime() - mChronometer.getBase();
        String mTaskName = mTask.getName();
        isRunning = false;
        getLog();
        saveLogToDb();
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


    public void getLog(){
        mLog = new Log();
        Date today = new Date();
        android.util.Log.d(TAG, "getLog: " + today);
        mLog.setTodayDate(today);
        mLog.setTodayTimeAmount(timeAmount);
        mLog.setDesiredTimeAmount(desiredTimeAmount);
        mLog.setTaskId(mTask.getId());

    }

    public void saveLogToDb (){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.logDao().insertLog(mLog);
            }
        });
        android.util.Log.d(TAG, "saveLogToDb: " + mLog.getId() + " / " + mLog.getTodayDate() +
                " / " + mLog.getTodayTimeAmount() + " / " + mLog.getDesiredTimeAmount() + " / " + mLog.getTaskId() );
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isRunning) {
            timeWhenStopped = SystemClock.elapsedRealtime() - mChronometer.getBase();
        }
        outState.putLong(CHRONOMETER_TIME_KEY, timeWhenStopped );
        outState.putBoolean(CHRONOMETER_RUNNING_KEY, isRunning);
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, TimeTrackingService.class);
        serviceIntent.putExtra("taskExtra", mTask);
        serviceIntent.putExtra("desiredTime", desiredTimeAmount);
        if (isRunning) {
            timeWhenStopped = SystemClock.elapsedRealtime() - mChronometer.getBase();
        }
        serviceIntent.putExtra("time", timeWhenStopped);
        serviceIntent.putExtra("running", isRunning);

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, TimeTrackingService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        startService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService();
    }
}
