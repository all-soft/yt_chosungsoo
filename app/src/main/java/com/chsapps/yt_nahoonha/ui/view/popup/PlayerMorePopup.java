package com.chsapps.yt_nahoonha.ui.view.popup;

import android.content.Context;

import com.chsapps.yt_nahoonha.R;

import butterknife.OnClick;

public class PlayerMorePopup extends BaseDialog {

    private final static String TAG = PlayerMorePopup.class.getSimpleName();

    private Context context;
    private MorePopupListener listener;

    public interface MorePopupListener {
        void onClick_battery();
        void onClick_prev();
        void onClick_next();
        void onClick_favorite();
        void onClick_share();
    }

    public PlayerMorePopup(Context context, MorePopupListener listener) {
        super(context, false, null);
        this.context = context;
        this.listener = listener;
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
        setContentView(R.layout.view_more_popup);
    }


    @OnClick(R.id.btn_battery)
    public void onClick_btn_battery() {
        if(listener != null)
            listener.onClick_battery();
        dismiss();
    }
    @OnClick(R.id.btn_prev)
    public void onClick_btn_prev() {
        if(listener != null)
            listener.onClick_prev();
        dismiss();
    }
    @OnClick(R.id.btn_next)
    public void onClick_btn_next() {
        if(listener != null)
            listener.onClick_next();
        dismiss();
    }
    @OnClick(R.id.btn_favorite)
    public void onClick_btn_favorite() {
        if(listener != null)
            listener.onClick_favorite();
        dismiss();
    }
    @OnClick(R.id.btn_share)
    public void onClick_btn_share() {
        if(listener != null)
            listener.onClick_share();
        dismiss();
    }
    @OnClick(R.id.btn_close)
    public void onClick_btn_close() {
        dismiss();
    }

}
