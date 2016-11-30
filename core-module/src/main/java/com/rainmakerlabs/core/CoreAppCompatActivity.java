package com.rainmakerlabs.core;

import android.support.v4.app.Fragment;

import com.rainmakerlabs.core.ui.IFragmentNavigation;
import com.rainmakerlabs.core.ui.IMessageHandler;
import com.rainmakerlabs.networking.NetworkAppCompatActivity;
import com.rainmakerlabs.networking.model.NetworkResponse;
import com.rainmakerlabs.support.FragmentTransition;
import com.rainmakerlabs.ui.dialog.IConfirmMessage;
import com.rainmakerlabs.ui.dialog.ILoadingMessage;
import com.rainmakerlabs.ui.dialog.IMessage;
import com.rainmakerlabs.ui.dialog.blur.BlurConfirmDialog;
import com.rainmakerlabs.ui.dialog.blur.BlurLoadingDialog;
import com.rainmakerlabs.ui.dialog.blur.BlurMessageDialog;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by thanhtritran on 9/13/16.
 */
public abstract class CoreAppCompatActivity extends NetworkAppCompatActivity
        implements IFragmentNavigation, IMessageHandler {

    private ILoadingMessage iLoading;
    private IConfirmMessage iConfirm;
    private IMessage iMessage;


    @Override
    public Fragment addFragment(Fragment fragment, boolean fullScreen) {
        return FragmentTransition.instantiate(this)
                .onto(fullScreen ? this.fullScreenFragmentContainerLayoutId() : this.fragmentContainerLayoutId())
                .add(fragment);

    }

    @Override
    public Fragment replaceFragment(Fragment fragment, boolean fullScreen) {
        return FragmentTransition.instantiate(this)
                .onto(fullScreen ? this.fullScreenFragmentContainerLayoutId() : this.fragmentContainerLayoutId())
                .replace(fragment);
    }

    protected int fullScreenFragmentContainerLayoutId() {
        return android.R.id.content;
    }

    protected int fragmentContainerLayoutId() {
        return 0;
    }

    protected ILoadingMessage defineLoadingMessage() {
        return new BlurLoadingDialog(this);
    }

    protected IConfirmMessage defineConfirmMessage() {
        return new BlurConfirmDialog(this);
    }

    protected IMessage defineMessage() {
        return new BlurMessageDialog(this);
    }

    @Override
    public ILoadingMessage getLoading() {
        if (this.iLoading == null) {
            this.iLoading = this.defineLoadingMessage();
        }
        this.iLoading.reset();
        return this.iLoading;
    }

    @Override
    public IConfirmMessage getConfirm() {
        if (this.iConfirm == null) {
            this.iConfirm = this.defineConfirmMessage();
        }
        this.iConfirm.reset();
        return this.iConfirm;
    }

    @Override
    public IMessage getMessage() {
        if (this.iMessage == null) {
            this.iMessage = this.defineMessage();
        }
        this.iMessage.reset();
        return this.iMessage;
    }

    @Override
    public void hideMessage() {
        if (this.iMessage != null) {
            this.iMessage.dismiss();
        }
    }

    @Override
    public void hideConfirm() {
        if (this.iConfirm != null) {
            this.iConfirm.dismiss();
        }
    }

    @Override
    public void hideLoading() {
        if (this.iLoading != null) {
            this.iLoading.dismiss();
        }
    }

    @Override
    public void onRequestFailure(Call<NetworkResponse> call, Response<NetworkResponse> response, int requestCode) {
        super.onRequestFailure(call, response, requestCode);

        String message;

        if (response.body() != null) {
            message = response.body().getMessage();
        } else {
            message = response.message();
        }

        this.getMessage().message(message).error().show();

    }

    @Override
    public void onNetworkFailure(Call<NetworkResponse> call, Throwable t, int requestCode) {
        super.onNetworkFailure(call, t, requestCode);
        this.getMessage().message(t.getMessage()).error().show();
    }
}
