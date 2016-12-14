package sample.sdk.holler.rainmakerlabs.com.hollersdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.rainmakerlabs.holler.sdk.response.error.ErrorCallback;
import com.rainmakerlabs.holler.sdk.response.error.Error;
import com.rainmakerlabs.holler.sdk.subscriber.Subscriber;
import com.rainmakerlabs.support.FragmentTransition;
import com.rainmakerlabs.ui.dialog.IConfirmMessage;
import com.rainmakerlabs.ui.dialog.ILoadingMessage;
import com.rainmakerlabs.ui.dialog.IMessage;
import com.rainmakerlabs.ui.dialog.blur.BlurConfirmDialog;
import com.rainmakerlabs.ui.dialog.blur.BlurMessageDialog;

import sample.sdk.holler.rainmakerlabs.com.hollersdk.dialog.LoadingDialog;
import sample.sdk.holler.rainmakerlabs.com.hollersdk.register.RegisterSubscriberActivity;
import sample.sdk.holler.rainmakerlabs.com.hollersdk.register.RegisterSuccessfulActivity;
import sample.sdk.holler.rainmakerlabs.com.hollersdk.ui.IFragmentNavigation;
import sample.sdk.holler.rainmakerlabs.com.hollersdk.ui.IMessageHandler;

/**
 * Created by thanhtritran on 29/11/16.
 */

public class HollerActivity extends AppCompatActivity implements IFragmentNavigation,
        IMessageHandler, NavigationHandler, ErrorCallback {

    private ILoadingMessage iLoading;
    private IConfirmMessage iConfirm;
    private IMessage iMessage;


    protected HollerApplication mApplication;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mApplication = (HollerApplication) this.getApplication();
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

    @Override
    public void openRegisterSuccessful(Subscriber subscriber) {
        Intent intent = new Intent(this, RegisterSuccessfulActivity.class);
        intent.putExtra("subscriber", subscriber);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public void openRegisterSubscriber() {
        Intent intent = new Intent(this, RegisterSubscriberActivity.class);
        this.startActivity(intent);
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

    @Override
    public void onError(Error error) {
        this.showToastMessage(error.getMessage());
        this.hideLoading();
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        this.showToastMessage(t.getMessage());
        this.hideLoading();
    }


    public void onBackClick(View view) {
        this.onBackPressed();
    }
}
