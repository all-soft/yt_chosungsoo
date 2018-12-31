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
import com.chsapps.yt_nahoonha.ui.adapter.holder.VideoAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SongAdapterHolderListener;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends BaseAdapter {
    private static String TAG = VideoAdapter.class.getSimpleName();

    private SongAdapterHolderListener listener;
    private List<SongData> arraySongs = new ArrayList<>();

    public VideoAdapter(Context context, SongAdapterHolderListener listener){
        this.context = context;
        this.listener = listener;

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
        return new VideoAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_video, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) {
            return;
        }

        if(holder instanceof VideoAdapterHolder) {
            ((VideoAdapterHolder) holder).update(arraySongs.get(getPosition(position)));
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
        return arraySongs.size() + arraySongs.size() / adTerm + 1;
    }

    public void insert(List<SongData> arraySongData) {
        this.arraySongs.addAll(arraySongData);

        notifyDataSetChanged();
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

    public void clear() {
        arraySongs.clear();
        currentPage = 1;
    }
}
