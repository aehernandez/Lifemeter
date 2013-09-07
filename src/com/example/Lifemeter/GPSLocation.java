package com.example.Lifemeter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alain
 * Date: 9/6/13
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class GPSLocation extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener,
        LocationClient.OnAddGeofencesResultListener {

    private final static int CONNECTION_FAILURE_RES_REQ = 9000;
    private final static long UPDATE_INTERVAL = 60000;
    private final static long DEFAULT_RADIUS = 20;

    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create the location client
        mLocationClient = new LocationClient(this, this, this);

        //Defines the interval
        mLocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        lastLocation = mLocationClient.getLastLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    @Override
    public void onAddGeofencesResult(int status, String[] geofenceRequestIds) {
        if(LocationStatusCodes.SUCCESS == status) {
            Toast.makeText(this, "Successfully added Geofence", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add Geofence", Toast.LENGTH_LONG).show();
        }
    }

    public void createGeofence(Location location, String category) {

        Geofence g = new Geofence.Builder().setCircularRegion(location.getLatitude()
                                                             ,location.getLongitude()
                                                             ,DEFAULT_RADIUS)
                                   .setRequestId(category)
                                   .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                   .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                                           | Geofence.GEOFENCE_TRANSITION_EXIT).build();
        ArrayList<Geofence> gl = new ArrayList<Geofence>();
        gl.add(g);
        mLocationClient.addGeofences(gl , getTransitionPendingIntent(), this);

    }

    private PendingIntent getTransitionPendingIntent() {
        // Create an explicit Intent
        Intent intent = new Intent(this,
                HandleGeofenceIntentService.class);
        /*
         * Return the PendingIntent
         */
        return PendingIntent.getService(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
        mLocationClient.disconnect();
    }

    //Returns the activity result when the activity is asked.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONNECTION_FAILURE_RES_REQ :
                switch (resultCode) {
                    case Activity.RESULT_OK :
                        break;
                }
        }
    }

    // Used to check the connection to Google Play Services.
    private boolean servicesConnection() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode){
            Log.d("Location Updates", "Google Play Services is available.");
            return true;
        } else {

            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RES_REQ);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),this,0).show();
        }
    }

    @Override
    public void onDisconnected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}



