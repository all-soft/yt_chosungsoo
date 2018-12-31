package com.chsapps.yt_nahoonha.ui.fragment.singer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.api.RequestServiceListener;
import com.chsapps.yt_nahoonha.api.RequestUtils;
import com.chsapps.yt_nahoonha.api.model.BaseAPIData;
import com.chsapps.yt_nahoonha.api.model.SongAPIData;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseFragment;
import com.chsapps.yt_nahoonha.constants.ParamConstants;
import com.chsapps.yt_nahoonha.data.SingersData;
import com.chsapps.yt_nahoonha.data.SongData;
import com.chsapps.yt_nahoonha.db.StorageDBHelper;
import com.chsapps.yt_nahoonha.ui.activity.PlayerActivity;
import com.chsapps.yt_nahoonha.ui.adapter.SongAdapter;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SongAdapterHolderListener;
import com.chsapps.yt_nahoonha.ui.view.EndlessRecyclerOnScrollListener;
import com.chsapps.yt_nahoonha.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SingerPopularFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    public final static String TAG = SingerPopularFragment.class.getSimpleName();

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;
    @BindView(R.id.list_view)
    RecyclerView list_view;

    @BindView(R.id.view_empty)
    ViewGroup view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    @BindView(R.id.btn_medley)
    TextView btn_medley;
    @BindView(R.id.btn_hit)
    TextView btn_hit;
    @BindView(R.id.btn_all_songs)
    TextView btn_all_songs;

    private int songsType = 0;
    private static List<SongData> responseData = new ArrayList<>();
    private SingersData singersData;
    private SongAdapter adapter;

    private EndlessRecyclerOnScrollListener scrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore(int currentPage) {
            if(currentPage - 1 != adapter.getCurrentPage()) {
                adapter.setCurrentPage(currentPage - 1);
                requestSongList(true);
            }
        }
    };

    public static void clearData() {
        responseData.clear();
    }

    public static SingerPopularFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        SingerPopularFragment fragment = new SingerPopularFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SingerPopularFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_singer_popular, null);
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
        tv_empty_title.setText(R.string.no_songs);
        singersData = getArguments().getParcelable(ParamConstants.PARAM_SINGER_DATA);
        Utils.initLayoutListView(parentActivity, list_view);
        adapter = new SongAdapter(parentActivity, new SongAdapterHolderListener() {
            @Override
            public void selected(SongData song) {
                Global.getInstance().setPlaySongListData(adapter.getSongDataList());

                Intent intent = new Intent(parentActivity, PlayerActivity.class);
                intent.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, song.song_idx);
                parentActivity.startActivity(intent);
            }

            @Override
            public void save(SongData song) {
                if(StorageDBHelper.getInstance().addData(song)) {
                    Toast.makeText(parentActivity, R.string.success_to_save_storage, Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
        list_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(this);

        scrollListener.initialize();
        list_view.addOnScrollListener(scrollListener);

        requestSongList();
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
        parentActivity.getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void requestSongList(boolean isForceRequest) {
        if(!isForceRequest && responseData.size() > 0) {
            adapter.insert(responseData);
            return;
        }
        if(RequestUtils.getInstanse().requestSongList(parentActivity, subscription, singersData.getCategory_idx(), adapter.getCurrentPage() - 1, songsType + 1, new RequestServiceListener() {
            @Override
            public void response(boolean is_success, BaseAPIData response) {
                if(is_success) {
                    if(response instanceof SongAPIData) {
                        responseData.addAll(((SongAPIData) response).message);
                        adapter.insert(((SongAPIData) response).message);
                    }
                }
            }

            @Override
            public void complete() {
                refresh_layout.setRefreshing(false);

                dismissLoading();
                view_empty.setVisibility(adapter.getItemDataCount() == 0 ? View.VISIBLE : View.GONE);
                list_view.setVisibility(adapter.getItemDataCount() == 0 ? View.GONE: View.VISIBLE);
            }
        })) {
            showLoading();
        }
    }

    private void requestSongList() {
        requestSongList(false);
    }

    @Override
    public void onRefresh() {
        responseData.clear();
        refresh_layout.setRefreshing(false);
        scrollListener.initialize();

        adapter.clear();
        requestSongList(true);
    }

    @OnClick(R.id.btn_medley)
    public void onClick_btn_medley() {
        if(songsType != 0) {
            updatePopularSongsTap(0);
            responseData.clear();
            adapter.clear();
            requestSongList();
        }
    }

    @OnClick(R.id.btn_hit)
    public void onClick_btn_hit() {
        if(songsType != 1) {
            updatePopularSongsTap(1);
            responseData.clear();
            adapter.clear();
            requestSongList();
        }
    }

    @OnClick(R.id.btn_all_songs)
    public void onClick_btn_all_songs() {
        if(songsType != 2) {
            updatePopularSongsTap(2);
            responseData.clear();
            adapter.clear();
            requestSongList();
        }
    }

    private void updatePopularSongsTap(int type) {
        songsType = type;

        btn_medley.setBackgroundResource(R.drawable.xml_singers_popular_type_btn_none_selected);
        btn_medley.setTypeface(btn_medley.getTypeface(), Typeface.NORMAL);
        btn_medley.setTextColor(getResources().getColor(R.color.color_5c5c5c));
        btn_hit.setBackgroundResource(R.drawable.xml_singers_popular_type_btn_none_selected);
        btn_hit.setTypeface(btn_hit.getTypeface(), Typeface.NORMAL);
        btn_hit.setTextColor(getResources().getColor(R.color.color_5c5c5c));
        btn_all_songs.setBackgroundResource(R.drawable.xml_singers_popular_type_btn_none_selected);
        btn_all_songs.setTypeface(btn_all_songs.getTypeface(), Typeface.NORMAL);
        btn_all_songs.setTextColor(getResources().getColor(R.color.color_5c5c5c));
        switch (type){
            case 0: {
                btn_medley.setBackgroundResource(R.drawable.xml_singers_popular_type_btn);
                btn_medley.setTypeface(btn_medley.getTypeface(), Typeface.BOLD);
                btn_medley.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
            case 1: {
                btn_hit.setBackgroundResource(R.drawable.xml_singers_popular_type_btn);
                btn_hit.setTypeface(btn_hit.getTypeface(), Typeface.BOLD);
                btn_hit.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
            case 2: {
                btn_all_songs.setBackgroundResource(R.drawable.xml_singers_popular_type_btn);
                btn_all_songs.setTypeface(btn_all_songs.getTypeface(), Typeface.BOLD);
                btn_all_songs.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
        }
    }
}
