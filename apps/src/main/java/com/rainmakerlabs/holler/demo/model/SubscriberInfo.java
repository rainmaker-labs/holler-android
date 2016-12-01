package com.rainmakerlabs.holler.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class SubscriberInfo implements Parcelable {

    private String gender = "male";

    private String country;

    @SerializedName("gps_latitude")
    private double gpsLatitude;

    @SerializedName("gps_longitude")
    private double gpsLongitude;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(double gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public double getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gender);
    }

    public SubscriberInfo() {
    }

    protected SubscriberInfo(Parcel in) {
        this.gender = in.readString();
    }

    public static final Creator<SubscriberInfo> CREATOR = new Creator<SubscriberInfo>() {
        @Override
        public SubscriberInfo createFromParcel(Parcel source) {
            return new SubscriberInfo(source);
        }

        @Override
        public SubscriberInfo[] newArray(int size) {
            return new SubscriberInfo[size];
        }
    };

    public boolean isMale() {
        return "male".equals(this.gender);
    }

    public boolean isFemale() {
        return !this.isMale();
    }
}
