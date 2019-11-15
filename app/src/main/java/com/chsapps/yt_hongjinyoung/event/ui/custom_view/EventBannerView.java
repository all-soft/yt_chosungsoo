package com.chsapps.yt_hongjinyoung.event.ui.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.event.AppEventConstants;
import com.chsapps.yt_hongjinyoung.event.data.AppEventData;
import com.chsapps.yt_hongjinyoung.event.service.ClickEventService;
import com.chsapps.yt_hongjinyoung.event.ui.activity.AppEventDetailActivity;
import com.chsapps.yt_hongjinyoung.event.ui.adapter.EventBannerViewerAdapterListener;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EventBannerView extends RelativeLayout {

    private Context context;

    private LayoutInflater inflater;
    private Unbinder unbinder;
    private View mainView;

    @BindView(R.id.iv_radius)
    RadiusImageView iv_radius;

    private AppEventData eventData;
    private EventBannerViewerAdapterListener listener;

    public EventBannerView(Context context) {
        super(context);
        initialize(context);
    }

    public EventBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public EventBannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_event_banner_pager, this, true);
        unbinder = ButterKnife.bind(this, mainView);

        iv_radius.setRadius(0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void update(AppEventData eventData, EventBannerViewerAdapterListener listener) {
        this.eventData = eventData;
        this.listener = listener;
        Glide.with(context).load(eventData.getBanner()).into(iv_radius);
    }

    @OnClick(R.id.iv_radius)
    void onClick_iv_radius() {
        {
            Intent intent = new Intent(context, ClickEventService.class).setAction(ClickEventService.ACTION_TYPE_CLICK_EVENT);
            intent.putExtra(AppEventConstants.PARAM_EVENT_DATA, eventData);
            context.startService(intent);
        }
        if(eventData.type == 3) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(eventData.getDetail_url())));
        } else {
            Intent intent = new Intent(context, AppEventDetailActivity.class);
            intent.putExtra(AppEventConstants.PARAM_EVENT_DATA, eventData);
            context.startActivity(intent);
        }
        listener.selectBanner(eventData);
    }

    private class WebViewClientClass extends WebViewClient {//페이지 이동

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.e("WEBVIEW", url);
            view.loadUrl(url);
            return true;
        }
    }

}
