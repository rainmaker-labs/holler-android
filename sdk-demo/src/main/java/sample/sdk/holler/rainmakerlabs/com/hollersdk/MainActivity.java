package sample.sdk.holler.rainmakerlabs.com.hollersdk;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rainmakerlabs.holler.sdk.HollerSDK;
import com.rainmakerlabs.holler.sdk.subscriber.SubscriberCallback;
import com.rainmakerlabs.holler.sdk.subscriber.SubscribersCallback;
import com.rainmakerlabs.holler.sdk.subscriber.Subscriber;
import com.rainmakerlabs.holler.sdk.subscriber.TotalCallback;
import com.rainmakerlabs.rainrecyclerview.DividerItemDecoration;
import com.rainmakerlabs.rainrecyclerview.RainAdapter;
import com.rainmakerlabs.rainrecyclerview.RainViewHolder;

import java.util.ArrayList;
import java.util.List;

import sample.sdk.holler.rainmakerlabs.com.hollersdk.databinding.ActivityMainBinding;
import sample.sdk.holler.rainmakerlabs.com.hollersdk.register.RegisterSubscriberActivity;

public class MainActivity extends HollerActivity implements SubscribersCallback {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.binding.setActivity(this);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        this.refresh();
    }

    private void refresh() {
        this.getLoading().show();
        Subscriber.list(this, this);
        Subscriber.total(new TotalCallback() {
            @Override
            public void onSuccess(int total) {
                binding.txtTitle.setText("Total subscriber: " + total);
            }
        }, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            this.refresh();
        }
    }

    @Override
    public void onSuccess(ArrayList<Subscriber> subscribers) {
        this.hideLoading();
        this.binding.recyclerView.setAdapter(new SubscriberAdapter(subscribers));
    }

    class SubscriberAdapter extends RainAdapter<Subscriber> {

        private List<Subscriber> items;

        public SubscriberAdapter(List<Subscriber> items) {
            this.items = items;
        }

        @Override
        public Subscriber getItem(int position) {
            return this.items.get(position);
        }

        @Override
        public RainViewHolder<Subscriber> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SubscriberViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subscriber_item, parent, false));
        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }
    }

    class SubscriberViewHolder extends RainViewHolder<Subscriber> {

        private TextView text1, text2, text3;

        public SubscriberViewHolder(View itemView) {
            super(itemView);
            this.text1 = (TextView) itemView.findViewById(R.id.text1);
            this.text2 = (TextView) itemView.findViewById(R.id.text2);
            this.text3 = (TextView) itemView.findViewById(R.id.text3);
        }

        @Override
        public void onItemClick(Subscriber item, int position) {
            openUpdateSubscriber(item);
        }

        @Override
        public void bindData(Subscriber item, int position) {
            this.text1.setText(item.getFirstName());
            this.text2.setText(item.getPhone());
            this.text3.setText(item.getFormattedDateOfBirth());
        }
    }

    public void registerSubscriber(View view) {
        Intent intent = new Intent(this, RegisterSubscriberActivity.class);
        this.startActivityForResult(intent, 100);
    }

    @Override
    public void openUpdateSubscriber(Subscriber subscriber) {
        Subscriber.get(subscriber.getId(), new SubscriberCallback() {
            @Override
            public void onSuccess(Subscriber subscriber) {
                hideLoading();
                Intent intent = new Intent(MainActivity.this, RegisterSubscriberActivity.class);
                intent.putExtra("subscriber", subscriber);
                startActivityForResult(intent, 100);
            }
        }, this);
    }
}
