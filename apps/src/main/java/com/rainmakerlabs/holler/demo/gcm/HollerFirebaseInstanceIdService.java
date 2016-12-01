package com.rainmakerlabs.holler.demo.gcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rainmakerlabs.holler.demo.UserLocalStorage;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class HollerFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        UserLocalStorage.saveDeviceToken(this.getApplicationContext(), refreshedToken);

    }
}
