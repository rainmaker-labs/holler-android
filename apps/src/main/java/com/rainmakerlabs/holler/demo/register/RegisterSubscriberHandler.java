package com.rainmakerlabs.holler.demo.register;

import android.view.View;

/**
 * Created by thanhtritran on 30/11/16.
 */

public interface RegisterSubscriberHandler {

    void onCountryCodeClick(View view);

    void onRegisterClick(View view);

    void onBackClick(View view);

    void onDatePickerClick(View view);
}
