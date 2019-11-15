package com.chsapps.yt_hongjinyoung.ui.view.popup;

import android.content.Context;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import butterknife.OnClick;

public class RecommendedPopup extends BaseDialog {

    private final static String TAG = RecommendedPopup.class.getSimpleName();

    private Context context;

    public RecommendedPopup(Context context) {
        super(context, true, null);
        this.context = context;
        initialize();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initialize() {
        setContentView(R.layout.view_recommended_popup);
    }

    @OnClick(R.id.btn_share)
    public void onClick_btn_share() {
        Utils.showShare(context, Utils.getString(R.string.title_share_application), Utils.getString(R.string.message_share_application));
    }

    @OnClick(R.id.btn_negative)
    public void onClick_btn_negative() {
        dismiss();
    }
}
