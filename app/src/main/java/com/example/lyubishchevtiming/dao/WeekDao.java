package com.example.lyubishchevtiming.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lyubishchevtiming.model.Week;

import java.util.List;

@Dao
public interface WeekDao {

    @Query("SELECT * FROM week")
    LiveData<List<Week>> loadAllWeekDaysCombinations();

    @Query("SELECT * FROM week WHERE id = :id")
    LiveData<Week> loadRecipeById(int id);

    @Insert
    void insertWeekDayCombination(Week week);
}
