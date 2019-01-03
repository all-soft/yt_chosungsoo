package com.chsapps.yt_hongjinyoung.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseAdapter;
import com.chsapps.yt_hongjinyoung.data.NewsReviewData;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.NewsReviewAdapterHolder;

import java.util.List;

public class NewsReviewAdapter extends BaseAdapter {
    private static String TAG = NewsReviewAdapter.class.getSimpleName();

    private Context context;
    private List<NewsReviewData> arrayNewsReview;

    public NewsReviewAdapter(Context context){
        super(context);

        this.context = context;

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
        return new NewsReviewAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_news, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) {
            return;
        }

        if(holder instanceof NewsReviewAdapterHolder) {
            ((NewsReviewAdapterHolder) holder).update(arrayNewsReview.get(getPosition(position)));
        } else if(holder instanceof AdmobNativeAdAdapterHolder) {
            loadAdmobAd(position, ((AdmobNativeAdAdapterHolder)holder));
        } /*else if(holder instanceof FacebookNativeAdAdapterHolder) {
            loadFacebookAd(position, ((FacebookNativeAdAdapterHolder)holder));
        }*/
    }

    @Override
    public int getItemCount() {
        if(arrayNewsReview == null) {
            return 0;
        }
        if(!isAddAd || isPlayerStatus) {
            return arrayNewsReview.size();
        }
        return arrayNewsReview.size() + arrayNewsReview.size() / adTerm + 1;
    }

    public void insert(List<NewsReviewData> arrNewsList) {
        if(this.arrayNewsReview!= null)
            this.arrayNewsReview.clear();
        this.arrayNewsReview = arrNewsList;

        notifyDataSetChanged();
    }
}
