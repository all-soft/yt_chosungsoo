package com.chsapps.yt_hongjinyoung.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.data.PlayTimeStatus;
import com.chsapps.yt_hongjinyoung.data.PlayerStatus;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.db.StorageDBHelper;
import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;
import com.chsapps.yt_hongjinyoung.ui.activity.BatterySaveActivity;
import com.chsapps.yt_hongjinyoung.ui.activity.FullScreenPlayerActivity;
import com.chsapps.yt_hongjinyoung.ui.adapter.PlaySongAdapter;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.SongListHolderListener;
import com.chsapps.yt_hongjinyoung.ui.base.BaseFragment;
import com.chsapps.yt_hongjinyoung.ui.view.player.ConstantStrings;
import com.chsapps.yt_hongjinyoung.ui.view.player.JavaScript;
import com.chsapps.yt_hongjinyoung.ui.view.player.WebPlayer;
import com.chsapps.yt_hongjinyoung.ui.view.popup.AutoClosePopup;
import com.chsapps.yt_hongjinyoung.utils.DelayListenerListener;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

public class PlaySongListFragment extends BaseFragment implements SongListHolderListener {
    public final static String TAG = PlaySongListFragment.class.getSimpleName();

    private PlaySongAdapter adapter;

    @BindView(R.id.layer_top)
    RelativeLayout layer_top;
    @BindView(R.id.play_list_view)
    RecyclerView play_list_view;

    @BindView(R.id.play_button)
    ImageView play_button;

    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @BindView(R.id.icon_random)
    ImageView icon_random;
    @BindView(R.id.icon_repeat)
    ImageView icon_repeat;

    @BindView(R.id.tv_repeat)
    TextView tv_repeat;
    @BindView(R.id.tv_random)
    TextView tv_random;


    private WebView player;
    private ViewGroup parent;

    private int currentVideoTotalTime = 0;

    private CompositeDisposable alarmSubscription = new CompositeDisposable();

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
        return mainView = inflater.inflate(R.layout.fragment_play_song_list, null);
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

