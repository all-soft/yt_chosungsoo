package com.chsapps.yt_nahoonha.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseAdapter;
import com.chsapps.yt_nahoonha.data.SingersData;
import com.chsapps.yt_nahoonha.ui.adapter.holder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.holder.SingerAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SingerAdapterHolderListener;

import java.util.List;

public class SingersAdapter extends BaseAdapter {
    private static String TAG = SingersAdapter.class.getSimpleName();

    private SingerAdapterHolderListener listener;
    private List<SingersData> arraySingers;
    private SingersData selectedSinger;

    public SingersAdapter(Context context, SingerAdapterHolderListener listener) {
        this.context = context;
        this.listener = listener;

        try {
            adTerm = Global.getInstance().getAdConfig().getNative_ad_term();
        } catch (Exception e) {
            adTerm = 8;
        }
    }

    public SingersAdapter(Context context, boolean isAddAd, SingerAdapterHolderListener listener) {
        this.context = context;
        this.listener = listener;
        setIsAddedAd(isAddAd);

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
        return new SingerAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_singer, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        if (holder instanceof SingerAdapterHolder) {
            int gap = position / adTerm == 0 ? 1 : position / adTerm;
            if(!isAddAd) {
                gap = 0;
            }
            SingersData startData, endData;
            int indexStart = (position - gap) * 2;
            int indexEnd = indexStart + 1;

            if (arraySingers.size() <= indexStart) {
                startData = null;
            } else {
                startData = arraySingers.get(indexStart);
            }

            if (arraySingers.size() <= indexEnd) {
                endData = null;
            } else {
                endData = arraySingers.get(indexEnd);
            }
            ((SingerAdapterHolder) holder).update(startData, endData, selectedSinger);
        } else if (holder instanceof AdmobNativeAdAdapterHolder) {
            loadAdmobAd(position, ((AdmobNativeAdAdapterHolder) holder));
        } /*else if (holder instanceof FacebookNativeAdAdapterHolder) {
            loadFacebookAd(position, ((FacebookNativeAdAdapterHolder) holder));
        }*/
    }

    @Override
    public int getItemCount() {
        if (arraySingers == null) {
            return 0;
        }
        return arraySingers.size() == 1 ? 1 : getItemCount(arraySingers.size()) / 2;
    }



    public void insert(List<SingersData> arraySongData) {
        if (this.arraySingers != null)
            this.arraySingers.clear();
        this.arraySingers = arraySongData;
        notifyDataSetChanged();
    }

    public List<SingersData> getSongDataList() {
        return arraySingers;
    }

    public void selectedSinger(SingersData selectedSinger) {
        this.selectedSinger = selectedSinger;
        notifyDataSetChanged();
    }
}
