package com.chsapps.yt_nahoonha.app;

import android.content.SharedPreferences;

import com.chsapps.yt_nahoonha.common.BuildConfig;
import com.chsapps.yt_nahoonha.api.model.HomeData;
import com.chsapps.yt_nahoonha.constants.PreferenceConstants;
import com.chsapps.yt_nahoonha.data.SingersData;
import com.chsapps.yt_nahoonha.data.SongData;
import com.chsapps.yt_nahoonha.preference.Preference;
import com.securepreferences.SecurePreferences;

import java.util.List;

public class Global {
    private final static String TAG = Global.class.getSimpleName();

    private static Global instance;
    public static SingersData staticSingersData;
    /***
     * Preference setting.
     * */
    private SharedPreferences prefs = new SecurePreferences(AllSoft.getContext());

    public static Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    public boolean isMainActivityAdShow = false;

    private HomeData homeData;

    public void setHomeData(HomeData data) {
        homeData = data;
    }

    public String getMinimumVName() {
        if (homeData == null) {
            return null;
        }
        if (homeData.message.version_info != null && homeData.message.version_info.size() > 0) {
            return homeData.message.version_info.get(0).getMinium_version_name();
        }
        return null;
    }

    public String getLastestVName() {
        if (homeData == null) {
            return null;
        }

        if (homeData.message.version_info != null && homeData.message.version_info.size() > 0) {
            return homeData.message.version_info.get(0).getLasted_version_name();
        }
        return null;
    }

    public List<HomeData.NOTICE> getNoticeInfo() {
        if (homeData == null) {
            return null;
        }

        if (homeData.message.notice != null && homeData.message.notice.size() > 0) {
            return homeData.message.notice;
        }
        return null;
    }

    public boolean isShowInterstitialAdInMainActivity() {
        try {
            return homeData.message.ad_config.get(0).ad_is_show_at_launch == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isShowBannerAdInBottom() {
        try {
            return homeData.message.ad_config.get(0).ad_is_show_bottom_banner == 1;
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


    public String getADID() {
        return Preference.getInstance().getString(PreferenceConstants.PREFERS_GOOGLE_AD_ID, "");
    }

    public void setADID(String value) {
        Preference.getInstance().putString(PreferenceConstants.PREFERS_GOOGLE_AD_ID, value);
    }

    /**
     * 0 :  none random
     * 1 : random
     */
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
     */
    public int getRepeatType() {
        return Preference.getInstance().getInteger(PreferenceConstants.PREFERS_PLAY_REPEAT_TYPE, 0);
    }

    public void setRepeatType() {
        int curType = getRepeatType() + 1;
        if (curType > 2) {
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

    private int playerState = -2;

    public int getPlayerState() {
        return playerState;
    }

    public void setPlayerState(int val) {
        playerState = val;
    }

    public int getDuration(String video_id) {
        for (SongData data : playSongListData) {
            if (data.getRealVideoId().equals(video_id)) {
                return data.getDurationSec();
            }
        }
        return 0;
    }


    public boolean isShowAppPolicyDialog() {
        return Preference.getInstance().getBoolean(PreferenceConstants.PREFERS_IS_SHOW_APP_POLICY_DIALOG, false);
    }

    public void setShowAppPolicyDialog(boolean val) {
        Preference.getInstance().putBoolean(PreferenceConstants.PREFERS_IS_SHOW_APP_POLICY_DIALOG, val);
    }

    private List<SongData> playSongListData = null;

    public List<SongData> getPlaySongListData() {
        return playSongListData;
    }

    public void setPlaySongListData(List<SongData> val) {
        playSongListData = val;
    }


    public boolean isAllowNewNotice() {
        return Preference.getInstance().getBoolean("IS_ALLOW_NEW_NOTICE", true);
    }

    public void setAllowNewNotice(boolean value) {
        Preference.getInstance().putBoolean("IS_ALLOW_NEW_NOTICE", value);
    }

    public void setSingersData(SingersData singer) {
        int category_idx = singer.getCategory_idx();
        String category_name = singer.getCategory_name();
        String category_image_url = singer.getCategory_image_url();
        String category_share_url = singer.getCategory_share_url();
        String video_keyword = singer.getVideo_keyword();
        String news_keyword = singer.getNews_keyword();

        Preference.getInstance().putInteger("SINGER_CATEGORY_IDX", category_idx);
        Preference.getInstance().putString("SINGER_CATEGORY_NAME", category_name);
        Preference.getInstance().putString("SINGER_CATEGORY_IMG_URL", category_image_url);
        Preference.getInstance().putString("SINGER_CATEGORY_SHARE_URL", category_share_url);
        Preference.getInstance().putString("SINGER_VIDEO_KEYWORD", video_keyword);
        Preference.getInstance().putString("SINGER_NEWS_KEYWORD", news_keyword);
    }

    public SingersData getSingersData() {
        if (!BuildConfig.IS_FIXED_SINGER) {
            int category_idx = Preference.getInstance().getInteger("SINGER_CATEGORY_IDX", -1);
            if (category_idx == -1) {
                return null;
            }
            String category_name = Preference.getInstance().getString("SINGER_CATEGORY_NAME", "");
            String category_image_url = Preference.getInstance().getString("SINGER_CATEGORY_IMG_URL", "");
            String category_share_url = Preference.getInstance().getString("SINGER_CATEGORY_SHARE_URL", "");
            String video_keyword = Preference.getInstance().getString("SINGER_VIDEO_KEYWORD", "");
            String news_keyword = Preference.getInstance().getString("SINGER_NEWS_KEYWORD", "");
            return new SingersData(category_idx, category_name, category_image_url, category_share_url, video_keyword, news_keyword);
        }
        int category_idx = BuildConfig.FLAG_SINGER_INFO_INDEX;
        String category_name = BuildConfig.FLAG_SINGER_INFO_NAME;
        String category_image_url = "";
        String category_share_url = BuildConfig.FLAG_SINGER_INFO_SHARE_URL;
        String video_keyword = BuildConfig.FLAG_SINGER_INFO_VIDEO_KEYWORD;
        String news_keyword = BuildConfig.FLAG_SINGER_INFO_NEWS_KEYWORD;

        return new SingersData(category_idx, category_name, category_image_url, category_share_url, video_keyword, news_keyword);
    }
}
