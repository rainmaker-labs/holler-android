package sample.sdk.holler.rainmakerlabs.com.hollersdk.register;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.rainmakerlabs.holler.sdk.HollerSDK;
import com.rainmakerlabs.holler.sdk.subscriber.Subscriber;
import com.rainmakerlabs.holler.sdk.subscriber.SubscriberCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sample.sdk.holler.rainmakerlabs.com.hollersdk.HollerActivity;
import sample.sdk.holler.rainmakerlabs.com.hollersdk.R;
import sample.sdk.holler.rainmakerlabs.com.hollersdk.databinding.RegisterSubscriberActivityBinding;
import sample.sdk.holler.rainmakerlabs.com.hollersdk.validator.FullNameValidator;
import sample.sdk.holler.rainmakerlabs.com.hollersdk.validator.PhoneMaxLengthValidator;
import sample.sdk.holler.rainmakerlabs.com.hollersdk.validator.PhoneMinLengthValidator;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class RegisterSubscriberActivity extends HollerActivity
        implements RegisterSubscriberHandler, CountryPickerListener,
        DatePickerDialog.OnDateSetListener {

    private final int REGISTER_CODE = 1;
    private final int UPDATE_CODE = 2;
    private final int GET_ALL_CODE = 3;
    private final int REQUEST_CODE_REQUEST_LOCATION_PERMISSION = 101;

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
        this.binding.etFullName.addValidator(new FullNameValidator(this));

//        this.binding.etDob.addValidator(new BirthdayValidator(this));

        Country country = this.picker.getUserCountryInfo(this);

        if (country.getCode().equals("AF")) {
            country = new Country();
            country.setCode("SG");
            country.setDialCode("+65");
        }

        this.subscriber = this.getIntent().getParcelableExtra("subscriber");

        if (subscriber == null) {
            this.subscriber = new Subscriber();
//            this.subscriber.setFirstName("Vinh Pham Tien");
//            this.subscriber.setEmail("vinh.phamtien@rainmaker-labs.com");
//            this.subscriber.setPhone("903670967");
            this.subscriber.setCountry(country.getCode());
        } else {
            binding.btnSubscribe.setText(R.string.update_information);
        }


        int size = this.getResources().getDisplayMetrics().widthPixels;


        this.binding.setHandler(this);
        this.binding.setSubscriber(this.subscriber);

        binding.imageLogo.getLayoutParams().width = size / 2;

        binding.btnSubscribe.getLayoutParams().width = (int) (size * 0.7);


        if (this.subscriber != null) {
            HollerSDK.appActive(this.subscriber.getId(), null, null);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.subscriber != null && !this.subscriber.isNew()) {
            HollerSDK.appTerminate(this.subscriber.getId(), null, null);
        }
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
                && binding.etDob.testValidity()
                && binding.etPhoneNumber.testValidity()
                && binding.etEmail.testValidity()) {

            this.getLoading().show();

            this.subscriber.setUsername(binding.etEmail.getText().toString());
            this.subscriber.setEmail(binding.etEmail.getText().toString());
            this.subscriber.setFirstName(binding.etFullName.getText().toString());
            this.subscriber.setPhone(binding.tvCountryCode.getText().toString() + binding.etPhoneNumber.getText().toString());
            this.subscriber.setGender(binding.groupGender.getCheckedRadioButtonId() == R.id.radio_male ? "male" : "female");
            this.subscriber.setDeviceToken(HollerSDK.getDeviceToken(this));
            this.subscriber.setLatitude(10.7960495);
            this.subscriber.setLongitude(106.6746586);
            this.subscriber.setAddress("173A Nguyen Van Troi, Phu Nhuan, Tp.HCM");

            if (this.subscriber.isNew()) {
                this.subscriber.register(new SubscriberCallback() {
                    @Override
                    public void onSuccess(Subscriber subscriber) {
                        hideLoading();
                        subscriber.events.register(null, null);
                        openRegisterSuccessful(subscriber);
                    }
                }, this);
            } else {
                this.subscriber.update(new SubscriberCallback() {
                    @Override
                    public void onSuccess(Subscriber subscriber) {
                        hideLoading();
                        subscriber.events.open(null, null);
                        openRegisterSuccessful(subscriber);
                    }
                }, this);
            }


        } else if (!binding.etDob.testValidity()) {
            Toast.makeText(this, getString(R.string.error_date_of_birth_empty), Toast.LENGTH_LONG).show();
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar dob = Calendar.getInstance();
        dob.set(Calendar.YEAR, year);
        dob.set(Calendar.MONTH, month);
        dob.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String d = sdf.format(dob.getTime());
        this.subscriber.setDateOfBirth(dob.getTime());
        if (!TextUtils.isEmpty(binding.etFullName.getText().toString())) {
            this.subscriber.setFirstName(binding.etFullName.getText().toString());
        }

        this.binding.etDob.setText(d);
        this.binding.setSubscriber(subscriber);

    }

    @Override
    public void openRegisterSuccessful(Subscriber subscriber) {
        this.setResult(Activity.RESULT_OK);
        this.finish();
    }


}
