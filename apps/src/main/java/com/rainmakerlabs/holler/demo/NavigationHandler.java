package com.rainmakerlabs.holler.demo;

import com.rainmakerlabs.holler.demo.model.Application;
import com.rainmakerlabs.holler.demo.model.Subscriber;

/**
 * Created by thanhtritran on 30/11/16.
 */

public interface NavigationHandler {

    void openRegisterSubscribe(Application app);

    void openLogin();

    void openRegisterSuccessful(Subscriber subscriber);

    void openUpdateSubscriber(Subscriber subscriber);
}
