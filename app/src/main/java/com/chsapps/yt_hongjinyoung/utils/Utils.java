package com.chsapps.yt_hongjinyoung.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.adjust.sdk.Adjust;
import com.chsapps.yt_hongjinyoung.BuildConfig;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.app.yt7080;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class Utils {
    public static final String TAG = Utils.class.getSimpleName();

    public static String getString(int string_id) {
        return yt7080.getContext().getResources().getString(string_id);
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
        return Locale.getDefault().getDisplayLanguage();
    }

    public static String getCC() {
        return Locale.getDefault().getCountry();
    }

    public static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
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
//                try {
//                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(yt7080.getContext());
//                    String adId = adInfo != null ? adInfo.getId() : null;
////                    Global.getInstance().setADID(adId);
//                } catch (IOException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException exception) {
//                    LogUtil.e("UTILS_EXCEPTION. (setADID)", exception.getMessage());
//                }
                String adid = Adjust.getAdid();
                Global.getInstance().setADID(adid);
            }
        });
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        float density = yt7080.getContext().getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * value);
    }

    public static String getText(int resId) {
        return yt7080.getContext().getResources().getText(resId).toString();
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
//        activity.startActivityForResult(intent, reqCode);
            Intent localIntent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
            localIntent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(localIntent);
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

    public static void moveCompanyAppsMarket() {
        Intent goToMarket = new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(Uri.parse("market://search?q=pub:CHS32Apps"));
        yt7080.getContext().startActivity(goToMarket);
    }


    public static void initLayoutListView(Activity activity, RecyclerView list_view) {
        list_view.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        list_view.setHasFixedSize(true);
    }

    public static void initLayoutHorizontalListView(Activity activity, RecyclerView list_view) {
        list_view.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        list_view.setHasFixedSize(true);
    }

    public static boolean isInstallApp(String pakageName) {
        return yt7080.getContext().getPackageManager().getLaunchIntentForPackage(pakageName) != null;
    }
    public static String getUUID(Context context) {
        String androidId = "";
        try {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
        }

        try {
            if (!TextUtils.isEmpty(androidId) && !androidId.equals("9774d56d682e549c") && !androidId.equals("unknown") && !androidId.equals("000000000000000")) {
                return UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
            }
        } catch (Exception e) {
        }
        return getUniqueID(context);
    }
    public static String getUniqueID(Context context) {
        int abi = 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abi = Build.SUPPORTED_ABIS[0].length();
        } else {
            abi = Build.CPU_ABI.length();
        }

        String deviceIdShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                abi % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits

        try {
            return new UUID(deviceIdShort.hashCode(), Build.SERIAL.hashCode()).toString();
        } catch (Exception e) {
            return new UUID(deviceIdShort.hashCode(), "serial".hashCode()).toString();
        }
    }
}
