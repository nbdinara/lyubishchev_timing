package com.example.lyubishchevtiming;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Log;
import com.example.lyubishchevtiming.model.Summary;
import com.example.lyubishchevtiming.viewmodel.AllLogsViewModel;
import com.example.lyubishchevtiming.viewmodel.SummaryViewModel;
import com.example.lyubishchevtiming.viewmodel.SummaryViewModelFactory;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class SummaryFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SummaryAdapter mAdapter;

    private List<Summary> mSummaries;
    private PieChart pieChart;
    private TextView mTimePeriod;
    private String[] timePeriods = {"today", "last 7 days", "last 30 days", "last 365 days"};
    private AppDatabase mDb;
    private TextView mDate;
    private FloatingActionButton mDownloadLogsButton;
    private List<Log> allLogs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_summary, container, false);

        mDb = AppDatabase.getInstance(getActivity());
        mDownloadLogsButton = rootView.findViewById(R.id.fab_download_csv);
        mDate = rootView.findViewById(R.id.period);
        mTimePeriod = rootView.findViewById(R.id.time_period);
        pieChart = rootView.findViewById(R.id.pie_chart);
        loadSummaryData(mTimePeriod.getText().toString());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.tasks_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if (mSummaries!=null) {
            mAdapter = new SummaryAdapter(mSummaries, getActivity());
        }
        recyclerView.setAdapter(mAdapter);

        mTimePeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
                    //alt_bld.setIcon(R.drawable.icon);
                    alt_bld.setTitle("Select a Group Name");
                    alt_bld.setSingleChoiceItems(timePeriods, -1, new DialogInterface
                            .OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            //Toast.makeText(getContext(),
                                    mTimePeriod.setText(timePeriods[item]);
                                    loadSummaryData(timePeriods[item]);

                                    //"Group Name = "+ timePeriods[item], Toast.LENGTH_SHORT).show();
                            dialog.dismiss();// dismiss the alertbox after chose option

                        }
                    });
                    AlertDialog alert = alt_bld.create();
                    alert.show();


            }
        });

        mDownloadLogsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                downloadAllLogs();
                StringBuilder data = new StringBuilder();
                data.append("Id,Date,Actual Time Amount,Desired Time Amount,Task Id");
                if (allLogs!=null) {
                    for (int i = 0; i < allLogs.size(); i++) {
                        String actualTime = convertTimeAmountToString(allLogs.get(i).getTodayTimeAmount());
                        String actual = formateTimeString(actualTime);

                        String desired = "";
                        if (allLogs.get(i).getDesiredTimeAmount() != 0) {
                            String desiredTime = convertTimeAmountToStringWithoutUTF(allLogs.get(i).getDesiredTimeAmount());
                            desired = formateTimeString(desiredTime);
                        } else {
                            desired = "day off";
                        }

                        data.append("\n" + allLogs.get(i).getId() + "," +
                                converDateToString(allLogs.get(i).getTodayDate()) + "," + actual
                                + "," + desired + "," + allLogs.get(i).getTaskId());
                    }
                        try {
                            //saving
                            FileOutputStream out = getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
                            out.write(data.toString().getBytes());
                            out.close();

                            //exporting
                            Context context = getContext();
                            File fileLocation = new File (getContext().getFilesDir(), "data.csv");
                            Uri path = FileProvider.getUriForFile(context, "com.example.lyubishchevtiming.fileprovider", fileLocation);
                            Intent fileIntent  = new Intent(Intent.ACTION_SEND);
                            fileIntent.setType("text/csv");
                            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Logs");
                            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                            startActivity(Intent.createChooser(fileIntent, "Send logs"));
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                }
            }
        });

        return rootView;
    }

    public String formateTimeString(String time){

        int hours = Integer.parseInt(time.substring(0, 2));
        int min = Integer.parseInt(time.substring(3, 5));
        int sec = Integer.parseInt(time.substring(6, 8));
        String formattedTime = hours + " h " + min + " min " + sec + " sec ";
        return formattedTime;
    }

    public void downloadAllLogs(){
        AllLogsViewModel viewModel = ViewModelProviders.of(getActivity()).get(AllLogsViewModel.class);
        viewModel.getLogs().observe(getViewLifecycleOwner(), new Observer<List<Log>>() {
            @Override
            public void onChanged(List<Log> mLogs) {
                if (!mLogs.isEmpty()) {
                    allLogs = mLogs;
                }

            }
        });
    }


    public void loadSummaryData(String timePeriod){

        Calendar calendar = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date end = calendar.getTime();
        Date start =  calendar.getTime();

        if (timePeriod.equals("today")) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            start = calendar.getTime();

        } else if (timePeriod.equals("last 7 days")){
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            start = calendar.getTime();
        }  else if (timePeriod.equals("last 30 days")){
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            start = calendar.getTime();
        } else if (timePeriod.equals("last 365 days")){
            calendar.add(Calendar.DAY_OF_MONTH, -365);
            start = calendar.getTime();
        }

        String pattern = "EEE, MMM dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        final String startString = simpleDateFormat.format(start);
        final String endString = simpleDateFormat.format(end);
        //Log.d(TAG, "loadSummaryData: start " + start + " end " + end);
        SummaryViewModelFactory factory = new SummaryViewModelFactory(mDb, start, end);
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final SummaryViewModel viewModel
                = ViewModelProviders.of(this, factory).get(SummaryViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getSummary().observe(getActivity(), new Observer<List<Summary>>() {
            @Override
            public void onChanged(@Nullable final List<Summary> summaries) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (summaries != null) {
                            mSummaries = summaries;
                            //Log.d(TAG, "loadSummaryData: " + mSummaries.size());
                            for (int  i = 0; i < mSummaries.size(); i++){
                               // Log.d(TAG, "loadSummaryData: actual: " + mSummaries.get(i).getActualTimeAmount() + " desired: " + mSummaries.get(i).getDesiredTimeAmount());

                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter = new SummaryAdapter(mSummaries, getActivity());
                                    recyclerView.setAdapter(mAdapter);
                                    recyclerView.setNestedScrollingEnabled(false);
                                    adjustPieChart();
                                    addData();
                                    mDate.setText(startString + " - " + endString);

                                }
                            });
                        }
                    }
                });
            }
        });
    }



    public String converDateToString(Date date){
        String pattern = "dd/MM/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String dateAsString = df.format(date);
        return dateAsString;
    }

    public void adjustPieChart(){
        //pieChart.setDescription(R.string.add);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(60f);
        pieChart.setCenterTextSize(20);
        pieChart.setUsePercentValues(true);


    }


    public void addData(){
        final ArrayList<PieEntry> yEntries = new ArrayList<>();
        final ArrayList<String> xEntries = new ArrayList<>();


        long desiredTotalAmountOfTime = 0;
        long actualTotalAmountOfTime = 0;
        for (int i = 0; i < mSummaries.size(); i++){
            String timeAmount = convertTimeAmountToString(mSummaries.get(i).getActualTimeAmount());
            int hours = Integer.parseInt(timeAmount.substring(0, 2));
            int min = Integer.parseInt(timeAmount.substring(3, 5));
            int sec = Integer.parseInt(timeAmount.substring(6, 8));
            long time = hours * 60 * 60 + min * 60 + sec;

            yEntries.add(new PieEntry(time, mSummaries.get(i).getTaskName()));
            android.util.Log.d(TAG, "addData: yEntries" + time + mSummaries.get(i).getTaskName());
            desiredTotalAmountOfTime = desiredTotalAmountOfTime + mSummaries.get(i).getDesiredTimeAmount();
            actualTotalAmountOfTime = actualTotalAmountOfTime + mSummaries.get(i).getActualTimeAmount();
        }

        pieChart.setEntryLabelColor(R.color.colorIcons);

        String actualTime = convertTimeAmountToString(actualTotalAmountOfTime);
        String desiredTime = convertTimeAmountToStringWithoutUTF(desiredTotalAmountOfTime);

        int actualTimeHours = Integer.parseInt(actualTime.substring(0, 2));
        int actualTimeMin = Integer.parseInt(actualTime.substring(3, 5));
        int actualTimeSec = Integer.parseInt(actualTime.substring(6, 8));

        int desiredTimeHours = Integer.parseInt(desiredTime.substring(0, 2));
        int desiredTimeMin = Integer.parseInt(desiredTime.substring(3, 5));
        int desiredTimeSec = Integer.parseInt(desiredTime.substring(6, 8));

        long actual = actualTimeHours * 60 * 60 + actualTimeMin * 60 + actualTimeSec;
        long desired = desiredTimeHours * 60 * 60 + desiredTimeMin * 60 + desiredTimeSec;

        long difference = desired - actual;

        yEntries.add(new PieEntry(difference, getResources().getString(R.string.ineffective_time)));

        for (int i = 0; i < mSummaries.size(); i++){
            android.util.Log.d(TAG, "addData: "  + mSummaries.get(i).getTaskName());
            xEntries.add(mSummaries.get(i).getTaskName());
        }

        PieDataSet pieDataSet = new PieDataSet(yEntries, "Productivity");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(14);
        PieData data = new PieData(pieDataSet);
        pieChart.setData(data);
        if(mSummaries.size()!=0) {
            pieChart.setCenterText(getResources().getString(R.string.chart_name, actualTimeHours,
                    actualTimeMin, desiredTimeHours, desiredTimeMin));
        } else {
            pieChart.setCenterText("Nothing done today");
        }
/*
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        Date dt = getSomeDate();
        prefs.edit().putLong(dateTimeKey, dt.getTime()).apply();*/

        pieChart.setDrawSliceText(false);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                android.util.Log.d(TAG, "onValueSelected: " + e.toString());
                android.util.Log.d(TAG, "onValueSelected: " + h.toString());

                int pos = e.toString().indexOf("y: ");
                String s = e.toString();
                String value = e.toString().substring(pos + 3,s.length()-2);

                String taskName ="";

                for (int i = 0; i < yEntries.size(); i++){
                    if (yEntries.get(i).getValue() == Long.parseLong(value)){
                        taskName = yEntries.get(i).getLabel();
                    }
                }
                Toast.makeText(getContext(), taskName, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected() {

            }
        });
        pieChart.invalidate(); // refresh



    }

    public String convertTimeAmountToStringWithoutUTF(long timeAmount){
        Date date = new Date(timeAmount);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public String convertTimeAmountToString(long timeAmount){
        Date date = new Date(timeAmount);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

}
