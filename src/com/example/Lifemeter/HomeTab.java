package com.example.Lifemeter;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class HomeTab extends Fragment {

    LayoutInflater infl;
    ViewGroup contain;
    TextView elapsedTime;
    CountDownTimer count;
    String currTime;
    TextView currentActivity;
    String currAct;
    private Timer c = new Timer();
    private String previousGeofenceId;
    private String currentGeofenceId;
    private int currentTime = 0;
    private int currentSeconds;
    private int currentMinutes;
    private boolean time;

    private Runnable read;
    private Handler mHandler = new Handler();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        time = true;



        View v = inflater.inflate(R.layout.home, container, false);
        elapsedTime = (TextView) v.findViewById(R.id.time_elapsed);
        currentActivity = (TextView) v.findViewById(R.id.current_activity);

        return v;
    }

    public void onResume() {
        time = true;
        super.onResume();
        Lifemeter.updateHome();
        if (currTime != null)
            elapsedTime.setText(currTime);
        if (currAct != null)
            currentActivity.setText(currAct);
        if(read == null) {
            read = new Runnable() {

                @Override
                public void run() {

                    mHandler.postDelayed(read, 1000);
                    if(time){
                        SharedPreferences settings = getActivity().getSharedPreferences("SETTINGS", 0);
                        previousGeofenceId = currentGeofenceId;
                        currentGeofenceId = settings.getString("Current Geofence", "Travel");

                        if(currentGeofenceId.equals(previousGeofenceId)) {
                            currentTime++;
                            currentSeconds = currentTime % 60;
                            currentMinutes = currentTime/60;
                            elapsedTime.setText(String.format("00:%02d:%02d", currentMinutes,currentSeconds));
                            currentActivity.setText(currentGeofenceId);

                        } else {
                            currentTime = 0;

                        }
                    }
                }
            };

            read.run();
        }




    }

    public void onPause() {
        super.onPause();
        time = false;
//        View v = infl.inflate(R.layout.home,contain,false);
//        elapsedTime =(TextView)v.findViewById(R.id.time_elapsed);
//        currentActivity = (TextView)v.findViewById(R.id.current_activity);
//        currTime = (String) elapsedTime.getText();
//        currAct = (String) currentActivity.getText();
    }
}
