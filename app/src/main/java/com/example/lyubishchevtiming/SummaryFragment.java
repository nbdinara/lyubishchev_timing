package com.example.lyubishchevtiming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SummaryFragment extends Fragment {

    public SummaryFragment(){
        // empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_summary, container, false);

        TextView text = (TextView) rootView.findViewById(R.id.text);

        return rootView;
    }
}
