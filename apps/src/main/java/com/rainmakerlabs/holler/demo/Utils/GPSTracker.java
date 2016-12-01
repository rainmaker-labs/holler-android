package com.rainmakerlabs.holler.demo.Utils;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;


import java.util.ArrayList;

/**
 * Created by QuangAnh on 11/16/2015.
 */
public class GPSTracker extends Service implements LocationListener {
    private static final long MIN_TIME_UPDATE = 4000;
    private static final long MIN_DISTANCE_UPDATE = 0;
    private static final long MIN_DISTANCE_NOTIFY = 200;
    private static final long MIN_TIME_NOTIFY = 30000;//60*5*1000

    private static IBinder mBinder;

    private boolean gpsEnable = false;
    private boolean networkEnable = false;
    private Location lastLocation;
    private LocationManager locationManager;
    private Context context;
    ArrayList<GPSServiceListener> listeners;

    public void startGettingLocation() {
        if (context == null) {
            this.context = getApplication();
        }
        try {
            this.locationManager = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
            this.gpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.networkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gpsEnable && !networkEnable) {

            } else {

                Location location = null;

                if (this.networkEnable) {
                    //check permission for Marshall and up
                    if (!PermissionHelper.hasLocationPermission(this.context)) {
                        //Retry at activity already , do not retry here
                        return;
                    }
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);
                    if (locationManager != null) {

                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            if (this.lastLocation == null) {
                                this.lastLocation = location;
                            }
                        }
                    }
                }

                if (this.gpsEnable) {
                    if (location == null) {
                        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                                , MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);

                        if (this.locationManager != null) {
                            location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                if (this.lastLocation == null) {
                                    this.lastLocation = location;
                                }
                            }
                        }
                    } else {
                        this.lastLocation = location;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (this.lastLocation != null) {

            //first init
            notifyOnLocationChange(this.lastLocation);

        }
    }

    public void addGPSServiceListener(GPSServiceListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    public void removeGPSServiceListener(GPSServiceListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public Location getLastLocation() {
        return this.lastLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

        if (this.lastLocation == null) {
            this.lastLocation = location;
            notifyOnLocationChange(this.lastLocation);
        } else {
            if (isNotifyLocationChange(this.lastLocation, location)) {
                this.lastLocation = location;
                notifyOnLocationChange(this.lastLocation);
            }
        }
    }

    private void notifyOnLocationChange(Location location) {
//        MyLog.w("location_public", "here " + location.getLongitude() + " " + location.getLatitude());
        if (listeners != null) {
            for (GPSServiceListener listener : listeners) {
                if (listener != null) {
                    listener.onLocationChange(location);
                }
            }
        }
    }

    /**
     * return true if distance increase more than 200 meters or time more than 5 minutes
     **/
    private boolean isNotifyLocationChange(Location old, Location update) {
        boolean isNotifyLocationChange = false;

        isNotifyLocationChange = (old.distanceTo(update) >= MIN_DISTANCE_NOTIFY) && (update.getTime() - old.getTime() >= MIN_TIME_NOTIFY);

        return isNotifyLocationChange;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new MyBinder();
        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        this.stopSelf();
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        public GPSTracker getService() {
            return GPSTracker.this;
        }
    }
}
