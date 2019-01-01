package com.chsapps.yt_nahoonha.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseAdapter;
import com.chsapps.yt_nahoonha.data.NewsData;
import com.chsapps.yt_nahoonha.ui.adapter.holder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.holder.NewsAdapterHolder;
import com.chsapps.yt_nahoonha.ui.adapter.listener.NewsAdapterHolderListener;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends BaseAdapter {
    private static String TAG = NewsAdapter.class.getSimpleName();

    private Context context;
    private NewsAdapterHolderListener listener;
    private List<NewsData> arrayNews = new ArrayList<>();

    public NewsAdapter(Context context, NewsAdapterHolderListener listener){
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
        return new NewsAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_news, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) {
            return;
        }

        if(holder instanceof NewsAdapterHolder) {
            ((NewsAdapterHolder) holder).update(arrayNews.get(getPosition(position)));
        } else if(holder instanceof AdmobNativeAdAdapterHolder) {
            loadAdmobAd(position, ((AdmobNativeAdAdapterHolder)holder));
        } /*else if(holder instanceof FacebookNativeAdAdapterHolder) {
            loadFacebookAd(position, ((FacebookNativeAdAdapterHolder)holder));
        }*/
    }

    @Override
    public int getItemCount() {
        if(arrayNews == null) {
            return 0;
        }
        if(!isAddAd || isPlayerStatus) {
            return arrayNews.size();
        }
        return arrayNews.size() + arrayNews.size() / adTerm + 1;
    }

    public void insert(List<NewsData> arrNewsList) {
        arrayNews.addAll(arrNewsList);

        notifyDataSetChanged();
    }

    public List<NewsData> getArrayNews() {
        return arrayNews;
    }

    public void clear() {
        arrayNews.clear();
        currentPage = 1;
    }
}
