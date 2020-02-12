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

    public SummaryViewModelFactory(AppDatabase database, Date today) {
        mDb = database;
        mStartDate= today;
        mEndDate = null;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        if (mEndDate !=null) {
            return (T) new SummaryViewModel(mDb, mStartDate, mEndDate);
        } else return (T) new SummaryViewModel(mDb, mStartDate);
    }


}