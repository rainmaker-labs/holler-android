package com.rainmakerlabs.holler.demo;

import com.rainmakerlabs.holler.demo.model.Application;
import com.rainmakerlabs.networking.ICacheConfig;
import com.rainmakerlabs.networking.INetworkConfig;
import com.rainmakerlabs.networking.NetworkApplication;
import com.rainmakerlabs.networking.NetworkUtils;

import java.util.HashMap;
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
}
