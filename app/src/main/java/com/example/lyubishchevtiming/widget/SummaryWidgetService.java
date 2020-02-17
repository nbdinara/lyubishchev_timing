package com.example.lyubishchevtiming.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lyubishchevtiming.R;
import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.database.AppExecutors;
import com.example.lyubishchevtiming.model.Summary;
import com.example.lyubishchevtiming.viewmodel.SummaryViewModel;
import com.example.lyubishchevtiming.viewmodel.SummaryViewModelFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class SummaryWidgetService extends IntentService {

    public static final String ACTION_UPDATE_SUMMARY_WIDGETS = "com.example.lyubishchevtiming.action.update_summary_widgets";
    private AppDatabase mDb;

    public SummaryWidgetService() {
        super("SummaryWidgetService");
    }

    public static void startActionUpdateSummaryWidgets(Context context) {
        Intent intent = new Intent(context, SummaryWidgetService.class);
        intent.setAction(ACTION_UPDATE_SUMMARY_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_SUMMARY_WIDGETS.equals(action)) {
                handleActionUpdatePlantWidgets();
            }
        }
    }

    private void handleActionUpdatePlantWidgets() {

        Log.d(TAG, "run: actual and desired start" );

        mDb = AppDatabase.getInstance(getApplicationContext());

        Calendar calendar = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        final Date start = calendar.getTime();

        // next day
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        final Date end = calendar.getTime();

        String pattern = "EEE, MMM dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        final String startString = simpleDateFormat.format(start);
        final String endString = simpleDateFormat.format(end);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Summary> summaries = mDb.logDao().getLogsForService(start, end);

                long desiredTotalAmountOfTime = 0;
                long actualTotalAmountOfTime = 0;
                for (int i = 0; i < summaries.size(); i++) {
                    String timeAmount = convertTimeAmountToString(summaries.get(i).getActualTimeAmount());
                    int hours = Integer.parseInt(timeAmount.substring(0, 2));
                    int min = Integer.parseInt(timeAmount.substring(3, 5));
                    int sec = Integer.parseInt(timeAmount.substring(6, 8));

                    desiredTotalAmountOfTime = desiredTotalAmountOfTime + summaries.get(i).getDesiredTimeAmount();
                    actualTotalAmountOfTime = actualTotalAmountOfTime + summaries.get(i).getActualTimeAmount();
                }
                String actualTime = convertTimeAmountToString(actualTotalAmountOfTime);
                String desiredTime = convertTimeAmountToStringWithoutUTF(desiredTotalAmountOfTime);

                int actualTimeHours = Integer.parseInt(actualTime.substring(0, 2));
                int actualTimeMin = Integer.parseInt(actualTime.substring(3, 5));

                int desiredTimeHours = Integer.parseInt(desiredTime.substring(0, 2));
                int desiredTimeMin = Integer.parseInt(desiredTime.substring(3, 5));

                String actual = getResources().getString(R.string.actual_time, actualTimeHours, actualTimeMin);
                String desired = getResources().getString(R.string.desired_time, desiredTimeHours, desiredTimeMin);
                Log.d(TAG, "run: actual and desired " + actual + desired);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), SummaryWidgetProvider.class));
                //Now update all widgets
                SummaryWidgetProvider.updateSummaryWidgets(getApplicationContext(), appWidgetManager, actual, desired, appWidgetIds);
            }
        });

    }


    public String convertTimeAmountToStringWithoutUTF(long timeAmount) {
        Date date = new Date(timeAmount);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public String convertTimeAmountToString(long timeAmount) {
        Date date = new Date(timeAmount);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }
}
