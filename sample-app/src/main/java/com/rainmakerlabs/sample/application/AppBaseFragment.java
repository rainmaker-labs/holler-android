package com.rainmakerlabs.sample.application;

import android.content.Context;

import com.rainmakerlabs.core.CoreAppFragment;

/**
 * Created by thanhtritran on 9/27/16.
 */

public class AppBaseFragment extends CoreAppFragment {

    protected AppBaseActivity activity;

    protected NetworkService networkService;

    protected SampleApplication myApplication;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (AppBaseActivity) context;
        this.myApplication = (SampleApplication) this.activity.getApplication();
        this.networkService = this.myApplication.getNetworkService();
    }


}
