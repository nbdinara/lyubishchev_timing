package com.example.lyubishchevtiming.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Summary;
import com.example.lyubishchevtiming.model.Task;

import java.sql.Date;
import java.util.List;

public class SummaryViewModel extends ViewModel {

    // Constant for logging
    private static final String TAG = SummaryViewModel.class.getSimpleName();

    private LiveData<List<Summary>> summary;

    public SummaryViewModel(AppDatabase database, Date startDate, Date endDate) {
        summary = database.logDao().getLogsAndTaskInfoForSpecificDate(startDate, endDate);
    }


    public LiveData<List<Summary>> getSummary(){
        return summary;
    }
}
