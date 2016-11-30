package com.rainmakerlabs.holler.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class Application extends AbstractModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("application_id")
    @Expose
    private String applicationId;
    @SerializedName("access_key_api")
    @Expose
    private String accessKeyApi;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("apns_env")
    @Expose
    private String apnsEnv;
    @SerializedName("gcm_apikey")
    @Expose
    private String gcmApikey;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("logo_img")
    @Expose
    private String logoImg;
    @SerializedName("is_demo")
    @Expose
    private boolean demo;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getAccessKeyApi() {
        return accessKeyApi;
    }

    public String getDescription() {
        return description;
    }

    public String getApnsEnv() {
        return apnsEnv;
    }

    public String getGcmApikey() {
        return gcmApikey;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public boolean isDemo() {
        return demo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.applicationId);
        dest.writeString(this.accessKeyApi);
        dest.writeString(this.description);
        dest.writeString(this.apnsEnv);
        dest.writeString(this.gcmApikey);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.timezone);
        dest.writeString(this.logoImg);
        dest.writeByte(this.demo ? (byte) 1 : (byte) 0);
    }

    public Application() {
    }

    protected Application(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.applicationId = in.readString();
        this.accessKeyApi = in.readString();
        this.description = in.readString();
        this.apnsEnv = in.readString();
        this.gcmApikey = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.timezone = in.readString();
        this.logoImg = in.readString();
        this.demo = in.readByte() != 0;
    }

    public static final Creator<Application> CREATOR = new Creator<Application>() {
        @Override
        public Application createFromParcel(Parcel source) {
            return new Application(source);
        }

        @Override
        public Application[] newArray(int size) {
            return new Application[size];
        }
    };

    public static List<Application> parseArray(JsonElement body) {

        Type type = new TypeToken<ArrayList<Application>>() {
        }.getType();

        return getGson().fromJson(body, type);
    }

    public static Application parse(String json) {
        return getGson().fromJson(json, Application.class);
    }
}
