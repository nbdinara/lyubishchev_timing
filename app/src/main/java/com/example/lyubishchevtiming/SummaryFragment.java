package com.example.lyubishchevtiming;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Summary;
import com.example.lyubishchevtiming.viewmodel.LogByDateForTaskViewModel;
import com.example.lyubishchevtiming.viewmodel.LogByDateForTaskViewModelFactory;
import com.example.lyubishchevtiming.viewmodel.SummaryViewModel;
import com.example.lyubishchevtiming.viewmodel.SummaryViewModelFactory;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SummaryFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SummaryAdapter mAdapter;

    private List<Summary> mSummaries;
    private PieChart pieChart;
    private float[] yData = {25f, 27f, 55f};
    private String[] xData = {"Max", "Dinara", "Aman"};
    private TextView mTimePeriod;
    private String[] timePeriods = {"today", "last 7 days", "last 30 days", "last 365 days"};
    private AppDatabase mDb;


    public SummaryFragment(){
        // empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_summary, container, false);

        mDb = AppDatabase.getInstance(getActivity());
        mTimePeriod = rootView.findViewById(R.id.time_period);

        pieChart = rootView.findViewById(R.id.pie_chart);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.tasks_list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        loadSummaryData(mTimePeriod.getText().toString());

        mSummaries = new ArrayList<>();

        mSummaries.add(new Summary(1, "name", 10, 1, "blue"));
        mSummaries.add(new Summary(2, "name2", 10, 5, "blue"));
        mSummaries.add(new Summary(3, "name3", 10, 10, "blue"));

        recyclerView.setLayoutManager(layoutManager);
        if (mSummaries!=null) {
            mAdapter = new SummaryAdapter(mSummaries, getActivity());
        }
        recyclerView.setAdapter(mAdapter);
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



        //mSummaries = new ArrayList<>();



        return rootView;
    }


    public void adjustPieChart(){
        //pieChart.setDescription(R.string.add);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setCenterText("My pie chart");
        pieChart.setCenterTextSize(24);
        addData(pieChart);


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        dateFormat.format(date); //2016/11/16 12:08:43
    }

    public void loadSummaryData(String timePeriod){


        Calendar calendar = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date start = calendar.getTime();
        Date end =  calendar.getTime();

        if (timePeriod.equals("today")) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            end = calendar.getTime();

        } else if (timePeriod.equals("last 7 days")){
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            end = calendar.getTime();
        }  else if (timePeriod.equals("last 30 days")){
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            end = calendar.getTime();
        } else if (timePeriod.equals("last 365 days")){
            calendar.add(Calendar.DAY_OF_MONTH, -365);
            end = calendar.getTime();
        }

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
                            mAdapter = new SummaryAdapter(mSummaries, getActivity());
                            recyclerView.setAdapter(mAdapter);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adjustPieChart();
                                }
                            });



                        }
                    }
                });
            }
        });
    }




        public void addData(PieChart chart){
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        for (int i = 0; i < mSummaries.size(); i++){
            yEntries.add(new PieEntry(mSummaries.get(i).getActualTimeAmount(), i));
        }

        for (int i = 0; i < mSummaries.size(); i++){
            xEntries.add(mSummaries.get(i).getTaskName());
        }

        PieDataSet pieDataSet = new PieDataSet(yEntries, "Sales");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(14);
        PieData data = new PieData(pieDataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }
}
