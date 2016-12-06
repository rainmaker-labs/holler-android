package com.rainmakerlabs.holler.demo.register;

import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.rainmakerlabs.holler.demo.HollerActivity;
import com.rainmakerlabs.holler.demo.R;
import com.rainmakerlabs.holler.demo.UserLocalStorage;
import com.rainmakerlabs.holler.demo.Utils.GPSServiceListener;
import com.rainmakerlabs.holler.demo.Utils.GPSTracker;
import com.rainmakerlabs.holler.demo.Utils.PermissionHelper;
import com.rainmakerlabs.holler.demo.databinding.RegisterSubscriberActivityBinding;
import com.rainmakerlabs.holler.demo.model.AbstractModel;
import com.rainmakerlabs.holler.demo.model.Application;
import com.rainmakerlabs.holler.demo.model.ErrorMessage;
import com.rainmakerlabs.holler.demo.model.Subscriber;
import com.rainmakerlabs.holler.demo.validator.PhoneMaxLengthValidator;
import com.rainmakerlabs.holler.demo.validator.PhoneMinLengthValidator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class RegisterSubscriberActivity extends HollerActivity implements RegisterSubscriberHandler, CountryPickerListener, GPSServiceListener, DatePickerDialog.OnDateSetListener {

    private final int REGISTER_CODE = 1;
    private final int UPDATE_CODE = 2;
    private final int GET_ALL_CODE = 3;
    private final int REQUEST_CODE_REQUEST_LOCATION_PERMISSION =101;

    private RegisterSubscriberActivityBinding binding;

    private CountryPicker picker;

    private Subscriber subscriber;
    private boolean isBound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.picker = CountryPicker.newInstance(this.getString(R.string.country));

        this.binding = DataBindingUtil.setContentView(this, R.layout.register_subscriber_activity);

        this.binding.etPhoneNumber.addValidator(new PhoneMinLengthValidator(this));
        this.binding.etPhoneNumber.addValidator(new PhoneMaxLengthValidator(this));

        Country country = this.picker.getUserCountryInfo(this);

        if (country.getCode().equals("AF")) {
            country = new Country();
            country.setCode("SG");
            country.setDialCode("+65");
        }



        this.subscriber = this.getIntent().getParcelableExtra("subscriber");

        if (subscriber == null) {
            this.subscriber = new Subscriber();
            this.subscriber.setFirstName("Vinh Pham Tien");
            this.subscriber.setEmail("vinh.phamtien@rainmaker-labs.com");
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
            Location lastKnowLoction= gpsTracker.getLastLocation();
            if(lastKnowLoction!=null){
                subscriber.getInfo().setGpsLatitude(lastKnowLoction.getLatitude());
                subscriber.getInfo().setGpsLongitude(lastKnowLoction.getLongitude());
            }

            if (this.subscriber.isNew()) {
                this.makeRequest(mNetwork.registerSubscriber(this.subscriber.toJsonObject()), REGISTER_CODE);
            } else {
                this.makeRequest(mNetwork.updateSubscriber(this.subscriber.getId(), this.subscriber.toJsonObject()), UPDATE_CODE);
            }

        }
    }

    @Override
    public void onDatePickerClick(View view) {
        Calendar today = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(RegisterSubscriberActivity.this, this, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMaxDate(today.getTimeInMillis());
        datePicker.show();
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
                        }else {
                            this.showToastMessage(String.valueOf(error.getMessage()));
                        }
                        this.hideLoading();
                        break;
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

    @Override
    public void onLocationChange(Location location) {
        Log.i("onLocationChange", location.getLatitude() + " : " + location.getLongitude());
        if(location!=null){
            subscriber.getInfo().setGpsLatitude(location.getLatitude());
            subscriber.getInfo().setGpsLongitude(location.getLongitude());
        }
    }

    private void checkLocationService() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        boolean gpsEnable = false, networkEnable = false;

        try {
            gpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gpsEnable && !networkEnable) {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            this.startActivity(intent);

        } else {
            Intent intent = new Intent(this, GPSTracker.class);

            this.bindService(intent, myConnection, BIND_AUTO_CREATE);
            this.getLoading().show();

        }
    }

    private GPSTracker gpsTracker;
    public ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (gpsTracker == null) {
                gpsTracker = ((GPSTracker.MyBinder) service).getService();
                gpsTracker.addGPSServiceListener(RegisterSubscriberActivity.this);
                gpsTracker.startGettingLocation();
            }
            isBound = true;
            RegisterSubscriberActivity.this.hideLoading();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(!PermissionHelper.hasLocationPermission(this)){
            PermissionHelper.requestLocationPermission(this, REQUEST_CODE_REQUEST_LOCATION_PERMISSION);
        }else{
            this.checkLocationService();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isBound) {
            this.unbindService(myConnection);
            if (gpsTracker != null) {
                gpsTracker.removeGPSServiceListener(RegisterSubscriberActivity.this);
            }
            isBound = false;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar dob = Calendar.getInstance();
        dob.set(Calendar.YEAR,year);
        dob.set(Calendar.MONTH,month);
        dob.set(Calendar.DAY_OF_MONTH,dayOfMonth);

//        String d = ISO8601Utils.format(new Date(dob.getTimeInMillis()),true);
        //please ignore the document, use this intead....
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String d = sdf.format(new Date(dob.getTimeInMillis()));
        subscriber.getInfo().setDateOfBirth(d);
        this.binding.setSubscriber(subscriber);

    }
}
