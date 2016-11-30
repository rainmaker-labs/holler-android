package com.rainmakerlabs.holler.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class SubscriberInfo implements Parcelable {

    private String gender = "male";

    private String country;

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
