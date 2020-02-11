package com.example.lyubishchevtiming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class SummaryFragment extends Fragment {

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
