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
    @ColumnInfo(name = "week_id")
    private String weekId;
    private long duration;


    public Task (int id, String name, String color, String weekId, long duration){
        this.id = id;
        this.name = name;
        this.color = color;
        this.color = color;
        this.weekId = weekId;
        this.duration = duration;
    }

    @Ignore
    public Task (){

    }

    @Ignore
    public Task (String name, String color, String weekId, long duration){
        this.name = name;
        this.color = color;
        this.color = color;
        this.weekId = weekId;
        this.duration = duration;
    }

    @Ignore
    protected Task(Parcel in) {
        id = in.readInt();
        name = in.readString();
        color  = in.readString();
        weekId = in.readString();
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
        parcel.writeString(weekId);
        parcel.writeLong(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeekId() {
        return weekId;
    }

    public void setWeekId(String week_id) {
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
