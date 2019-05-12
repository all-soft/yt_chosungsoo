package com.chsapps.yt_hongjinyoung.api.model;

import android.text.TextUtils;

import java.util.List;

public class HomeData extends BaseAPIData {
    private final static String TAG = HomeData.class.getSimpleName();

    private final static boolean isUseFacebookAd = false;

    public HomeMessageData message;
    public static class HomeMessageData {
        public List<AD_CONFIG> ad_config;
        public List<VERSION_INFO> version_info;
        public List<NOTICE> notice;
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
            if(isUseFacebookAd) {
                return "2";
            }
            return getString(native_type);
        }

        public String getFullad_id() {
            if(getString(fullad_type).equals("1"))
                return getString(fullad_id1);

            return getString(fullad_id2);
        }

        public String getBanner_id() {
            if(getString(banner_type).equals("1"))
                return getString(banner_id1);

            return getString(banner_id2);
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
            return 100;//8;
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

        public boolean isAppRemoved() {
            if(app_removed == 1 && !TextUtils.isEmpty(appstore_url)) {
                return true;
            }
            return false;
        }

        public String getAppStoreURL() {
            if(appstore_url == null)
                return "";
            return appstore_url;
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

        public int getCategory_layout() {
            return category_layout;
        }

        int lastest_version_code;
        String lasted_version_name;
        int minimum_version_code;
        String minium_version_name;
        int app_removed;
        String appstore_url;
        int category_layout;
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


}
