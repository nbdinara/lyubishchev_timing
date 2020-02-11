package com.example.lyubishchevtiming.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

public class Week implements Parcelable {

    private boolean mon;
    private boolean tue;
    private boolean wed;
    private boolean thu;
    private boolean fri;
    private boolean sat;
    private boolean sun;

    public Week(){
        //empty constructor
    }

    public Week(boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat,
                boolean sun){
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    protected Week(Parcel in) {
        mon = in.readByte() != 0;
        tue = in.readByte() != 0;
        wed  = in.readByte() != 0;
        thu = in.readByte() != 0;
        fri = in.readByte() != 0;
        sat = in.readByte() != 0;
        sun = in.readByte() != 0;
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
        parcel.writeByte((byte) (mon ? 1 : 0));
        parcel.writeByte((byte) (tue ? 1 : 0));
        parcel.writeByte((byte) (wed ? 1 : 0));
        parcel.writeByte((byte) (thu ? 1 : 0));
        parcel.writeByte((byte) (fri ? 1 : 0));
        parcel.writeByte((byte) (sat ? 1 : 0));
        parcel.writeByte((byte) (sun ? 1 : 0));
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }
}
