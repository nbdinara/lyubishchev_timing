package com.example.lyubishchevtiming.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.model.Week;

import java.util.List;

@Dao
public interface WeekDao {

    @Query("SELECT * FROM week")
    LiveData<List<Week>> loadAllWeekDaysCombinations();

    @Query("SELECT * FROM week WHERE id = :id")
    LiveData<Week> loadWeekById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeekDayCombination(Week week);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWeek(Week week);
}
