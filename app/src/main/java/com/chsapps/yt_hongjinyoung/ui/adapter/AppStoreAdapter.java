package com.chsapps.yt_hongjinyoung.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.model.response.RecommandAppAPIData;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.AppStoreAdapterHolder;

import java.util.List;

public class AppStoreAdapter extends RecyclerView.Adapter<AppStoreAdapterHolder>{

    private Context context;
    private List<RecommandAppAPIData.RecommandApp> arrayData;

    private AppStoreAdapterHolder.AppStoreAdapterListener listener;

    public AppStoreAdapter(Context context, AppStoreAdapterHolder.AppStoreAdapterListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    public AppStoreAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppStoreAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_appstore, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(AppStoreAdapterHolder holder, int position) {
        if(holder == null) {
            return;
        }
        RecommandAppAPIData.RecommandApp data = arrayData.get(position);
        holder.update(data.app_title, data.image_url, data.app_package_name);
    }

    @Override
    public int getItemCount() {
        if(arrayData == null) {
            return 0;
        }
        return arrayData.size();
    }

    public void addAll(List<RecommandAppAPIData.RecommandApp> arrayKeywords) {
        if(this.arrayData != null)
            this.arrayData.clear();
        this.arrayData = arrayKeywords;

        notifyDataSetChanged();
    }

    public RecommandAppAPIData.RecommandApp getData(String packagename) {
        try {
            for(RecommandAppAPIData.RecommandApp data : arrayData) {
                if(data.app_package_name.equals(packagename)) {
                    return data;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
