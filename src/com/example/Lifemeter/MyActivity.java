package com.example.Lifemeter;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
        String[] fakeArray;
        fakeArray = new String[7];
        fakeArray[0] = "Home";
        fakeArray[1] = "Work";
        fakeArray[2] = "Eating";
        fakeArray[3] = "Gym";
        fakeArray[4] = "Travel";
        fakeArray[5] = "Shopping";
        fakeArray[6] = "Studying";
        return fakeArray;
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
