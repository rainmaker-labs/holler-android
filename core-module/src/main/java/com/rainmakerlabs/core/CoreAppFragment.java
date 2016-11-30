package com.rainmakerlabs.core;

import android.content.Context;

import com.rainmakerlabs.core.ui.IFragmentNavigation;
import com.rainmakerlabs.core.ui.IMessageHandler;
import com.rainmakerlabs.networking.NetworkAppFragment;
import com.rainmakerlabs.networking.model.NetworkResponse;
import com.rainmakerlabs.ui.dialog.IConfirmMessage;
import com.rainmakerlabs.ui.dialog.ILoadingMessage;
import com.rainmakerlabs.ui.dialog.IMessage;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by thanhtritran on 12/10/16.
 */

public abstract class CoreAppFragment extends NetworkAppFragment implements IMessageHandler {

    private IMessageHandler iMessageHandler;

    protected IFragmentNavigation fragmentNavigation;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.iMessageHandler = (IMessageHandler) context;
        this.fragmentNavigation = (IFragmentNavigation) context;
    }

    @Override
    public ILoadingMessage getLoading() {
        return this.iMessageHandler.getLoading();
    }

    @Override
    public IConfirmMessage getConfirm() {
        return this.iMessageHandler.getConfirm();
    }

    @Override
    public IMessage getMessage() {
        return this.iMessageHandler.getMessage();
    }

    @Override
    public void hideMessage() {
        this.iMessageHandler.hideMessage();
    }

    @Override
    public void hideConfirm() {
        this.iMessageHandler.hideConfirm();
    }

    @Override
    public void hideLoading() {
        this.iMessageHandler.hideLoading();
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
