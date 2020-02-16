package com.example.lyubishchevtiming;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.database.AppExecutors;
import com.example.lyubishchevtiming.model.Summary;
import com.example.lyubishchevtiming.viewmodel.SummaryViewModel;
import com.example.lyubishchevtiming.viewmodel.SummaryViewModelFactory;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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
  //  private float[] yData = {25f, 27f, 55f};
  //  private String[] xData = {"Max", "Dinara", "Aman"};
    private TextView mTimePeriod;
    private String[] timePeriods = {"today", "last 7 days", "last 30 days", "last 365 days"};
    private AppDatabase mDb;
    private TextView mDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_summary, container, false);

        mDb = AppDatabase.getInstance(getActivity());
        mDate = rootView.findViewById(R.id.period);
        mTimePeriod = rootView.findViewById(R.id.time_period);
        pieChart = rootView.findViewById(R.id.pie_chart);
        loadSummaryData(mTimePeriod.getText().toString());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.tasks_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if (mSummaries!=null) {
            mAdapter = new SummaryAdapter(mSummaries, getActivity());
        }
        recyclerView.setAdapter(mAdapter);

       /* mSummaries = new ArrayList<>();

        mSummaries.add(new Summary(1, "name", 10, 1, "blue"));
        mSummaries.add(new Summary(2, "name2", 10, 5, "blue"));
        mSummaries.add(new Summary(3, "name3", 10, 10, "blue"));*/


        // specify an adapter (see also next example)




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
        return rootView;
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
        Log.d(TAG, "loadSummaryData: start " + start + " end " + end);
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
                            Log.d(TAG, "loadSummaryData: " + mSummaries.size());
                            for (int  i = 0; i < mSummaries.size(); i++){
                                Log.d(TAG, "loadSummaryData: actual: " + mSummaries.get(i).getActualTimeAmount() + " desired: " + mSummaries.get(i).getDesiredTimeAmount());

                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter = new SummaryAdapter(mSummaries, getActivity());
                                    recyclerView.setAdapter(mAdapter);
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




    public void adjustPieChart(){
        //pieChart.setDescription(R.string.add);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setCenterTextSize(20);
        pieChart.setUsePercentValues(true);


    }


    public void addData(){
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();


        long desiredTotalAmountOfTime = 0;
        long actualTotalAmountOfTime = 0;
        for (int i = 0; i < mSummaries.size(); i++){
            String timeAmount = convertTimeAmountToString(mSummaries.get(i).getActualTimeAmount());
            int hours = Integer.parseInt(timeAmount.substring(0, 2));
            int min = Integer.parseInt(timeAmount.substring(3, 5));
            int sec = Integer.parseInt(timeAmount.substring(6, 8));
            long time = hours * 60 * 60 + min * 60 + sec;

            yEntries.add(new PieEntry(time, mSummaries.get(i).getTaskName()));
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
            xEntries.add(mSummaries.get(i).getTaskName());
        }

        PieDataSet pieDataSet = new PieDataSet(yEntries, "Productivity");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(14);
        PieData data = new PieData(pieDataSet);
        pieChart.setData(data);
        pieChart.setCenterText(getResources().getString(R.string.chart_name, actualTimeHours,
                actualTimeMin, desiredTimeHours, desiredTimeMin));

        pieChart.setDrawSliceText(false);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(getContext(), h.toString(), Toast.LENGTH_LONG).show();

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
