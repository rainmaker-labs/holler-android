package com.rainmakerlabs.holler.demo.register;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.rainmakerlabs.holler.demo.HollerActivity;
import com.rainmakerlabs.holler.demo.R;
import com.rainmakerlabs.holler.demo.databinding.RegisterSuccessfulActivityBinding;
import com.rainmakerlabs.holler.demo.model.Subscriber;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class RegisterSuccessfulActivity extends HollerActivity {

    private RegisterSuccessfulActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.setContentView(this, R.layout.register_successful_activity);
        this.binding.setHandler(this);

        int size = this.getResources().getDisplayMetrics().widthPixels;
        int h = this.getResources().getDisplayMetrics().heightPixels;

        this.binding.imageLogo.getLayoutParams().width = size / 2;
        this.binding.imageLogo.getLayoutParams().height = size / 2;
    }

    public void onUpdateClick(View view) {
        Subscriber subscriber = this.getIntent().getParcelableExtra("subscriber");
        this.openUpdateSubscriber(subscriber);
    }

}
