package com.chsapps.yt_nahoonha.ui.view.popup;

import android.content.Context;

import com.chsapps.yt_nahoonha.R;

import butterknife.OnClick;

public class PermissionGuidePopup extends BaseDialog {

    private final static String TAG = PermissionGuidePopup.class.getSimpleName();

    private Context context;

    public interface PermissionGuidePopupListener {
        public void onClickButton();
    }

    private PermissionGuidePopupListener listener;
    public PermissionGuidePopup(Context context) {
        super(context, false, null);
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
        setContentView(R.layout.view_permission_guide_popup);
    }

    @OnClick(R.id.btn_done)
    public void onClick_btn_done() {
        if(listener != null) {
            listener.onClickButton();
        }
        dismiss();
    }

    public void setListner(PermissionGuidePopupListener listner) {
        this.listener = listner;
    }
}
