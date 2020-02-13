package com.example.lyubishchevtiming.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.lyubishchevtiming.dao.LogDao;
import com.example.lyubishchevtiming.dao.TaskDao;
import com.example.lyubishchevtiming.dao.WeekDao;
import com.example.lyubishchevtiming.model.Log;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.model.Week;

@Database(entities = {Log.class, Task.class, Week.class}, version = 2, exportSchema = false)
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
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract LogDao logDao();

    public abstract WeekDao weekDao();

    public abstract TaskDao taskDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("DROP TABLE week");

            database.execSQL(
                    "CREATE TABLE week (id INTEGER PRIMARY KEY NOT NULL, task_name TEXT, mon INTEGER NOT NULL, " +
                            "tue INTEGER NOT NULL, wed INTEGER NOT NULL, thu INTEGER NOT NULL, fri INTEGER NOT NULL, sat INTEGER NOT NULL, sun INTEGER NOT NULL) ");

            database.execSQL("DROP TABLE task");

            database.execSQL(
                    "CREATE TABLE task (id INTEGER PRIMARY KEY NOT NULL, name TEXT, color TEXT, " +
                            " duration INTEGER NOT NULL, week_id INTEGER NOT NULL, FOREIGN KEY (week_id) " +
                            "REFERENCES week(id) ON DELETE CASCADE)");

            database.execSQL("CREATE INDEX IF NOT EXISTS index_task_week_id ON task (week_id)");


        }

    };


}