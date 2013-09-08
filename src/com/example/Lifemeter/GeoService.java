package com.example.Lifemeter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: danielge
 * Date: 9/7/13
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */

interface GeoServiceInterface{
    void createGeofence(Location location, String uniqueID, long radius);
    Location getLocation();
}

public class GeoService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener,
        LocationClient.OnAddGeofencesResultListener
{
    private static final String TAG = "SlenderService";

    public static boolean started = false;

    private final static long DEFAULT_RADIUS = 20;
    // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = 1;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = 1;

    private static final int SLENDER_STICKY_NOTIFICATION_ID = 0x101;
    private static final int SLENDER_SERVICE_NOTIFICATION_ID = 0x100;


    private LocationClient locationClient;
    private LocationRequest locationRequest;
    private Location location;
    private LocationClient.OnAddGeofencesResultListener locationListener = this;

    private NotificationManager notificationManager;

    IBinder mBinder = new LocalBinder();
    public ArrayList<Geofence> geoFenceList;
    private PendingIntent mGeofencePendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();

        locationClient = new LocationClient(this, this, this);
        // Create the LocationRequest object
        locationRequest = LocationRequest.create();
        // Use high accuracy
        locationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        locationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private PendingIntent getTransitionPendingIntent() {
        // If the PendingIntent already exists
        if (null != mGeofencePendingIntent) {

            // Return the existing intent
            return mGeofencePendingIntent;

            // If no PendingIntent exists
        } else {
        // Create an explicit Intent
        Intent intent = new Intent(getApplicationContext(),
                HandleGeofenceIntentService.class);
        /*
         * Return the PendingIntent
         */
        return PendingIntent.getService(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public void createGeofence(Location location, float radius, String uniqueID) {
        Geofence g = new Geofence.Builder().setCircularRegion(location.getLatitude()
                ,location.getLongitude()
                ,radius)
                .setRequestId(uniqueID)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT).build();
        ArrayList<Geofence> gl = new ArrayList<Geofence>();

        gl.add(g);
        mGeofencePendingIntent = getTransitionPendingIntent();
        locationClient.addGeofences(gl , mGeofencePendingIntent, locationListener);
        Toast.makeText(getApplicationContext(),"Created Geofences", Toast.LENGTH_SHORT).show();
    }

    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent,startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        locationClient.connect();

        // Start this service in the foreground
        Intent notificationIntent = new Intent(this, Lifemeter.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(getText(R.string.app_name))
                .setContentText("Geo Listener")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .getNotification();
        startForeground(SLENDER_STICKY_NOTIFICATION_ID, notification);
        Toast.makeText(getApplicationContext(),"Started GeoService", Toast.LENGTH_SHORT).show();
        started = true;

        return START_STICKY;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        started = false;

        if (locationClient.isConnected()) {
            locationClient.removeLocationUpdates(this);
        }
        locationClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = locationClient.getLastLocation();
        locationClient.requestLocationUpdates(locationRequest, this);

        Location moore = new Location("developer");
        moore.setLatitude((double)39.9524081);
        moore.setLongitude((double)-75.1903237);
        createGeofence(moore, 100.0f, "Studying");

        Location palestra = new Location("developer");
        palestra.setLatitude(39.95144);
        palestra.setLongitude(-75.18868);
        createGeofence(palestra, 200.0f, "Hacking");

        Location irvene = new Location("developer");
        palestra.setLatitude(39.95094);
        palestra.setLongitude(-75.19305);
        createGeofence(irvene, 200.0f, "Presentation");
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText("Resolve Google Play Services issue")
                    .setContentIntent(connectionResult.getResolution())
                    .getNotification();

            notificationManager.notify(SLENDER_SERVICE_NOTIFICATION_ID, notification);
        } else {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText("Application not compatible with this phone.")
                    .setContentIntent(
                            PendingIntent.getActivity(getApplicationContext(),
                                    0,
                                    new Intent(this, Lifemeter.class),
                                    0
                            ))
                    .getNotification();
            notificationManager.notify(SLENDER_SERVICE_NOTIFICATION_ID, notification);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, location.toString());
        if(location != null) {


        }

    }

    @Override
    public void onAddGeofencesResult(int i, String[] strings) {
        if (i == LocationStatusCodes.SUCCESS) {
            //Do anything on success?
        }

        else
            Toast.makeText(getApplicationContext(), "Failed to add Geofence, please try again" , Toast.LENGTH_SHORT).show();
    }

    public class LocalBinder extends Binder {
        public void createGeofence(Location location, float radius, String uniqueID) {
            Geofence g = new Geofence.Builder().setCircularRegion(location.getLatitude()
                    ,location.getLongitude()
                    ,radius)
                    .setRequestId(uniqueID)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                            | Geofence.GEOFENCE_TRANSITION_EXIT).build();
            ArrayList<Geofence> gl = new ArrayList<Geofence>();

            gl.add(g);
            mGeofencePendingIntent = getTransitionPendingIntent();
            locationClient.addGeofences(gl , mGeofencePendingIntent, locationListener);
            Toast.makeText(getApplicationContext(),"Actually created the Geofence, " + g.getRequestId(), Toast.LENGTH_SHORT).show();
        }
        public Location getLocation() {
            return location;
        }
    }
}