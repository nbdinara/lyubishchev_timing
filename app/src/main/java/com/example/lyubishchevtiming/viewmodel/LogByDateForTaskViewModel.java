package com.example.lyubishchevtiming.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Log;
import com.example.lyubishchevtiming.model.Summary;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.model.Week;

import java.util.Date;
import java.util.List;

public class LogByDateForTaskViewModel extends ViewModel {

    // Constant for logging
    private static final String TAG = LogByDateForTaskViewModel.class.getSimpleName();

    private LiveData<Long> logsForTask;

    public LogByDateForTaskViewModel(AppDatabase database, int id, Date start, Date end) {
        logsForTask = database.logDao().loadLogsForTodayTask(id, start, end);
    }


    public LiveData<Long> getLogsForTask(){
        return logsForTask;
    }
}

