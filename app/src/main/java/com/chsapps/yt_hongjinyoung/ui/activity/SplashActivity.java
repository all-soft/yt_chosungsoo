package com.chsapps.yt_hongjinyoung.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.allsoft.notification.NotificationUtils;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.APIUtils;
import com.chsapps.yt_hongjinyoung.api.model.response.CategoryListData;
import com.chsapps.yt_hongjinyoung.api.model.response.HomeData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.APIConstants;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.data.CategoryData;
import com.chsapps.yt_hongjinyoung.db.GenreLastClickDBHelper;
import com.chsapps.yt_hongjinyoung.event.AppEventManager;
import com.chsapps.yt_hongjinyoung.event.data.AppNoticeAPIData;
import com.chsapps.yt_hongjinyoung.service.TokenAPIService;
import com.chsapps.yt_hongjinyoung.ui.base.BaseActivity;
import com.chsapps.yt_hongjinyoung.ui.view.popup.CommonPopup;
import com.chsapps.yt_hongjinyoung.utils.DelayListenerListener;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();

    private static final int DELAY_TIME = 2000;
    private static final int REQ_PERMISSION_CODE = 1000;

    private int showNoticeIndex = 0;
    private boolean isSetLastClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initialize() {
        try {
            Intent i = new Intent(this, TokenAPIService.class);
            i.setAction(TokenAPIService.ACTION_TYPE_TOKEN);
            startService(i);
        } catch (Exception e) {
        }

        Utils.delay(subscription, 1000, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                requestHome();
            }
        });
    }

    @Override
    protected void clearMemory() {
        if (subscription != null)
            subscription.dispose();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void moveNext() {
        //앱 삭제.
        HomeData.VERSION_INFO version_info = Global.getInstance().getVersionInfo();


        if (version_info != null && version_info.isRemovedApplication() && !TextUtils.isEmpty(version_info.getAppstore_url())) {
            Intent intent = new Intent(SplashActivity.this, RemovedAppActivity.class);
            intent.putExtra("PARAM_MARKET_URL", version_info.getAppstore_url());
            startActivity(intent);

            finish();
        } else {
            //강제업데이트?
            String minVersion = Global.getInstance().getMinimumVName();
            if (!TextUtils.isEmpty(minVersion)) {
                boolean isShowDlg = false;
                String[] version = Utils.getAppVersion().split("\\.");
                String[] min = minVersion.split("\\.");

                for (int i = 0; i < version.length; i++) {
                    try {
                        int cNum = Integer.parseInt(version[i]);
                        int mNum = Integer.parseInt(min[i]);
                        if (cNum < mNum) {
                            isShowDlg = true;
                            break;
                        }
                    } catch (Exception e) {
                    }
                }

                if (isShowDlg) {
                    showForceUpdateDlg();
                    return;
                }
            }
            //선택업데이트?

            //공지사항?
            List<HomeData.NOTICE> listNotice = Global.getInstance().getNoticeInfo();
            if (listNotice != null && listNotice.size() > 0 && listNotice.size() < showNoticeIndex) {
                showNoticeDlg(listNotice.get(showNoticeIndex));
                return;
            }

            Global.getInstance().mapCategoryIdLastClickTime = GenreLastClickDBHelper.getInstance().getAllData();
            if(Global.getInstance().mapCategoryIdLastClickTime == null || Global.getInstance().mapCategoryIdLastClickTime.size() == 0) {
                Global.getInstance().isNewGenreTypeSinger = Global.getInstance().isNewGenreTypeGenre = false;
                isSetLastClick = true;
            }
            requestGenreTypeGenre();

        }
    }

    private void requestHome() {
        if (Utils.unableRequestAPI(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_home(APIConstants.API_KEY, Utils.getLanguage(), Constants.APPID, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new Consumer<HomeData>() {
                    @Override
                    public void accept(HomeData homeData) throws Exception {
                        if (homeData != null) {
                            if (homeData.isSuccess()) {
                                Global.getInstance().setHomeData(homeData);
                                requestNotice();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void requestNotice() {
        if (Utils.unableRequestAPI(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_app_notice(APIConstants.API_KEY, Constants.APPID, Utils.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        moveNext();
                    }
                })
                .subscribe(new Consumer<AppNoticeAPIData>() {
                    @Override
                    public void accept(AppNoticeAPIData appNoticeAPIData) throws Exception {
                        if (appNoticeAPIData != null) {
                            if (appNoticeAPIData.isSuccess()) {
                                AppEventManager.getInstance().setAppNoticeAPIData(appNoticeAPIData.message);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }


    private void showForceUpdateDlg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_force_update);
        builder.setMessage(R.string.message_force_update);
        builder.setPositiveButton(R.string.button_force_update,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MARKET_URL)));
                    }
                });
        builder.show();
    }

    private void showNoticeDlg(HomeData.NOTICE data) {
        CommonPopup popup = new CommonPopup(this);
        popup.setTitleString(data.getTitle())
                .setMessageString(data.getContents())
                .setBtn_positive(true, R.string.Done)
                .setBtn_negative(false, null)
                .setActionListener(new CommonPopup.CommonPopupActionListener() {
                    @Override
                    public void onActionPositiveBtn() {
                        showNoticeIndex++;
                        moveNext();
                    }

                    @Override
                    public void onActionNegativeBtn() {

                    }
                })
                .show();
    }

    private void requestGenreTypeGenre() {

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_category(APIConstants.API_KEY, Utils.getLanguage(), Constants.APPID, Constants.CATEGORY_MAX, 1, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        requestGenreTypeSinger();
                    }
                })
                .subscribe(new Consumer<CategoryListData>() {
                    @Override
                    public void accept(CategoryListData response) throws Exception {
                        if (response != null) {
                            if (response.isSuccess()) {
                                if(isSetLastClick) {
                                    for(CategoryData categoryData : response.message) {
                                        Global.getInstance().mapCategoryIdLastClickTime.put(String.valueOf(categoryData.category_idx), categoryData.update_at * 1000);
                                    }
                                } else {
                                    int cnt = 0;
                                    for(CategoryData categoryData : response.message) {
                                        if(Global.getInstance().mapCategoryIdLastClickTime.containsKey(String.valueOf(categoryData.category_idx))) {
                                            if(categoryData.update_at * 1000 > Global.getInstance().mapCategoryIdLastClickTime.get(String.valueOf(categoryData.category_idx))) {
//                                                Global.getInstance().isNewGenreTypeGenre = true;
//                                                break;
                                                cnt++;
                                            }
                                        }
                                    }
                                    if(cnt > 0) {
                                        Global.getInstance().isNewGenreTypeGenre = true;
                                        Global.getInstance().cntNewGenreTypeGenre = cnt;
                                    }
                                }

                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void requestGenreTypeSinger() {
        subscription.add(APIUtils.getInstanse().getApiService()
                .request_category(APIConstants.API_KEY, Utils.getLanguage(), Constants.APPID, Constants.CATEGORY_MAX, 2, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .subscribe(new Consumer<CategoryListData>() {
                    @Override
                    public void accept(CategoryListData response) throws Exception {
                        if (response != null) {
                            if (response.isSuccess()) {
                                if(response.message.size() == 0) {
                                    Global.getInstance().isHaveNotSinger = true;
                                }
                                if(isSetLastClick) {
                                    for(CategoryData categoryData : response.message) {
                                        Global.getInstance().mapCategoryIdLastClickTime.put(String.valueOf(categoryData.category_idx), categoryData.update_at * 1000);
                                    }
                                } else {
                                    int cnt = 0;
                                    for (CategoryData categoryData : response.message) {
                                        if (Global.getInstance().mapCategoryIdLastClickTime.containsKey(String.valueOf(categoryData.category_idx))) {
                                            if (categoryData.update_at * 1000 > Global.getInstance().mapCategoryIdLastClickTime.get(String.valueOf(categoryData.category_idx))) {
//                                                Global.getInstance().isNewGenreTypeSinger = true;
//                                                break;
                                                cnt++;
                                            }
                                        }

                                        if(cnt > 0) {
                                            Global.getInstance().isNewGenreTypeSinger = true;
                                            Global.getInstance().cntNewGenreTypeSinger = cnt;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }
}
