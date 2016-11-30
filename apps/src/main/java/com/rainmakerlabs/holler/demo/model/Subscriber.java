package com.rainmakerlabs.holler.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class Subscriber extends AbstractModel implements Parcelable {

    private Integer id;

    @SerializedName("username")
    private String username;

    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("phone")
    private String phone;

    public String getLocalPhone() {
        return localPhone;
    }

    public void setLocalPhone(String localPhone) {
        this.localPhone = localPhone;
    }

    private transient String localPhone;

    @SerializedName("device_token")
    private String deviceToken;

    @SerializedName("device_type")
    private String deviceType = "android";

    @SerializedName("info")
    private SubscriberInfo info;

    private transient String countryCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public SubscriberInfo getInfo() {
        return info;
    }

    public void setInfo(SubscriberInfo info) {
        this.info = info;
    }


    public Subscriber() {
        this.info = new SubscriberInfo();
    }

    public JsonObject toJsonObject() {
        JsonObject object = getGson().toJsonTree(this).getAsJsonObject();
        object.addProperty("phone", this.countryCode + this.localPhone);
        return object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.firstName);
        dest.writeString(this.localPhone);
        dest.writeString(this.phone);
        dest.writeString(this.deviceToken);
        dest.writeString(this.deviceType);
        dest.writeParcelable(this.info, flags);
        dest.writeString(this.countryCode);
    }

    protected Subscriber(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.username = in.readString();
        this.email = in.readString();
        this.firstName = in.readString();
        this.localPhone = in.readString();
        this.phone = in.readString();
        this.deviceToken = in.readString();
        this.deviceType = in.readString();
        this.info = in.readParcelable(SubscriberInfo.class.getClassLoader());
        this.countryCode = in.readString();
    }

    public static final Creator<Subscriber> CREATOR = new Creator<Subscriber>() {
        @Override
        public Subscriber createFromParcel(Parcel source) {
            return new Subscriber(source);
        }

        @Override
        public Subscriber[] newArray(int size) {
            return new Subscriber[size];
        }
    };

    public static List<Subscriber> parseArray(JsonElement body) {

        Type type = new TypeToken<ArrayList<Subscriber>>() {
        }.getType();

        return getGson().fromJson(body, type);
    }

    public void setGender(String gender) {
        if (this.info == null) {
            this.info = new SubscriberInfo();
        }

        this.info.setGender(gender);
    }

    public boolean isNew() {
        return this.id == null;
    }

    public void setCountry(String country) {
        if (this.info == null) {
            this.info = new SubscriberInfo();
        }

        this.info.setCountry(country);
    }
}
