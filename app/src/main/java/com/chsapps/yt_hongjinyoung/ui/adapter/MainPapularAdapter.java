package com.chsapps.yt_hongjinyoung.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.ADConstants;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.data.YoutubePlayerStatus;
import com.chsapps.yt_hongjinyoung.event.AppEventManager;
import com.chsapps.yt_hongjinyoung.event.data.AppEventData;
import com.chsapps.yt_hongjinyoung.event.ui.custom_view.EventBannerAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.AdmobNativeAdAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.PlaySongAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.SongListHolderListener;
import com.chsapps.yt_hongjinyoung.utils.AdUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainPapularAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static int LIST_ITEM_TYPE_SONG = 0;
    private static int LIST_ITEM_TYPE_AD = 1;

    private static int EVENT_BANNER_LAYOUT_1_POSITION = 0;
    private static int EVENT_BANNER_LAYOUT_2_POSITION = 4;
    private static int LIST_ITEM_TYPE_EVENT_BANNER_LAYOUT_1 = 2;
    private static int LIST_ITEM_TYPE_EVENT_BANNER_LAYOUT_2 = 3;

    private Context context;
    private SongListHolderListener listener;

    private List<SongData> arraySongData = new ArrayList<>();
    private HashMap<Integer, UnifiedNativeAd> mapAdmobAd = new HashMap<>();

    private int adTerm = 10;
    private boolean isPlayerAd = true;

    private HashMap<Integer, SongData> arraySelectedSongData = new HashMap<>();

    private boolean isEventBanner = false;
    private boolean event1stBanner = false, event2ndBanner = false;

    public MainPapularAdapter(Context context, boolean isEventBanner, SongListHolderListener listener){
        this.isEventBanner = isEventBanner;
        init(context, listener);
    }

    public MainPapularAdapter(Context context, SongListHolderListener listener){
        init(context, listener);
    }

    private void init(Context context, SongListHolderListener listener) {
        this.context = context;
        this.listener = listener;

        EventBus.getDefault().register(this);

        isPlayerAd = Global.getInstance().isShowNativeBanner();

        try {
            adTerm = Global.getInstance().getAdConfig().getNative_ad_term();
        } catch (Exception e) {
            adTerm = 8;
        }

        if(isEventBanner) {
            ArrayList<AppEventData> list = AppEventManager.getInstance().getEventListData(1);
            if (list != null) {
                event1stBanner = list.size() > 0;
            }
            list = AppEventManager.getInstance().getEventListData(2);
            if (list != null) {
                event2ndBanner = list.size() > 0;
            }
        } else {
            event1stBanner = event2ndBanner = false;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        EventBus.getDefault().unregister(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == LIST_ITEM_TYPE_AD) {
            try {
                return new AdmobNativeAdAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_admob_list_ad, parent, false));
            } catch (Exception e) {
                return new AdmobNativeAdAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_admob_list_ad, parent, false));
            }
        } else if (viewType == LIST_ITEM_TYPE_EVENT_BANNER_LAYOUT_1) {
            return new EventBannerAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_event_adapter_banner, parent, false), 1);
        } else if (viewType == LIST_ITEM_TYPE_EVENT_BANNER_LAYOUT_2) {
            return new EventBannerAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.view_event_adapter_banner, parent, false), 2);
        }
        return new PlaySongAdapterHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main_songlist, parent, false), listener);
    }

    @Override
    public int getItemViewType(int position) {
//        if ((arraySongData != null && arraySongData.size() == 0) || (isPlayerAd && position % adTerm == 4)) {
//            return LIST_ITEM_TYPE_AD;
//        }
        if(event1stBanner && event2ndBanner) {
            if(position == EVENT_BANNER_LAYOUT_1_POSITION) {
                return LIST_ITEM_TYPE_EVENT_BANNER_LAYOUT_1;
            } else if(position == EVENT_BANNER_LAYOUT_2_POSITION) {
                return LIST_ITEM_TYPE_EVENT_BANNER_LAYOUT_2;
            }
        } else if(event1stBanner && position == EVENT_BANNER_LAYOUT_1_POSITION) {
            return LIST_ITEM_TYPE_EVENT_BANNER_LAYOUT_1;
        } else if(event2ndBanner && position == EVENT_BANNER_LAYOUT_1_POSITION) {
            return LIST_ITEM_TYPE_EVENT_BANNER_LAYOUT_2;
        }
        return LIST_ITEM_TYPE_SONG;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) {
            return;
        }

        if(holder instanceof PlaySongAdapterHolder) {
            int realPosition = position;
            if(event1stBanner) {
                realPosition--;
            }
            if(event2ndBanner && position > EVENT_BANNER_LAYOUT_2_POSITION) {
                realPosition--;
            }
            try {
                SongData song = arraySongData.get(realPosition);
                ((PlaySongAdapterHolder) holder).update(realPosition, arraySelectedSongData.containsKey(song.song_idx), song);
            } catch (Exception e) {
            }
        } else if(holder instanceof EventBannerAdapterHolder) {}
        else {
            loadAdmobAd(position, ((AdmobNativeAdAdapterHolder)holder));
        }
    }

    @Override
    public int getItemCount() {
        if(arraySongData == null) {
            return 0;
        }
        return arraySongData.size() + (event1stBanner ? 1:0) + (event2ndBanner ? 1:0);
    }

    public void insertSongDataList(List<SongData> arraySongData) {
        if(this.arraySongData != null)
            this.arraySongData.clear();
        this.arraySongData = arraySongData;

        arraySelectedSongData.clear();
        notifyDataSetChanged();
    }

    public List<SongData> getSongDataList() {
        return arraySongData;
    }

    private void loadAdmobAd(final int position, final AdmobNativeAdAdapterHolder holder) {
        if(mapAdmobAd.containsKey(position)) {
            UnifiedNativeAd unifiedNativeAd = mapAdmobAd.get(position);
            holder.update(unifiedNativeAd, unifiedNativeAd.getHeadline(), unifiedNativeAd.getBody());
        } else {
            holder.loadingAd();
            holder.position = position;
            AdLoader adLoader = new AdLoader.Builder(context, ADConstants.ADMOB_NATIVE_AD_ID)
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            mapAdmobAd.put(position, unifiedNativeAd);
                            if(holder.position == position) {
                                holder.update(unifiedNativeAd, unifiedNativeAd.getHeadline(), unifiedNativeAd.getBody());
                            }
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Handle the failure by logging, altering the UI, and so on.
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();
            adLoader.loadAd(AdUtils.getInstance().getAdMobAdRequest());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResumePlayer(YoutubePlayerStatus status) {
    }

    public HashMap<Integer, SongData> getSelectedSongDataMap() {
        return arraySelectedSongData;
    }
    public void setSelectedSongDataMap(HashMap<Integer, SongData> mapData) {
        arraySelectedSongData = mapData;
    }
    public List<SongData> getSelectedSongDataList() {
        List<SongData> array = new ArrayList<>();
        for(SongData song : arraySongData) {
            if(arraySelectedSongData.containsKey(song.song_idx)) {
                array.add(song);
            }
        }
        return array;
    }
    public void addSelectedSongData(SongData song) {
        if(song == null)    return;

        if(arraySelectedSongData.containsKey(song.song_idx)) {
            arraySelectedSongData.remove(song.song_idx);
        } else {
            arraySelectedSongData.put(song.song_idx, song);
        }
        notifyDataSetChanged();
    }
    public void addAllSelected() {
        arraySelectedSongData.clear();
        for(SongData song : arraySongData) {
            arraySelectedSongData.put(song.song_idx, song);
        }
        notifyDataSetChanged();
    }
    public void addAllDeSelected() {
        arraySelectedSongData.clear();
        notifyDataSetChanged();
    }
}
