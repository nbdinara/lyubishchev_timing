package com.example.lyubishchevtiming;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MyViewHolder> {

    private ArrayList<Summary> summaryList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, timeAmount;
        public View viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.task_name_summary);
            timeAmount = (TextView) view.findViewById(R.id.task_time_summary);
            viewForeground = (View) view.findViewById(R.id.view_foreground);
        }
    }


    public SummaryAdapter(ArrayList<Summary> summaryList, Context context) {
        this.summaryList = summaryList;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Summary taskSummary = summaryList.get(position);
        holder.name.setText(taskSummary.getTaskName());
        //TODO convert long timeAmount to the hours and minutes
        holder.timeAmount.setText(taskSummary.getActualTimeAmount() + "\\" + taskSummary.getDesiredTimeAmount());
        holder.viewForeground.setBackgroundResource(R.color.blue);

        //Log.d(TAG, "onBindViewHolder: " + ((float)taskSummary.getActualTimeAmount()/taskSummary.getDesiredTimeAmount()));

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) holder.viewForeground.getLayoutParams();

        //TODO add corresponding color for every viewForeground
        Log.d(TAG, "onBindViewHolder: " + lp.rightMargin);
        holder.viewForeground.getLayoutParams().width = (int) (getScreenWidth(mContext, lp.rightMargin) *
                ((float)taskSummary.getActualTimeAmount()/taskSummary.getDesiredTimeAmount()));
    }


    public static int getScreenWidth(Context context, int margin) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (displayMetrics.widthPixels - (margin * 2));
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }
}
