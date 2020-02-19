package com.victoria.driver.ui.location_service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonSyntaxException;
import com.victoria.driver.core.AppPreferences;
import com.victoria.driver.core.Common;
import com.victoria.driver.core.Session;
import com.victoria.driver.data.pojo.DataWrapper;
import com.victoria.driver.data.pojo.User;
import com.victoria.driver.data.repository.UserRepository;
import com.victoria.driver.di.Injector;
import com.victoria.driver.exception.AuthenticationException;
import com.victoria.driver.ui.model.Parameter;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;


public class LocationService extends
        Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "LocationService";

    // use the websmithing defaultUploadWebsite for testing and then check your
    // location with your browser here: https://www.websmithing.com/gpstracker/displaymap.php

    @Inject
    UserRepository driverRepository;

    Parameter driver;

    @Inject
    Session session;

    // use the websmithing defaultUploadWebsite for testing and then check your
    // location with your browser here: https://www.websmithing.com/gpstracker/displaymap.php

    private boolean currentlyProcessingLocation = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    public static String CurrentLatitude = "0.0";
    public static String CurrentLongitude = "0.0";

    public static String duration = "";
    public static String time = "";

    public static LatLng currentLatLng = null;

    Runnable runnable;
    Handler handler = new Handler();

    public static final String BROADCAST_ACTION = "CheckVersion";
    private Intent intent;

    public static String LastCurrentLatitude = "0.0";
    public static String LastCurrentLongitude = "0.0";
    //private KalmanFilter kalmanFilter = new KalmanFilter(28);

    Disposable disposable;


    @Inject
    AppPreferences appPreferences;


    @Override
    public void onCreate() {
        super.onCreate();

        DaggerServiceComponent.builder()
                .applicationComponent(Injector.INSTANCE.getApplicationComponent())
                .build()
                .inject(this);

        intent = new Intent(BROADCAST_ACTION);

        driver = new Parameter();


        //driver = session.getUser();

        //CommonConstant.PUBNUB_CHANNEL_NAME=driver.id.toString();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // if we are currently trying to get a location and the alarm manager has called this again,
        // no need to start processing a new location.

        if (!currentlyProcessingLocation) {
            currentlyProcessingLocation = true;
            startTracking();
        }

        return START_NOT_STICKY;
    }

    private void startTracking() {

        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        handler.removeCallbacks(runnable);
        stopLocationUpdates();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {


            if (location.getAccuracy() < 200 && location.getAccuracy() != 0.0) {

                //currentLatLng = kalmanFilter.process(location.getLatitude(), location.getLongitude(), location.getAccuracy(), location.getTime());

                CurrentLatitude = String.valueOf(location.getLatitude());
                CurrentLongitude = String.valueOf(location.getLongitude());

                if (!LastCurrentLatitude.equalsIgnoreCase(CurrentLatitude) &&
                        !LastCurrentLongitude.equalsIgnoreCase(CurrentLongitude)) {

                    sendLocationDataToWebsite(location);
                }

                LocationService.LastCurrentLatitude = LocationService.CurrentLatitude;
                LocationService.LastCurrentLongitude = LocationService.CurrentLongitude;
            }

        }
    }


    protected void sendLocationDataToWebsite(Location location) {
        try {
            CurrentLatitude = String.valueOf(location.getLatitude());
            CurrentLongitude = String.valueOf(location.getLongitude());

            // IsLocationChanged(location);

            if (!CurrentLatitude.equals("") && !CurrentLongitude.equals("")) {

                // if (UTILS.isInternetAvailable(getApplicationContext())) {

                //  driver.setId(appPreferences.getString(Common.DRIVER_ID));
                driver.latitude = (LocationService.CurrentLatitude);
                driver.longitude = (LocationService.CurrentLongitude);

                if (disposable != null) {

                    disposable.dispose();
                }

                driverRepository.updateLocation(driver).subscribe
                        (new SingleObserver<DataWrapper<User>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onSuccess(DataWrapper<User> userDataWrapper) {

                            }

                            @Override
                            public void onError(Throwable e) {
                                    /*if (rootView.get() != null)
                                        rootView.get().onError(e);*/
                                if (e instanceof AuthenticationException) {
                                    appPreferences.clearAll();
                                    appPreferences.putString(Common.IS_LOGIN, "false");
                                }
                            }
                        });
                //}
            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }


    private void stopLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }


    /**
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); // milliseconds 10000
        locationRequest.setFastestInterval(1000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*stopLocationUpdates();
        stopSelf();*/
    }

    @Override
    public void onConnectionSuspended(int i) {
    }


}
