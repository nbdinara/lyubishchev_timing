package com.example.lyubishchevtiming.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.lyubishchevtiming.database.AppDatabase;

import java.sql.Date;

public class SummaryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final Date mStartDate;
    private final Date mEndDate;


    public SummaryViewModelFactory(AppDatabase database, Date startDate, Date endDate) {
        mDb = database;
        mStartDate= startDate;
        mEndDate = endDate;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SummaryViewModel(mDb, mStartDate, mEndDate);
    }


}