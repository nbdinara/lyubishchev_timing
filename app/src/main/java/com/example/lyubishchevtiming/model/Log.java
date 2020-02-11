package com.example.lyubishchevtiming.model;

import java.util.Calendar;
import java.util.Date;

public class Log {

    private Date todayDate;
    private long todayTimeAmount;
    private int taskId;


    public Log(){
    }

    public Log(long todayTimeAmount, int taskId){
        this.todayDate = Calendar.getInstance().getTime();;
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
