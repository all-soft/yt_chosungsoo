package com.chsapps.yt_nahoonha.ui.fragment.singer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseFragment;
import com.chsapps.yt_nahoonha.constants.ParamConstants;
import com.chsapps.yt_nahoonha.data.SingersData;
import com.chsapps.yt_nahoonha.ui.activity.MainActivity;
import com.chsapps.yt_nahoonha.ui.activity.NavigationListener;
import com.chsapps.yt_nahoonha.ui.activity.SearchActivity;
import com.chsapps.yt_nahoonha.ui.view.popup.AutoClosePopup;
import com.chsapps.yt_nahoonha.ui.view.popup.ResizeTextSizePopup;
import com.chsapps.yt_nahoonha.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class SingerMainFragment extends BaseFragment implements NavigationListener {
    public final static String TAG = SingerMainFragment.class.getSimpleName();

    @BindView(R.id.tab_popular)
    ViewGroup tab_popular;
    @BindView(R.id.tv_popular)
    TextView tv_popular;
    @BindView(R.id.line_popular)
    View line_popular;

    @BindView(R.id.tab_video)
    ViewGroup tab_video;
    @BindView(R.id.tv_video)
    TextView tv_video;
    @BindView(R.id.line_video)
    View line_video;

    @BindView(R.id.tab_news)
    ViewGroup tab_news;
    @BindView(R.id.tv_news)
    TextView tv_news;
    @BindView(R.id.line_news)
    View line_news;

    @BindView(R.id.tab_storage)
    ViewGroup tab_storage;
    @BindView(R.id.tv_storage)
    TextView tv_storage;
    @BindView(R.id.line_storage)
    View line_storage;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private SingersData singersData;
    private int selectedTab = 0;
    private MainViewPagerAdapter adapter;

    public static SingerMainFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        SingerMainFragment fragment = new SingerMainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SingerMainFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_singer_main, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initialize() {
        Global.staticSingersData = singersData = getArguments().getParcelable(ParamConstants.PARAM_SINGER_DATA);
        if(singersData == null) {
            parentActivity.finish();
            return;
        }
        setBackKey(true);
        setTitle(singersData.getCategory_name() + Utils.getText(R.string.listen_song));
        parentActivity.setAlaramTitle(singersData.getCategory_name() + Utils.getText(R.string.notice_allow));
        parentActivity.setNavigationListener(this);
        updateTab(0);

        adapter = new MainViewPagerAdapter(parentActivity.getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selectedTab);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void clearMemory() {
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        parentActivity.getMenuInflater().inflate(R.menu.singer_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_share:
                Utils.showShare(parentActivity, Utils.getText(R.string.title_share_application), Utils.getText(R.string.message_share_application));
                break;
            case R.id.btn_search:
                startActivity(new Intent(parentActivity, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTab(int selectedTab) {
        this.selectedTab = selectedTab;

        View arrayLines[] = {
                line_popular, line_video, line_news, line_storage
        };
        TextView arrayText[] = {
                tv_popular, tv_video, tv_news, tv_storage
        };

        for(int i = 0 ; i < 4 ; i++) {
            if(selectedTab == i) {
                arrayLines[i].setVisibility(View.VISIBLE);
                arrayText[i].setTextColor(parentActivity.getResources().getColor(R.color.colorPrimary));
            } else {
                arrayLines[i].setVisibility(View.GONE);
                arrayText[i].setTextColor(parentActivity.getResources().getColor(R.color.color_a3a3a3));
            }
        }
        viewPager.setCurrentItem(selectedTab);
    }

    @OnClick(R.id.tab_popular)
    public void onClick_tab_popular() {
        updateTab(0);
    }
    @OnClick(R.id.tab_video)
    public void onClick_tab_video() {
        updateTab(1);
    }
    @OnClick(R.id.tab_news)
    public void onClick_tab_news() {
        updateTab(2);
    }
    @OnClick(R.id.tab_storage)
    public void onClick_tab_storage() {
        updateTab(3);
    }

    @Override
    public void onChanged_switch(boolean isChecked) {

    }

    @Override
    public void onClick_navigation(int type) {
        switch (type) {
            case 0:
                startActivity(new Intent(parentActivity, MainActivity.class));
                break;
            case 1:
                Utils.showShare(parentActivity, Utils.getText(R.string.title_share_application), Utils.getText(R.string.message_share_application));
                break;
            case 2:
                startActivity(new Intent(parentActivity, SearchActivity.class));
                break;
            case 3: {
                ResizeTextSizePopup dlg = new ResizeTextSizePopup(parentActivity, new ResizeTextSizePopup.ResizeTextSizePopupListener() {
                    @Override
                    public void onChangedTextSize(int size) {
                        parentActivity.resizeTextSize(true, size);
                    }
                });
                dlg.show();
            }
            break;
            case 4: {
                AutoClosePopup dlg = new AutoClosePopup(parentActivity);
                dlg.show();
            }
            break;
            case 5:
                Utils.moveMarket();
                break;
            case 6:
                break;
        }
        parentActivity.closeDrawer();
    }

    public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

        public MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SingerPopularFragment.newInstance(getArguments());
                case 1:
                    return SingerVideoFragment.newInstance(getArguments());
                case 2:
                    return SingerNewsFragment.newInstance(getArguments());
                case 3:
                    return SingerStorageFragment.newInstance(getArguments());
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }
}
