package com.example.lyubishchevtiming.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "task")
public class Task implements Parcelable  {

    @PrimaryKey (autoGenerate = true)
    private int id;
    private String name;
    private String color;
    @ColumnInfo(name = "days_of_activity")
    private Week daysOfActivity;
    private long duration;

    public Task (){
        // empty constructor
    }

    public Task (int id, String name, String color, Week daysOfActivity, long duration){
        this.id = id;
        this.name = name;
        this.color = color;
        this.color = color;
        this.daysOfActivity = daysOfActivity;
        this.duration = duration;
    }

    @Ignore
    public Task (String name, String color, Week daysOfActivity, long duration){
        this.name = name;
        this.color = color;
        this.color = color;
        this.daysOfActivity = daysOfActivity;
        this.duration = duration;
    }

    @Ignore
    protected Task(Parcel in) {
        id = in.readInt();
        name = in.readString();
        color  = in.readString();
        daysOfActivity = (Week) in.readParcelable(Week.class.getClassLoader());
        duration = in.readLong();
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
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(color);
        parcel.writeParcelable(daysOfActivity, i);
        parcel.writeLong(duration);
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
