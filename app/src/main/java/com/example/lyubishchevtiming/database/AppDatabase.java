package com.example.lyubishchevtiming.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.lyubishchevtiming.dao.LogDao;
import com.example.lyubishchevtiming.dao.TaskDao;
import com.example.lyubishchevtiming.dao.WeekDao;
import com.example.lyubishchevtiming.model.Log;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.model.Week;

@Database(entities = {Log.class, Task.class, Week.class}, version = 1, exportSchema = false)
@TypeConverters (DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "lyubishchevTiming";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract LogDao logDao();

    public abstract WeekDao weekDao();

    public abstract TaskDao taskDao();

}