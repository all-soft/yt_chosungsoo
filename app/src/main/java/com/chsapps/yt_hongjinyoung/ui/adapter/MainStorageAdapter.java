package com.chsapps.yt_hongjinyoung.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.ADConstants;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.data.YoutubePlayerStatus;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.SongListHolderListener;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.StoragePlaySongAdapterHolder;
import com.chsapps.yt_hongjinyoung.utils.AdUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainStorageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int LIST_ITEM_TYPE_SONG = 0;
    private static int LIST_ITEM_TYPE_AD = 1;

    private Context context;
    private SongListHolderListener listener;

    private List<SongData> arraySongData;
    private HashMap<Integer, UnifiedNativeAd> mapAdmobAd = new HashMap<>();

    private int adTerm = 10;
    private boolean isPlayerAd = false;

    private HashMap<Integer, SongData> arraySelectedSongData = new HashMap<>();

    public MainStorageAdapter(Context context, SongListHolderListener listener) {
        this.context = context;
        this.listener = listener;

        EventBus.getDefault().register(this);

        isPlayerAd = Global.getInstance().isShowNativeBanner();

        try {
            adTerm = Global.getInstance().getAdConfig().getNative_ad_term();
        } catch (Exception e) {
            adTerm = 8;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        EventBus.getDefault().unregister(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LIST_ITEM_TYPE_AD) {
            try {
                return new AdmobNativeAdAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_admob_list_ad, parent, false));
            } catch (Exception e) {
                return new AdmobNativeAdAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_admob_list_ad, parent, false));
            }
        }
        return new StoragePlaySongAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main_storage_songlist, parent, false), listener);
    }

    @Override
    public int getItemViewType(int position) {

        if ((arraySongData != null && arraySongData.size() == 0) || (isPlayerAd && position % adTerm == 4)) {
            return LIST_ITEM_TYPE_AD;
        }
        return LIST_ITEM_TYPE_SONG;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        if (holder instanceof StoragePlaySongAdapterHolder) {
            try {
                int idx = position % adTerm < 4 ? position - (position / adTerm) : position - (position / adTerm) - 1;
                if(!isPlayerAd) {
                    idx = position;
                }
                SongData song = arraySongData.get(idx);
                ((StoragePlaySongAdapterHolder) holder).update(idx, arraySelectedSongData.containsKey(song.song_idx), song);
            } catch (Exception e) {
            }
        } else {
            loadAdmobAd(position, ((AdmobNativeAdAdapterHolder) holder));
        }
    }

    @Override
    public int getItemCount() {
        if (arraySongData == null) {
            return 0;
        }
        if (!isPlayerAd) {
            return arraySongData.size();
        }

        if(arraySongData.size() == 0)
            return 0;

        return arraySongData.size() + arraySongData.size() / adTerm + 1;
    }

    public void insertSongDataList(List<SongData> arraySongData) {
        if (this.arraySongData != null)
            this.arraySongData.clear();
        this.arraySongData = arraySongData;

        notifyDataSetChanged();
    }

    public List<SongData> getSongDataList() {
        return arraySongData;
    }

    private void loadAdmobAd(final int position, final AdmobNativeAdAdapterHolder holder) {
        if (mapAdmobAd.containsKey(position)) {
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
                            if (holder.position == position) {
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

    public HashMap<Integer, SongData> getSelectedSongDataMap() {
        return arraySelectedSongData;
    }

    public void setSelectedSongDataMap(HashMap<Integer, SongData> mapData) {
        arraySelectedSongData = mapData;
    }

    public List<SongData> getSelectedSongDataList() {
        List<SongData> array = new ArrayList<>();
        for (SongData song : arraySongData) {
            if (arraySelectedSongData.containsKey(song.song_idx)) {
                array.add(song);
            }
        }
        return array;
    }

    public void addSelectedSongData(SongData song) {
        if (song == null) return;

        if (arraySelectedSongData.containsKey(song.song_idx)) {
            arraySelectedSongData.remove(song.song_idx);
        } else {
            arraySelectedSongData.put(song.song_idx, song);
        }
        notifyDataSetChanged();
    }

    public void addAllSelected() {
        arraySelectedSongData.clear();
        for (SongData song : arraySongData) {
            arraySelectedSongData.put(song.song_idx, song);
        }
        notifyDataSetChanged();
    }

    public void addAllDeSelected() {
        arraySelectedSongData.clear();
        notifyDataSetChanged();
    }
}
