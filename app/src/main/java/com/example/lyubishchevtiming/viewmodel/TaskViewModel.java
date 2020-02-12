package com.example.lyubishchevtiming.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Task;

public class TaskViewModel extends ViewModel {

    // Constant for logging
    private static final String TAG = TaskViewModel.class.getSimpleName();

    private LiveData<Task> task;

    public TaskViewModel(AppDatabase database, int taskId) {
        task = database.taskDao().loadTaskById(taskId);
    }
    public LiveData<Task> getTask(){
        return task;
    }
}
