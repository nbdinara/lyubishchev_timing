package com.example.lyubishchevtiming.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "log", indices = {@Index("task_id")},
        foreignKeys = @ForeignKey(
                entity = Task.class,
                parentColumns = "id",
                childColumns = "task_id",
                onDelete = CASCADE))
public class Log {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "today_date")
    private Date todayDate;
    @ColumnInfo(name = "today_time_amount")
    private long todayTimeAmount;
    @ColumnInfo(name = "task_id")
    private int taskId;


    public Log(int id, Date todayDate, long todayTimeAmount, int taskId){
        this.id = id;
        this.todayDate = todayDate;
        this.todayTimeAmount = todayTimeAmount;
        this.taskId = taskId;
    }


    @Ignore
    public Log(Date todayDate, long todayTimeAmount, int taskId){
        this.todayDate = todayDate;
        this.todayTimeAmount = todayTimeAmount;
        this.taskId = taskId;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public long getTodayTimeAmount() {
        return todayTimeAmount;
    }

    public void setTodayTimeAmount(long todayTimeAmount) {
        this.todayTimeAmount = todayTimeAmount;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
