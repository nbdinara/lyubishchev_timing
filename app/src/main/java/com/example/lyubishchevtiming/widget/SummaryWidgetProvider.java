package com.example.lyubishchevtiming.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lyubishchevtiming.MainActivity;
import com.example.lyubishchevtiming.R;
import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Summary;
import com.example.lyubishchevtiming.service.App;
import com.example.lyubishchevtiming.viewmodel.SummaryViewModel;
import com.example.lyubishchevtiming.viewmodel.SummaryViewModelFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class SummaryWidgetProvider extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String actual,
                                String desired, int appWidgetId) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("tab", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.summary_widget_provider);

        views.setTextViewText(R.id.actual_time, actual);
        views.setTextViewText(R.id.desired_time, desired);


        views.setOnClickPendingIntent(R.id.actual_time, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        SummaryWidgetService.startActionUpdateSummaryWidgets(context);

    }

    public static void updateSummaryWidgets(Context context, AppWidgetManager appWidgetManager,
                                            String actual, String desired, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, actual, desired, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

