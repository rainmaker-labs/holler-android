package com.rainmakerlabs.holler.demo.validator;

import android.content.Context;
import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;
import com.rainmakerlabs.holler.demo.R;

/**
 * Created by thanhtritran on 1/12/16.
 */

public class PhoneMinLengthValidator extends Validator {
    public PhoneMinLengthValidator(Context context) {
        super(context.getString(R.string.error_cellphone_6));
    }

    @Override
    public boolean isValid(EditText et) {
        return et.getText().length() >= 6;
    }
}
