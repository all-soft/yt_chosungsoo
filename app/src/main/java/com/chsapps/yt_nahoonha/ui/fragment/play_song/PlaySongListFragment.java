package com.chsapps.yt_nahoonha.ui.fragment.play_song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
import com.chsapps.yt_nahoonha.constants.PlayerConstants;
import com.chsapps.yt_nahoonha.data.PlayerStatus;
import com.chsapps.yt_nahoonha.data.SongData;
import com.chsapps.yt_nahoonha.service.YoutubePlayerService;
import com.chsapps.yt_nahoonha.ui.adapter.PlayingAdapter;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SongAdapterHolderListener;
import com.chsapps.yt_nahoonha.ui.youtube_player.ConstantStrings;
import com.chsapps.yt_nahoonha.utils.DelayListenerListener;
import com.chsapps.yt_nahoonha.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlaySongListFragment extends BaseFragment {
    public final static String TAG = PlaySongListFragment.class.getSimpleName();

    @BindView(R.id.list_view)
    RecyclerView list_view;

    @BindView(R.id.view_empty)
    ViewGroup view_empty;

    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    private PlayingAdapter adapter;
    private List<SongData> arrSongs = new ArrayList<>();

    public static PlaySongListFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        PlaySongListFragment fragment = new PlaySongListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlaySongListFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_list_play_song, null);
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
        EventBus.getDefault().register(this);

        tv_empty_title.setText(R.string.no_songs);
        Utils.initLayoutListView(parentActivity, list_view);
        adapter = new PlayingAdapter(parentActivity, new SongAdapterHolderListener() {
            @Override
            public void selected(SongData song) {

                //refreshPlayer();

                song.log();

                Intent i = new Intent(parentActivity, YoutubePlayerService.class);
                i.putExtra(ParamConstants.PARAM_VID_ID, song.getRealVideoId());
                i.putExtra(ParamConstants.PARAM_LIST_IDS, adapter.getPlayList());
                i.putExtra(ParamConstants.PARAM_LIST_SONG_INDEX, adapter.getPlaySongIdxList());
                i.putExtra(ParamConstants.PARAM_ACTION_SET_YOUTUBE_PLAYER_TYPE, PlayerConstants.PLAYER_TYPE_IN_FRAGMENT);

                i.setAction(PlayerConstants.ACTION.ACTION_START_FORGROUNT_WEB);
                parentActivity.startService(i);

                scrollListView(adapter.setPlayingVideoId(song.getRealVideoId()));
            }

            @Override
            public void save(SongData song) {

            }
        });
        list_view.setAdapter(adapter);
        arrSongs = Global.getInstance().getPlaySongListData();
        adapter.insert(arrSongs);

        int playSongIdx = getArguments().getInt(ParamConstants.PARAM_PLAY_SONG_IDX, -1);
        if(playSongIdx != -1) {
            adapter.playSong(playSongIdx);
        }
    }

    @Override
    public void clearMemory() {
        EventBus.getDefault().unregister(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePlayerStatus(PlayerStatus status) {
        if(status != null) {
            scrollListView(adapter.setPlayingVideoId(ConstantStrings.getCurrentSongVideoId()));
        }
    }

    private void scrollListView(final int position) {
        if(position == -1) {
            return;
        }

        Utils.delay(subscription, 1000, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                list_view.scrollToPosition(position);
            }
        });
    }

}
