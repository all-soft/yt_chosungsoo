package com.chsapps.yt_hongjinyoung.ui.fragment.singer;

import android.content.Context;
import android.content.Intent;
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

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.RequestServiceListener;
import com.chsapps.yt_hongjinyoung.api.RequestUtils;
import com.chsapps.yt_hongjinyoung.api.model.BaseAPIData;
import com.chsapps.yt_hongjinyoung.api.model.SongAPIData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseFragment;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.data.SingersData;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.db.StorageDBHelper;
import com.chsapps.yt_hongjinyoung.ui.activity.PlayerActivity;
import com.chsapps.yt_hongjinyoung.ui.adapter.VideoAdapter;
import com.chsapps.yt_hongjinyoung.ui.adapter.listener.SongAdapterHolderListener;
import com.chsapps.yt_hongjinyoung.ui.view.EndlessRecyclerOnScrollListener;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SingerVideoFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    public final static String TAG = SingerVideoFragment.class.getSimpleName();

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;
    @BindView(R.id.list_view)
    RecyclerView list_view;

    @BindView(R.id.view_empty)
    ViewGroup view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    private SingersData singersData;
    private VideoAdapter adapter;

    private static List<SongData> responseData = new ArrayList<>();

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

    public static SingerVideoFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        SingerVideoFragment fragment = new SingerVideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SingerVideoFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_singer_video, null);
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
        adapter = new VideoAdapter(parentActivity, new SongAdapterHolderListener() {
            @Override
            public void selected(SongData song) {
                song.log();
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

        requestSongList(false);
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


    private void requestSongList(boolean isForce) {
        if(!isForce && responseData.size() > 0) {
            adapter.insert(responseData);
            return;
        }
        if(RequestUtils.getInstanse().requestVideoSong(parentActivity, subscription, singersData.getVideo_keyword(), adapter.getCurrentPage() - 1, new RequestServiceListener() {
            @Override
            public void response(boolean is_success, BaseAPIData response) {
                if(is_success) {
                    if(response instanceof SongAPIData) {
                        List<SongData> list = ((SongAPIData)response).message;
                        if(list.size() > 1)
                        {
                            list.remove(0);
                            responseData.addAll(list);
                            adapter.insert(list);
                        }
                    }
                }
            }

            @Override
            public void complete() {
                refresh_layout.setRefreshing(false);

                dismissLoading();
                view_empty.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                list_view.setVisibility(adapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
            }
        })) {
            showLoading();
        }
    }


    @Override
    public void onRefresh() {
        responseData.clear();
        refresh_layout.setRefreshing(false);
        scrollListener.initialize();

        adapter.clear();
        requestSongList(true);
    }
}
