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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.lyubishchevtiming.database.AppDatabase;
import com.example.lyubishchevtiming.model.Task;
import com.example.lyubishchevtiming.viewmodel.MainViewModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.content.ContentValues.TAG;


public class TaskFragment extends Fragment {

    private List<Task> tasks;
    private AppDatabase mDb;
    private TaskAdapter taskAdapter;
    private FloatingActionButton mAddTaskButton;
    private FloatingActionButton mDeleteTaskButton;

    private GridView gridView;
    private AdView mAdView;


    public TaskFragment(){
        // empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.from(getContext()).inflate(R.layout.fragment_task, container, false);

        gridView = (GridView)rootView.findViewById(R.id.grid_view_tasks);
        setupViewModel();

        if (tasks!=null) {
            taskAdapter = new TaskAdapter(getActivity(), tasks);
        }

        gridView.setAdapter(taskAdapter);

        mDb = AppDatabase.getInstance(getActivity());
        mAddTaskButton = rootView.findViewById(R.id.fab_add_task);

        mDeleteTaskButton = rootView.findViewById(R.id.fab_delete_task);

        mAddTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Class destinationClass = AddEditTaskActivity.class;
                Intent intentToStartTaskActivity = new Intent(getContext(), destinationClass);
                getContext().startActivity(intentToStartTaskActivity);            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Task task = tasks.get(position);
                Context context = getActivity();
                Class destinationClass = TaskActivity.class;
                Intent intentToStartTaskActivity = new Intent(context, destinationClass);
                intentToStartTaskActivity.putExtra("task", task);
                startActivity(intentToStartTaskActivity);
            }
        });


        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        setupViewModel();

        return rootView;
    }


    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        viewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> mTasks) {
                if (!mTasks.isEmpty()) {
                    tasks = mTasks;
                    for (int i =0; i < tasks.size(); i++) {
                        Log.d(TAG, "onChanged: listId " +i + " n: " + tasks.get(i).getName() + " t_id: " + tasks.get(i).getId() + " w:id " + tasks.get(i).getWeekId());
                    }
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
        gridView.setVisibility(View.GONE);
        mAddTaskButton.setVisibility(View.VISIBLE);
    }

    public void showTasksList() {
        mAddTaskButton.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.VISIBLE);
    }
}
