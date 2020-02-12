package com.example.lyubishchevtiming.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.lyubishchevtiming.database.AppDatabase;

public class TaskWeekViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mWeekId;


    public TaskWeekViewModelFactory(AppDatabase database, int weekId) {
        mDb = database;
        mWeekId = weekId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new TaskWeekViewModel(mDb, mWeekId);
    }
}
