package com.example.lyubishchevtiming.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "week")
public class Week implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    String id;
    private int mon;
    private int tue;
    private int wed;
    private int thu;
    private int fri;
    private int sat;
    private int sun;


    public Week(String id, int mon, int tue, int wed, int thu, int fri, int sat,
                int sun){
        this.id = id;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    @Ignore
    protected Week(Parcel in) {
        id = in.readString();
        mon = in.readInt();
        tue = in.readInt();
        wed  = in.readInt();
        thu = in.readInt();
        fri = in.readInt();
        sat = in.readInt();
        sun = in.readInt();
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
        parcel.writeString(id);
        parcel.writeInt(mon);
        parcel.writeInt(tue);
        parcel.writeInt(wed);
        parcel.writeInt(thu);
        parcel.writeInt(fri);
        parcel.writeInt(sat);
        parcel.writeInt(sun);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getTue() {
        return tue;
    }

    public void setTue(int tue) {
        this.tue = tue;
    }

    public int getWed() {
        return wed;
    }

    public void setWed(int wed) {
        this.wed = wed;
    }

    public int getThu() {
        return thu;
    }

    public void setThu(int thu) {
        this.thu = thu;
    }

    public int getFri() {
        return fri;
    }

    public void setFri(int fri) {
        this.fri = fri;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public int getSun() {
        return sun;
    }

    public void setSun(int sun) {
        this.sun = sun;
    }
}
