package com.example.lyubishchevtiming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.example.lyubishchevtiming.service.TimeTrackingService;
import com.example.lyubishchevtiming.widget.SummaryWidgetService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        this.getSupportActionBar().setElevation(0);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        int defaultValue = 0;

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        if (getIntent().hasExtra("tab")) {
            Log.d(TAG, "onCreate: tab" );
            int page = getIntent().getIntExtra("tab", defaultValue);
            viewPager.setCurrentItem(1, true);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        isLocationServiceRunning(getApplicationContext());



    }



    public static boolean isLocationServiceRunning(Context context)
    {
        Log.i(TAG, "TRACKING? Reviewing all services to see if LocationService is running.");

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        // Go through every service until we find LocationService

        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE))
        {
            Log.v(TAG, "TRACKING?    service.getClassName() = " + service.service.getClassName());

            if(TimeTrackingService.class.getName().equals(service.service.getClassName()))
            {

                return true;
            }

        }

        Log.i(TAG, "TRACKING? LocationService is NOT running.");
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
       // SummaryWidgetService.startActionUpdateSummaryWidgets(this);

    }
}