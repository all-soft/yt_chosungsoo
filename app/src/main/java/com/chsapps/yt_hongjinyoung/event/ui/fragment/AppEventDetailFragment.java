package com.chsapps.yt_hongjinyoung.event.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.APIUtils;
import com.chsapps.yt_hongjinyoung.api.model.response.BaseAPIData;
import com.chsapps.yt_hongjinyoung.constants.APIConstants;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.event.AppEventConstants;
import com.chsapps.yt_hongjinyoung.event.data.AppEventData;
import com.chsapps.yt_hongjinyoung.event.ui.popup.AppEventEnterPopup;
import com.chsapps.yt_hongjinyoung.ui.base.BaseFragment;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class AppEventDetailFragment extends BaseFragment {
    public final static String TAG = AppEventDetailFragment.class.getSimpleName();

    private AppEventData eventData;

    @BindView(R.id.layer_buttons)
    ViewGroup layer_buttons;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.tv_title)
    TextView tv_title;

    public static AppEventDetailFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        AppEventDetailFragment fragment = new AppEventDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AppEventDetailFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_app_event_detail, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void initialize() {
        setBackKey(true);
        setTitle("이벤트 상세");
        initLayout();
    }

    @Override
    public void clearMemory() {
    }

    @Override
    public boolean onBackPressed() {
        LogUtil.e("HSSEO", "onBackPressed 1");
        if (webview.canGoBack()) {
            LogUtil.e("HSSEO", "onBackPressed canGoBack O");
            webview.goBack();
            return true;
        } else {
            LogUtil.e("HSSEO", "onBackPressed canGoBack X");
            return super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initLayout() {

        eventData = getArguments().getParcelable(AppEventConstants.PARAM_EVENT_DATA);
        if (eventData == null) {
            parentActivity.finish();
            return;
        }
        if (eventData.getType() == 1) {
            layer_buttons.setVisibility(View.VISIBLE);
        } else {
            layer_buttons.setVisibility(View.GONE);
        }

        tv_title.setText(eventData.getTitle());

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        webview.setWebViewClient(new WebViewClientClass());
        webview.loadUrl(eventData.getDetail_url());
    }

    private class WebViewClientClass extends WebViewClient {//페이지 이동

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.e("WEBVIEW", url);
            view.loadUrl(url);
            return true;
        }
    }

    @OnClick(R.id.btn_close)
    void onClick_btn_close() {
        parentActivity.finish();
    }

    @OnClick(R.id.btn_enter_event)
    void onClick_btn_enter_event() {
        AppEventEnterPopup dlg = new AppEventEnterPopup(parentActivity, new AppEventEnterPopup.AppEventEnterPopupListener() {
            @Override
            public void onClickEnter(String name, String phone) {
                requestRegisterEvent(name, phone);
            }
        });
        dlg.show();
    }

    @OnClick(R.id.btn_check_event)
    void onClick_btn_check_event() {
        webview.loadUrl(eventData.getCheck_url());
    }

    @OnClick({R.id.tv_share, R.id.btn_kakao})
    void onClick_shareButton() {
        String title = eventData.getShare_title();
        String message = eventData.getShare_msg();
        Utils.showKakaoShare(parentActivity, title, message);
    }

    private void requestRegisterEvent(String name, String phone) {
        if (Utils.unableRequestAPI(parentActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_app_event_register(
                        APIConstants.API_KEY,
                        Utils.getLanguage(),
                        Constants.APPID,
                        0,
                        "",
                        eventData.getEvent_idx(),
                        name, phone,
                        eventData.getLayout())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new Consumer<BaseAPIData>() {
                    @Override
                    public void accept(BaseAPIData response) throws Exception {
                        if (response != null && response.isSuccess()) {
                            Toast.makeText(parentActivity, "응모에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }
}
