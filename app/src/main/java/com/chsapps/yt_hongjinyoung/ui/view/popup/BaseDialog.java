package com.chsapps.yt_hongjinyoung.ui.view.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseDialog extends Dialog {

    private final static String TAG = BaseDialog.class.getSimpleName();

    private Context context;
    private Unbinder unbinder;

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        init();
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getWindow() != null) {
            getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
