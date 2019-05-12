package com.chsapps.yt_hongjinyoung.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.RequestServiceListener;
import com.chsapps.yt_hongjinyoung.api.RequestUtils;
import com.chsapps.yt_hongjinyoung.api.model.BaseAPIData;
import com.chsapps.yt_hongjinyoung.api.model.HomeData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseActivity;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.data.SingersData;
import com.chsapps.yt_hongjinyoung.service.TokenAPIService;
import com.chsapps.yt_hongjinyoung.ui.view.popup.AppPolicyPopup;
import com.chsapps.yt_hongjinyoung.ui.view.popup.CommonPopup;
import com.chsapps.yt_hongjinyoung.utils.DelayListenerListener;
import com.chsapps.yt_hongjinyoung.utils.Utils;
import com.fingerpush.android.FingerPushManager;
import com.fingerpush.android.NetworkUtility;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

import java.util.List;

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();

    private static final int DELAY_TIME = 2000;

    private int showNoticeIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getIntent().getExtras() != null) {
            onNewIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getExtras() != null) {

            String msgTag = intent.getExtras().getString("msgTag");
            String mode = intent.getExtras().getString("mode");
            String lCode = intent.getExtras().getString("lCode");

            FingerPushManager.getInstance(this).checkPush(msgTag, mode, lCode, new NetworkUtility.ObjectListener() {
                @Override
                public void onComplete(String code, String message, JSONObject jsonObject) {

                }

                @Override
                public void onError(String code, String message) {

                }
            });
        }

        if(intent != null) {
            boolean isReceive = intent.getBooleanExtra("receive_push", false);
            if (isReceive) {
                Bundle bundle = new Bundle();
                bundle.putString("push_type", "app_launching");
                FirebaseAnalytics.getInstance(this).logEvent("push_event", bundle);
            }
        }

        setIntent(intent);
    }

    @Override
    protected void initialize() {
        Intent i = new Intent(this, TokenAPIService.class);
        i.setAction(TokenAPIService.ACTION_TYPE_TOKEN);
        startService(i);

        Utils.delay(subscription, DELAY_TIME, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                request_home();
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

    private void showAppPolicyDialog() {
        if (Global.getInstance().isShowAppPolicyDialog()) {
            moveRemovedAppGuideActivity();
            return;
        }
        AppPolicyPopup dlg = new AppPolicyPopup(this);
        dlg.setCancelable(false);
        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Global.getInstance().setShowAppPolicyDialog(true);
                moveRemovedAppGuideActivity();
            }
        });
        dlg.show();
    }

    private void moveRemovedAppGuideActivity() {
        try {
            HomeData.VERSION_INFO version_info = Global.getInstance().getVersionInfo();
            if (version_info != null && version_info.isRemovedApplication() && !TextUtils.isEmpty(version_info.getAppstore_url())) {
                Intent intent = new Intent(this, RemovedAppActivity.class);
                intent.putExtra(ParamConstants.PARAM_MARKET_URL, version_info.getAppstore_url());
                startActivity(intent);
                finish();
            } else {
                moveMainActivity();
            }
        } catch (Exception e) {
            moveMainActivity();
        }
    }

    private void moveMainActivity() {
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

        SingersData selectedSinger = Global.getInstance().getSingersData();
        if (selectedSinger == null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Intent intent = new Intent(this, SingerMainActivity.class);
            intent.putExtra(ParamConstants.PARAM_SINGER_DATA, selectedSinger);
            startActivity(intent);
        }
        finish();
    }

    private void request_home() {
        RequestUtils.getInstanse().requestHome(SplashActivity.this, subscription, new RequestServiceListener() {
            @Override
            public void response(boolean is_success, BaseAPIData response) {
                if (is_success) {
                    Global.getInstance().setHomeData((HomeData) response);
                    showAppPolicyDialog();
                }
            }

            @Override
            public void complete() {

            }
        });
    }

    private void showForceUpdateDlg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_force_update);
        builder.setMessage(R.string.message_force_update);
        builder.setPositiveButton(R.string.button_force_update,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.moveMarket();
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
                        showAppPolicyDialog();
                    }

                    @Override
                    public void onActionNegativeBtn() {

                    }
                })
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1004) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // 하나라도 거부한다면.
                        new AlertDialog.Builder(this).setTitle(R.string.Alarm).setMessage(R.string.must_need_permission)
                                .setPositiveButton(R.string.Exit, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        SplashActivity.this.finish();
                                    }
                                }).setNegativeButton(R.string.SettingPermission, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                getApplicationContext().startActivity(intent);
                            }
                        }).setCancelable(false).show();

                        return;
                    }
                }
                request_home();
            }
        }
    }
}
