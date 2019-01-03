package com.chsapps.yt_hongjinyoung.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseAdapter;
import com.chsapps.yt_hongjinyoung.data.ReviewData;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.ReviewAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.listener.ReviewAdapterHolderListener;

import java.util.List;

public class ReviewAdapter extends BaseAdapter {
    private static String TAG = ReviewAdapter.class.getSimpleName();

    private ReviewAdapterHolderListener listener;
    private List<ReviewData> arrayReview;

    public ReviewAdapter(Context context, ReviewAdapterHolderListener listener){
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
        return new ReviewAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_adapter_video, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) {
            return;
        }

        if(holder instanceof ReviewAdapterHolder) {
            ((ReviewAdapterHolder) holder).update(arrayReview.get(getPosition(position)));
        } else if(holder instanceof AdmobNativeAdAdapterHolder) {
            loadAdmobAd(position, ((AdmobNativeAdAdapterHolder)holder));
        } /*else if(holder instanceof FacebookNativeAdAdapterHolder) {
            loadFacebookAd(position, ((FacebookNativeAdAdapterHolder)holder));
        }*/
    }

    @Override
    public int getItemCount() {
        if(arrayReview == null) {
            return 0;
        }
        if(!isAddAd || isPlayerStatus) {
            return arrayReview.size();
        }
        return getItemCount(arrayReview.size());
    }

    public boolean insert(List<ReviewData> arrayReview) {
        if(this.arrayReview != null)
            this.arrayReview.clear();
        this.arrayReview = arrayReview;

        notifyDataSetChanged();

        return this.arrayReview.size() > 0;
    }
}
