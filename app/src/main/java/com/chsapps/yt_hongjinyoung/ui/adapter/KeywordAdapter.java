package com.chsapps.yt_hongjinyoung.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.KeywordAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.KeywordListHolderListener;

import java.util.List;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapterHolder>{

    private Context context;
    private KeywordListHolderListener listener;

    private List<String> arrayKeywords;

    public KeywordAdapter(Context context, KeywordListHolderListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    public KeywordAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KeywordAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_keyword, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(KeywordAdapterHolder holder, int position) {
        if(holder == null) {
            return;
        }
        holder.update(arrayKeywords.get(position));
    }

    @Override
    public int getItemCount() {
        if(arrayKeywords == null) {
            return 0;
        }
        return arrayKeywords.size();
    }

    public void addAll(List<String> arrayKeywords) {
        if(this.arrayKeywords != null)
            this.arrayKeywords.clear();
        this.arrayKeywords = arrayKeywords;

        notifyDataSetChanged();
    }
}
