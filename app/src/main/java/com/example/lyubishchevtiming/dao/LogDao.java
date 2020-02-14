package com.example.lyubishchevtiming.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lyubishchevtiming.model.Log;
import com.example.lyubishchevtiming.model.Summary;

import java.util.Date;
import java.util.List;

@Dao
public interface LogDao {

    @Query("SELECT * FROM log WHERE id = :id")
    LiveData<List<Log>> loadLogById(int id);

    @Query("SELECT * FROM log")
    LiveData<Log> loadAllLogs();

    @Insert
    void insertLog(Log log);
/*
    @Query("SELECT sum(today_time_amount), sum(desired_time_amount) FROM log WHERE today_date BETWEEN :start_date AND :end_date group by task_id")
    LiveData<List<Log>> loadLogsByStartAndEndDate(Date start_date, Date end_date);*/

    @Query("SELECT sum(today_time_amount) FROM log WHERE today_date BETWEEN :start AND :end AND task_id = :id group by task_id")
    LiveData<Long> loadLogsForTodayTask(int id, Date start, Date end);

    @Query("SELECT  task.id, task.name, sum(log.desired_time_amount) as desired_time_amount, sum(log.today_time_amount) as today_time_amount, task.color " +
            "FROM log LEFT JOIN task " +
            "ON log.task_id = task.id " +
            "GROUP BY task.id " +
            "HAVING (log.today_date BETWEEN :start_date AND :end_date)" )
    LiveData<List<Summary>> getLogsAndTaskInfoForSpecificDate(Date start_date, Date end_date);



    }
