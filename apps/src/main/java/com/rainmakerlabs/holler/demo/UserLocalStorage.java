package com.rainmakerlabs.holler.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.rainmakerlabs.holler.demo.model.Application;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class UserLocalStorage {

    private static Application app;

    /**
     * Gets the settings.
     *
     * @param context the context
     * @return the settings
     */
    private static SharedPreferences getSettings(Context context) {
        return context.getSharedPreferences(context.getPackageName(), 0);
    }

    /**
     * Gets the editor.
     *
     * @param context the context
     * @return the editor
     */
    private static SharedPreferences.Editor getEditor(Context context) {
        return context.getSharedPreferences(context.getPackageName(), 0).edit();
    }

    public static void saveAccessKey(Context context, String accessKey) {
        if (TextUtils.isEmpty(accessKey)) {
            getEditor(context).remove("MasterAccessKey").commit();
        } else {
            getEditor(context).putString("MasterAccessKey", "Token " + accessKey).commit();
        }

    }

    public static String getAccessKey(Context context) {
        return getSettings(context).getString("MasterAccessKey", null);
    }

    public static boolean isLogged(Context context) {
        return getSettings(context).contains("MasterAccessKey");
    }

    public static void saveDeviceToken(Context context, String token) {
        getEditor(context).putString("device-token", token).commit();
    }

    public static String getDeviceToken(Context context) {
        return getSettings(context).getString("device-token", null);
    }

    public static void saveApplication(Context context, Application application) {
        app = application;
        getEditor(context).putString("Application", Application.getGson().toJson(application)).commit();
    }

    public static Application getApplication(Context context) {

        if (app != null) {
            return app;
        }

        String json = getSettings(context).getString("Application", null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        return Application.parse(json);
    }

}
