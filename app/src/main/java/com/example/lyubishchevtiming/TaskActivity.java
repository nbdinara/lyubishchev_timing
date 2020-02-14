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
import com.example.lyubishchevtiming.viewmodel.TaskViewModel;
import com.example.lyubishchevtiming.viewmodel.TaskViewModelFactory;
import com.example.lyubishchevtiming.viewmodel.TaskWeekViewModel;
import com.example.lyubishchevtiming.viewmodel.TaskWeekViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import static android.content.ContentValues.TAG;


public class TaskActivity extends AppCompatActivity {

   // private Task intentTask;
    private Task mTask;
    private Week mWeek;
    private TextView mTaskNameTextView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mDb = AppDatabase.getInstance(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("task")) {
                mTask = intentThatStartedThisActivity.getExtras().getParcelable("task");
                Log.d(TAG, "onCreate: " + mTask.getWeekId());
                loadWeekFromDatabase(String.valueOf(mTask.getWeekId()));
                loadTaskFromDatabase(mTask.getId());
            }
        }


        mTaskNameTextView = findViewById(R.id.task_name_summary);
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
        setImageViewColor();
        setWeekDayIcons();
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
                            Log.d(TAG, "run: task" + mTask.getName() + " " + mTask.getWeekId());
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
                            Log.d(TAG, "run: week" + mWeek.getId() + mWeek.getMon());

                        }
                    }
                });
            }
        });
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
