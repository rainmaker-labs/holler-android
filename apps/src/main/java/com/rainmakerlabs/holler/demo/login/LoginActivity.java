package com.rainmakerlabs.holler.demo.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rainmakerlabs.holler.demo.HollerActivity;
import com.rainmakerlabs.holler.demo.R;
import com.rainmakerlabs.holler.demo.UserLocalStorage;
import com.rainmakerlabs.holler.demo.databinding.ActivityLoginBinding;
import com.rainmakerlabs.holler.demo.model.Application;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends HollerActivity implements LoginHandler {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserLocalStorage.saveAccessKey(this, null);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        this.binding.setHandler(this);


        int size = this.getResources().getDisplayMetrics().widthPixels;
        int h = this.getResources().getDisplayMetrics().heightPixels;

        this.binding.imageBackground.getLayoutParams().height = h;
        this.binding.imageLogo.getLayoutParams().width = size / 2;
        this.binding.imageLogo.getLayoutParams().height = size / 2;

        this.binding.btnLogin.getLayoutParams().width = size / 2;
        this.binding.viewPlaceHolder.getLayoutParams().height = size / 8;

        this.binding.editUsername.setText("phong.nguyen@rainmaker-labs.com");
        this.binding.editPassword.setText("Ph0ng*120693");

    }


    @Override
    public void onLoginClick(View view) {
        if (this.binding.editUsername.testValidity()
                || this.binding.editPassword.testValidity()) {
            this.getLoading().show();
            JsonObject body = new JsonObject();
            body.addProperty("email", binding.editUsername.getText().toString());
            body.addProperty("password", binding.editPassword.getText().toString());
            this.makeRequest(mNetwork.login(body), 1);
        }
    }

    @Override
    public void onResponse(Call<JsonElement> call, Response<JsonElement> response, int requestCode) {

        if (requestCode == 1) {
            if (response.code() == 200) {
                String key = response.body().getAsJsonObject().get("key").getAsString();
                UserLocalStorage.saveAccessKey(this, key);

                this.makeRequest(mNetwork.applications(), 2);

            } else {
                this.showToastMessage(this.getString(R.string.error_login));
            }
        } else if (requestCode == 2) {
            if (response.code() == 200) {
                Application app = Application.parseArray(response.body().getAsJsonObject().get("apps")).get(0);
                UserLocalStorage.saveApplication(this, app);
                this.openRegisterSubscribe(app);
            }
        }

        this.hideLoading();
    }
}
