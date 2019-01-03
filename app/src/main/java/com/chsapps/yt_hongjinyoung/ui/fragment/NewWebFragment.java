package com.chsapps.yt_hongjinyoung.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.common.BaseFragment;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;

import butterknife.BindView;

public class NewWebFragment extends BaseFragment {
    public final static String TAG = NewWebFragment.class.getSimpleName();

    @BindView(R.id.webview)
    WebView webview;

    public static NewWebFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        NewWebFragment fragment = new NewWebFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static NewWebFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_news_web, null);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initialize() {
        setBackKey(true);

        String title = getArguments().getString(ParamConstants.PARAM_NEWS_TITLE);
        LogUtil.e(TAG, "title : " + title);
        setTitle(Html.fromHtml(title).toString());
        String url = getArguments().getString(ParamConstants.PARAM_NEWS_URL);

        webview.setWebViewClient(new NewsWebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(url);
    }

    @Override
    public void clearMemory() {
    }

    @Override
    public boolean onBackPressed() {
        if(webview.canGoBack()) {
            webview.goBack();
            return false;
        }
        return super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        parentActivity.getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    class NewsWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
