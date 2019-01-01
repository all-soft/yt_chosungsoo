package com.chsapps.yt_nahoonha.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.AllSoft;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.constants.AdConstants;
import com.chsapps.yt_nahoonha.ui.adapter.holder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.holder.FacebookNativeAdAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.holder.FailedLoadAdAdapterHolder;
import com.chsapps.yt_nahoonha.ui.youtube_player.WebPlayer;
import com.chsapps.yt_nahoonha.utils.LogUtil;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

public class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = BaseAdapter.class.getSimpleName();

    protected static final int LIST_ITEM_TYPE_DATA = 0;
    protected static final int LIST_ITEM_TYPE_AD = 1;
    protected static final int LIST_ITEM_TYPE_NONE = 2;

    protected Context context;
    protected boolean isAddAd = true;
    protected static HashMap<Integer, UnifiedNativeAd> mapAdmobAd = new HashMap<>();
    protected static HashMap<Integer, Boolean> mapFailedLoadAdmobAd = new HashMap<>();
    protected static HashMap<Integer, NativeAd> mapFacebookAd = new HashMap<>();
    protected int adTerm = 0;
    protected int currentPage = 1;

    protected boolean isPlayerStatus = false;

    public BaseAdapter(Context context) {
        this.context = context;
        isPlayerStatus = WebPlayer.getPlayer() != null;
        EventBus.getDefault().register(this);
    }

    public void clear() {
        EventBus.getDefault().unregister(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Admob mediation
        if(viewType == LIST_ITEM_TYPE_AD)
            return new AdmobNativeAdAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_admob_list_ad, parent, false));

        return new FailedLoadAdAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_failed_load_ad, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (!isPlayerStatus && isAddAd && position % adTerm == 0) {
            if(mapFailedLoadAdmobAd.containsKey(position)) {
                return LIST_ITEM_TYPE_NONE;
            }
            return LIST_ITEM_TYPE_AD;
        }
        return LIST_ITEM_TYPE_DATA;
    }

    public boolean isAddedAd() {
        return isAddAd;
    }

    public void setIsAddedAd(boolean value) {
        isAddAd = value;
    }

    protected int getItemCount(int totalCnt) {
        if(!isAddAd) {
            return totalCnt;
        }
        return totalCnt / 2 + totalCnt / adTerm;
    }

    protected int getPosition(int position) {
        if(isPlayerStatus || !isAddAd || adTerm == 0) {
            return position;
        }
        return position - (position / adTerm) - 1;
    }

    protected void loadAdmobAd(final int position, final AdmobNativeAdAdapterHolder holder) {
        if(mapFailedLoadAdmobAd.containsKey(position)) {
        } else {
            if (mapAdmobAd.containsKey(position)) {
                UnifiedNativeAd unifiedNativeAd = mapAdmobAd.get(position);
                holder.update(unifiedNativeAd, unifiedNativeAd.getHeadline(), unifiedNativeAd.getBody());
            } else {
                holder.loadingAd();
                holder.position = position;
                AdLoader adLoader = new AdLoader.Builder(AllSoft.getContext(), AdConstants.AD_ID_NATIVE)
                        .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                            @Override
                            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                                mapAdmobAd.put(position, unifiedNativeAd);
                                if (holder.position == position) {
                                    holder.update(unifiedNativeAd, unifiedNativeAd.getHeadline(), unifiedNativeAd.getBody());
                                }
                            }
                        })
                        .withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                // Handle the failure by logging, altering the UI, and so on.
                                mapFailedLoadAdmobAd.put(position, false);

                                notifyDataSetChanged();
                            }
                        })
                        .withNativeAdOptions(new NativeAdOptions.Builder()
                                // Methods in the NativeAdOptions.Builder class can be
                                // used here to specify individual options settings.
                                .build())
                        .build();
                Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
                        .setNativeAdChoicesIconExpandable(false)
                        .build();
                AdRequest adRequest = new AdRequest.Builder()
                        .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                        .addTestDevice("086A436107A5322A6AD435A899DADB5A")
                        .build();
                adLoader.loadAd(adRequest);
            }
        }
    }

    protected void loadFacebookAd(final int position, final FacebookNativeAdAdapterHolder holder) {
        if (mapFacebookAd.containsKey(position)) {
            NativeAd nativeAd = mapFacebookAd.get(position);
            holder.update(nativeAd, nativeAd.getAdHeadline(), nativeAd.getAdBodyText());
        } else {
            holder.loadingAd();
            holder.position = position;
            final NativeAd nativeAd = new NativeAd(context, Global.getInstance().getAdConfig().getNative_id());
            nativeAd.setAdListener(new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    LogUtil.e(TAG, "ERROR : " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Race condition, load() called again before last ad was displayed
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    mapFacebookAd.put(position, nativeAd);
                    if (position == holder.position) {
                        holder.update(nativeAd, nativeAd.getAdHeadline(), nativeAd.getAdBodyText());
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });
            nativeAd.loadAd();
        }
    }

    public void setCurrentPage(int page) {
        currentPage = page;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResumeListView(com.chsapps.yt_nahoonha.data.YoutubePlayerStatus status) {
        LogUtil.e(TAG, "Ad status in base adapter : " + status.playStatus);
        if(status.playStatus != isPlayerStatus) {
            isPlayerStatus = status.playStatus;
            notifyDataSetChanged();
        }
    }
}
