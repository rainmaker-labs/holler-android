package sample.sdk.holler.rainmakerlabs.com.hollersdk.validator;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;

import sample.sdk.holler.rainmakerlabs.com.hollersdk.R;

/**
 * Created by vinh.phamtien on 12/8/2016.
 */

public class BirthdayValidator extends Validator {
    public BirthdayValidator(Context context) {
        super(context.getString(R.string.error_date_of_birth_empty));
    }

    @Override
    public boolean isValid(EditText et) {
        return !TextUtils.isEmpty(et.getText()) && !TextUtils.isEmpty(et.getText().toString().trim());
    }
}
