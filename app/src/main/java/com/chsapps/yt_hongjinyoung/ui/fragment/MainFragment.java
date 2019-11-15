package com.chsapps.yt_hongjinyoung.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.ui.activity.SearchSongActivity;
import com.chsapps.yt_hongjinyoung.ui.base.BaseFragment;
import com.chsapps.yt_hongjinyoung.ui.view.popup.ResizeTextSizePopup;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment {
    public final static String TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tab_popular)
    ViewGroup tab_popular;
    @BindView(R.id.tab_newest)
    ViewGroup tab_newest;
    @BindView(R.id.tab_genre)
    ViewGroup tab_genre;
    @BindView(R.id.tab_storage)
    ViewGroup tab_storage;

    @BindView(R.id.tv_popular)
    TextView tv_popular;
    @BindView(R.id.tv_newest)
    TextView tv_newest;
    @BindView(R.id.tv_genre)
    TextView tv_genre;
    @BindView(R.id.tv_storage)
    TextView tv_storage;

    @BindView(R.id.line_popular)
    View line_popular;
    @BindView(R.id.line_newest)
    View line_newest;
    @BindView(R.id.line_genre)
    View line_genre;
    @BindView(R.id.line_storage)
    View line_storage;

    @BindView(R.id.tv_badge_genre)
    TextView tv_badge_genre;

    private int selectedTabIdx = 0;

    private MainViewPagerAdapter viewPagerAdapter;

    public static MainFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_main, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        setHasOptionsMenu(true);
        initViewPager();
        updateTab(0);
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
        parentActivity.getMenuInflater().inflate(
                R.menu.menu_search,
                actionBarMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_search) {
            startActivity(new Intent(parentActivity, SearchSongActivity.class));
        } else if(item.getItemId() == R.id.btn_scale_font) {
            ResizeTextSizePopup dlg = new ResizeTextSizePopup(parentActivity, new ResizeTextSizePopup.ResizeTextSizePopupListener() {
                @Override
                public void onChangedTextSize(int size) {
                    parentActivity.resizeTextSize(true, size);
                }
            });
            dlg.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tab_popular, R.id.tab_newest, R.id.tab_genre, R.id.tab_storage})
    public void onClick_tab(View v) {
        int idx = 0;
        switch (v.getId()) {
            case R.id.tab_popular:
                idx = 0;
                break;
            case R.id.tab_newest:
                idx = 1;
                break;
            case R.id.tab_genre:
                idx = 2;
                break;
            case R.id.tab_storage:
                idx = 3;
                break;
            default:
                return;
        }

        updateTab(idx);
    }

    public MainGenreFragment genreFragment;
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
                    return MainPopularFragment.newInstance();
                case 1:
                    return MainNewestFragment.newInstance();
                case 2:
                    genreFragment = MainGenreFragment.newInstance();
                    return genreFragment;
                case 3:
                    return MainStorageFragment.newInstance();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab_popular);
                case 1:
                    return getResources().getString(R.string.tab_newest);
                case 2:
                    return getResources().getString(R.string.tab_genre);
                case 3:
                    return getResources().getString(R.string.tab_storage);
            }
            return super.getPageTitle(position);
        }
    }

    private void initViewPager() {

        int newCnt = Global.getInstance().cntNewGenreTypeGenre + Global.getInstance().cntNewGenreTypeSinger;
        if(newCnt > 0) {
            tv_badge_genre.setVisibility(View.VISIBLE);
            tv_badge_genre.setText(String.valueOf(newCnt));
        } else {
            tv_badge_genre.setVisibility(View.GONE);
        }


        viewPagerAdapter = new MainViewPagerAdapter(parentActivity.getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(selectedTabIdx);
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

    public void updateTab(int selected_tab_id) {
        if(selected_tab_id == 2)
            Global.getInstance().isGenreTypeUpdate0 = true;

        viewPager.setCurrentItem(selected_tab_id);

        selectedTabIdx = selected_tab_id;
        TextView[] arrTextView= {tv_popular, tv_newest, tv_genre, tv_storage};
        View[] arrLine= {line_popular, line_newest, line_genre, line_storage};
        for(int i = 0 ; i < arrTextView.length ; i++) {
            arrTextView[i].setTextColor(
                    getResources().getColor(
                            i == selectedTabIdx ? R.color.White: R.color.color_cccccc));

            arrLine[i].setVisibility(
                    i == selectedTabIdx ? View.VISIBLE : View.GONE);
        }
        setTitle(viewPagerAdapter.getPageTitle(selectedTabIdx).toString());
    }

    public void updateGenreType(int type) {
        if(genreFragment.setType(type))
            genreFragment.requestCategory();
    }
}
