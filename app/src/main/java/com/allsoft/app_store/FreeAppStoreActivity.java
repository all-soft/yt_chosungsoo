package com.allsoft.app_store;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.ui.base.BaseActivity;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;

public class FreeAppStoreActivity extends BaseActivity {
    public final static String TAG = FreeAppStoreActivity.class.getSimpleName();

    @BindView(R.id.layer_banner_ad)
    RelativeLayout layer_banner_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void initialize() {
        setDoubleBackToExit(false);
        replace(R.id.layout_main, FreeAppStoreFragment.newInstance(getIntent().getExtras()), FreeAppStoreFragment.TAG, false, false);
    }

    @Override
    protected void clearMemory() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
