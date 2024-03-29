package com.chsapps.yt_hongjinyoung.app;

import com.chsapps.yt_hongjinyoung.api.model.response.HomeData;
import com.chsapps.yt_hongjinyoung.constants.PreferenceConstants;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.preference.Preference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Global {
    private final static String TAG = Global.class.getSimpleName();

    private static Global instance;

    public boolean isMainActivityAdShow = false;
    public boolean isPlayerClosed = false;

    public boolean isGenreTypeUpdate0 = false;
    public boolean isGenreTypeUpdate1 = false;

    public boolean isShowAdInterstitial = false;
    public boolean isNewGenreTypeGenre = false, isNewGenreTypeSinger = false;
    public int cntNewGenreTypeGenre = 0, cntNewGenreTypeSinger = 0;
    public Map<String, Long> mapCategoryIdLastClickTime = new HashMap<>();

    public boolean isHaveNotSinger = false;
    public boolean isExitPopupShare = true;

    public int cntMovePlaySongList = 0;

    /***
     * Preference setting.
     * */
    public static Global getInstance() {
        if(instance == null) {
            instance = new Global();
        }
        return instance;
    }

    public static void clear() {
        instance = null;
    }

    private int playerState = -2;
    public int getPlayerState() {
        return playerState;
    }
    public void setPlayerState(int val) {
        playerState = val;
    }



    public String getADID() {
        return Preference.getInstance().getString(PreferenceConstants.PREFERS_GOOGLE_AD_ID, "");
    }
    public void setADID(String value) {
        Preference.getInstance().putString(PreferenceConstants.PREFERS_GOOGLE_AD_ID, value);
    }

    private List<SongData> playSongListData = null;
    public List<SongData> getPlaySongListData() {
        return playSongListData;
    }
    public void setPlaySongListData(List<SongData> val) {
        playSongListData = val;
    }
    public int getDuration(String video_id) {
        try {
            for (SongData data : playSongListData) {
                if (data.getVideoid().equals(video_id)) {
                    return data.getDurationSec();
                }
            }
        } catch (Exception e) {
        }
        return 0;
    }
    private HomeData homeData;
    public void setHomeData(HomeData data) {
        homeData = data;
    }
    public String getMinimumVName() {
        if(homeData == null) {
            return null;
        }
        if(homeData.message.version_info != null && homeData.message.version_info.size() > 0) {
            return homeData.message.version_info.get(0).getMinium_version_name();
        }
        return null;
    }
    public String getLastestVName() {
        if(homeData == null) {
            return null;
        }

        if(homeData.message.version_info != null && homeData.message.version_info.size() > 0) {
            return homeData.message.version_info.get(0).getLasted_version_name();
        }
        return null;
    }
    public HomeData.VERSION_INFO getVersionInfo() {
        if(homeData == null) {
            return null;
        }

        try {
            return homeData.message.version_info.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    public List<HomeData.NOTICE> getNoticeInfo() {
        if(homeData == null) {
            return null;
        }

        if(homeData.message.notice != null && homeData.message.notice.size() > 0) {
            return homeData.message.notice;
        }
        return null;
    }
    public boolean isShowInterstitialAdInMainActivity() {
        return true;
//        try {
//            return homeData.message.ad_config.get(0).ad_is_show_at_launch == 1;
//        } catch (Exception e) {
//            return false;
//        }
    }
    public boolean isShowBannerAdInBottom() {
        return true;
//        try {
//            return homeData.message.ad_config.get(0).ad_is_show_bottom_banner== 1;
//        } catch (Exception e) {
//            return false;
//        }
    }
    public boolean isShowNativeBanner() {
        try {
            return homeData.message.ad_config.get(0).ad_is_show_native_banner== 1;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isShowAdFinishPlayed() {
        try {
            return homeData.message.ad_config.get(0).ad_is_show_at_finish_play == 1;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isShowExitAd() {
        try {
            return homeData.message.ad_config.get(0).ad_is_show_at_exit == 1;
        } catch (Exception e) {
            return false;
        }
    }
    public HomeData.AD_CONFIG getAdConfig() {
        try {
            return homeData.message.ad_config.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public int getGenreCategoryLayout() {
        try {
            return homeData.message.version_info.get(0).category_layout;
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 0 :  none random
     * 1 : random
     * */
    public int getRandomType() {
        return Preference.getInstance().getInteger(PreferenceConstants.PREFERS_PLAY_RANDOM_TYPE, 0);
    }
    public void setRandomType() {
        int curType = getRandomType();
        Preference.getInstance().putInteger(PreferenceConstants.PREFERS_PLAY_RANDOM_TYPE, curType == 0 ? 1 : 0);
    }

    /**
     * 0 : none repeat
     * 1 : repeat all
     * 2 : repeat one song
     * */
    public int getRepeatType() {
        return Preference.getInstance().getInteger(PreferenceConstants.PREFERS_PLAY_REPEAT_TYPE, 0);
    }
    public void setRepeatType() {
        int curType = getRepeatType() + 1;
        if(curType > 2) {
            curType = 0;
        }
        Preference.getInstance().putInteger(PreferenceConstants.PREFERS_PLAY_REPEAT_TYPE, curType);
    }

    public int getResizeTextSize() {
        return Preference.getInstance().getInteger(PreferenceConstants.PREFERS_RESIZE_TEXT_SIZE, 5);
    }

    public void setResizeTextSize(int val) {
        Preference.getInstance().putInteger(PreferenceConstants.PREFERS_RESIZE_TEXT_SIZE, val);
    }

    public boolean isShowYoutubePlayPolicy() {
        return Preference.getInstance().getBoolean(PreferenceConstants.PREFERS_IS_SHOW_YOUTUBBE_DLG, true);
    }

    public void setShowYoutubePlayPolicy() {
        Preference.getInstance().putBoolean(PreferenceConstants.PREFERS_IS_SHOW_YOUTUBBE_DLG, false);
    }

    public long getLastLaunchTime(int type) {
        return Preference.getInstance().getLong(PreferenceConstants.PREFERS_LAST_LAUNCH_TIME + type, 0);
    }

    public void setLastLaunchTime(int type) {
        Preference.getInstance().putLong(PreferenceConstants.PREFERS_LAST_LAUNCH_TIME + type, System.currentTimeMillis());
    }

    public boolean isExitPopupShare() {
        return Preference.getInstance().getBoolean(PreferenceConstants.PREFERS_IS_EXIT_DLG_SHARE_TYPE, true);
    }
    public void setExitPopupShare(boolean val) {
        Preference.getInstance().putBoolean(PreferenceConstants.PREFERS_IS_EXIT_DLG_SHARE_TYPE, val);
    }

    public boolean isFirstRecommendedApp() {
        return Preference.getInstance().getBoolean(PreferenceConstants.PREFERS_FIRST_RECOMMEND_APP, true);
    }
    public void setFirstRecommendedApp(boolean val) {
        Preference.getInstance().putBoolean(PreferenceConstants.PREFERS_FIRST_RECOMMEND_APP, val);
    }


    public int isFirstExitApp() {
        return Preference.getInstance().getInteger(PreferenceConstants.PREFERS_FIRST_EXIT_APP, 0);
    }
    public void setFirstExitApp(int val) {
        Preference.getInstance().putInteger(PreferenceConstants.PREFERS_FIRST_EXIT_APP, val);
    }



}
