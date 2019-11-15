package com.chsapps.yt_hongjinyoung.api.model.response;

import java.util.List;

public class RecommandAppAPIData extends BaseAPIData {
    private final static String TAG = RecommandAppAPIData.class.getSimpleName();

    public List<RecommandApp> message;

    public class RecommandApp {
        public int app_idx;
        public String app_title;
        public String app_download_url;
        public String image_url;
        public String app_package_name;
        public String app_description;
    }
}
