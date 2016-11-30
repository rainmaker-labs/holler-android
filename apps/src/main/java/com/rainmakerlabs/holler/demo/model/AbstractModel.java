package com.rainmakerlabs.holler.demo.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class AbstractModel {

    public static Gson getGson(){
        return new GsonBuilder().create();
    }

}
