package com.example.lyubishchevtiming.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Log;
import com.example.lyubishchevtiming.model.Task;

import java.util.List;

public class AllLogsViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = AllLogsViewModel.class.getSimpleName();

    private LiveData<List<Log>> logs;


    public AllLogsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        logs = database.logDao().loadAllLogs();
    }

    public LiveData<List<Log>> getLogs(){
        return logs;
    }
}
