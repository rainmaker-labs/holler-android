package com.rainmakerlabs.holler.demo.dialog;

import android.content.Context;

import com.rainmakerlabs.ui.dialog.blur.BlurLoadingDialog;

/**
 * Created by thanhtritran on 30/11/16.
 */

public class LoadingDialog extends BlurLoadingDialog {
    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    public boolean blur() {
        return false;
    }

    @Override
    public float getDimAmount() {
        return 0.7f;
    }
}
