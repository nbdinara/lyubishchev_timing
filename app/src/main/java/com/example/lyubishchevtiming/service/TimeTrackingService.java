package com.example.lyubishchevtiming.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.lyubishchevtiming.R;
import com.example.lyubishchevtiming.TimeTrackingActivity;
import com.example.lyubishchevtiming.model.Task;

import static android.content.ContentValues.TAG;
import static com.example.lyubishchevtiming.service.App.CHANNEL_ID;

public class TimeTrackingService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Task task = intent.getExtras().getParcelable("taskExtra");
        Log.d(TAG, "onStartCommand: " + task.getName());
        long desiredTimeAmount = intent.getLongExtra("desiredTime", 0);
        Log.d(TAG, "onStartCommand: " + desiredTimeAmount);


        Intent notificationIntent = new Intent(this, TimeTrackingActivity.class);
        notificationIntent.putExtra("task", task);
        notificationIntent.putExtra("todayDesiredTime", desiredTimeAmount);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Lyubishchev Timing")
                .setContentText("Stopwatch is running...")
                .setSmallIcon(R.drawable.ic_edit)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
