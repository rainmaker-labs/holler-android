package sample.sdk.holler.rainmakerlabs.com.hollersdk;

import com.rainmakerlabs.holler.sdk.subscriber.Subscriber;

/**
 * Created by thanhtritran on 30/11/16.
 */

public interface NavigationHandler {

    void openRegisterSuccessful(Subscriber subscriber);

    void openRegisterSubscriber();

    void openUpdateSubscriber(Subscriber subscriber);
}
