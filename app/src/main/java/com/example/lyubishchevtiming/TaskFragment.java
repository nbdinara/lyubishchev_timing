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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    private ArrayList<Task> tasks;
    private AppDatabase mDb;
    private TaskAdapter taskAdapter;

    public TaskFragment(){
        // empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_task, container, false);
        TextView text = (TextView) rootView.findViewById(R.id.text);

        GridView gridView = (GridView)rootView.findViewById(R.id.grid_view_tasks);
        setupViewModel();
        //taskAdapter = new TaskAdapter(getActivity(), tasks);
        gridView.setAdapter(taskAdapter);
        mDb = AppDatabase.getInstance(getActivity());


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


    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                if (tasks != null) {
                    taskAdapter = new TaskAdapter(getActivity(), tasks);
                } else {
                    //showErrorMessage();
                }
            }
        });
    }

}
