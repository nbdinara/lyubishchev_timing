package com.example.lyubishchevtiming.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lyubishchevtiming.model.Log;
import com.example.lyubishchevtiming.model.Summary;

import java.sql.Date;
import java.util.List;

@Dao
public interface LogDao {

    @Query("SELECT * FROM log WHERE id = :id")
    LiveData<List<Log>> loadLogById(int id);

    @Query("SELECT * FROM log")
    LiveData<Log> loadAllLogs();

    @Insert
    void insertLog(Log log);

    @Query("SELECT * FROM log WHERE today_date BETWEEN :start_date AND :end_date")
    LiveData<List<Log>> loadLogsByStartAndEndDate(Date start_date, Date end_date);

    @Query("SELECT * FROM log WHERE today_date = :date")
    LiveData<List<Log>> loadLogsByDate(Date date);

    @Query("SELECT  t.id, t.name, COUNT(l.desired_time_amount), COUNT(l.today_time_amount), t.color " +
            "FROM log l LEFT JOIN task t " +
            "ON l.task_id = t.id " +
            "GROUP BY l.task_id " +
            "HAVING (l.today_date BETWEEN :start_date AND :end_date)" )
    LiveData<List<Summary>> getLogsAndTaskInfoForSpecificDate(Date start_date, Date end_date);

}
