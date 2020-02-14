package com.example.lyubishchevtiming.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "week")
public class Week implements Parcelable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    Integer id;
    @ColumnInfo (name = "task_name")
    private String taskName;
    private long mon;
    private long tue;
    private long wed;
    private long thu;
    private long fri;
    private long sat;
    private long sun;


    public Week(String taskName, long mon, long tue, long wed, long thu, long fri, long sat,
                long sun){
        this.taskName = taskName;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    @Ignore
    public Week(Integer id, String taskName, long mon, long tue, long wed, long thu, long fri, long sat,
                long sun){
        this.id = id;
        this.taskName = taskName;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    @Ignore
    public Week(){

    }

    @Ignore
    protected Week(Parcel in) {
        id = in.readInt();
        taskName = in.readString();
        mon = in.readLong();
        tue = in.readLong();
        wed  = in.readLong();
        thu = in.readLong();
        fri = in.readLong();
        sat = in.readLong();
        sun = in.readLong();
    }

    public static final Creator<Week> CREATOR = new Creator<Week>() {
        @Override
        public Week createFromParcel(Parcel in) {
            return new Week(in);
        }

        @Override
        public Week[] newArray(int size) {
            return new Week[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeInt(0);
        }
        else {
            parcel.writeInt(1);
            parcel.writeInt(id);
        }
        parcel.writeString(taskName);
        parcel.writeLong(mon);
        parcel.writeLong(tue);
        parcel.writeLong(wed);
        parcel.writeLong(thu);
        parcel.writeLong(fri);
        parcel.writeLong(sat);
        parcel.writeLong(sun);
    }

    public Integer getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getMon() {
        return mon;
    }

    public void setMon(long mon) {
        this.mon = mon;
    }

    public long getTue() {
        return tue;
    }

    public void setTue(long tue) {
        this.tue = tue;
    }

    public long getWed() {
        return wed;
    }

    public void setWed(long wed) {
        this.wed = wed;
    }

    public long getThu() {
        return thu;
    }

    public void setThu(long thu) {
        this.thu = thu;
    }

    public long getFri() {
        return fri;
    }

    public void setFri(long fri) {
        this.fri = fri;
    }

    public long getSat() {
        return sat;
    }

    public void setSat(long sat) {
        this.sat = sat;
    }

    public long getSun() {
        return sun;
    }

    public void setSun(long sun) {
        this.sun = sun;
    }
}
