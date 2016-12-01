package com.rainmakerlabs.holler.demo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thanh Tri TRAN on 12/18/2015.
 */
public class PermissionHelper {

    public static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static List<String> getPermissionsNeedRequest(Context context, String[] permissions) {

        List<String> result = new ArrayList<>();

        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED
                    != ContextCompat.checkSelfPermission(context, permission)) {
                result.add(permission);
            }
        }

        return result;

    }

    public static List<String> shouldShowRationalePermissions(Activity context, String[] permissions ) {
        List<String> result = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                result.add(permission);
            }
        }
        return result;
    }


    public static boolean hasReadFilePermission(Context context) {
        return PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public static boolean shouldShowRequestReadFilePermissionRationale(Activity activity) {

        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public static void requestReadFilePermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                requestCode);
    }

    public static boolean hasWriteFilePermission(Context context) {
        return PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean shouldShowRequestWriteFilePermissionRationale(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestWriteFilePermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                requestCode);
    }

    //Location permission
    public static boolean hasLocationPermission(Context context) {
        return PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)&&PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static boolean hasCameraPermission(Context context) {
        return PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
    }

    public static boolean hasGetAccountsPermission(Context context) {
        return hasPermission(context, Manifest.permission.GET_ACCOUNTS);
    }

//    public static boolean shouldShowRequestLocationPermissionRationale(Activity activity) {
//        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)&& ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION);
//    }

    public static void requestLocationPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                requestCode);
    }

    public static void requestCameraPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA},
                requestCode);
    }

    public static void requestGetAccountsPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                requestCode);
    }

    public static boolean hasPermission(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }

    public static void requestPermissions(Activity context, String[] permissions,int requestCode) {
        ActivityCompat.requestPermissions(context,permissions,requestCode);
    }


}
