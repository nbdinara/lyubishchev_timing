package com.example.lyubishchevtiming.model;

import java.util.Date;

public class Summary {

    //private Date startDate;
    //private Date endDate;
    private int taskId;
    private String taskName;
    private long desiredTimeAmount;
    private long actualTimeAmount;
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
