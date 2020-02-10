package com.example.lyubishchevtiming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TaskFragment extends Fragment {

    private ArrayList<Task> tasks;


    public TaskFragment(){
        // empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_task, container, false);
        TextView text = (TextView) rootView.findViewById(R.id.text);

        tasks = new ArrayList<>();
        tasks.add(new Task ( "Meditation", "yellow"));
        tasks.add(new Task ("Yoga", "red"));
        tasks.add(new Task ("Reading", "green"));
        tasks.add(new Task ("Dancing", "red"));
        tasks.add(new Task ("Cooking", "blue"));

        GridView gridView = (GridView)rootView.findViewById(R.id.grid_view_tasks);
        TaskAdapter taskAdapter = new TaskAdapter(getActivity(), tasks);
        gridView.setAdapter(taskAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Task task = tasks.get(position);
                Context context = getActivity();
                Class destinationClass = TaskActivity.class;
                Intent intentToStartTaskActivity = new Intent(context, destinationClass);
                //Log.d(TAG, "myrecipe name: " + recipe.getName());
                //Log.d(TAG, "myrecipe step 3: " + recipe.getSteps().get(2).getDescription());
                intentToStartTaskActivity.putExtra("task", task);
                startActivity(intentToStartTaskActivity);
            }
        });


        return rootView;
    }

}