        player = WebPlayer.getPlayer();
        if(player != null) {
            setPlayerLayout();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        WebView WebYoutubePlayer = WebPlayer.getPlayer();
        if (WebYoutubePlayer != null && YoutubePlayerService.isVideoPlaying) {
//            LogUtil.e("HSSEO", "PAUSE");
            Intent i = new Intent(context, YoutubePlayerService.class);
            i.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE);
            context.startService(i);
        }
    }

    @Override
    public void initialize() {
        EventBus.getDefault().register(this);

        setHasOptionsMenu(true);
        setBackKey(true);
        setTitle(R.string.play_song);

        updatePlayMode();
        initListView();
        adapter.insertSongDataList(Global.getInstance().getPlaySongListData());

        int playSongIdx = getArguments().getInt(ParamConstants.PARAM_PLAY_SONG_IDX, -1);
        if(playSongIdx != -1) {
            adapter.playSong(playSongIdx);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    YoutubePlayerService.seek(currentVideoTotalTime * progress / 100, true);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        layer_player.setVisibility(View.GONE);
        onClick_layer_play();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        parentActivity.getMenuInflater().inflate(
                R.menu.menu_play_song,
                actionBarMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_auto_close) {
            AutoClosePopup dlg = new AutoClosePopup(parentActivity, new AutoClosePopup.onAutoClosePopupEventListener() {
                @Override
                public void autoClose(int msTime) {
                    alarmSubscription.remove(alarmSubscription);
                    Utils.delay(alarmSubscription, msTime, new DelayListenerListener() {
                        @Override
                        public void delayedTime() {
                            int tag = 0;
                            try {
                                tag = (int) play_button.getTag();
                            } catch (Exception e) {

                            }
                            if(tag == 0) {
                                try {
                                    Intent i = new Intent(parentActivity, YoutubePlayerService.class);
                                    i.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE);
                                    parentActivity.startService(i);
                                } catch (Exception e) {
                                }
                            }
                        }
                    });
                }
            });
            dlg.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clearMemory() {
        EventBus.getDefault().unregister(this);

        if(alarmSubscription != null && !alarmSubscription.isDisposed()) {
            alarmSubscription.dispose();
        }
        try {
            if (YoutubePlayerService.isVideoPlaying) {
                Intent i = new Intent(context, YoutubePlayerService.class);
                i.setAction(PlayerConstants.ACTION.ACTION_CLOSE_PLAYER);
                context.stopService(i);
            }
        } catch(Exception e) {
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePlayerStatus(PlayerStatus status) {
        if(status != null) {
            scrollListView(adapter.setPlayingVideoId(ConstantStrings.getCurrentSongVideoId()));
            if(status.playStatus == 1) {
                play_button.setTag(1);
                play_button.setImageResource(R.drawable.pause);
            } else {
                play_button.setTag(0);
                play_button.setImageResource(R.drawable.play);
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void playTime(PlayTimeStatus status) {
        currentVideoTotalTime = status.totalTime;
        int current = status.currentTime == 0 ? 0 : status.currentTime * 100 / status.totalTime;
        seekBar.setProgress(current);
    }

    private void initListView() {
        if(adapter == null) {
            adapter = new PlaySongAdapter(parentActivity, this);
        }
        play_list_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        play_list_view.setHasFixedSize(true);
        play_list_view.setAdapter(adapter);
    }

    private void refreshPlayer() {
        Utils.delay(subscription, 100, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                player = WebPlayer.getPlayer();
                if(player == null) {
                    refreshPlayer();
                } else {
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                    );

                    try {
                        ViewGroup parent = (ViewGroup) player.getParent();
                        parent.removeView(player);
                    } catch (Exception e) {
                    }
                    layer_top.addView(player, params);
                    WebPlayer.loadScript(JavaScript.playVideoScript());
                }
            }
        });
    }

    private void setPlayerLayout() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );

        parent = (ViewGroup) player.getParent();
        if(parent != null)
            parent.removeView(player);

        layer_top.addView(player, params);
        WebPlayer.loadScript(JavaScript.playVideoScript());

        Intent i = new Intent(parentActivity, YoutubePlayerService.class);
        i.putExtra("ACTION_SET_YOUTUBE_PLAYER_TYPE", PlayerConstants.PLAYER_TYPE_IN_FRAGMENT);
        i.setAction(PlayerConstants.ACTION.ACTION_SET_YOUTUBE_PLAYER_TYPE);
        parentActivity.startService(i);
    }

    @Override
    public void playSong(SongData song) {
        refreshPlayer();

        Intent i = new Intent(parentActivity, YoutubePlayerService.class);
        i.putExtra("VID_ID", song.videoid);
        i.putExtra("PLAY_LIST_IDS", adapter.getPlayList());
        i.putExtra("PLAY_LIST_SONG_INDEX", adapter.getPlaySongIdxList());
        i.putExtra("ACTION_SET_YOUTUBE_PLAYER_TYPE", PlayerConstants.PLAYER_TYPE_IN_FRAGMENT);

        i.setAction(PlayerConstants.ACTION.ACTION_START_FORGROUNT_WEB);
        parentActivity.startService(i);

        scrollListView(adapter.setPlayingVideoId(song.getVideoid()));
    }

    @Override
    public void deleteSong(SongData song) {

    }

    @Override
    public void shareSong(SongData song) {

    }

    @Override
    public void saveSong(SongData song) {
        StorageDBHelper.getInstance().addData(song);
        Toast.makeText(parentActivity, "보관함에 추가하였습니다.", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.previous_button)
    public void onClick_previous_button() {
        Intent i = new Intent(parentActivity, YoutubePlayerService.class);
        i.setAction(PlayerConstants.ACTION.ACTION_PLAY_PREV_SONG);
        parentActivity.startService(i);
    }
    @OnClick(R.id.play_button)
    public void onClick_play_button() {
        Intent i = new Intent(parentActivity, YoutubePlayerService.class);
        i.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE);
        parentActivity.startService(i);
    }
    @OnClick(R.id.next_button)
    public void onClick_next_button() {
        Intent i = new Intent(parentActivity, YoutubePlayerService.class);
        i.setAction(PlayerConstants.ACTION.ACTION_PLAY_NEXT_SONG);
        parentActivity.startService(i);
    }
    @OnClick(R.id.tv_favorite)
    public void onClick_tv_favorite() {
        String video_id = ConstantStrings.getCurrentSongVideoId();
        if(video_id == null) {
            return;
        }
        List<SongData> list = Global.getInstance().getPlaySongListData();
        for(SongData song : list) {
            if(song.videoid.equals(video_id)) {
                StorageDBHelper.getInstance().addData(song);
                Toast.makeText(parentActivity, R.string.insert_to_favorite_song, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @OnClick(R.id.tv_share)
    public void onClic_tv_share() {
        Utils.showShare(parentActivity, Utils.getString(R.string.title_share_application), Utils.getString(R.string.message_share_application));
    }

    @OnClick(R.id.btn_random)
    public void onClick_btn_random() {
        Global.getInstance().setRandomType();
        updatePlayMode();
    }

    @OnClick(R.id.btn_repeat)
    public void onClick_btn_repeat() {
        Global.getInstance().setRepeatType();
        updatePlayMode();
    }

    @OnClick(R.id.tv_save)
    public void onClick_tv_save() {
        startActivity(new Intent(context, BatterySaveActivity.class));
    }

    private void scrollListView(final int position) {
        if(position == -1) {
            return;
        }

        play_list_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                play_list_view.scrollToPosition(position);
            }
        }, 1000);
    }

    @OnClick(R.id.btn_full_screen)
    public void onClick_btn_full_screen() {
        startActivity(new Intent(parentActivity, FullScreenPlayerActivity.class));
    }

    @BindView(R.id.layer_player)
    ViewGroup layer_player;

    @OnClick(R.id.layer_play)
    public void onClick_layer_play() {
        if(layer_player.getVisibility() == View.VISIBLE) {
            layer_player.setVisibility(View.GONE);
            subscription.remove(subscription);
        } else {
            layer_player.setVisibility(View.VISIBLE);
            Utils.delay(subscription, 2000, new DelayListenerListener() {
                @Override
                public void delayedTime() {
                    layer_player.setVisibility(View.GONE);
                }
            });
        }
    }

    private void updatePlayMode() {
        int randomType = Global.getInstance().getRandomType();
        int repeatType = Global.getInstance().getRepeatType();

        /**
         * 0 : none repeat
         * 1 : repeat all
         * 2 : repeat one song
         * */
        icon_random.setBackgroundResource(randomType == 0 ? R.drawable.none_shuffle : R.drawable.shuffle);
        tv_random.setText(randomType == 0 ? R.string.play_no_shuffle : R.string.play_shuffle);
        switch (repeatType) {
            case 0:
                icon_repeat.setBackgroundResource(R.drawable.none_repeat);
                tv_repeat.setText(R.string.repeat_none);
                break;
            case 1:
                icon_repeat.setBackgroundResource(R.drawable.repeat);
                tv_repeat.setText(R.string.repeat_all_songs);
                break;
            case 2:
                icon_repeat.setBackgroundResource(R.drawable.repeat_one);
                tv_repeat.setText(R.string.repeat_one_song);
                break;
        }
    }
}
