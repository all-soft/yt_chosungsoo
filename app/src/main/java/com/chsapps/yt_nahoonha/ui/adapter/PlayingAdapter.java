package com.chsapps.yt_nahoonha.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseAdapter;
import com.chsapps.yt_nahoonha.data.SongData;
import com.chsapps.yt_nahoonha.ui.adapter.holder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.holder.PlayingAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SongAdapterHolderListener;

import java.util.List;

public class PlayingAdapter extends BaseAdapter {
    private static String TAG = PlayingAdapter.class.getSimpleName();

    private Context context;
    private SongAdapterHolderListener listener;

    private List<SongData> arraySongs;

    private String playingVideoId = "";

    public PlayingAdapter(Context context, SongAdapterHolderListener listener){
        super(context);

        this.context = context;
        this.listener = listener;
        this.isAddAd = false;

        try {
            adTerm = Global.getInstance().getAdConfig().getNative_ad_term();
        } catch (Exception e) {
            adTerm = 8;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType != LIST_ITEM_TYPE_DATA) {
            return super.onCreateViewHolder(parent, viewType);
        }
        return new PlayingAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_playing, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) {
            return;
        }

        if(holder instanceof PlayingAdapterHolder) {
            ((PlayingAdapterHolder) holder).update(arraySongs.get(getPosition(position)), playingVideoId);
        } else if(holder instanceof AdmobNativeAdAdapterHolder) {
            loadAdmobAd(position, ((AdmobNativeAdAdapterHolder)holder));
        } /*else if(holder instanceof FacebookNativeAdAdapterHolder) {
            loadFacebookAd(position, ((FacebookNativeAdAdapterHolder)holder));
        }*/
    }

    @Override
    public int getItemCount() {
        if(arraySongs == null) {
            return 0;
        }
        if(!isAddAd || isPlayerStatus) {
            return arraySongs.size();
        }
        return arraySongs.size() + arraySongs.size() / adTerm + 1;
    }

    public int getItemDataCount() {
        if(arraySongs == null) {
            return 0;
        }
        return arraySongs.size();
    }

    public boolean insert(List<SongData> arraySongData) {
        if(this.arraySongs != null)
            this.arraySongs.clear();
        this.arraySongs = arraySongData;

        notifyDataSetChanged();
        return this.arraySongs.size() > 0;
    }

    public List<SongData> getSongDataList() {
        return arraySongs;
    }

    public String getPlayList() {
        StringBuilder stringBuilder = new StringBuilder();
        for(SongData song : arraySongs) {
            if(!TextUtils.isEmpty(stringBuilder)){
                stringBuilder.append(",");
            }
            stringBuilder.append(song.getRealVideoId());
        }
        return stringBuilder.toString();
    }

    public String getPlaySongIdxList() {
        StringBuilder stringBuilder = new StringBuilder();
        for(SongData song : arraySongs) {
            if(!TextUtils.isEmpty(stringBuilder)){
                stringBuilder.append(",");
            }
            stringBuilder.append(song.song_idx);
        }
        return stringBuilder.toString();
    }

    public void playSong(int idx) {
        for(SongData song : arraySongs) {
            if(song.song_idx == idx) {
                if(listener != null)
                    listener.selected(song);
                break;
            }
        }
    }

    public int setPlayingVideoId(String id) {
        if(playingVideoId.equals(id)) {
            return -1;
        }
        playingVideoId = id;
        notifyDataSetChanged();

        int index = 0;
        for(SongData song : arraySongs) {
            if(song.getRealVideoId().equals(id)) {
                break;
            }
            index++;
        }
        return index;
    }
}
