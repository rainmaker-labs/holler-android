package com.rainmakerlabs.sample.application.ui;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rainmakerlabs.sample.application.R;


/**
 * Created by thanhtritran on 10/4/16.
 */

public class ToolbarWrapper implements View.OnClickListener {

    private Toolbar toolbar;

    private ImageButton btnLeft;

    private ImageButton btnRight;

    private TextView tvTitle;

    private OnButtonClickListener onButtonClickListener;

    public static final ToolbarWrapper newInstance(Toolbar toolbar) {
        return new ToolbarWrapper(toolbar);
    }

    private ToolbarWrapper(Toolbar toolbar) {
        this.toolbar = toolbar;
        this.btnLeft = (ImageButton) toolbar.findViewById(R.id.btn_left);
        this.btnRight = (ImageButton) toolbar.findViewById(R.id.btn_right);
        this.tvTitle = (TextView) toolbar.findViewById(R.id.tv_title);

        this.btnLeft.setOnClickListener(this);
        this.btnRight.setOnClickListener(this);
    }




    public ToolbarWrapper setTitle(Spanned title) {
        this.tvTitle.setText(title);
        return this;
    }

    public ToolbarWrapper setTitle(CharSequence title) {
        this.tvTitle.setText(title);
        return this;
    }

    public ToolbarWrapper setTitle(@StringRes int resId) {
        this.tvTitle.setText(resId);
        return this;
    }


    public ToolbarWrapper setLeftButtonImageRes(@DrawableRes int resId) {
        this.btnLeft.setImageResource(resId);
        return this;
    }

    public ToolbarWrapper setRightButtonImageRes(@DrawableRes int resId) {
        this.btnRight.setImageResource(resId);
        return this;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public void onClick(View view) {
        if (this.onButtonClickListener != null) {
            if (view.getId() == this.btnLeft.getId()) {
                this.onButtonClickListener.onLeftButtonClick();
            } else {
                this.onButtonClickListener.onRightButtonClick();
            }
        }
    }

    public interface OnButtonClickListener {

        void onLeftButtonClick();

        void onRightButtonClick();
    }


}
