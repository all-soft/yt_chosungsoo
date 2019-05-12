package com.chsapps.yt_hongjinyoung.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseAdapter;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.SongAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.listener.SongAdapterHolderListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongAdapter extends BaseAdapter {
    private static String TAG = SongAdapter.class.getSimpleName();

    private Context context;
    private SongAdapterHolderListener listener;

    private List<SongData> arraySongs = new ArrayList<>();

    private boolean isDeletedItem = false;

    public SongAdapter(Context context, SongAdapterHolderListener listener){
        super(context);

        this.context = context;
        this.listener = listener;

        try {
            adTerm = Global.getInstance().getAdConfig().getNative_ad_term();
        } catch (Exception e) {
            adTerm = 8;
        }
    }

    public void setIsDeletedItem(boolean value) {
        isDeletedItem = value;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType != LIST_ITEM_TYPE_DATA) {
            return super.onCreateViewHolder(parent, viewType);
        }
        return new SongAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_song, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) {
            return;
        }

        if(holder instanceof SongAdapterHolder) {
            int index = position - position / adTerm;
            SongData song = arraySongs.get(getPosition(position));
            boolean isSelected = mapSelectedSongs.containsKey(song.getRealVideoId());
            ((SongAdapterHolder) holder).update(index, song, true, isDeletedItem, isSelected);
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

    public void clear() {
        arraySongs.clear();
        currentPage = 1;
    }

    public int getItemDataCount() {
        if(arraySongs == null) {
            return 0;
        }
        return arraySongs.size();
    }

    public boolean insert(List<SongData> arraySongData) {
        this.arraySongs.addAll(arraySongData);

        notifyDataSetChanged();
        return this.arraySongs.size() > 0;
    }

    public List<SongData> getSongDataList() {
        return arraySongs;
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
