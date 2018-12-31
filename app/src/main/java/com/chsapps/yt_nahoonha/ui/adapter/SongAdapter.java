package com.chsapps.yt_nahoonha.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseAdapter;
import com.chsapps.yt_nahoonha.data.SongData;
import com.chsapps.yt_nahoonha.ui.adapter.holder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.holder.SongAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SongAdapterHolderListener;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends BaseAdapter {
    private static String TAG = SongAdapter.class.getSimpleName();

    private Context context;
    private SongAdapterHolderListener listener;

    private List<SongData> arraySongs = new ArrayList<>();

    private boolean isDeletedItem = false;


    public SongAdapter(Context context, SongAdapterHolderListener listener){
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
            ((SongAdapterHolder) holder).update(arraySongs.get(getPosition(position)), true, isDeletedItem);
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
}
