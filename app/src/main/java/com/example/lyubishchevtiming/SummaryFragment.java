package com.example.lyubishchevtiming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SummaryFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SummaryAdapter mAdapter;

    private ArrayList<Summary> mSummaries;
    private PieChart pieChart;
    private float[] yData = {25f, 27f, 55f};
    private String[] xData = {"Max", "Dinara", "Aman"};


    public SummaryFragment(){
        // empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_summary, container, false);

        pieChart = rootView.findViewById(R.id.pie_chart);

        //pieChart.setDescription(R.string.add);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setCenterText("My pie chart");
        pieChart.setCenterTextSize(24);
        addData(pieChart);


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        dateFormat.format(date); //2016/11/16 12:08:43

        mSummaries = new ArrayList<>();

        mSummaries.add(new Summary(date, date, 1, "Meditation", 10,
                5, "blue"));
        mSummaries.add(new Summary(date, date, 1, "Yoga", 10,
                2, "blue"));
        mSummaries.add(new Summary(date, date, 1, "Yoga", 10,
                10, "blue"));

        recyclerView = (RecyclerView) rootView.findViewById(R.id.tasks_list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new SummaryAdapter(mSummaries, getActivity());
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public void addData(PieChart chart){
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        for (int i = 0; i < yData.length; i++){
            yEntries.add(new PieEntry(yData[i], i));
        }

        for (int i = 0; i < xData.length; i++){
            xEntries.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntries, "Sales");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(14);
        PieData data = new PieData(pieDataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }
}
