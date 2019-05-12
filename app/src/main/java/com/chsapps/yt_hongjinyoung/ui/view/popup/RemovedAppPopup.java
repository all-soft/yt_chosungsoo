package com.chsapps.yt_hongjinyoung.ui.view.popup;

import android.content.Context;

import com.chsapps.yt_hongjinyoung.R;

import butterknife.OnClick;

public class RemovedAppPopup extends BaseDialog {

    private final static String TAG = RemovedAppPopup.class.getSimpleName();

    private Context context;

    public interface OnRemovedAppPopupListener {
        void onClick_Closed();
        void onClick_MoveAppStore();
    }

    private OnRemovedAppPopupListener listener;

    public RemovedAppPopup(Context context) {
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
        setContentView(R.layout.view_dlg_app_removed);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void setListener(OnRemovedAppPopupListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.btn_close_application)
    public void onClick_btn_close_application() {
        if(listener != null)
            listener.onClick_Closed();
    }

    @OnClick(R.id.btn_update_application)
    public void onClick_btn_update_application() {
        if(listener != null)
            listener.onClick_MoveAppStore();
    }


}
