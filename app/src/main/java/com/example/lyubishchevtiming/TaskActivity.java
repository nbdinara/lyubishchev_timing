package com.example.lyubishchevtiming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskActivity extends AppCompatActivity {

    private Task mTask;
    private TextView mTaskNameTextView;
    private LinearLayout mHeader;
    private FloatingActionButton mAddEditTaskButton;
    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mTask = intentThatStartedThisActivity.getExtras().getParcelable("task");
        }

        mTaskNameTextView = findViewById(R.id.task_name);
        mTaskNameTextView.setText(mTask.getName());

        mHeader = findViewById(R.id.viewA);

        mAddEditTaskButton = findViewById(R.id.fab_edit_add_task);
        mAddEditTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class destinationClass = AddEditTaskActivity.class;
                Intent intent = new Intent(TaskActivity.this, destinationClass);
                intent.putExtra("task", mTask);
                startActivity(intent);
            }
        });

        mStartButton = findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class destinationClass = TimeTrackingActivity.class;
                Intent intent = new Intent(TaskActivity.this, destinationClass);
                intent.putExtra("task", mTask);
                startActivity(intent);
            }
        });

        setImageViewColor();
    }


    private void setImageViewColor(){
        switch (mTask.getColor()) {
            case "red":
                mHeader.setBackgroundColor(getResources().getColor(R.color.red));
                break;
            case "glaucous":
                mHeader.setBackgroundColor(getResources().getColor(R.color.glaucous));
                break;
            case "yellow":
                mHeader.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case "green":
                mHeader.setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case "orange":
                mHeader.setBackgroundColor(getResources().getColor(R.color.orange));
                break;
            case "peach":
                mHeader.setBackgroundColor(getResources().getColor(R.color.peach));
                break;
            case "lavender":
                mHeader.setBackgroundColor(getResources().getColor(R.color.lavender));
                break;
            case "blue":
                mHeader.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            default:
                mHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        }
    }
}
