package com.chsapps.yt_nahoonha.ui.view.popup;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.AllSoft;

import butterknife.OnClick;

public class AutoClosePopup extends BaseDialog {

    private final static String TAG = AutoClosePopup.class.getSimpleName();

    private Context context;

    public AutoClosePopup(Context context) {
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
        setContentView(R.layout.view_destroy_timer);
    }

    @OnClick(R.id.btn_close)
    public void onClick_btn_close() {
        dismiss();
    }

    @OnClick({R.id.btn_30_min, R.id.btn_1_hour, R.id.btn_2_hour, R.id.btn_4_hour, R.id.btn_6_hour})
    public void onClick_Btn(View view) {
        String message = "";
        int msTime = -1;
        switch (view.getId()) {
            case R.id.btn_30_min:
                message = "30분 후에 종료 됩니다.";
                msTime = 1000 * 60 * 30;
//                msTime = 1000 * 30;
                break;
            case R.id.btn_1_hour:
                message = "1시간 후에 종료 됩니다.";
                msTime = 1000 * 60 * 60;
                break;
            case R.id.btn_2_hour:
                message = "2시간 후에 종료 됩니다.";
                msTime = 1000 * 60 * 120;
                break;
            case R.id.btn_4_hour:
                message = "4시간 후에 종료 됩니다.";
                msTime = 1000 * 60 * 240;
                break;
            case R.id.btn_6_hour:
                message = "6시간 후에 종료 됩니다.";
                msTime = 1000 * 60 * 360;
                break;
        }

        if(msTime > 0) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            AllSoft.SetClosePlayerTimer(msTime);
        }

        dismiss();
    }
}
