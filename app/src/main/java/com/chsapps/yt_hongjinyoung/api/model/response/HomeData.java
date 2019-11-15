package com.chsapps.yt_hongjinyoung.api.model.response;

import com.chsapps.yt_hongjinyoung.constants.ADConstants;
import com.chsapps.yt_hongjinyoung.data.CategoryData;
import com.chsapps.yt_hongjinyoung.data.SongData;

import java.util.List;

public class HomeData extends BaseAPIData {
    private final static String TAG = HomeData.class.getSimpleName();

    private final static boolean isUseFacebookAd = false;
    public static class HomeMessageData {
        public List<AD_CONFIG> ad_config;
        public List<VERSION_INFO> version_info;
        public List<NOTICE> notice;
        public List<CategoryData> category_list;
        public List<SongData> rank_list;
        public List<SongData> recent_list;
    }

    public class AD_CONFIG {
        public String getString(String value) {
            if(value == null)
                return "";
            return value;
        }

        public String getAd_appid() {
            return getString(ad_appid);
        }

        public String getAd_fullad_type() {
            return getString(fullad_type);
        }
        public String getAd_banner_type() {
            return getString(banner_type);
        }
        public String getAd_native_type() {
            return getString(native_type);
        }

        public String getFullad_id() {
//            if(getString(fullad_type).equals("1"))
//                return ADConstants.ADMOBB_AD_INTERSTITIALS_ID;

            return ADConstants.ADMOBB_AD_INTERSTITIALS_ID;
        }

        public String getFullad_2nd_id() {
//            if(getString(fullad_type).equals("1"))
//                return ADConstants.ADMOBB_AD_INTERSTITIALS_2ND_ID;

            return ADConstants.ADMOBB_AD_INTERSTITIALS_ID;
        }

        public String getBanner_id() {
//            if(getString(banner_type).equals("1"))
//                return ADConstants.ADMOBB_AD_BANNER_ID;

            return ADConstants.ADMOBB_AD_BANNER_ID;
        }

        public String getReward_id() {
            return getString(reward_id);
        }

        public String getNative_id() {
            if(getString(native_type).equals("1"))
                return getString(native_id1);

            return getString(native_id2);
        }

        public int getNative_ad_term() {
            //TODO : for Test
            return 8;
//            return native_ad_term;
        }

        String fullad_type;
        String banner_type;
        String native_type;

        String ad_appid;
        String fullad_id1;
        String banner_id1;
        String native_id1;
        String fullad_id2;
        String banner_id2;
        String native_id2;
        String reward_id;
        public int ad_is_show_at_launch;
        public int ad_is_show_at_exit;
        public int ad_is_show_bottom_banner;
        public int ad_is_show_at_finish_play;
        public int ad_is_show_native_banner;

        public int native_ad_term;
    }

    public class VERSION_INFO {
        public String getLasted_version_name() {
            if(lasted_version_name == null) {
                return null;
            }
            return getString(lasted_version_name);
        }

        public String getMinium_version_name() {
            if(minium_version_name == null) {
                return null;
            }
            return getString(minium_version_name);
        }

        public boolean isRemovedApplication() {
            return app_removed == 1;
        }

        public String getAppstore_url() {
            if(appstore_url == null) {
                return "";
            }
            return appstore_url;
        }

        int lastest_version_code;
        String lasted_version_name;
        int minimum_version_code;
        String minium_version_name;
        public int category_layout;
        public int app_removed;
        public String appstore_url;
    }

    public class NOTICE {
        public String getTitle() {
            return getString(title);
        }

        public String getContents() {
            return getString(contents);
        }

        public String getCreate_at() {
            return getString(create_at);
        }

        int idx;
        String title;
        String contents;
        String create_at;
    }

    public HomeMessageData message;
}
