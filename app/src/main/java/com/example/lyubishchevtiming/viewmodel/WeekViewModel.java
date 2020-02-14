package com.example.lyubishchevtiming.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.model.Week;

import java.util.List;

public class WeekViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Week>> weeks;


    public WeekViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        weeks = database.weekDao().loadAllWeekDaysCombinations();
    }

    public LiveData<List<Week>> getWeeks(){
        return weeks;
    }
}