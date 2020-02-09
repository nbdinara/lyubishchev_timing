package com.example.lyubishchevtiming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.awt.font.TextAttribute;

public class TaskActivity extends AppCompatActivity {

    private Task mTask;
    private TextView mTaskNameTextView;
    private LinearLayout mHeader;

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
        setImageViewColor();


    }


    private void setImageViewColor(){
        switch (mTask.getColor()) {
            case "red":
                mHeader.setBackgroundColor(getResources().getColor(R.color.red));
                break;
            case "blue":
                mHeader.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case "yellow":
                mHeader.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case "green":
                mHeader.setBackgroundColor(getResources().getColor(R.color.green));
                break;
            default:
                mHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}
