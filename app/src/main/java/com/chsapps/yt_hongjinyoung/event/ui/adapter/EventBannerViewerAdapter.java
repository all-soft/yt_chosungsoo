package com.chsapps.yt_hongjinyoung.event.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.event.data.AppEventData;
import com.chsapps.yt_hongjinyoung.event.ui.custom_view.EventBannerView;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;

import java.util.ArrayList;

public class EventBannerViewerAdapter extends PagerAdapter {

    private static final String TAG = EventBannerViewerAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater mLayoutInflater;

    private ArrayList<AppEventData> viewArrayList;

    private EventBannerViewerAdapterListener listener;

    public EventBannerViewerAdapter(final Context context, EventBannerViewerAdapterListener listener) {
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        viewArrayList = new ArrayList<>();
        this.listener = listener;
    }

    public void insert(ArrayList<AppEventData> datas) {
        LogUtil.e("HSSEO", "event banner adater size : " + datas.size());
        viewArrayList = datas;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        EventBannerView view = new EventBannerView(context);
        view.update(viewArrayList.get(position), listener);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return viewArrayList.size();
    }
}
