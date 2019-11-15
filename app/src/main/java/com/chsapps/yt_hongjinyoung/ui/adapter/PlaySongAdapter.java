package com.chsapps.yt_hongjinyoung.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.ADConstants;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.data.YoutubePlayerStatus;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.PlaySongAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.SongListHolderListener;
import com.chsapps.yt_hongjinyoung.ui.view.player.WebPlayer;
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

public class PlaySongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SongListHolderListener {
    private static int LIST_ITEM_TYPE_SONG = 0;
    private static int LIST_ITEM_TYPE_AD = 1;

    private Context context;
    private SongListHolderListener listener;

    private List<SongData> arraySongData;

    private String playingVideoId = "";

    private HashMap<Integer, UnifiedNativeAd> mapAdmobAd = new HashMap<>();

    private int adTerm = 10;
    private boolean isPlayerAd = true;
    public boolean isAddAd = false;

    private HashMap<Integer, SongData> arraySelectedSongData = new HashMap<>();

    public PlaySongAdapter(Context context, SongListHolderListener listener) {
        this.context = context;
        this.listener = listener;

        if(Global.getInstance().isPlayerClosed) {
            isPlayerAd = true;
        } else {
            isPlayerAd = WebPlayer.getPlayer() == null;
        }
        EventBus.getDefault().register(this);

        try {
            adTerm = Global.getInstance().getAdConfig().getNative_ad_term();
        } catch (Exception e) {
            adTerm = 8;
        }
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
        return new PlaySongAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main_songlist, parent, false), this);
    }

    @Override
    public int getItemViewType(int position) {
        if (isAddAd && isPlayerAd && position % adTerm == 4) {
            return LIST_ITEM_TYPE_AD;
        }
        return LIST_ITEM_TYPE_SONG;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        if (holder instanceof PlaySongAdapterHolder) {
            ((PlaySongAdapterHolder) holder).update(position - position / adTerm, arraySongData.get(position - position / adTerm), playingVideoId);
        } else{
            loadAdmobAd(position, ((AdmobNativeAdAdapterHolder) holder));
        }
    }

    @Override
    public int getItemCount() {
        if (arraySongData == null) {
            return 0;
        }
        if(!isAddAd || !isPlayerAd) {
            return arraySongData.size();
        }
        return arraySongData.size() + arraySongData.size() / adTerm;
    }

    public int setPlayingVideoId(String id) {
        if (!TextUtils.isEmpty(playingVideoId) && playingVideoId.equals(id)) {
            return -1;
        }
        playingVideoId = id;
        notifyDataSetChanged();

        int index = 0;
        for (SongData song : arraySongData) {
            if (song.getVideoid().equals(id)) {
                break;
            }
            index++;
        }
        return index;
    }

    public void insertSongDataList(List<SongData> arraySongData) {
        if (this.arraySongData != null)
            this.arraySongData.clear();
        this.arraySongData = arraySongData;

        notifyDataSetChanged();
    }

    public List<SongData> getItemsList() {
        return arraySongData;
    }

    public String getPlayList() {
        StringBuilder stringBuilder = new StringBuilder();
        for (SongData song : arraySongData) {
            if (!TextUtils.isEmpty(stringBuilder)) {
                stringBuilder.append(",");
            }
            stringBuilder.append(song.getVideoid());
        }
        return stringBuilder.toString();
    }

    public String getPlaySongIdxList() {
        StringBuilder stringBuilder = new StringBuilder();
        for (SongData song : arraySongData) {
            if (!TextUtils.isEmpty(stringBuilder)) {
                stringBuilder.append(",");
            }
            stringBuilder.append(song.song_idx);
        }
        return stringBuilder.toString();
    }

    public void playSong(int idx) {
        if(arraySongData == null || arraySongData.size() == 0) {
            return;
        }

        for (SongData song : arraySongData) {
            if (song.song_idx == idx) {
                if (listener != null)
                    listener.playSong(song);
                break;
            }
        }

    }

    @Override
    public void playSong(SongData song) {
        if (listener != null)
            listener.playSong(song);
    }

    @Override
    public void deleteSong(SongData song) {

    }

    @Override
    public void shareSong(SongData song) {

    }

    @Override
    public void saveSong(SongData song) {
        if(listener != null)
            listener.saveSong(song);
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
        if(isPlayerAd !=  status.playStatus) {
            isPlayerAd = status.playStatus;
            notifyDataSetChanged();
        }
    }

    public HashMap<Integer, SongData> getSelectedSongDataMap() {
        return arraySelectedSongData;
    }
    public void setSelectedSongDataMap(HashMap<Integer, SongData> mapData) {
        arraySelectedSongData = mapData;
    }
    public List<SongData> getSelectedSongDataList() {
        List<SongData> array = new ArrayList<>();
        for(SongData song : arraySongData) {
            if(arraySelectedSongData.containsKey(song.song_idx)) {
                array.add(song);
            }
        }
        return array;
    }
    public void addSelectedSongData(SongData song) {
        if(song == null)    return;

        if(arraySelectedSongData.containsKey(song.song_idx)) {
            arraySelectedSongData.remove(song.song_idx);
        } else {
            arraySelectedSongData.put(song.song_idx, song);
        }
        notifyDataSetChanged();
    }
    public void addAllSelected() {
        arraySelectedSongData.clear();
        for(SongData song : arraySongData) {
            arraySelectedSongData.put(song.song_idx, song);
        }
        notifyDataSetChanged();
    }
    public void addAllDeSelected() {
        arraySelectedSongData.clear();
        notifyDataSetChanged();
    }
}
