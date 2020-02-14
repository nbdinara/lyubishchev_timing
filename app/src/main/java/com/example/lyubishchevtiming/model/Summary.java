package com.example.lyubishchevtiming.model;

import androidx.room.ColumnInfo;

import java.util.Date;

public class Summary {

    //private Date startDate;
    //private Date endDate;
    @ColumnInfo(name = "id")
    private int taskId;
    @ColumnInfo(name = "name")
    private String taskName;
    @ColumnInfo(name = "desired_time_amount")
    private long desiredTimeAmount;
    @ColumnInfo(name = "today_time_amount")
    private long actualTimeAmount;
    @ColumnInfo(name = "color")
    private String taskColor;


    public Summary(int taskId, String taskName, long desiredTimeAmount,
                   long actualTimeAmount, String taskColor){
        this.taskId = taskId;
        this.taskName = taskName;
        this.desiredTimeAmount = desiredTimeAmount;
        this.actualTimeAmount = actualTimeAmount;
        this.taskColor = taskColor;

    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getDesiredTimeAmount() {
        return desiredTimeAmount;
    }

    public void setDesiredTimeAmount(long desiredTimeAmount) {
        this.desiredTimeAmount = desiredTimeAmount;
    }

    public long getActualTimeAmount() {
        return actualTimeAmount;
    }

    public void setActualTimeAmount(long actualTimeAmount) {
        this.actualTimeAmount = actualTimeAmount;
    }

    public String getTaskColor() {
        return taskColor;
    }

    public void setTaskColor(String taskColor) {
        this.taskColor = taskColor;
    }
}
