package com.example.lyubishchevtiming.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.lyubishchevtiming.R;
import com.example.lyubishchevtiming.TimeTrackingActivity;
import com.example.lyubishchevtiming.model.Task;

import static android.content.ContentValues.TAG;
import static com.example.lyubishchevtiming.service.App.CHANNEL_ID;

public class TimeTrackingService extends Service {

    private Intent intent;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationChannel();
        }

        Task task = intent.getExtras().getParcelable("taskExtra");
        Log.d(TAG, "onStartCommand: " + task.getName());
        long desiredTimeAmount = intent.getLongExtra("desiredTime", 0);
        Log.d(TAG, "onStartCommand: " + desiredTimeAmount);
        long timeWhenStopped = intent.getLongExtra("time", 0);
        boolean isRunning = intent.getBooleanExtra("running", true);


        Intent notificationIntent = new Intent(this, TimeTrackingActivity.class);
        notificationIntent.putExtra("task", task);
        notificationIntent.putExtra("todayDesiredTime", desiredTimeAmount);
        notificationIntent.putExtra("time", timeWhenStopped);
        notificationIntent.putExtra("running", isRunning);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Lyubishchev Timing")
                .setContentText("Stopwatch is running...")
                .setSmallIcon(R.drawable.ic_lyubishchev_r)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
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
