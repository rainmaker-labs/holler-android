package com.rainmakerlabs.sample.application.users.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rainmakerlabs.sample.application.R;
import com.rainmakerlabs.sample.application.ToolbarFragment;
import com.rainmakerlabs.sample.application.databinding.UserDetailsViewBinding;
import com.rainmakerlabs.sample.application.users.model.User;
import com.rainmakerlabs.sample.application.users.presenter.UserPresenter;

/**
 * Created by thanhtritran on 18/10/16.
 */

public class UserDetailsFragment extends ToolbarFragment implements UserPresenter {

    private User user;

    private UserDetailsViewBinding binding;


    public static Fragment newInstance(Context context, User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", user);
        return Fragment.instantiate(context, UserDetailsFragment.class.getName(), bundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.user = this.getArguments().getParcelable("data");
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.toolbar.setTitle("User details");
    }

    @Override
    public View onCreateSubView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.binding = DataBindingUtil.inflate(inflater, R.layout.user_details_view, container, false);

        this.binding.setPresenter(this);

        binding.setUser(this.user);

        binding.imvProfile.display(this.user.getImage());

        int size = this.getResources().getDisplayMetrics().widthPixels / 2;

        binding.imvProfile.getLayoutParams().width = size;
        binding.imvProfile.getLayoutParams().height = size;

        return binding.getRoot();
    }

    @Override
    public void onProfileClick(View view, User user) {
        Snackbar.make(this.view, user.getEmail(), Snackbar.LENGTH_SHORT).show();
    }
}
