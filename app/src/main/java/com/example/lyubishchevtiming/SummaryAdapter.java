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

import com.example.lyubishchevtiming.model.Summary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MyViewHolder> {

    private List<Summary> summaryList;
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


    public SummaryAdapter(List<Summary> summaryList, Context context) {
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
        String actualTime = convertTimeAmountToString(taskSummary.getActualTimeAmount());
        String desiredTime = convertTimeAmountToStringWithoutUTF(taskSummary.getDesiredTimeAmount());
        Log.d(TAG, "onBindViewHolder: actual " + taskSummary.getActualTimeAmount() +  " desiredtime "  + taskSummary.getDesiredTimeAmount());
        holder.timeAmount.setText(actualTime + "\\" + desiredTime);
        holder.viewForeground.setBackgroundResource(R.color.blue);

        //Log.d(TAG, "onBindViewHolder: " + ((float)taskSummary.getActualTimeAmount()/taskSummary.getDesiredTimeAmount()));

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) holder.viewForeground.getLayoutParams();

        //TODO add corresponding color for every viewForeground
        //Log.d(TAG, "onBindViewHolder: " + lp.rightMargin);
        double ratio = calculateRatio(actualTime, desiredTime);


        holder.viewForeground.getLayoutParams().width = (int) (getScreenWidth(mContext, lp.rightMargin) * ratio);
    }

    public double calculateRatio(String actualTime, String desiredTime){

        int actualTimeHours = Integer.parseInt(actualTime.substring(0, 2));
        int actualTimeMin = Integer.parseInt(actualTime.substring(3, 5));
        int actualTimeSec = Integer.parseInt(actualTime.substring(6, 8));

        int desiredTimeHours = Integer.parseInt(desiredTime.substring(0, 2));
        int desiredTimeMin = Integer.parseInt(desiredTime.substring(3, 5));
        int desiredTimeSec = Integer.parseInt(desiredTime.substring(6, 8));

        int actual = actualTimeHours * 60 * 60 + actualTimeMin * 60 + actualTimeSec;
        int desired = desiredTimeHours * 60 * 60 + desiredTimeMin * 60 + desiredTimeSec;


        double ratio;
        if (actual > desired){
            Log.d(TAG, "onBindViewHolder: i am here");
            ratio = 1d;
        } else {

            ratio =  (double)(actual / desired);
            if (ratio < 0.01){
                ratio = 0.01;
            }
        }
        return ratio;
    }

    public String convertTimeAmountToString(long timeAmount){
        Date date = new Date(timeAmount);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public String convertTimeAmountToStringWithoutUTF(long timeAmount){
        Date date = new Date(timeAmount);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
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
