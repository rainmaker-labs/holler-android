package com.rainmakerlabs.holler.demo.model;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class ErrorMessage extends AbstractModel {

    private String field;

    private String message;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static List<ErrorMessage> parseArray(JsonElement body) {

        Type type = new TypeToken<ArrayList<ErrorMessage>>(){}.getType();

        return getGson().fromJson(body, type);
    }
}
