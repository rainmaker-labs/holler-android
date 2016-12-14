package sample.sdk.holler.rainmakerlabs.com.hollersdk.validator;

import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;

/**
 * Created by thanhtritran on 1/12/16.
 */

public class PasswordValidator extends Validator {
    public PasswordValidator(String message) {
        super(message);
    }

    @Override
    public boolean isValid(EditText et) {
        return et.getText().toString().length() >= 6;
    }
}
