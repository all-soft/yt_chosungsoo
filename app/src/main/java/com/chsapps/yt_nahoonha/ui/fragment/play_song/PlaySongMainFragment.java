package com.chsapps.yt_nahoonha.ui.fragment.play_song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseFragment;
import com.chsapps.yt_nahoonha.constants.PlayerConstants;
import com.chsapps.yt_nahoonha.data.PlayTimeStatus;
import com.chsapps.yt_nahoonha.data.PlayerStatus;
import com.chsapps.yt_nahoonha.data.SongData;
import com.chsapps.yt_nahoonha.db.StorageDBHelper;
import com.chsapps.yt_nahoonha.service.YoutubePlayerService;
import com.chsapps.yt_nahoonha.ui.activity.BatterySaveActivity;
import com.chsapps.yt_nahoonha.ui.youtube_player.ConstantStrings;
import com.chsapps.yt_nahoonha.ui.youtube_player.JavaScript;
import com.chsapps.yt_nahoonha.ui.youtube_player.WebPlayer;
import com.chsapps.yt_nahoonha.utils.DelayListenerListener;
import com.chsapps.yt_nahoonha.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class PlaySongMainFragment extends BaseFragment {
    public final static String TAG = PlaySongMainFragment.class.getSimpleName();

    @BindView(R.id.layer_top)
    RelativeLayout layer_top;

    @BindView(R.id.play_button)
    ImageView play_button;

    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @BindView(R.id.icon_random)
    ImageView icon_random;

    @BindView(R.id.icon_repeat)
    ImageView icon_repeat;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tab_song)
    ViewGroup tab_song;
    @BindView(R.id.tv_song)
    TextView tv_song;
    @BindView(R.id.line_song)
    View line_song;
    @BindView(R.id.tab_review)
    ViewGroup tab_review;
    @BindView(R.id.tv_review)
    TextView tv_review;
    @BindView(R.id.line_review)
    View line_review;

    private WebView player;
    private ViewGroup parent;

    private int currentVideoTotalTime = 0;

    private int selectedTab = 0;
    private PlaySongMainPagerAdapter adapter;

    public static PlaySongMainFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        PlaySongMainFragment fragment = new PlaySongMainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlaySongMainFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_main_play_song, null);
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

        Utils.delay(subscription, 500, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                player = WebPlayer.getPlayer();
                if(player != null) {
                    setPlayerLayout();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void initialize() {
        EventBus.getDefault().register(this);

        setBackKey(true);
        setTitle(R.string.PlaySong);

        updatePlayMode();

        initViewPager();
        initSeekBar();
    }

    private void initViewPager() {
        updateTab(selectedTab);
        adapter = new PlaySongMainPagerAdapter(parentActivity.getSupportFragmentManager());
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

    private void initSeekBar() {
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
    }

    private void updateTab(int selectedTab) {
        this.selectedTab = selectedTab;

        View[] arrayLines = {
                line_song, line_review
        };
        TextView[] arrayText = {
                tv_song, tv_review
        };

        View[] arrayTab = {
                tab_song, tab_review
        };
        for(int i = 0 ; i < 2 ; i++) {
            if(selectedTab == i) {
                arrayLines[i].setVisibility(View.GONE);
                arrayText[i].setTextColor(parentActivity.getResources().getColor(R.color.colorPrimary));
                arrayTab[i].setBackgroundColor(parentActivity.getResources().getColor(R.color.White));
            } else {
                arrayLines[i].setVisibility(View.GONE);
                arrayText[i].setTextColor(parentActivity.getResources().getColor(R.color.White));
                arrayTab[i].setBackgroundColor(parentActivity.getResources().getColor(R.color.color_e9e9e9));
            }
        }
        viewPager.setCurrentItem(selectedTab);
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

    private void refreshPlayer() {
        subscription.add(Observable.empty()
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        player = WebPlayer.getPlayer();
                        if(player == null) {
                            refreshPlayer();
                        } else {
                            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                                    WindowManager.LayoutParams.MATCH_PARENT,
                                    WindowManager.LayoutParams.MATCH_PARENT
                            );

                            ViewGroup parent = (ViewGroup) player.getParent();
                            parent.removeView(player);
                            layer_top.addView(player, params);
                            WebPlayer.loadScript(JavaScript.playVideoScript());
                        }
                    }
                }));
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

    @OnClick(R.id.btn_save)
    public void onClick_tv_favorite() {
        String video_id = ConstantStrings.getCurrentSongVideoId();
        if(video_id == null) {
            return;
        }
        List<SongData> list = Global.getInstance().getPlaySongListData();
        for(SongData song : list) {
            if(song.getRealVideoId().equals(video_id)) {
                StorageDBHelper.getInstance().addData(song);
                Toast.makeText(parentActivity, R.string.success_to_save_storage, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @OnClick(R.id.btn_share)
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

    @OnClick(R.id.btn_save_mode)
    public void onClick_btn_save_mode() {
        startActivity(new Intent(context, BatterySaveActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePlayerStatus(PlayerStatus status) {
        if(status != null) {
            if(status.playStatus == 1) {
                play_button.setImageResource(R.drawable.pause);
            } else {
                play_button.setImageResource(R.drawable.play);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void playTime(PlayTimeStatus status) {
        currentVideoTotalTime = status.totalTime;
        int current = status.currentTime == 0  || status.totalTime == 0 ? 0 : status.currentTime * 100 / status.totalTime;
        seekBar.setProgress(current);
    }

    private void scrollListView(final int position) {
        if(position == -1) {
            return;
        }

        Utils.delay(subscription, 1000, new DelayListenerListener() {
            @Override
            public void delayedTime() {

            }
        });
    }

    private void updatePlayMode() {
        int randomType = Global.getInstance().getRandomType();
        int repeatType = Global.getInstance().getRepeatType();

        /**
         * 0 : none repeat
         * 1 : repeat all
         * 2 : repeat one song
         * */
        icon_random.setBackgroundResource(randomType == 0 ? R.drawable.none_shuffle : R.drawable.ic_shuffle);
        switch (repeatType) {
            case 0:
                icon_repeat.setBackgroundResource(R.drawable.none_repeat);
                break;
            case 1:
                icon_repeat.setBackgroundResource(R.drawable.ic_repeat);
                break;
            case 2:
                icon_repeat.setBackgroundResource(R.drawable.ic_repeat_one);
                break;
        }
    }

    public class PlaySongMainPagerAdapter extends FragmentStatePagerAdapter {

        public PlaySongMainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PlaySongListFragment.newInstance(getArguments());
                case 1:
                    return PlaySongReviewFragment.newInstance(getArguments());
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }
}
