package com.rainmakerlabs.holler.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rainmakerlabs.core.CoreAppCompatActivity;
import com.rainmakerlabs.core.ui.IFragmentNavigation;
import com.rainmakerlabs.core.ui.IMessageHandler;
import com.rainmakerlabs.holler.demo.dialog.LoadingDialog;
import com.rainmakerlabs.holler.demo.gcm.HollerFirebaseMessagingService;
import com.rainmakerlabs.holler.demo.login.LoginActivity;
import com.rainmakerlabs.holler.demo.model.Application;
import com.rainmakerlabs.holler.demo.model.Subscriber;
import com.rainmakerlabs.holler.demo.register.RegisterSubscriberActivity;
import com.rainmakerlabs.holler.demo.register.RegisterSuccessfulActivity;
import com.rainmakerlabs.networking.model.NetworkResponse;
import com.rainmakerlabs.support.FragmentTransition;
import com.rainmakerlabs.ui.dialog.IConfirmMessage;
import com.rainmakerlabs.ui.dialog.ILoadingMessage;
import com.rainmakerlabs.ui.dialog.IMessage;
import com.rainmakerlabs.ui.dialog.blur.BlurConfirmDialog;
import com.rainmakerlabs.ui.dialog.blur.BlurLoadingDialog;
import com.rainmakerlabs.ui.dialog.blur.BlurMessageDialog;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by thanhtritran on 29/11/16.
 */

public class HollerActivity extends AppCompatActivity implements IFragmentNavigation,
        IMessageHandler, Callback<JsonElement>, NavigationHandler {

    private Map<Call<JsonElement>, Integer> requestQueue = new HashMap<>();

    private ILoadingMessage iLoading;
    private IConfirmMessage iConfirm;
    private IMessage iMessage;


    protected HollerService mNetwork;
    protected HollerApplication mApplication;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mApplication = (HollerApplication) this.getApplication();
        this.mNetwork = this.mApplication.getNetworkService();
    }

    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


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
        return new LoadingDialog(this);
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

    public void makeRequest(Call<JsonElement> call, int requestCode) {
        this.requestQueue.put(call, requestCode);
        call.enqueue(this);
    }

    public void onResponse(Call<JsonElement> call, Response<JsonElement> response, int requestCode) {

    }


    @Override
    public final void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
        this.onResponse(call, response, this.requestQueue.get(call));
        this.requestQueue.remove(call);
    }

    @Override
    public void onFailure(Call<JsonElement> call, Throwable t) {
        this.hideLoading();
        this.getMessage().error().message(t.getMessage()).show();
    }

    @Override
    public void openRegisterSubscribe(Application app) {
        Intent intent = new Intent(this, RegisterSubscriberActivity.class);
        intent.putExtra("data", app);
        this.startActivity(intent);
    }

    @Override
    public void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void openRegisterSuccessful(Subscriber subscriber) {
        Intent intent = new Intent(this, RegisterSuccessfulActivity.class);
        intent.putExtra("subscriber", subscriber);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public void openUpdateSubscriber(Subscriber subscriber) {
        Intent intent = new Intent(this, RegisterSubscriberActivity.class);
        intent.putExtra("subscriber", subscriber);
        this.startActivity(intent);
        this.finish();
    }

    public void hideKeyBoard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(HollerActivity.this, NotificationActivity.class);
            HollerActivity.this.startActivity(i);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter(HollerFirebaseMessagingService.INTENT_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    @Override
    public void onBackPressed() {
        Log.w("TAG", "Locked on HollerActivity");
    }
}
