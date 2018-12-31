package com.chsapps.yt_nahoonha.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.api.RequestServiceListener;
import com.chsapps.yt_nahoonha.api.RequestUtils;
import com.chsapps.yt_nahoonha.api.model.BaseAPIData;
import com.chsapps.yt_nahoonha.api.model.HomeData;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseActivity;
import com.chsapps.yt_nahoonha.constants.ParamConstants;
import com.chsapps.yt_nahoonha.data.SingersData;
import com.chsapps.yt_nahoonha.service.TokenAPIService;
import com.chsapps.yt_nahoonha.ui.view.popup.AppPolicyPopup;
import com.chsapps.yt_nahoonha.ui.view.popup.CommonPopup;
import com.chsapps.yt_nahoonha.utils.DelayListenerListener;
import com.chsapps.yt_nahoonha.utils.Utils;
import com.chsapps.yt_nahoonha.utils.permission.PermissionsUtils;

import java.util.List;

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();

    private static final int DELAY_TIME = 2000;

    private int showNoticeIndex = 0;
    private boolean isMoveSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initialize() {
        Intent i = new Intent(this, TokenAPIService.class);
        i.setAction(TokenAPIService.ACTION_TYPE_TOKEN);
        startService(i);

        Utils.delay(subscription, DELAY_TIME, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                checkPermission();
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
        if (isMoveSettings) {
            checkPermission();
        }
    }

    private void checkPermission() {
        //TODO : Camera and Recoding Permission Check. {}
        if (PermissionsUtils.reqAllPermissions()) {
            request_home();
        } else {
            CommonPopup dlg = new CommonPopup(this);
            dlg.setTitleString(R.string.must_permission)
                    .setMessageString(R.string.permission_system_alert_message)
                    .setBtn_negative(false, null)
                    .setBtn_positive(true, R.string.Setting)
                    .setActionListener(new CommonPopup.CommonPopupActionListener() {
                        @Override
                        public void onActionPositiveBtn() {
                            isMoveSettings = true;
                            Utils.moveOverlaySetting(SplashActivity.this, 1000);
                        }

                        @Override
                        public void onActionNegativeBtn() {
                        }
                    }).show();
//                dlg.show();
//                PermissionGuidePopup popup = new PermissionGuidePopup(this);
//                popup.setCanceledOnTouchOutside(false);
//                popup.setListner(new PermissionGuidePopup.PermissionGuidePopupListener() {
//                    @Override
//                    public void onClickButton() {
//                        PermissionsUtils.showPermissionPopup(SplashActivity.this);
//                    }
//                });
//                popup.show();

        }
    }

    private void showAppPolicyDialog() {
        if (Global.getInstance().isShowAppPolicyDialog()) {
            moveMainActivity();
            return;
        }
        AppPolicyPopup dlg = new AppPolicyPopup(this);
        dlg.setCancelable(false);
        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Global.getInstance().setShowAppPolicyDialog(true);
                moveMainActivity();
            }
        });
        dlg.show();
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
