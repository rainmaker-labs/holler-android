package com.rainmakerlabs.sample.application.users.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rainmakerlabs.networking.model.NetworkResponse;
import com.rainmakerlabs.rainrecyclerview.RainAdapter;
import com.rainmakerlabs.rainrecyclerview.RainRecyclerView;
import com.rainmakerlabs.rainrecyclerview.RainViewHolder;
import com.rainmakerlabs.sample.application.R;
import com.rainmakerlabs.sample.application.ToolbarFragment;
import com.rainmakerlabs.sample.application.nav.UserNavigation;
import com.rainmakerlabs.sample.application.users.model.User;
import com.rainmakerlabs.support.ui.widget.RoundedImageView;
import com.rainmakerlabs.ui.dialog.ILoadingMessage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by thanhtritran on 17/10/16.
 */

public class UsersFragment extends ToolbarFragment {

    private final int USERS_REQUEST = 100;

    private RainRecyclerView recyclerView;

    private UserAdapter adapter;

    private List<User> items = new ArrayList<>();

    private UserNavigation userNavigation;

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context, UsersFragment.class.getName());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.userNavigation = (UserNavigation) context;
    }


    @Override
    public void onFinishInflate(CoordinatorLayout root, @Nullable Bundle savedInstanceState) {
        this.recyclerView = (RainRecyclerView) root.findViewById(R.id.recycler_view);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
    }

    @Override
    public View onCreateSubView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.toolbar.setTitle("List of user");

        this.getLoading().onShow(new ILoadingMessage.OnShowListener() {
            @Override
            public void onShow() {
                executeRequest(networkService.getUsers(1), USERS_REQUEST, true);
            }
        }).onCancel(new ILoadingMessage.OnCancelListener() {
            @Override
            public void onCancel() {
                cancelRequest(USERS_REQUEST);
            }
        }).show();

    }

    private Snackbar snackbar;

    @Override
    public void onRequestSuccessful(Call<NetworkResponse> call, Response<NetworkResponse> response, int requestCode, boolean cached) {

        if(cached){
            if(this.snackbar == null ){
                this.snackbar = Snackbar.make(this.view, "Offline mode", Snackbar.LENGTH_INDEFINITE);
            }
            this.snackbar.show();
        }else{
            if(this.snackbar != null ){
                this.snackbar.dismiss();
                this.snackbar = null;
            }

        }

        if (requestCode == USERS_REQUEST) {
            this.items.addAll(response.body().getArrayList(User.class));

            if (this.adapter == null) {
                this.adapter = new UserAdapter();
                this.recyclerView.setAdapter(this.adapter);
            } else {
                this.adapter.notifyDataSetChanged();
            }

        }

        this.hideLoading();
    }

    class UserAdapter extends RainAdapter<User> {
        @Override
        public User getItem(int position) {
            return items.get(position);
        }

        @Override
        public RainViewHolder<User> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }


    }

    class ViewHolder extends RainViewHolder<User> {

        TextView tvName, tvEmail, tvPhone;

        RoundedImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tvEmail = (TextView) view.findViewById(R.id.tv_email);
            this.tvPhone = (TextView) view.findViewById(R.id.tv_phone);
            this.imageView = (RoundedImageView) view.findViewById(R.id.image_view);
            int size = view.getContext().getResources().getDisplayMetrics().widthPixels / 4;
            this.imageView.getLayoutParams().width = size;
            this.imageView.getLayoutParams().height = size;
            this.imageView.setRadius(size / 10);
        }

        @Override
        public void bindData(User item, int position) {
            this.tvName.setText(item.getName());
            this.tvEmail.setText(item.getEmail());
            this.tvPhone.setText(item.getPhone());
            this.imageView.display(item.getImage());
        }

        @Override
        public void onItemClick(User item, int position) {
            userNavigation.openUserDetails(item);
        }
    }
}
