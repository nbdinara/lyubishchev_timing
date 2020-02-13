package com.example.lyubishchevtiming;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.database.AppExecutors;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.model.Week;
import com.example.lyubishchevtiming.viewmodel.TaskWeekByNameViewModel;
import com.example.lyubishchevtiming.viewmodel.TaskWeekByNameViewModelFactory;
import com.example.lyubishchevtiming.viewmodel.TaskWeekViewModel;
import com.example.lyubishchevtiming.viewmodel.TaskWeekViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class AddEditTaskActivity extends AppCompatActivity {

    private Task mTask;
    private NumberPicker hoursNumberPicker;
    private NumberPicker minutesNumberPicker;
    private FloatingActionButton mSaveButton;
    private AppDatabase mDb;
    private boolean isEdit;
    private EditText mTaskNameEditText;
    private Week mWeek;
    private CheckBox monCheckBox;
    private CheckBox tueCheckBox;
    private CheckBox wedCheckBox;
    private CheckBox thuCheckBox;
    private CheckBox friCheckBox;
    private CheckBox satCheckBox;
    private CheckBox sunCheckBox;
    Task toDbTask;
    Week toDbWeek;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
        mDb = AppDatabase.getInstance(this);
        deleteWeeks();

        mTaskNameEditText = findViewById(R.id.task_name_edit_text);
        monCheckBox = findViewById(R.id.mon_checkBox);
        tueCheckBox = findViewById(R.id.tue_checkBox);
        wedCheckBox = findViewById(R.id.wed_checkBox);
        thuCheckBox = findViewById(R.id.thu_checkBox);
        friCheckBox = findViewById(R.id.fri_checkBox);
        satCheckBox = findViewById(R.id.sat_checkBox);
        sunCheckBox = findViewById(R.id.sun_checkBox);
        hoursNumberPicker  = findViewById(R.id.hours_number_picker);
        minutesNumberPicker  = findViewById(R.id.minutes_number_picker);


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra("task")) {
            mTask = intentThatStartedThisActivity.getExtras().getParcelable("task");
            isEdit = true;
            fillFieldsFromIntent();
            Log.d(TAG, "onCreate addTask if: " + isEdit);

        } else {
            mTask = new Task();
            mWeek = new Week();
            isEdit = false;
            Log.d(TAG, "onCreate addTask: " + isEdit);

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        hoursNumberPicker.setMinValue(0);
        hoursNumberPicker.setMaxValue(23);
        hoursNumberPicker.setOnValueChangedListener(onHoursChangeListener);

        minutesNumberPicker.setMinValue(0);
        minutesNumberPicker.setMaxValue(59);
        minutesNumberPicker.setOnValueChangedListener(onMinutesChangeListener);

        mSaveButton = findViewById(R.id.save_fab);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO save changes to database
                if (isEdit){
                    Log.d(TAG, "onCreate updated: " + isEdit);

                    getUpdatedDataFromFields();
                    updateTaskInDatabase();
                } else {
                    Log.d(TAG, "onCreate inserted: " + isEdit);

                    getDataFromTheFields();
                    addWeekToDatabase();
                    loadWeekFromDatabaseByName(toDbTask.getName());
                   // toDbTask.setWeekId(toDbWeek.getId());
                    addTaskToDatabase();
                }
                finish();

            }
        });
    }


    public void fillFieldsFromIntent(){
        if (mTask != null){
            //TODO fill views with data from intent
            loadWeekFromDatabase(mTask.getWeekId());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTaskNameEditText.setText(mTask.getName(), TextView.BufferType.EDITABLE);
                            if(mWeek.getMon() != 0){
                                monCheckBox.setChecked(true);
                            }
                            if(mWeek.getTue() != 0){
                                tueCheckBox.setChecked(true);
                            }
                            if(mWeek.getWed() != 0){
                                wedCheckBox.setChecked(true);
                            }
                            if(mWeek.getThu() != 0){
                                thuCheckBox.setChecked(true);
                            }
                            if(mWeek.getFri() != 0){
                                friCheckBox.setChecked(true);
                            }
                            if(mWeek.getSat() != 0){
                                satCheckBox.setChecked(true);
                            }
                            if(mWeek.getSun() != 0){
                                sunCheckBox.setChecked(true);
                            }
                            if(mTask.getDuration()!=0){

                            }
                            Date date = convertLongToDate(mTask.getDuration());
                            Log.d(TAG, "run: duration" + mTask.getDuration());
                            Log.d(TAG, "run: date" + date);
                            hoursNumberPicker.setValue(date.getHours());
                            Log.d(TAG, "run: hour" + date.getHours());

                            minutesNumberPicker.setValue(date.getMinutes());
                            Log.d(TAG, "run: minutes" + date.getMinutes());


                        }
                    });
                }
            }, 100);



        }
    }

    public void getDataFromTheFields(){
        toDbTask = new Task();
        toDbWeek = new Week();
        toDbTask.setName(mTaskNameEditText.getText().toString());
        toDbTask.setColor("blue");
        int hours = hoursNumberPicker.getValue();
        int minutes = minutesNumberPicker.getValue();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateInString = hours + ":" + minutes + ":" + "00";
        Date date = new Date();
        try {
            date = formatter.parse(dateInString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = date.getTime();
        toDbTask.setDuration(milliseconds);

        if (monCheckBox.isChecked()) {
            toDbWeek.setMon(milliseconds);
        } else {
            toDbWeek.setMon(0);
        }
        if (tueCheckBox.isChecked()) {
            toDbWeek.setTue(milliseconds);
        } else {
            toDbWeek.setTue(0);
        }
        if (wedCheckBox.isChecked()) {
            toDbWeek.setWed(milliseconds);
        } else {
            toDbWeek.setWed(0);
        }
        if (thuCheckBox.isChecked()) {
            toDbWeek.setThu(milliseconds);
        } else {
            toDbWeek.setThu(0);
        }
        if (friCheckBox.isChecked()) {
            toDbWeek.setFri(milliseconds);
        } else {
            toDbWeek.setFri(0);
        }
        if (satCheckBox.isChecked()) {
            toDbWeek.setSat(milliseconds);
        } else {
            toDbWeek.setSat(0);
        }
        if (sunCheckBox.isChecked()) {
            toDbWeek.setSun(milliseconds);
        } else {
            toDbWeek.setSun(0);
        }
        toDbWeek.setTaskName(toDbTask.getName());

    }


    public void getUpdatedDataFromFields(){
        mTask.setName(mTaskNameEditText.getText().toString());
        Log.d(TAG, "getUpdatedDataFromFields: mTaskNameEditText.getText() " + mTaskNameEditText.getText().toString());
        mTask.setColor("blue");
        int hours = hoursNumberPicker.getValue();
        Log.d(TAG, "getUpdatedDataFromFields: hoursNumberPicker.getValue() " + hoursNumberPicker.getValue());

        int minutes = minutesNumberPicker.getValue();
        Log.d(TAG, "getUpdatedDataFromFields: minutesNumberPicker.getValue() " + hoursNumberPicker.getValue());

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateInString = hours + ":" + minutes + ":" + "00";
        Date date = new Date();

        try {
            date = formatter.parse(dateInString);
            Log.d(TAG, "getUpdatedDataFromFields: date " + date);

            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = date.getTime();
        mTask.setDuration(milliseconds);

        if (monCheckBox.isChecked()) {
            mWeek.setMon(milliseconds);
        } else {
            mWeek.setMon(0);
        }
        if (tueCheckBox.isChecked()) {
            mWeek.setTue(milliseconds);
        } else {
            mWeek.setTue(0);
        }
        if (wedCheckBox.isChecked()) {
            mWeek.setWed(milliseconds);
        } else {
            mWeek.setWed(0);
        }
        if (thuCheckBox.isChecked()) {
            mWeek.setThu(milliseconds);
        } else {
            mWeek.setThu(0);
        }
        if (friCheckBox.isChecked()) {
            mWeek.setFri(milliseconds);
        } else {
            mWeek.setFri(0);
        }
        if (satCheckBox.isChecked()) {
            mWeek.setSat(milliseconds);
        } else {
            mWeek.setSat(0);
        }
        if (sunCheckBox.isChecked()) {
            mWeek.setSun(milliseconds);
        } else {
            mWeek.setSun(0);
        }
    }


    public Date convertLongToDate(long longNumber){
        Date date = new Date(longNumber);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return date;
    }


    private void loadWeekFromDatabase(int id) {
        mWeek = new Week();

        TaskWeekViewModelFactory factory = new TaskWeekViewModelFactory(mDb, id);
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final TaskWeekViewModel viewModel
                = ViewModelProviders.of(this, factory).get(TaskWeekViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getWeek().observe(this, new Observer<Week>() {
            @Override
            public void onChanged(@Nullable final Week week) {
                viewModel.getWeek().removeObserver(this);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (week != null) {
                            mWeek = week;
                        }
                    }
                });
            }
        });
    }

    private void loadWeekFromDatabaseByName(String taskName) {
        TaskWeekByNameViewModelFactory factory = new TaskWeekByNameViewModelFactory(mDb, taskName);
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final TaskWeekByNameViewModel viewModel
                = ViewModelProviders.of(this, factory).get(TaskWeekByNameViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getWeekByTaskName().observe(this, new Observer<Week>() {
            @Override
            public void onChanged(@Nullable final Week week) {
                viewModel.getWeekByTaskName().removeObserver(this);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (week != null) {
                            toDbWeek = week;
                            toDbTask.setWeekId(toDbWeek.getId());
                        }
                    }
                });
            }
        });
    }
    //                            mDb.recipeDao().updateRecipe(recipe);

    public void addTaskToDatabase(){

        Log.d(TAG, "addWeekToDatabase: " + toDbTask.getWeekId());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().insertTask(toDbTask);
                Log.d(TAG, "addTaskToDatabase task: " + toDbTask.getId() + toDbTask.getName());
            }
        });


    }

    public void addWeekToDatabase(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.weekDao().insertWeekDayCombination(toDbWeek);
                Log.d(TAG, "addWeekToDatabase week: " + toDbWeek.getId());

            }
        });


    }


    public void updateTaskInDatabase(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.weekDao().updateWeek(mWeek);
            }
        });

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().updateTask(mTask);
            }
        });

    }

    public void deleteWeeks() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.weekDao().deleteWeeks();
            }
        });
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
