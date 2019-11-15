package com.chsapps.yt_hongjinyoung.api.model.response;

import com.chsapps.yt_hongjinyoung.data.SongData;

import java.util.List;

public class VersionAPIData extends BaseAPIData {
    private final static String TAG = VersionAPIData.class.getSimpleName();

    public List<SongData> message;

    public class VersionData {
        public int lastest_version_code;
        public int lastest_version_name;
        public int minimum_version_code;
        public int minimum_version_name;
        public int category_layout;
        public int app_removed;
        public String appstore_url;
    }
}
