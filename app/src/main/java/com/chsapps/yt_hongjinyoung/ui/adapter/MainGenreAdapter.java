package com.chsapps.yt_hongjinyoung.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.ADConstants;
import com.chsapps.yt_hongjinyoung.data.CategoryData;
import com.chsapps.yt_hongjinyoung.data.YoutubePlayerStatus;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.CategoryListHolderListener;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.MainGenreAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.view.player.WebPlayer;
import com.chsapps.yt_hongjinyoung.utils.AdUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

public class MainGenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static int LIST_ITEM_TYPE_SONG = 0;
    private static int LIST_ITEM_TYPE_AD = 1;

    private Context context;
    private CategoryListHolderListener listener;

    private List<CategoryData> arrayCategoryData;

    public boolean isPlayerAd = true;

    private HashMap<Integer, UnifiedNativeAd> mapAdmobAd = new HashMap<>();

    private int adTerm = 10;

    public MainGenreAdapter(Context context, CategoryListHolderListener listener){
        this.context = context;
        this.listener = listener;

        if(Global.getInstance().isPlayerClosed) {
            isPlayerAd = true;
        } else {
            isPlayerAd = WebPlayer.getPlayer() == null;
        }

        isPlayerAd = Global.getInstance().isShowNativeBanner();

        EventBus.getDefault().register(this);

        try {
            adTerm = Global.getInstance().getAdConfig().getNative_ad_term();
        } catch (Exception e) {
            adTerm = 6;
        }
    }

    public void setAdTerm(int term) {
        adTerm = term;
    }


    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == LIST_ITEM_TYPE_AD) {
            try {
                return new AdmobNativeAdAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_admob_list_ad, parent, false));
            } catch (Exception e) {
                return new AdmobNativeAdAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_admob_list_ad, parent, false));
            }
        }
        return new MainGenreAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main_genre, parent, false), listener);
    }

    @Override
    public int getItemViewType(int position) {
        if(isPlayerAd && (position % adTerm == 4)) {
            return LIST_ITEM_TYPE_AD;
        }
        return LIST_ITEM_TYPE_SONG;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) {
            return;
        }
        if(holder instanceof MainGenreAdapterHolder) {
            int idx = position % adTerm < 4 ? position - (position / adTerm) : position - (position / adTerm) - 1;
            if(!isPlayerAd) {
                idx = position;
            }
            try {
                ((MainGenreAdapterHolder) holder).update(genreType, arrayCategoryData.get(idx));
            } catch (Exception e) {
                ((MainGenreAdapterHolder) holder).update(genreType, null);
            }
        } else{
            loadAdmobAd(position, ((AdmobNativeAdAdapterHolder)holder));
        }
    }

    @Override
    public int getItemCount() {
        if(arrayCategoryData == null) {
            return 0;
        }

        if(!isPlayerAd) {
            return arrayCategoryData.size();
        }
        return arrayCategoryData.size() + arrayCategoryData.size() / adTerm + 1;
    }

    int genreType = 0;
    public void setGenreType(int type) {
        genreType = type;
    }

    public void insertCategoryDataList(List<CategoryData> arraySongData) {
        if(this.arrayCategoryData != null)
            this.arrayCategoryData.clear();
        this.arrayCategoryData = arraySongData;
        notifyDataSetChanged();
    }

    private void loadAdmobAd(final int position, final AdmobNativeAdAdapterHolder holder) {
        if(mapAdmobAd.containsKey(position)) {
            UnifiedNativeAd unifiedNativeAd = mapAdmobAd.get(position);
            holder.update(unifiedNativeAd, unifiedNativeAd.getHeadline(), unifiedNativeAd.getBody());
        } else {
            holder.loadingAd();
            holder.position = position;
            AdLoader adLoader = new AdLoader.Builder(context, ADConstants.ADMOB_NATIVE_AD_ID)
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            mapAdmobAd.put(position, unifiedNativeAd);
                            if(holder.position == position) {
                                holder.update(unifiedNativeAd, unifiedNativeAd.getHeadline(), unifiedNativeAd.getBody());
                            }
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Handle the failure by logging, altering the UI, and so on.
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();
            adLoader.loadAd(AdUtils.getInstance().getAdMobAdRequest());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResumePlayer(YoutubePlayerStatus status) {
    }

}
