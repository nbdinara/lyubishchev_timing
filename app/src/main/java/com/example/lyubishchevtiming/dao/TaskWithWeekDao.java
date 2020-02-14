package com.example.lyubishchevtiming.dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.model.TaskWithWeek;
import com.example.lyubishchevtiming.model.Week;

import java.util.List;

public interface TaskWithWeekDao  {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeek(Week week);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Transaction
    @Query("SELECT * FROM week")
    public List<TaskWithWeek> getWeekAndTask();

}
