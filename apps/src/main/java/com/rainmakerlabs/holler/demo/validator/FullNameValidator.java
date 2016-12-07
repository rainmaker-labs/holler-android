package com.rainmakerlabs.holler.demo.validator;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;
import com.rainmakerlabs.holler.demo.R;

/**
 * Created by vinh.phamtien on 12/7/2016.
 */

public class FullNameValidator extends Validator {

    public FullNameValidator(Context context) {
        super(context.getString(R.string.error_fullname_less_than_two_words));
    }

    @Override
    public boolean isValid(EditText et) {
        String s = et.getText().toString();
        if(TextUtils.isEmpty(s)){
            return false;
        }else{
            String[]ss = s.split(" ");
            return ss.length > 1;
        }

    }
}
