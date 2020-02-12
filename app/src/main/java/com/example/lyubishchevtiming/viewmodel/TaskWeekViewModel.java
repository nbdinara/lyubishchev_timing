package com.example.lyubishchevtiming.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.model.Week;

public class TaskWeekViewModel extends ViewModel {

    // Constant for logging
    private static final String TAG = TaskWeekViewModel.class.getSimpleName();

    private LiveData<Week> week;

    public TaskWeekViewModel(AppDatabase database, int taskId) {
        week = database.weekDao().loadWeekById(taskId);
    }
    public LiveData<Week> getWeek(){
        return week;
    }
}

