package com.example.Lifemeter;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Lifemeter extends Activity {

    private SQLiteDatabase database;
    private DbList sampledb;
    //Location classes
    public GPSLocation gps;
    private GeofenceBroadcast geofenceReceiver;

    private TextView currentActivity;
    private TextView timeElapsed;
    private ProgressBar firstBar;
    private ProgressBar secondBar;
    private ProgressBar thirdBar;
    private ProgressBar fourthBar;
    private ProgressBar fifthBar;
    private HomeTab homeFragment;
    private AnalyticsTab analyticsFragment;
    private GoalsTab goalsFragment;
    private SettingsTab settingsFragment;
    
    private TextView [] topActivities = new TextView[5];
    private TextView [] topTimes = new TextView[5];

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Handles all the GPS pings and Geofencing capabilities
        gps = new GPSLocation();

        //Uses BroadcastReceiver in order to update the last entered Geofence for use in the frontend
        IntentFilter locationFilter = new IntentFilter(GeofenceBroadcast.ACTION_REP);
        locationFilter.addCategory(Intent.CATEGORY_DEFAULT);
        geofenceReceiver = new GeofenceBroadcast();
        registerReceiver(geofenceReceiver,locationFilter);

        ActionBar bar = getActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tabA = bar.newTab().setText("Home");
        ActionBar.Tab tabB = bar.newTab().setText("Data");
        ActionBar.Tab tabC = bar.newTab().setText("Goals");
        ActionBar.Tab tabD = bar.newTab().setText("Settings");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
        homeFragment = new HomeTab();
        fragmentTransaction.add(R.id.fragment_container, homeFragment);
        tabA.setTabListener(new CustomTabListener(homeFragment));
        bar.addTab(tabA);
        
        analyticsFragment = new AnalyticsTab();
        fragmentTransaction.add(R.id.fragment_container, analyticsFragment);
        tabB.setTabListener(new CustomTabListener(analyticsFragment));
        bar.addTab(tabB);
        
        goalsFragment = new GoalsTab();
        fragmentTransaction.add(R.id.fragment_container, goalsFragment);
        tabC.setTabListener(new CustomTabListener(goalsFragment));
        bar.addTab(tabC);
        
        settingsFragment = new SettingsTab();
        fragmentTransaction.add(R.id.fragment_container, settingsFragment);
        tabD.setTabListener(new CustomTabListener(settingsFragment));
        bar.addTab(tabD);

        double[] totals = calculateTotalsPeriod(17,19);
        String[] categories = getActivityList(17);
        for (int x=0; x<totals.length; x++) {
            System.out.println(categories[x]);
            System.out.println(totals[x]);
        }
    }

    public int whatToday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND,0);
        today.set(Calendar.SECOND,0);
        today.set(Calendar.MINUTE,0);
        today.set(Calendar.HOUR_OF_DAY,0);
        long msSince = today.getTimeInMillis();
        return (int)(msSince/86400000);
    }

    public void updateHome() {
        String[] activities = getActivityList(whatToday());
        double[] weeklyTotals = new double[activities.length];

        if (whatToday() - getFirstDay()>5) {
            weeklyTotals = calculateTotalsPeriod(whatToday()-6,whatToday());
        } else {
            weeklyTotals = calculateTotalsPeriod(getFirstDay(),whatToday());
        }

        double tempTime;
        String tempCat = new String();

        // Basic bubble sort
        for (int y=0; y<6; y++) {
            for (int x=0; x<(weeklyTotals.length-1); x++) {
                if (weeklyTotals[x]<weeklyTotals[x+1]) {
                    tempTime = weeklyTotals[x];
                    tempCat = activities[x];
                    weeklyTotals[x] = weeklyTotals[x+1];
                    activities[x] = activities [x+1];
                    weeklyTotals[x+1] = tempTime;
                    activities[x+1] = tempCat;
                }
            }
        }
    }

    /**
     * BAR GRAPH WIDGET
     * How to get a list of activities and cumulative times:
     * Use calculateTotalsPeriod(earlier day, later day)
     * Earlier day and later day are used depending on the time interval.
     * The list of activities corresponds in order using getActivityList(use the later day)
     *
     * PIE CHART WIDGET
     * How to get data for pie chart:
     * Use calculatePieChart(earlier day, later day)
     * Earlier day and later day are used depending on the time interval.
     * The list of activities corresponds in order using getActivityList(use the later day)
     *
     * LINE GRAPH WIDGET
     * How to get data for line graph:
     * Use calculateLineGraph(activity, earlier day, later day)
     * Earlier day and later day are used depending on the time interval.
     * The list of activities corresponds in order using getActivityList(use the later day)
     */

    // Get the first reference date for which data is available
    public int getFirstDay() {
        return 15;
    }

    // Get the list of activities being tracked
    public String[] getActivitiesTracked() {
        String[] FakeArray = new String[100];
        Cursor CursorArray;
        String activity = "activity";
        CursorArray = database.query(sampledb.TableName, new String[] {activity}, null, null, null, null, null, null);

        CursorArray.moveToFirst();
        for (int i=0; i < CursorArray.getCount(); i++){
            FakeArray[i] = CursorArray.getString(1);
        }
        return FakeArray;
    }


    // Get the activities in chronological order for a given day
    public String[] getActivityList(int day) {
        String[] fakeArray;
        fakeArray = new String[6];
        fakeArray[0] = "Home";
        fakeArray[1] = "Work";
        fakeArray[2] = "Home";
        fakeArray[3] = "Gym";
        fakeArray[4] = "Travel";
        fakeArray[5] = "Shopping";
        return fakeArray;
    }

    // Get the activity times in chronological order for a given day
    public double[] getTimeList(int day) {
        double[] fakeArray;
        fakeArray = new double[6];
        fakeArray[0] = 38.56;
        fakeArray[1] = 593.23;
        fakeArray[2] = 192.80;
        fakeArray[3] = 60.97;
        fakeArray[4] = 15.40;
        fakeArray[5] = 78.95;
        return fakeArray;
    }

    // Calculate the number of minutes of each category for a day
    public double[] calculateTotalsDay(int day, String[] activityList) {


        double[] timeList = getTimeList(day);

        String[] categories = getActivityList(day);
        double[] totals;
        totals = new double[categories.length];
        for (int x=0; x<totals.length; x++) {
            totals[x] = 0.00;
        }

        int amount = categories.length;
        for (int x=0; x<amount; x++) {
            for (int y=0; y<activityList.length; y++) {
                if (activityList[y].equals(categories[x])) {
                    totals[x] = totals[x] + timeList[y];
                }
            }
        }
        return totals;
    }
    
	public void onResume() {
		super.onResume();
		currentActivity = (TextView) homeFragment.getView().findViewById(R.id.current_activity);
		timeElapsed = (TextView) homeFragment.getView().findViewById(R.id.time_elapsed);
		firstBar=(ProgressBar) homeFragment.getView().findViewById(R.id.first_progress_bar);
		firstBar.setMax(100);
		firstBar.setProgress(57);
		secondBar=(ProgressBar) homeFragment.getView().findViewById(R.id.second_progress_bar);
		secondBar.setMax(100);
		secondBar.setProgress(40);
		thirdBar=(ProgressBar) homeFragment.getView().findViewById(R.id.third_progress_bar);
		thirdBar.setMax(100);
		thirdBar.setProgress(28);
		fourthBar=(ProgressBar) homeFragment.getView().findViewById(R.id.fourth_progress_bar);
		fourthBar.setMax(100);
		fourthBar.setProgress(13);
		fifthBar=(ProgressBar) homeFragment.getView().findViewById(R.id.fifth_progress_bar);
		fifthBar.setMax(100);
		fifthBar.setProgress(4);
		for(int j=0;j<topActivities.length;j++) {
			String id = "activity"+(j+1);
			int resID = getResources().getIdentifier(id, "id", "com.example.Lifemeter");
			topActivities[j] = (TextView)homeFragment.getView().findViewById(resID);
			id = "time"+(j+1);
			resID= getResources().getIdentifier(id,"id","com.example.Lifemeter");
			topTimes[j]=(TextView)homeFragment.getView().findViewById(resID);
		}
	}

    // Calculate the number of minutes for a given timeframe inclusive of days
    public double[] calculateTotalsPeriod(int dayBegin, int dayEnd) {
        String[] activityList = getActivityList(dayEnd);
        double[] totals = calculateTotalsDay(dayEnd, activityList);
        for (int x=dayBegin; x<dayEnd; x++) {
            for (int y=0; y<totals.length; y++) {
                totals[y] = totals[y] + calculateTotalsDay(x, activityList)[y];
            }
        }
        return totals;
    }

    // Calculate the percentage of the whole given a array of number of minutes
    public double[] calculatePieChart(int dayBegin, int dayEnd) {
        double total = 0.00;
        double[] totals = calculateTotalsPeriod(dayBegin, dayEnd);
        double[] percentages = new double[totals.length];
        for (int x=0; x<totals.length; x++) {
            total = total + totals[x];
        }

        for (int x=0; x<totals.length; x++) {
            percentages[x] = totals[x]/total*100;
        }

        return percentages;
    }

    // Returns a list of minutes in chronological order for a specific activity and timeframe inclusive
    public double[] calculateLineGraph(String activity, int dayBegin, int dayEnd) {
        int activityId = 0;
        double[] lineData = new double[dayEnd - dayBegin + 1];

        String[] activityList = getActivityList(dayEnd);
        for (int x=0; x<activityList.length; x++) {
            if (activityList[x].equals(activity)) {
                activityId = x;
            }
        }

        for (int x=dayBegin; x<=dayEnd; x++) {
            lineData[x-dayBegin] = calculateTotalsDay(x,activityList)[activityId];
        }

        return lineData;
    }

    //Used to broadcast information about the last known Geofence, could be expanded
    public class GeofenceBroadcast extends BroadcastReceiver {
        public static final String ACTION_REP = "com.mamlambo.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            String lastGeo = intent.getStringExtra(HandleGeofenceIntentService.UPDATE_LASTGEOFENCE);
            gps.setLastGeofence(lastGeo);

        }

}

class CustomTabListener implements ActionBar.TabListener {

    private Fragment frag;

    public CustomTabListener(Fragment f) {
        frag=f;
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.fragment_container, frag);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    }
}
