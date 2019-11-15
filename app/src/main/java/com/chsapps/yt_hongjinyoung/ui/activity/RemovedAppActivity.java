package com.chsapps.yt_hongjinyoung.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.ui.base.BaseActivity;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class RemovedAppActivity extends BaseActivity {
    public final static String TAG = RemovedAppActivity.class.getSimpleName();

    @BindView(R.id.tv_comments)
    TextView tv_comments;
    @BindView(R.id.tv_comments1)
    TextView tv_comments1;
    private String market_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dlg_removed_app);
        market_url = getIntent().getStringExtra("PARAM_MARKET_URL");
    }

    @Override
    protected void initialize() {
        tv_comments.setText(Html.fromHtml(Utils.getString(R.string.removed_app_comment1)));
        tv_comments1.setText(Html.fromHtml(Utils.getString(R.string.removed_app_comment2)));
    }

    @Override
    protected void clearMemory() {
    }

    @OnClick(R.id.btn_close)
    public void onClick_btn_close() {
        finish();
    }

    @OnClick(R.id.btn_new)
    public void onClick_btn_new() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(market_url)));
    }

}
