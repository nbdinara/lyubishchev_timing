package com.example.lyubishchevtiming.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable  {

    private int id;
    private String name;
    private String color;
    private Week daysOfActivity;
    private long duration;

    public Task (){
        // empty constructor
    }

    public Task (String name, String color){
        this.name = name;
        this.color = color;
    }

    protected Task(Parcel in) {
        name = in.readString();
        color  = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(color);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Week getDaysOfActivity() {
        return daysOfActivity;
    }

    public void setDaysOfActivity(Week daysOfActivity) {
        this.daysOfActivity = daysOfActivity;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getName(){
        return name;
    }

    public String getColor(){
        return color;
    }

    public void setName (String name){
        this.name = name;
    }

    public void setColor (String color){
        this.color = color;
    }


}
