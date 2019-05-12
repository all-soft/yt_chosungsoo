package com.chsapps.yt_hongjinyoung.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseAdapter;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.VideoAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.listener.SongAdapterHolderListener;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoAdapter extends BaseAdapter {
    private static String TAG = VideoAdapter.class.getSimpleName();

    private SongAdapterHolderListener listener;
    private List<SongData> arraySongs = new ArrayList<>();

    public VideoAdapter(Context context, SongAdapterHolderListener listener){
        super(context);

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
            SongData song = arraySongs.get(getPosition(position));
            song.log();
            boolean isSelected = mapSelectedSongs.containsKey(song.getRealVideoId());
            LogUtil.e("HSSEO", "SIZE : " + mapSelectedSongs.size() + "/" + song.getRealVideoId());
            ((VideoAdapterHolder) holder).update(song, isSelected);
        } else if(holder instanceof AdmobNativeAdAdapterHolder) {
            loadAdmobAd(position, ((AdmobNativeAdAdapterHolder)holder));
        }
    }

    @Override
    public int getItemCount() {
        if(arraySongs == null) {
            return 0;
        }
        if(!isAddAd) {
            return arraySongs.size();
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
            stringBuilder.append(song.getRealVideoId());
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

    private Map<String, SongData> mapSelectedSongs = new HashMap<>();
    public void setSelectedSongs(Map<String, SongData> mapSelectedSongs) {
        this.mapSelectedSongs = mapSelectedSongs;
        notifyDataSetChanged();
    }
    public Map<String, SongData> getSelectedSongs() {
        return mapSelectedSongs;
    }
    public ArrayList<SongData> getSelectedSongsList() {
        ArrayList<SongData> selectedSongsList = new ArrayList<>();
        for(SongData song : arraySongs) {
            if(song == null)  continue;
            if(mapSelectedSongs.containsKey(song.getRealVideoId()))
                selectedSongsList.add(song);
        }
        return selectedSongsList;
    }
    public void selectedSong(SongData song) {
        song.log();
        if(mapSelectedSongs.containsKey(song.getRealVideoId())) {
            mapSelectedSongs.remove(song.getRealVideoId());
        } else {
            mapSelectedSongs.put(song.getRealVideoId(), song);
        }
        notifyDataSetChanged();
    }
    public boolean isAllSelected() {
        return arraySongs.size() == mapSelectedSongs.size();
    }
    public void setAllSelectedSongs() {
        mapSelectedSongs.clear();
        for(SongData song : arraySongs) {
            if(song == null)  continue;
            mapSelectedSongs.put(song.getRealVideoId(), song);
        }
        notifyDataSetChanged();
    }
    public void setAllDeSelectedSongs() {
        mapSelectedSongs.clear();
        notifyDataSetChanged();
    }
}
