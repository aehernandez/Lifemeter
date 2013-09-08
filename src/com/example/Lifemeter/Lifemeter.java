package com.example.Lifemeter;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.*;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class Lifemeter extends Activity {

    public static SQLiteDatabase database;
    public DbList sampledb;
    public DbClass GeoFenceDb;
    public static DbActivities ActivityDb;

    //Location classes
    public GPSLocation gps;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GeoService.LocalBinder mGeoBinder;
    private ServiceConnection mConnection;

    private static TextView currentActivity;
    private static TextView timeElapsed;
    private static ProgressBar firstBar;
    private static ProgressBar secondBar;
    private static ProgressBar thirdBar;
    private static ProgressBar fourthBar;
    private static ProgressBar fifthBar;
    private static HomeTab homeFragment;
    private AnalyticsTab analyticsFragment;
    private GoalsTab goalsFragment;
    private SettingsTab settingsFragment;
    private static Context context;
    
    private static TextView [] topActivities = new TextView[5];
    private static TextView [] topTimes = new TextView[5];
    private CountDownTimer count;
    private String currentGeofenceId;
    private String previousGeofenceId;
    private int currentTime = 0;
    private int currentSeconds = 0;
    private int currentMinutes = 0;
    private Handler mHandler = new Handler();
    private Runnable read;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        context=this;
        //ActivityDb = new DbActivities(this);
        
        //ActivityDb.Insert(0,"Home",100);
        //ActivityDb.Insert(0, "Eat", 150);

        //Handles all the GPS pings and Geofencing capabilities
        //gps = new GPSLocation();

        if (!servicesConnected()) {
            Toast.makeText(this, "Please connect to Google Play Services", Toast.LENGTH_LONG);
        }

        //Uses BroadcastReceiver in order to update the last entered Geofence for use in the frontend
        //IntentFilter locationFilter = new IntentFilter(GeofenceBroadcast.ACTION_REP);
        //locationFilter.addCategory(Intent.CATEGORY_DEFAULT);
        //geofenceReceiver = new GeofenceBroadcast();
        //

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
    }


    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.

        //Binds the GeoService to be able to create and keep track of classes
        Intent mIntent = new Intent(this, GeoService.class);

       mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                 mGeoBinder = (GeoService.LocalBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        

        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        startService(mIntent);


    }

    @Override
    protected void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
        //Unbinds the GeoService for power management reasons
        unbindService(mConnection);
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Get the error code
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(),
                        "Location Updates");
            }
        }

        return false;

    }

    public void read() {
    	count.start();
    }
    
    public void onResume() {


        super.onResume();
        currentActivity = (TextView) homeFragment.getView().findViewById(R.id.current_activity);
        timeElapsed = (TextView) homeFragment.getView().findViewById(R.id.time_elapsed);
        updateHome();

//        Button testButton = (Button) homeFragment.getView().findViewById(R.id.button);
//        testButton.setText("Add Geofence");
//        testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            Location loc = mGeoBinder.getLocation();
//            mGeoBinder.createGeofence(loc,20.0f,"test");
//            Toast.makeText(context, "Created Geofence at " + loc.getLatitude() + " , " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public static void updateHome() {


        String[] activities = getActivitiesTracked();
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


        firstBar=(ProgressBar) homeFragment.getView().findViewById(R.id.first_progress_bar);
        firstBar.setMax((int)weeklyTotals[0]);
        firstBar.setProgress((int)weeklyTotals[0]);
        secondBar=(ProgressBar) homeFragment.getView().findViewById(R.id.second_progress_bar);
        secondBar.setMax((int)weeklyTotals[0]);
        secondBar.setProgress((int)weeklyTotals[1]);
        thirdBar=(ProgressBar) homeFragment.getView().findViewById(R.id.third_progress_bar);
        thirdBar.setMax((int)weeklyTotals[0]);
        thirdBar.setProgress((int)weeklyTotals[2]);
        fourthBar=(ProgressBar) homeFragment.getView().findViewById(R.id.fourth_progress_bar);
        fourthBar.setMax((int)weeklyTotals[0]);
        fourthBar.setProgress((int)weeklyTotals[3]);
        fifthBar=(ProgressBar) homeFragment.getView().findViewById(R.id.fifth_progress_bar);
        fifthBar.setMax((int)weeklyTotals[0]);
        fifthBar.setProgress((int)weeklyTotals[4]);

        DecimalFormat hourForm = new DecimalFormat();
        hourForm.applyLocalizedPattern("##,###.#");
        double tempHours;

        for(int j=0;j<topActivities.length;j++) {
            String id = "activity"+(j+1);
            int resID = context.getResources().getIdentifier(id, "id", "com.example.Lifemeter");
            topActivities[j] = (TextView)homeFragment.getView().findViewById(resID);
            topActivities[j].setText(activities[j]);

            id = "time"+(j+1);
            resID= context.getResources().getIdentifier(id,"id","com.example.Lifemeter");

            topTimes[j]=(TextView)homeFragment.getView().findViewById(resID);
            topTimes[j].setText(hourForm.format(weeklyTotals[j]/60) + " hrs");

        }

    }

    /**
     * BAR GRAPH WIDGET
     * How to get a list of activities and cumulative times:
     * Use calculateTotalsPeriod(earlier day, later day)
     * Earlier day and later day are used depending on the elapsedTime interval.
     * The list of activities corresponds in order using getActivityList(use the later day)
     *
     * PIE CHART WIDGET
     * How to get data for pie chart:
     * Use calculatePieChart(earlier day, later day)
     * Earlier day and later day are used depending on the elapsedTime interval.
     * The list of activities corresponds in order using getActivityList(use the later day)
     *
     * LINE GRAPH WIDGET
     * How to get data for line graph:
     * Use calculateLineGraph(activity, earlier day, later day)
     * Earlier day and later day are used depending on the elapsedTime interval.
     * The list of activities corresponds in order using getActivityList(use the later day)
     */

    // Gives today's date in terms of days from the java reference date
    public static int whatToday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND,0);
        today.set(Calendar.SECOND,0);
        today.set(Calendar.MINUTE,0);
        today.set(Calendar.HOUR_OF_DAY,0);
        long msSince = today.getTimeInMillis();
        return (int)(msSince/86400000);
    }

    // Get the first reference date for which data is available
    public static int getFirstDay() {
        return 15;
    }

    // Get the list of activities being tracked
    public static String[] getActivitiesTracked() {
        //String[] FakeArray = new String[100];
        //Cursor CursorArray;
        //String activity = "activity";
        //CursorArray = database.query(sampledb.TableName, new String[] {activity}, null, null, null, null, null, null);

        //CursorArray.moveToFirst();
        //for (int i=0; i < CursorArray.getCount(); i++){
        //    FakeArray[i] = CursorArray.getString(1);
        //}
       // return FakeArray;
    	String[] activityArray = new String[10];

    	
    	activityArray = Data.PlacesTracked(); 

    	return activityArray;
    }


    //GeoFence Data retrieval for radius for Alain
    public double[] getRadius(){
		double[] FakeRad = new double[1000000];
		Cursor CursorArray;
		String rad = "radius";
		CursorArray = database.query(GeoFenceDb.TableName, new String[] {rad},null, null, null, null, null, null); 
		
		CursorArray.moveToFirst();
		for (int i=0; i < CursorArray.getCount(); i++){
			FakeRad[i] = CursorArray.getDouble(3);
		}
	    return FakeRad;
	}

	//GeoFence Data retrieval for lattitude for Alain
	public double[] getLatt(){
		double[] FakeLatt = new double[1000000];
		Cursor CursorArray;
		String l = "latt";
		CursorArray = database.query(GeoFenceDb.TableName, new String[] {l},null, null, null, null, null, null);
		

		CursorArray.moveToFirst();
		for (int i=0; i < CursorArray.getCount(); i++){
			FakeLatt[i] = CursorArray.getDouble(1);
		}
	    return FakeLatt;
	}

	//GeoFence Data retrieval for longittude for Alain
	public double[] getLongt(){
		double[] FakeLongt = new double[1000000];
		Cursor CursorArray;
		String Lt = "longt";
		CursorArray = database.query(GeoFenceDb.TableName, new String[] {Lt},null, null, null, null, null, null); 
		
		CursorArray.moveToFirst();
		for (int i=0; i < CursorArray.getCount(); i++){
			FakeLongt[i] = CursorArray.getDouble(2);
		}
	    return FakeLongt;
	}

	//GeoFence Data retrieval for ID for Alain
	public String[] getId(){
		String[] FakeId = new String[1000000];
		Cursor CursorArray;
		String Id = "id";
		CursorArray = database.query(GeoFenceDb.TableName, new String[] {Id},null, null, null, null, null, null);
		
		CursorArray.moveToFirst();
		for (int i=0; i < CursorArray.getCount(); i++){
			FakeId[i] = CursorArray.getString(4);
		}
	    return FakeId;
	}
	


    // Get the activities in chronological order for a given day
    public static String[] getActivityList(int day) {
        
        //    String[] fakeArray = new String[1000000];
	    //Cursor CursorArray;
	    //String Activity = "activity";
	    //CursorArray = database.rawQuery("SELECT activity FROM" +ActivityDb.TableName+ "WHERE day="+day ,null);
	    
	    //CursorArray.moveToFirst();
		//for (int i=0; i < CursorArray.getCount(); i++){
		//	fakeArray[i] = CursorArray.getString(0);
		//}
        String[] activityArray = new String[10];
    	

    	activityArray = Data.Places();

    	
    	return activityArray;
	}
    
    // Get the activity times in chronological order for a given day

    public static double[] getTimeList(int day) {
        //double[] fakeArray = new double[1000000];
	    //Cursor CursorArray;
	    //String Activity = "activity";
	    //CursorArray = database.rawQuery("SELECT elapsedTime FROM" +ActivityDb.TableName+ "WHERE day="+day ,null);
	    
	    //CursorArray.moveToFirst();
		//for (int i=0; i < CursorArray.getCount(); i++){
		//	fakeArray[i] = CursorArray.getDouble(0);
		//}
		
	
    	double[][] timespent = new double[10][400];
    	

    	timespent = Data.DataRandomizer();
    	double[] timeArray = new double[10];
    	timeArray[0] = timespent[0][0];
    	timeArray[1] = timespent[0][1];
    	timeArray[2] = timespent[0][2];
    	timeArray[3] = timespent[0][3];
    	timeArray[4] = timespent[0][4];
    	timeArray[5] = timespent[0][5];
    	timeArray[6] = timespent[0][6];
    	timeArray[7] = timespent[0][7];
    	timeArray[8] = timespent[0][8];
    	timeArray[9] = timespent[0][9];
    	return timeArray;
        
    }

    // Calculate the number of minutes of each category for a day
    public static double[] calculateTotalsDay(int day) {
        double[] timeList = getTimeList(day);
        String[] activityList = getActivityList(day);

        String[] categories = getActivitiesTracked();
        double[] totals;
        totals = new double[categories.length];
        for (int x=0; x<totals.length; x++) {
            totals[x] = 0.00;
        }

        for (int x=0; x<categories.length; x++) {
            for (int y=0; y<activityList.length; y++) {
                if (activityList[y].equals(categories[x])) {
                    totals[x] = totals[x] + timeList[y];
                }
            }
        }
        return totals;
    }


    // Calculate the number of minutes for a given timeframe inclusive of days
    public static double[] calculateTotalsPeriod(int dayBegin, int dayEnd) {
        String[] activityList = getActivityList(dayEnd);
        double[] totals = calculateTotalsDay(dayEnd);
        for (int x=dayBegin; x<dayEnd; x++) {
            activityList = getActivityList(x);
            for (int y=0; y<totals.length; y++) {
                totals[y] = totals[y] + calculateTotalsDay(x)[y];
            }
        }
        return totals;
    }

    // Calculate the percentage of the whole given a array of number of minutes
    public static double[] calculatePieChart(int dayBegin, int dayEnd) {
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


    public static double lineHelper(String activity, int day) {

    	double totalMinutes = 0.0;
    	double[] tempArray = getTimeList(day);
    	String[] tempName = getActivityList(day);
    	for (int x=0; x<tempName.length; x++) {
    		if (tempName[x].equals(activity)) {
    			totalMinutes = totalMinutes + tempArray[x];
    		}
    	}
    	return totalMinutes;
    }
    
    
    // Returns a list of minutes in chronological order for a specific activity and timeframe inclusive
    // 0 = week, 1 = month, 2 = year

    public static double[] calculateLineGraph(String activity, int timePeriod) {

        double[] lineData;
        int today = whatToday();
        if (timePeriod == 0) {
		lineData = new double[7];
		for (int x = (lineData.length-1); x>=0; x--) {
			lineData[x] = lineHelper(activity, today-x);
		}
        } else if (timePeriod == 1) {
        	lineData = new double[30];
		for (int x = (lineData.length-1); x>=0; x--) {
			lineData[x] = lineHelper(activity, today-x);
		}
        } else {
        	lineData = new double[12];
        	double sum;
        	for (int x = 11; x>=0; x--) {
        		sum = 0.0;
        		for (int y = 0; y<30; y++) {
        			sum = sum + lineHelper(activity, today-x*30-y);
        		}
        		lineData[x] = sum;
        	}
        }

        return lineData;
    }

    //Used to broadcast information about the last known Geofence, could be expanded


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
    	//if(tab.getText().equals("Home"))
    		//Lifemeter.updateHome();
        ft.replace(R.id.fragment_container, frag);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    }
}
