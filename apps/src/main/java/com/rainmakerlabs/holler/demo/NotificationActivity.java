package com.rainmakerlabs.holler.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.rainmakerlabs.holler.demo.databinding.NotificationActivityBinding;
import com.rainmakerlabs.holler.demo.databinding.RegisterSuccessfulActivityBinding;
import com.rainmakerlabs.holler.demo.model.Subscriber;

/**
 * Created by vinh.phamtien on 12/1/2016.
 */

public class NotificationActivity extends HollerActivity {

    private NotificationActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.setContentView(this, R.layout.notification_activity);
        this.binding.setHandler(this);

        int size = this.getResources().getDisplayMetrics().widthPixels;
        int h = this.getResources().getDisplayMetrics().heightPixels;

        this.binding.imageLogo.getLayoutParams().width = size / 2;
        this.binding.imageLogo.getLayoutParams().height = size / 2;
    }

    public void onUpdateClick(View view) {
//        Subscriber subscriber = this.getIntent().getParcelableExtra("subscriber");
//        this.openUpdateSubscriber(subscriber);
    }
}
