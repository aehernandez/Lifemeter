package com.example.Lifemeter;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.ArrayList;
import java.util.List;

// IntentService that handles what a specific Geofence should do on a transition.

public class HandleGeofenceIntentService extends IntentService {

    public HandleGeofenceIntentService() {
        super("HandleGeofenceIntentService");
    }

    //This Intent should only be provided by the GPSLocation and thrown by the mLocationClient

    @Override
    protected void onHandleIntent(Intent intent) {

        //If mLocationClient throws an error we should do nothing and log it.
        if (LocationClient.hasError(intent)) {
            Log.e("HandleGeofenceIntentService", "Location Services error: " +
                                                 Integer.toString(LocationClient
                                                         .getErrorCode(intent)));
        } else {

            //Get the transition type, either enter or exit
            int transitionType = LocationClient.getGeofenceTransition(intent);

            //Get which Geofences triggered
            List<Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);
            ArrayList<String> triggerIds = new ArrayList<String>();

            //Perform operations on the triggered Geofence;
            for (Geofence geofence : triggerList) {
                triggerIds.add(geofence.getRequestId());
            }


        }
    }
}
