package com.chsapps.yt_hongjinyoung.event.ui.custom_view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.event.AppEventManager;
import com.chsapps.yt_hongjinyoung.event.data.AppEventData;
import com.chsapps.yt_hongjinyoung.event.ui.adapter.EventBannerViewerAdapter;
import com.chsapps.yt_hongjinyoung.event.ui.adapter.EventBannerViewerAdapterListener;
import com.chsapps.yt_hongjinyoung.utils.DelayListenerListener;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public class EventBannerViewer extends RelativeLayout {

    private Context context;

    private LayoutInflater inflater;
    private Unbinder unbinder;
    private View mainView;

    @BindView(R.id.layer_main)
    ViewGroup layer_main;
    @BindView(R.id.view_pager)
    InfiniteViewPager view_pager;
    @BindView(R.id.page_indicator)
    InkPageIndicator page_indicator;

    private int layoutType = 1;

    private InfinitePagerAdapter wrappedAdapter;
    private EventBannerViewerAdapter apdater;
    protected CompositeDisposable subscription = new CompositeDisposable();

    public EventBannerViewer(Context context) {
        super(context);
        initialize(context);
    }

    public EventBannerViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public EventBannerViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;

        apdater = new EventBannerViewerAdapter(context, new EventBannerViewerAdapterListener() {
            @Override
            public void selectBanner(AppEventData eventData) {

            }
        });
        apdater.insert(AppEventManager.getInstance().getEventListData(layoutType));
        view_pager.setAdapter(apdater);
        int count = apdater.getCount();
        if(count > 1) {
            wrappedAdapter = new InfinitePagerAdapter(apdater);
            view_pager.setAdapter(wrappedAdapter);
        } else {
            view_pager.setAdapter(apdater);
        }
        page_indicator.setViewPager(view_pager, apdater.getCount());

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshTimer();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        refreshTimer();
    }

    private void initialize(Context context) {
        this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_event_banner, this, true);
        unbinder = ButterKnife.bind(this, mainView);

        WindowManager wm =  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = width / 4 + Utils.dp(10);

        LayoutParams params = (LayoutParams) layer_main.getLayoutParams();
        if(params == null) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        }
        params.height = height;
        layer_main.setLayoutParams(params);
    }

    private void refreshTimer() {
        subscription.clear();
        Utils.delay(subscription, AppEventManager.getInstance().getRollingTime() * 1000, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                int currnetIdx = view_pager.getRealCurrentItem();

                view_pager.setCurrentItem(currnetIdx + 1, true);
                refreshTimer();
            }
        });
    }
}
