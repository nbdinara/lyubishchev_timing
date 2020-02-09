package com.example.lyubishchevtiming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        tasks.add(new Task ( "Meditation", "red"));
        tasks.add(new Task ("Yoga", "red"));
        tasks.add(new Task ("Reading", "red"));
        tasks.add(new Task ("Dancing", "red"));
        tasks.add(new Task ("Cooking", "red"));

        GridView gridView = (GridView)rootView.findViewById(R.id.grid_view_tasks);
        TaskAdapter taskAdapter = new TaskAdapter(getActivity(), tasks);
        gridView.setAdapter(taskAdapter);
/*
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Book book = books[position];
                book.toggleFavorite();

                // This tells the GridView to redraw itself
                // in turn calling your BooksAdapter's getView method again for each cell
                booksAdapter.notifyDataSetChanged();
            }
        });*/

        return rootView;
    }

}
