package com.example.lyubishchevtiming.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Week;

public class TaskWeekByNameViewModel extends ViewModel {

    // Constant for logging
    private static final String TAG = TaskWeekViewModel.class.getSimpleName();

    private LiveData<Week> week;


    public TaskWeekByNameViewModel(AppDatabase database, String taskName) {
        week = database.weekDao().loadWeekByTaskName(taskName);
    }

    public LiveData<Week> getWeekByTaskName(){
        return week;
    }
}
