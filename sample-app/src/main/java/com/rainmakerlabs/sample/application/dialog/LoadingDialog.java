package com.rainmakerlabs.sample.application.dialog;

import android.content.Context;

import com.rainmakerlabs.ui.dialog.blur.BlurLoadingDialog;
import com.rainmakerlabs.ui.dialog.blur.BlurMessageDialog;

/**
 * Created by thanhtritran on 17/10/16.
 */

public class LoadingDialog extends BlurLoadingDialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    public boolean blur() {
        return false;
    }
}
