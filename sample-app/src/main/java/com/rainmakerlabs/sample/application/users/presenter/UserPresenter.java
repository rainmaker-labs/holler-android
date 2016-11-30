package com.rainmakerlabs.sample.application.users.presenter;

import android.view.View;

import com.rainmakerlabs.sample.application.users.model.User;

/**
 * Created by thanhtritran on 21/10/16.
 */

public interface UserPresenter {
    void onProfileClick(View view, User user);
}
