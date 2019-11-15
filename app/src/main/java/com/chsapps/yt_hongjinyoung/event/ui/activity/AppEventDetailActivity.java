package com.chsapps.yt_hongjinyoung.event.ui.activity;

import android.os.Bundle;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.event.ui.fragment.AppEventDetailFragment;
import com.chsapps.yt_hongjinyoung.ui.base.BaseActivity;

public class AppEventDetailActivity extends BaseActivity {
    public final static String TAG = AppEventDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_event_detail);
    }

    @Override
    protected void initialize() {
        setDoubleBackToExit(false);
        replace(R.id.layout_main, AppEventDetailFragment.newInstance(getIntent().getExtras()), AppEventDetailFragment.TAG, false, false);
    }

    @Override
    protected void clearMemory() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
