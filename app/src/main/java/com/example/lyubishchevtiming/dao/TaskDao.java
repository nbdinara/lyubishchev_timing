package com.example.lyubishchevtiming.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.lyubishchevtiming.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task t WHERE t.id = :id")
    LiveData<Task> loadTaskById(int id);

    @Query("SELECT * FROM task")
    LiveData<List<Task>>loadAllTasks();


    @Insert
    void insertTask(Task task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Task task);

    @Query("DELETE FROM task")
    void deleteTasks();
}
