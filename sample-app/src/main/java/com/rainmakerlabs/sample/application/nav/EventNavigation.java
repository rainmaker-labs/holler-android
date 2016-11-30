package com.rainmakerlabs.sample.application.nav;

import android.support.v4.app.Fragment;

/**
 * Created by thanhtritran on 17/10/16.
 */

public interface EventNavigation {

    Fragment openEvents();

    Fragment openEventDetails(int id);
}
