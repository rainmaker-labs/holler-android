package com.rainmakerlabs.holler.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.google.gson.JsonElement;
import com.rainmakerlabs.holler.demo.model.Application;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class SplashActivity extends HollerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.splash_activity);
        ImageView imageView = (ImageView) this.findViewById(R.id.ic_logo);
        int size = this.getResources().getDisplayMetrics().widthPixels;
        imageView.getLayoutParams().width = size / 2;

        if (UserLocalStorage.isLogged(this)) {
            this.makeRequest(mNetwork.applications(), 2);
        } else {
            imageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    openLogin();
                }
            }, 2000);
        }
    }

    @Override
    public void onResponse(Call<JsonElement> call, Response<JsonElement> response, int requestCode) {
        if (response.code() == 200) {
            Application app = Application.parseArray(response.body().getAsJsonObject().get("apps")).get(0);
            UserLocalStorage.saveApplication(this, app);
            this.openRegisterSubscribe(app);
        } else {
            this.openLogin();
        }
    }
}
