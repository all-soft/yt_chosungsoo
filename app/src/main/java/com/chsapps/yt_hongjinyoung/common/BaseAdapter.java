package com.chsapps.yt_hongjinyoung.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.AllSoft;
import com.chsapps.yt_hongjinyoung.constants.AdConstants;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.FailedLoadAdAdapterHolder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.greenrobot.eventbus.EventBus;

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
    protected int adTerm = 0;
    protected int currentPage = 1;

    public BaseAdapter(Context context) {
        this.context = context;
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
        if (isAddAd && position % adTerm == 0) {
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
        if(!isAddAd || adTerm == 0) {
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
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("086A436107A5322A6AD435A899DADB5A")
                        .build();
                adLoader.loadAd(adRequest);
            }
        }
    }

    public void setCurrentPage(int page) {
        currentPage = page;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
