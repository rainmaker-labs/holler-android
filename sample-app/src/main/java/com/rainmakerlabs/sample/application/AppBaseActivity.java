package com.rainmakerlabs.sample.application;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.rainmakerlabs.core.CoreAppCompatActivity;
import com.rainmakerlabs.sample.application.dialog.LoadingDialog;
import com.rainmakerlabs.sample.application.nav.UserNavigation;
import com.rainmakerlabs.sample.application.users.fragment.UserDetailsFragment;
import com.rainmakerlabs.sample.application.users.fragment.UsersFragment;
import com.rainmakerlabs.sample.application.users.model.User;
import com.rainmakerlabs.ui.dialog.ILoadingMessage;

/**
 * Created by thanhtritran on 10/3/16.
 */

public abstract class AppBaseActivity
        extends CoreAppCompatActivity implements UserNavigation {

    protected NetworkService networkService;

    protected SampleApplication myApplication;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.myApplication = (SampleApplication) this.getApplication();
        this.networkService = this.myApplication.getNetworkService();
    }

    @Override
    protected ILoadingMessage defineLoadingMessage() {
        return new LoadingDialog(this);
    }

    @Override
    protected int fullScreenFragmentContainerLayoutId() {
        return android.R.id.content;
    }

    @Override
    public Fragment openUserList() {
        return this.addFragment(UsersFragment.newInstance(this), true);
    }

    @Override
    public Fragment openUserDetails(User user) {
        return this.addFragment(UserDetailsFragment.newInstance(this, user), true);
    }
}
