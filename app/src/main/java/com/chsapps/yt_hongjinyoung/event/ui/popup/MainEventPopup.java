package com.chsapps.yt_hongjinyoung.event.ui.popup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.event.AppEventConstants;
import com.chsapps.yt_hongjinyoung.event.AppEventManager;
import com.chsapps.yt_hongjinyoung.event.data.AppEventData;
import com.chsapps.yt_hongjinyoung.event.service.ClickEventService;
import com.chsapps.yt_hongjinyoung.event.ui.activity.AppEventDetailActivity;
import com.chsapps.yt_hongjinyoung.ui.view.popup.BaseDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class MainEventPopup extends BaseDialog {

    private final static String TAG = MainEventPopup.class.getSimpleName();

    private Context context;

    public interface onMainEventPopupListener {
        void closePopup();
        void joinEvent();
    }

    private onMainEventPopupListener listener;

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.chb_donot_today)
    CheckBox chb_donot_today;


    public MainEventPopup(Context context, onMainEventPopupListener listener) {
        super(context, true, null);
        this.context = context;
        this.listener = listener;
        initialize();
    }

    @Override
    public void show() {
        if(AppEventManager.getInstance().isShowMainPageEventPopup()) {
            String eventUrl = AppEventManager.getInstance().getAppNoticeEventURL();
            webview.loadUrl(eventUrl);

            super.show();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }




    private void initialize() {
        setContentView(R.layout.view_main_event_popup);
        chb_donot_today.setChecked(false);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        webview.setWebViewClient(new WebViewClientClass());
        webview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    onClick_layer_webview();
                }
                return false;
            }

        });
    }

    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @OnClick(R.id.layer_webview)
    public void onClick_layer_webview() {
        AppEventData eventData = AppEventManager.getInstance().getNoticeData();
        {
            Intent intent = new Intent(context, ClickEventService.class).setAction(ClickEventService.ACTION_TYPE_CLICK_EVENT);
            intent.putExtra(AppEventConstants.PARAM_EVENT_DATA, AppEventManager.getInstance().getNoticeData());
            context.startService(intent);
        }
        if(eventData.type == 3) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(eventData.getDetail_url())));
        } else {
            Intent intent = new Intent(context, AppEventDetailActivity.class);
            intent.putExtra(AppEventConstants.PARAM_EVENT_DATA, eventData);
            context.startActivity(intent);
        }

        dismiss();
    }

    @OnClick(R.id.btn_donot_today)
    public void onClick_btn_donot_today() {
        chb_donot_today.setChecked(!chb_donot_today.isChecked());
    }

    @OnClick(R.id.btn_close)
    public void onClick_btn_close() {
        if(chb_donot_today.isChecked()) {
            AppEventManager.getInstance().setDoNotSeeTodaySetTime();
        }
//        context.startActivity(new Intent(context, AppEventDetailActivity.class));
        dismiss();
    }
}
