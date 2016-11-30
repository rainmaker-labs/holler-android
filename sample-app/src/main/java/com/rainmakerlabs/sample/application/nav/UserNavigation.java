package com.rainmakerlabs.sample.application.nav;

import android.support.v4.app.Fragment;

import com.rainmakerlabs.sample.application.users.model.User;

/**
 * Created by thanhtritran on 17/10/16.
 */

public interface UserNavigation {

    Fragment openUserList();

    Fragment openUserDetails(User user);
}
