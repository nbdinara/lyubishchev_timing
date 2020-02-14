package com.example.lyubishchevtiming.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.lyubishchevtiming.database.AppDatabase;

import java.util.Date;

public class LogByDateForTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mTaskId;
    private final Date mStart;
    private final Date mEnd;


    public LogByDateForTaskViewModelFactory(AppDatabase database,int taskId, Date start, Date end) {
        mDb = database;
        mTaskId = taskId;
        mStart = start;
        mEnd = end;

    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new LogByDateForTaskViewModel(mDb, mTaskId, mStart, mEnd);
    }


}



