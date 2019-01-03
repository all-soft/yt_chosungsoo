package com.chsapps.yt_hongjinyoung.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.chsapps.yt_hongjinyoung.ui.view.ViewUnbindHelper;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment extends Fragment implements FragmentWrapper {
    protected Context context;
    protected BaseActivity parentActivity;
    protected View mainView;
    protected Menu actionBarMenu;

    protected CompositeDisposable subscription = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (BaseActivity) getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Activity activity = (Activity) context;
        if (activity instanceof BaseActivity) {
            parentActivity = (BaseActivity) activity;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mainView = view;

        initialize();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (Exception e) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mainView != null) {
            ViewUnbindHelper.unbindReferences(mainView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        clearMemory();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        actionBarMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void back() {
        parentActivity.getSupportFragmentManager().popBackStack();
    }

    protected void back(String fragment) {
        parentActivity.getSupportFragmentManager().popBackStack(fragment, 0);
    }

    public void setTitle(String title) {
        parentActivity.setTitle(title);
    }

    public void setTitle(int title) {
        parentActivity.setTitle(title);
    }

    public void setBackKey(boolean is_show) {
        try {
            parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(is_show);
        } catch (Exception e) {
        }
    }

    public void showLoading() {
        parentActivity.showLoading();
    }

    public void dismissLoading() {
        parentActivity.dismissLoading();
    }

    public void replace(int mainViewId, Fragment fragment, String tag, boolean addToBackStack) {
        parentActivity.replace(mainViewId, fragment, tag, addToBackStack);
    }

    public void hideActionBar() {
        parentActivity.hideActionBar();
    }
    public void showActionBar() {
        parentActivity.showActionBar();
    }

    public void hideBannerAd() {
        parentActivity.hideBannerAd();
    }
    public void showBannerAd() {
        parentActivity.showBannerAd();
    }
}
