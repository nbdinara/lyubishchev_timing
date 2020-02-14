package com.example.lyubishchevtiming;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mDb = AppDatabase.getInstance(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("task")) {
                mTask = intentThatStartedThisActivity.getExtras().getParcelable("task");
                loadWeekFromDatabase(mTask.getWeekId());
                loadTaskFromDatabase(mTask.getId());
            }
        }


        mTaskNameTextView = findViewById(R.id.task_name_summary);
        mHeader = findViewById(R.id.viewA);
        mAddEditTaskButton = findViewById(R.id.fab_edit_add_task);
        mStartButton = findViewById(R.id.start_button);

        mAddEditTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class destinationClass = AddEditTaskActivity.class;
                Intent intent = new Intent(TaskActivity.this, destinationClass);
                intent.putExtra("task", mTask);
                startActivity(intent);
            }
        });

        setImageViewColor();



    }

    public void populateUI(){
        mTaskNameTextView.setText(mTask.getName());

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
        TaskWeekViewModelFactory factory = new TaskWeekViewModelFactory(mDb, id);
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
