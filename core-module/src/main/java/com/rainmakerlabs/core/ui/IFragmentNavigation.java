package com.rainmakerlabs.core.ui;

import android.support.v4.app.Fragment;

/**
 * Created by thanhtritran on 9/26/16.
 */

public interface IFragmentNavigation {

    Fragment addFragment(Fragment fragment, boolean fullScreen);

    Fragment replaceFragment(Fragment fragment, boolean fullScreen);



}
