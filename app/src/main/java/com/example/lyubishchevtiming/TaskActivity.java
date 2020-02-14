package com.example.lyubishchevtiming;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.ColumnInfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.model.Week;
import com.example.lyubishchevtiming.viewmodel.LogByDateForTaskViewModel;
import com.example.lyubishchevtiming.viewmodel.LogByDateForTaskViewModelFactory;
import com.example.lyubishchevtiming.viewmodel.TaskViewModel;
import com.example.lyubishchevtiming.viewmodel.TaskViewModelFactory;
import com.example.lyubishchevtiming.viewmodel.TaskWeekViewModel;
import com.example.lyubishchevtiming.viewmodel.TaskWeekViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;


public class TaskActivity extends AppCompatActivity {

   // private Task intentTask;
    private Task mTask;
    private Week mWeek;
    private TextView mTaskNameTextView;
    private TextView mGoalTextView;
    private TextView mLeftTextView;
    private LinearLayout mHeader;
    private FloatingActionButton mAddEditTaskButton;
    private Button mStartButton;
    private AppDatabase mDb;
    private ImageView mMonImageView;
    private ImageView mTueImageView;
    private ImageView mWedImageView;
    private ImageView mThuImageView;
    private ImageView mFriImageView;
    private ImageView mSatImageView;
    private ImageView mSunImageView;
    private TextView mDoneTextView;
    private long todayDesiredAmountOfTime;
    private long timeFromLogs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mDb = AppDatabase.getInstance(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("task")) {
                mTask = intentThatStartedThisActivity.getExtras().getParcelable("task");
               // Log.d(TAG, "onCreate: " + mTask.getWeekId());
                loadWeekFromDatabase(String.valueOf(mTask.getWeekId()));
                loadTaskFromDatabase(mTask.getId());
                getLogsForToday();
            }
        }


        mTaskNameTextView = findViewById(R.id.task_name_summary);
        mLeftTextView = findViewById(R.id.left);
        mGoalTextView = findViewById(R.id.goal);
        mDoneTextView = findViewById(R.id.done);
        mHeader = findViewById(R.id.viewA);
        mAddEditTaskButton = findViewById(R.id.fab_edit_add_task);
        mStartButton = findViewById(R.id.start_button);

        mMonImageView = findViewById(R.id.mon_icon);
        mTueImageView = findViewById(R.id.tue_icon);
        mWedImageView = findViewById(R.id.wed_icon);
        mThuImageView = findViewById(R.id.thu_icon);
        mFriImageView = findViewById(R.id.fri_icon);
        mSatImageView = findViewById(R.id.sat_icon);
        mSunImageView = findViewById(R.id.sun_icon);

        mAddEditTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class destinationClass = AddEditTaskActivity.class;
                Intent intent = new Intent(TaskActivity.this, destinationClass);
                intent.putExtra("task", mTask);
                startActivity(intent);
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class destinationClass = TimeTrackingActivity.class;
                Intent intent = new Intent(TaskActivity.this, destinationClass);
                intent.putExtra("task", mTask);
                intent.putExtra("todayDesiredTime", todayDesiredAmountOfTime);
                startActivity(intent);
            }
        });

    }

    public void populateUI(){
        mTaskNameTextView.setText(mTask.getName());
        String duration = convertTimeAmountToStringWithoutUTF(mTask.getDuration());
        mGoalTextView.setText(getResources().getString(R.string.goal,duration ));

        Log.d(TAG, "populateUI: " + timeFromLogs);

        setImageViewColor();

    }

    public String convertTimeAmountToString(long timeAmount){
        Date date = new Date(timeAmount);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public String convertTimeAmountToStringWithoutUTF(long timeAmount){
        Date date = new Date(timeAmount);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public void setWeekDayIcons(){
        todayDesiredAmountOfTime = 0;

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);


        if (mWeek.getMon() == 0){
            mMonImageView.setImageResource(R.mipmap.mon_disabled);
        } else if (mWeek.getMon() !=0 && day == Calendar.MONDAY){
            mMonImageView.setImageResource(R.mipmap.mon_today);
            todayDesiredAmountOfTime = mWeek.getMon();
        } else if (mWeek.getMon()!=0){
            mMonImageView.setImageResource(R.mipmap.mon_enabled);
        }

        if (mWeek.getTue() == 0){
            mTueImageView.setImageResource(R.mipmap.tue_disabled);
        } else if (mWeek.getTue() !=0 && day == Calendar.TUESDAY){
            mTueImageView.setImageResource(R.mipmap.tue_today);
            todayDesiredAmountOfTime = mWeek.getTue();
        } else if (mWeek.getTue()!=0){
            mTueImageView.setImageResource(R.mipmap.tue_enabled);
        }

        if (mWeek.getWed() == 0){
            mWedImageView.setImageResource(R.mipmap.wed_disabled);
        } else if (mWeek.getWed() !=0 && day == Calendar.WEDNESDAY){
            mWedImageView.setImageResource(R.mipmap.wed_today);
            todayDesiredAmountOfTime = mWeek.getWed();

        } else if (mWeek.getWed()!=0){
            mWedImageView.setImageResource(R.mipmap.wed_enabled);
        }

        if (mWeek.getThu() == 0){
            mThuImageView.setImageResource(R.mipmap.thu_disabled);
        } else if (mWeek.getThu() !=0 && day == Calendar.THURSDAY){
            mThuImageView.setImageResource(R.mipmap.thu_today);
            todayDesiredAmountOfTime = mWeek.getThu();
        } else if (mWeek.getThu()!=0){
            mThuImageView.setImageResource(R.mipmap.thu_enabled);
        }

        if (mWeek.getFri() == 0){
            mFriImageView.setImageResource(R.mipmap.fri_disabled);
        } else if (mWeek.getFri() !=0 && day == Calendar.FRIDAY){
            mFriImageView.setImageResource(R.mipmap.fri_today);
            todayDesiredAmountOfTime = mWeek.getFri();

          /*
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
            mFriImageView.setLayoutParams(layoutParams);*/
        } else if (mWeek.getFri()!=0){
            mFriImageView.setImageResource(R.mipmap.fri_enabled);
        }

        if (mWeek.getSat() == 0){
            mSatImageView.setImageResource(R.mipmap.sat_disabled);
        } else if (mWeek.getSat() !=0 && day == Calendar.SATURDAY){
            mSatImageView.setImageResource(R.mipmap.sat_today);
            todayDesiredAmountOfTime = mWeek.getSat();
        } else if (mWeek.getSat()!=0){
            mSatImageView.setImageResource(R.mipmap.sat_enabled);
        }

        if (mWeek.getSun() == 0){
            mSunImageView.setImageResource(R.mipmap.sun_disabled);
        } else if (mWeek.getSun() !=0 && day == Calendar.SUNDAY){
            mSunImageView.setImageResource(R.mipmap.sun_today);
            todayDesiredAmountOfTime = mWeek.getSun();
        } else if (mWeek.getSun()!=0){
            mSunImageView.setImageResource(R.mipmap.sun_enabled);
        }


    }

    private void loadTaskFromDatabase(int id) {
        TaskViewModelFactory factory = new TaskViewModelFactory(mDb, id);
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final TaskViewModel viewModel
                = ViewModelProviders.of(this, factory).get(TaskViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getTask().observe(this, new Observer<Task>() {
            @Override
            public void onChanged(@Nullable final Task task) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (task != null) {
                            mTask = task;
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    populateUI();

                                    // Stuff that updates the UI

                                }
                            });
                          //
                            //  Log.d(TAG, "run: task" + mTask.getName() + " " + mTask.getWeekId());
                        }
                    }
                });
            }
        });
    }



    private void loadWeekFromDatabase(String id) {
        TaskWeekViewModelFactory factory = new TaskWeekViewModelFactory(mDb, String.valueOf(id));
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final TaskWeekViewModel viewModel
                = ViewModelProviders.of(this, factory).get(TaskWeekViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getWeek().observe(this, new Observer<Week>() {
            @Override
            public void onChanged(@Nullable final Week week) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (week != null) {
                            mWeek = week;
                           // Log.d(TAG, "run: week" + mWeek.getId() + mWeek.getMon());

                        }
                    }
                });
            }
        });
    }

    private void getLogsForToday() {
        // today
        Calendar calendar = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date start = calendar.getTime();

        // next day
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        Log.d(TAG, "getLogsForToday: ");
        LogByDateForTaskViewModelFactory factory = new LogByDateForTaskViewModelFactory(mDb, mTask.getId(), start, end);
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final LogByDateForTaskViewModel viewModel
                = ViewModelProviders.of(this, factory).get(LogByDateForTaskViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getLogsForTask().observe(this, new Observer <Long>() {
            @Override
            public void onChanged(@Nullable final Long time) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (time != null) {
                            timeFromLogs = time.longValue();
                            Log.d(TAG, "timeFromLogs: " + timeFromLogs);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String timeFromLogsString = convertTimeAmountToString(timeFromLogs);
                                    Log.d(TAG, "populateUI: " + timeFromLogsString);
                                    mDoneTextView.setText(getResources().getString(R.string.done, timeFromLogsString));
                                    setWeekDayIcons();
                                    calculateLeftTime();



                                }
                            });

                        }
                    }
                });
            }
        });
    }

    private void calculateLeftTime(){

        if (todayDesiredAmountOfTime == 0){
            String defaultLeft = "day off";
            mLeftTextView.setText(getResources().getString(R.string.left, defaultLeft));
        } else {
            String timeFromLogsString = convertTimeAmountToString(timeFromLogs);
            String goal = convertTimeAmountToStringWithoutUTF(mTask.getDuration());

            int goal_hours = Integer.parseInt(goal.substring(0, 2));
            int done_hours = Integer.parseInt(timeFromLogsString.substring(0, 2));
            int goal_min = Integer.parseInt(goal.substring(3, 5));
            int done_min = Integer.parseInt(timeFromLogsString.substring(3, 5));
            int goal_sec = Integer.parseInt(goal.substring(6, 8));
            int done_sec = Integer.parseInt(timeFromLogsString.substring(6, 8));

            Log.d(TAG, "populateUI: " + goal_min);
            int goalInSec = goal_hours * 60 * 60 + goal_min * 60 + goal_sec;
            int doneInSec = done_hours * 60 * 60 + done_min * 60 + done_sec;
            int leftInSec = goalInSec - doneInSec;

            int left_hours = leftInSec / 3600;
            leftInSec = leftInSec % 3600;
            int left_min = leftInSec / 60;
            leftInSec = leftInSec % 60;
            int left_sec = leftInSec;
            String leftString = "";
            if (left_hours < 10) {
                leftString = leftString + "0" + left_hours + ":";
            } else {
                leftString = leftString + left_hours + ":";
            }

            if (left_min < 10) {
                leftString = leftString + "0" + left_min + ":";
            } else {
                leftString = leftString + left_min + ":";
            }

            if (left_sec < 10) {
                leftString = leftString + "0" + left_sec;
            } else {
                leftString = leftString + left_sec;
            }

            mLeftTextView.setText(getResources().getString(R.string.left, leftString));
        }
    }


    private void setImageViewColor(){
        switch (mTask.getColor()) {
            case "red":
                mHeader.setBackgroundColor(getResources().getColor(R.color.red));
                break;
            case "glaucous":
                mHeader.setBackgroundColor(getResources().getColor(R.color.glaucous));
                break;
            case "yellow":
                mHeader.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case "green":
                mHeader.setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case "orange":
                mHeader.setBackgroundColor(getResources().getColor(R.color.orange));
                break;
            case "peach":
                mHeader.setBackgroundColor(getResources().getColor(R.color.peach));
                break;
            case "lavender":
                mHeader.setBackgroundColor(getResources().getColor(R.color.lavender));
                break;
            case "blue":
                mHeader.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            default:
                mHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        }
    }


}
