package com.example.Lifemeter;

import android.content.Context; 
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.*;
import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity {
    
    private SQLiteDatabase database;
    private DbList sampledb; 
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        double[] totals = calculateTotalsPeriod(17,19);
        double[] percentages = calculatePieChart(17,19);
        String[] categories = getActivityList(17);
        for (int x=0; x<totals.length; x++) {
            System.out.println(categories[x]);
            System.out.println(percentages[x] + "%");
        }

        double[] lineData = calculateLineGraph("Home",15,17);
        for (int x=0; x<lineData.length; x++) {
            System.out.println(lineData[x]);
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
        String[] fakeArray;
        //fakeArray = new String[7];
        fakeArray = database.query(sampledb.TableName, new String[] {id, activity}, null, null);
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

}
