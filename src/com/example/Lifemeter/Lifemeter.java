package com.example.Lifemeter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class Lifemeter extends Activity {
	
    private SQLiteDatabase database;
    private DbList sampledb; 

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
        
        Fragment homeFragment = new HomeTab();
        fragmentTransaction.add(R.id.fragment_container, homeFragment);
        tabA.setTabListener(new CustomTabListener(homeFragment));
        bar.addTab(tabA);
        
        Fragment analyticsFragment = new AnalyticsTab();
        fragmentTransaction.add(R.id.fragment_container, analyticsFragment);
        tabB.setTabListener(new CustomTabListener(analyticsFragment));
        bar.addTab(tabB);
        
        Fragment goalsFragment = new GoalsTab();
        fragmentTransaction.add(R.id.fragment_container, goalsFragment);
        tabC.setTabListener(new CustomTabListener(goalsFragment));
        bar.addTab(tabC);
        
        Fragment settingsFragment = new SettingsTab();
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
