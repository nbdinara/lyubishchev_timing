package com.example.lyubishchevtiming;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.lyubishchevtiming.model.Task;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {

    private final Context mContext;
    private ArrayList<Task> tasks;
    private Task task;
    private ImageView taskImageView;
    private Button addButton;

    // 1
    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        this.mContext = context;
        this.tasks = tasks;
    }

    // 2
    @Override
    public int getCount() {
        return (tasks.size()+1);
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position == tasks.size()) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_button_add, null);
            addButton = convertView.findViewById(R.id.add_button);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class destinationClass = AddEditTaskActivity.class;
                    Intent intentToStartTaskActivity = new Intent(mContext, destinationClass);
                    mContext.startActivity(intentToStartTaskActivity);
                }
            });
            return convertView;

        } else {
            // 1
            task = tasks.get(position);

            // 2
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.grid_item, null);
            }

            // 3
            taskImageView = (ImageView) convertView.findViewById(R.id.task_image);
            final TextView taskLetter = (TextView) convertView.findViewById(R.id.task_letter);
            final TextView taskName = (TextView) convertView.findViewById(R.id.task_name_summary);

            setImageViewColor();


            // 4
            taskLetter.setText(Character.toString(task.getName().charAt(0)));
            taskName.setText(task.getName());
        }
        return convertView;
    }

    private void setImageViewColor(){
        switch (task.getColor()) {
            case "red":
                taskImageView.getBackground().setColorFilter(ContextCompat
                        .getColor(mContext, R.color.red), PorterDuff.Mode.SRC_ATOP);
                break;
            case "glaucous":
                taskImageView.getBackground().setColorFilter(ContextCompat
                        .getColor(mContext, R.color.glaucous), PorterDuff.Mode.SRC_ATOP);
                break;
            case "yellow":
                taskImageView.getBackground().setColorFilter(ContextCompat
                        .getColor(mContext, R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                break;
            case "green":
                taskImageView.getBackground().setColorFilter(ContextCompat
                        .getColor(mContext, R.color.green), PorterDuff.Mode.SRC_ATOP);
                break;
            case "orange":
                taskImageView.getBackground().setColorFilter(ContextCompat
                        .getColor(mContext, R.color.orange), PorterDuff.Mode.SRC_ATOP);
                break;
            case "peach":
                taskImageView.getBackground().setColorFilter(ContextCompat
                        .getColor(mContext, R.color.peach), PorterDuff.Mode.SRC_ATOP);
                break;
            case "lavender":
                taskImageView.getBackground().setColorFilter(ContextCompat
                        .getColor(mContext, R.color.lavender), PorterDuff.Mode.SRC_ATOP);
                break;
            case "blue":
                taskImageView.getBackground().setColorFilter(ContextCompat
                        .getColor(mContext, R.color.blue), PorterDuff.Mode.SRC_ATOP);
                break;
            default:
                taskImageView.getBackground().setColorFilter(ContextCompat
                        .getColor(mContext, R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
