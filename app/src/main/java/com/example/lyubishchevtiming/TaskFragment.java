package com.example.lyubishchevtiming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TaskFragment extends Fragment {

    private List<Task> tasks;
    private AppDatabase mDb;
    private TaskAdapter taskAdapter;
    private FloatingActionButton mAddTaskButton;
    private GridView gridView;

    public TaskFragment(){
        // empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.from(getContext()).inflate(R.layout.fragment_task, container, false);
        //LayoutInflater.from(context).inflate(R.layout.test, this, true);
        TextView text = (TextView) rootView.findViewById(R.id.text);

        gridView = (GridView)rootView.findViewById(R.id.grid_view_tasks);
        if (tasks!=null) {
            taskAdapter = new TaskAdapter(getActivity(), tasks);
        }
        gridView.setAdapter(taskAdapter);
        mDb = AppDatabase.getInstance(getActivity());
        mAddTaskButton = rootView.findViewById(R.id.fab_add_task);


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

        mAddTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Class destinationClass = AddEditTaskActivity.class;
                Intent intentToStartTaskActivity = new Intent(getContext(), destinationClass);
                getContext().startActivity(intentToStartTaskActivity);            }
        });

        setupViewModel();
        return rootView;
    }


    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        viewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> mTasks) {
                if (!mTasks.isEmpty()) {
                    Log.d(TAG, "onChanged: " + mTasks.isEmpty());
                    if (!mTasks.isEmpty()){
                        for (int i = 0; i < mTasks.size(); i++) {
                            Log.d(TAG, "onChanged: " + mTasks.get(i).getName());
                        }
                    }
                    tasks = mTasks;
                    taskAdapter = new TaskAdapter(getActivity(), tasks);
                    gridView.setAdapter(taskAdapter);
                    showTasksList();
                } else {
                    showAddButton();
                }
            }
        });
    }

    public void showAddButton() {
        //gridView.setVisibility(View.GONE);
        mAddTaskButton.setVisibility(View.VISIBLE);
    }

    public void showTasksList() {
        mAddTaskButton.setVisibility(View.VISIBLE);

        //mAddTaskButton.setVisibility(View.GONE);
       // gridView.setVisibility(View.VISIBLE);
    }
}
