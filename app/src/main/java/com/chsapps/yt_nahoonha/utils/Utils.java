package com.chsapps.yt_nahoonha.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.chsapps.yt_nahoonha.BuildConfig;
import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.AllSoft;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.constants.Constants;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.chsapps.yt_nahoonha.app.AllSoft.getContext;

public class Utils {
    public static final String TAG = Utils.class.getSimpleName();

    public static String getString(int string_id) {
        return getContext().getResources().getString(string_id);
    }

    public static boolean unableRequestAPI(Activity activity, DialogInterface.OnClickListener listener) {
        if (NetworkUtils.isNetworkConnected()) {
            return false;
        }
        AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
        dlg.setTitle(R.string.Warning);
        dlg.setMessage(Utils.getString(R.string.dont_connected_network));
        dlg.setNegativeButton(R.string.Done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dlg.show();
        return true;
    }

    public static String getFirebaseToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    public static String getLanguage() {
        return "ko";
//        return Locale.getDefault().getDisplayLanguage();
    }

    public static String getCC() {
        return Locale.getDefault().getCountry();
    }

    public static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * value);
    }

    public static String getText(int resId) {
        return getContext().getResources().getText(resId).toString();
    }

    public static void showShare(Context context, String title, String message) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!queryIntentActivities.isEmpty()) {
            List arrayList = new ArrayList();
            for (ResolveInfo resolveInfo : queryIntentActivities) {
                String str2 = resolveInfo.activityInfo.packageName;
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("text/plain");
                ComponentName componentName = new ComponentName(str2, resolveInfo.activityInfo.name);
                intent2.putExtra("android.intent.extra.TEXT", message);
                intent2.setComponent(componentName);
                intent2.setPackage(str2);
                arrayList.add(intent2);
            }
            if (!arrayList.isEmpty()) {
                intent = Intent.createChooser((Intent) arrayList.remove(0), title);
                intent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arrayList.toArray(new Parcelable[arrayList.size()]));
                context.startActivity(intent);
            }
        }

    }

    public static void showKakaoShare(Context context, String title, String message) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!queryIntentActivities.isEmpty()) {
            List arrayList = new ArrayList();
            for (ResolveInfo resolveInfo : queryIntentActivities) {
                String str2 = resolveInfo.activityInfo.packageName;
                if(str2.equals("com.kakao.talk")) {
                    Intent intent2 = new Intent("android.intent.action.SEND");
                    intent2.setType("text/plain");
                    ComponentName componentName = new ComponentName(str2, resolveInfo.activityInfo.name);
                    intent2.putExtra("android.intent.extra.TEXT", message);
                    intent2.setComponent(componentName);
                    intent2.setPackage(str2);
                    arrayList.add(intent2);
                }
            }
            if (!arrayList.isEmpty()) {
                intent = Intent.createChooser((Intent) arrayList.remove(0), title);
                intent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arrayList.toArray(new Parcelable[arrayList.size()]));
                context.startActivity(intent);
            }
        }

    }

    public static void showSoftKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void moveOverlaySetting(Activity activity, int reqCode) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + AllSoft.getContext().getPackageName()));
            activity.startActivity(intent);
        }

    }

    public static void movePermissionSetting(Activity activity) {
        activity.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null)));
    }

    public static String makeDefaultFolder() {
        String sdcard = Environment.getExternalStorageState();
        File file = null;

        if ( !sdcard.equals(Environment.MEDIA_MOUNTED))
        {
            // SD카드가 마운트되어있지 않음
            file = Environment.getRootDirectory();
        }
        else
        {
            // SD카드가 마운트되어있음
            file = Environment.getExternalStorageDirectory();
        }
        String dir = file.getAbsolutePath() + "/" + "MirrorMode";
        file = new File(dir);
        if ( !file.exists() )
        {
            // 디렉토리가 존재하지 않으면 디렉토리 생성
            file.mkdirs();
        }
        return dir;
    }

    public static String getDefaultFolderPath() {
        return makeDefaultFolder();
    }

    public static String getCpatureFilePath() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

        String fileName = sdf.format(date);

        return makeDefaultFolder() + "/" + fileName + ".png";
    }

    public static String getADID() {
        String adId = Global.getInstance().getADID();
        if (TextUtils.isEmpty(adId)) {
            setADID();
        }
        return adId;
    }

    public static void setADID() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
                    String adId = adInfo != null ? adInfo.getId() : null;
                    Global.getInstance().setADID(adId);
                } catch (IOException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException exception) {
                    LogUtil.e("UTILS_EXCEPTION. (setADID)", exception.getMessage());
                }
            }
        });
    }

    public static String getDeviceId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void moveMarket() {
        AllSoft.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MARKET_URL)));
    }

    public static void delay(CompositeDisposable subscription, int delayTime, final DelayListenerListener listener) {
        subscription.add(Observable.empty()
                .delay(delayTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if(listener != null)
                            listener.delayedTime();
                    }
                }));
    }

    public static void initLayoutListView(Activity activity, RecyclerView list_view) {
        list_view.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        list_view.setHasFixedSize(true);
    }
}
