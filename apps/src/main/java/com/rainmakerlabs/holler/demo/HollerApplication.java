package com.rainmakerlabs.holler.demo;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.rainmakerlabs.holler.demo.model.Application;
import com.rainmakerlabs.networking.ICacheConfig;
import com.rainmakerlabs.networking.INetworkConfig;
import com.rainmakerlabs.networking.NetworkApplication;
import com.rainmakerlabs.networking.NetworkUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Interceptor;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class HollerApplication extends NetworkApplication<HollerService>
        implements INetworkConfig, ICacheConfig {


    private final Map<String, String> headers = new HashMap<String, String>();

    @Override
    public INetworkConfig getNetworkConfig() {
        return this;
    }

    @Override
    public ICacheConfig getCacheConfig() {
        return this;
    }

    @Override
    public String requestBaseUrl() {
        return "http://stg-holler.rmlbs.co/api/v2/";
    }

    @Override
    public long requestTimeout() {
        return 20000;
    }

    @Override
    public Map<String, String> requestHeaders() {

        if (UserLocalStorage.isLogged(this)) {
            this.headers.put("Authorization", UserLocalStorage.getAccessKey(this));
        }

        Application app = UserLocalStorage.getApplication(this);

        if (app != null) {
            this.headers.put("HOLLER-ACCESS-KEY", app.getAccessKeyApi());
            this.headers.put("HOLLER-APP-ID", app.getApplicationId());
        }

        return this.headers;
    }

    @Override
    public boolean shouldShowNetworkLogs() {
        return false;
    }

    @Override
    public String contentType() {
        return "application/json";
    }

    @Override
    public Interceptor offlineCacheInterceptor() {
        return NetworkUtils.defaultOfflineCacheInterceptor(this);
    }

    @Override
    public Interceptor cacheInterceptor() {
        return NetworkUtils.defaultCacheInterceptor();
    }

    @Override
    public Cache cache() {
        return NetworkUtils.defaultCache(this);
    }

    /**
     * Method checks if the app is in background or not
     *
     * @param context
     * @return
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am
                    .getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
