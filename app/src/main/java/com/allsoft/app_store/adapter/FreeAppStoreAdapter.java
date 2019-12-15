package com.allsoft.app_store.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.model.response.RecommandAppAPIData;

import java.util.List;

public class FreeAppStoreAdapter extends RecyclerView.Adapter<FreeAppStoreAdapterHolder>{

    private Context context;
    private List<RecommandAppAPIData.RecommandApp> arrayData;

    private FreeAppStoreAdapterHolder.AppStoreAdapterListener listener;

    public FreeAppStoreAdapter(Context context, FreeAppStoreAdapterHolder.AppStoreAdapterListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    public FreeAppStoreAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FreeAppStoreAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.func_adapter_freeappstore, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(FreeAppStoreAdapterHolder holder, int position) {
        if(holder == null) {
            return;
        }
        RecommandAppAPIData.RecommandApp data = arrayData.get(position);
        holder.update(data);
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
