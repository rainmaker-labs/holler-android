package com.rainmakerlabs.holler.demo.register;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.rainmakerlabs.holler.demo.HollerActivity;
import com.rainmakerlabs.holler.demo.R;
import com.rainmakerlabs.holler.demo.UserLocalStorage;
import com.rainmakerlabs.holler.demo.databinding.RegisterSubscriberActivityBinding;
import com.rainmakerlabs.holler.demo.model.AbstractModel;
import com.rainmakerlabs.holler.demo.model.Application;
import com.rainmakerlabs.holler.demo.model.ErrorMessage;
import com.rainmakerlabs.holler.demo.model.Subscriber;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class RegisterSubscriberActivity extends HollerActivity implements RegisterSubscriberHandler, CountryPickerListener {

    private final int REGISTER_CODE = 1;
    private final int UPDATE_CODE = 2;
    private final int GET_ALL_CODE = 3;

    private RegisterSubscriberActivityBinding binding;

    private CountryPicker picker;

    private Subscriber subscriber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.picker = CountryPicker.newInstance(this.getString(R.string.country));

        this.binding = DataBindingUtil.setContentView(this, R.layout.register_subscriber_activity);

        Country country = this.picker.getUserCountryInfo(this);

        if (country.getCode().equals("AF")) {
            country = new Country();
            country.setCode("SG");
            country.setDialCode("+65");
        }

        this.subscriber = this.getIntent().getParcelableExtra("subscriber");

        if (subscriber == null) {
            this.subscriber = new Subscriber();
            this.subscriber.setFirstName("Thanh Tri TRAN");
            this.subscriber.setEmail("tri.tranthanh@rainmaker-labs.com");
            this.subscriber.setLocalPhone("903670967");
            this.subscriber.setCountryCode(country.getDialCode());
            this.subscriber.setCountry(country.getCode());
        } else {
            binding.btnSubscribe.setText(R.string.update_information);
        }


        int size = this.getResources().getDisplayMetrics().widthPixels;


        this.binding.setHandler(this);
        this.binding.setActivity(this);
        this.binding.setSubscriber(this.subscriber);

        binding.imageLogo.getLayoutParams().width = size / 2;

        binding.btnSubscribe.getLayoutParams().width = (int) (size * 0.7);


    }

    @Override
    public void onCountryCodeClick(View view) {

        picker.show(this.getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(this);
    }

    @Override
    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {

        binding.tvCountryCode.setText(dialCode);
        this.subscriber.setCountry(code);
        picker.dismiss();
        this.hideKeyBoard();

    }

    @Override
    public void onRegisterClick(View view) {

        if (binding.etFullName.testValidity()
                && binding.etPhoneNumber.testValidity()
                && binding.etEmail.testValidity()) {

            this.getLoading().show();

            this.subscriber.setUsername(binding.etEmail.getText().toString());
            this.subscriber.setEmail(binding.etEmail.getText().toString());
            this.subscriber.setFirstName(binding.etFullName.getText().toString());
            this.subscriber.setCountryCode(binding.tvCountryCode.getText().toString());
            this.subscriber.setLocalPhone(binding.etPhoneNumber.getText().toString());
            this.subscriber.setGender(binding.groupGender.getCheckedRadioButtonId() == R.id.radio_male ? "male" : "female");
            this.subscriber.setDeviceToken(UserLocalStorage.getDeviceToken(this));

            if (this.subscriber.isNew()) {
                this.makeRequest(mNetwork.registerSubscriber(this.subscriber.toJsonObject()), REGISTER_CODE);
            } else {
                this.makeRequest(mNetwork.updateSubscriber(this.subscriber.getId(), this.subscriber.toJsonObject()), UPDATE_CODE);
            }

        }
    }

    @Override
    public void onResponse(Call<JsonElement> call, Response<JsonElement> response, int requestCode) {

        if (requestCode == REGISTER_CODE) {

            if (response.code() == 201) {
                this.subscriber.setId(response.body().getAsJsonObject().get("id").getAsInt());
                this.openRegisterSuccessful(this.subscriber);
                this.hideLoading();
            } else {

                try {
                    JsonObject json = AbstractModel.getGson().fromJson(response.errorBody().string(), JsonObject.class);
                    List<ErrorMessage> errors = ErrorMessage.parseArray(json.get("errors").getAsJsonArray());
                    for (ErrorMessage error : errors) {
                        if ("Username is unique".equals(error.getMessage())) {
                            this.showToastMessage("This subscriber is existed");

//                            this.makeRequest(mNetwork.subscribers(), GET_ALL_CODE);


                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GET_ALL_CODE) {

            for (Subscriber subscriber : Subscriber.parseArray(response.body())) {
                if (subscriber.getEmail().equals(this.subscriber.getEmail())) {
                    this.subscriber.setId(subscriber.getId());
                    binding.btnSubscribe.setText(R.string.update_information);
                    break;
                }
            }


            this.hideLoading();
        } else if (requestCode == UPDATE_CODE) {
            this.hideLoading();
        }


    }
}
