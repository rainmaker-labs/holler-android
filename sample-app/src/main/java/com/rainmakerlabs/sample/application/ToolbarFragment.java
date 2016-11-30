package com.rainmakerlabs.sample.application;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rainmakerlabs.sample.application.ui.ToolbarWrapper;

/**
 * Created by thanhtritran on 17/10/16.
 */

public abstract class ToolbarFragment extends AppBaseFragment implements ToolbarWrapper.OnButtonClickListener {

    protected ToolbarWrapper toolbar;

    protected CoordinatorLayout view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.view = (CoordinatorLayout) inflater.inflate(R.layout.base_fragment, container, false);

        this.toolbar = ToolbarWrapper.newInstance((Toolbar) this.view.findViewById(R.id.toolbar));

        this.toolbar.setOnButtonClickListener(this);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) this.view.findViewById(R.id.content_fragment);

        coordinatorLayout.addView(this.onCreateSubView(inflater, coordinatorLayout, savedInstanceState), CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);

        this.onFinishInflate(this.view, savedInstanceState);

        return this.view;
    }

    public void onFinishInflate(CoordinatorLayout root, @Nullable Bundle savedInstanceState) {

    }

    public abstract View onCreateSubView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onLeftButtonClick() {
        this.activity.onBackPressed();
    }

    @Override
    public void onRightButtonClick() {

    }
}
