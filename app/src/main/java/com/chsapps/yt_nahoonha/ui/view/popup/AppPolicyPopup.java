package com.chsapps.yt_nahoonha.ui.view.popup;

import android.content.Context;

import com.chsapps.yt_nahoonha.R;

import butterknife.OnClick;

public class AppPolicyPopup extends BaseDialog {

    private final static String TAG = AppPolicyPopup.class.getSimpleName();

    private Context context;

    public AppPolicyPopup(Context context) {
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
        setContentView(R.layout.view_app_policy_dlg);
    }

    @OnClick(R.id.btn_done)
    public void onClick_btn_done() {
        dismiss();
    }
}
