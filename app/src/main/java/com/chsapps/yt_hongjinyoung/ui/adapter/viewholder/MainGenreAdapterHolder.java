package com.chsapps.yt_hongjinyoung.ui.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.data.CategoryData;
import com.chsapps.yt_hongjinyoung.db.GenreLastClickDBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainGenreAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = MainGenreAdapterHolder.class.getSimpleName();

    private Context context;
    private CategoryData category;
    private CategoryListHolderListener listener;

    @BindView(R.id.iv_background)
    ImageView iv_background;
    @BindView(R.id.tv_category_title)
    TextView tv_category_title;
    @BindView(R.id.view_bg)
    View view_bg;
    @BindView(R.id.tv_new)
    TextView tv_new;

    View itemView;
    public MainGenreAdapterHolder(Context context, View itemView, CategoryListHolderListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void update(int genreType, CategoryData category) {
        this.category = category;
        if(category == null) {
            itemView.setVisibility(View.GONE);
            return;
        } else {
            itemView.setVisibility(View.VISIBLE);
        }
        this.tv_category_title.setText(category.getCategory_name());
        if(TextUtils.isEmpty(category.getCategory_image_url())) {
            iv_background.setVisibility(View.GONE);
            view_bg.setVisibility(View.VISIBLE);
            tv_category_title.setVisibility(View.VISIBLE);
        } else {
            iv_background.setVisibility(View.VISIBLE);
            view_bg.setVisibility(View.GONE);
            tv_category_title.setVisibility(View.GONE);
            Glide.with(context).load(category.getCategory_image_url()).into(iv_background);
        }

        tv_new.setVisibility(View.GONE);
        if(Global.getInstance().mapCategoryIdLastClickTime.containsKey(String.valueOf(category.category_idx))) {
            long lastLaunchTime = Global.getInstance().mapCategoryIdLastClickTime.get(String.valueOf(category.category_idx));
            if(lastLaunchTime < category.update_at * 1000) {
                tv_new.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.layer_main)
    public void onClick_layer_main() {
        GenreLastClickDBHelper.getInstance().insert(String.valueOf(category.category_idx));
        Global.getInstance().mapCategoryIdLastClickTime.put(String.valueOf(category.category_idx), System.currentTimeMillis());
        if(listener != null) {
            listener.selectedCategory(category);
        }
    }
}
