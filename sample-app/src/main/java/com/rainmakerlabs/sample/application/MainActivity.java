package com.rainmakerlabs.sample.application;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openUsers( View view ){
        this.openUserList();
    }
}
