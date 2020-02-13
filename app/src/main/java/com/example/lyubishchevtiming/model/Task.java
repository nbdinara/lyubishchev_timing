package com.example.lyubishchevtiming.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "task", indices = {@Index("week_id")},
        foreignKeys = @ForeignKey(
                entity = Week.class,
                parentColumns = "id",
                childColumns = "week_id",
                onDelete = CASCADE))

public class Task implements Parcelable  {

    @NonNull
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String name;
    private String color;
    private long duration;
    @ColumnInfo(name = "week_id")
    private Integer weekId;


    public Task (int id, String name, String color,  long duration, Integer weekId){
        this.id = id;
        this.name = name;
        this.color = color;
        this.color = color;
        this.duration = duration;
        this.weekId = weekId;

    }

    @Ignore
    public Task (){

    }

    @Ignore
    public Task (String name, String color, long duration, Integer weekId){
        this.name = name;
        this.color = color;
        this.color = color;
        this.duration = duration;
        this.weekId = weekId;
    }

    @Ignore
    protected Task(Parcel in) {
        id = in.readInt();
        name = in.readString();
        color  = in.readString();
        duration = in.readLong();
        weekId = in.readInt();
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
        parcel.writeLong(duration);
        parcel.writeInt(weekId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getWeekId() {
        return weekId;
    }

    public void setWeekId(Integer week_id) {
        this.weekId = week_id;
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
