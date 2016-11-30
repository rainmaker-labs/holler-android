package com.rainmakerlabs.sample.application;

import com.rainmakerlabs.networking.ICacheConfig;
import com.rainmakerlabs.networking.INetworkConfig;
import com.rainmakerlabs.networking.NetworkApplication;
import com.rainmakerlabs.networking.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Interceptor;

/**
 * Created by thanhtritran on 17/10/16.
 */

public class SampleApplication
        extends NetworkApplication<NetworkService>
        implements INetworkConfig, ICacheConfig {

    private final Map<String, String> headers = new HashMap<String, String>() {{
        this.put("USER-TOKEN", "dNdrXsVL8oLbyTS7TsC9QndoBs0pNTs1");
        this.put("JsonStub-User-Key", "b69fe01e-8e54-4bf6-ba01-19d583f7a47f");
        this.put("JsonStub-Project-Key", "58982f0c-bfa0-47ef-9a10-37b503cddf2d");
    }};

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
        return "http://jsonstub.com/";
    }

    @Override
    public long requestTimeout() {
        return 20000;
    }

    @Override
    public Map<String, String> requestHeaders() {
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
