package com.chsapps.yt_hongjinyoung.api.model;

import java.util.List;

public class RecommAppAPIData extends BaseAPIData {
    private final static String TAG = RecommAppAPIData.class.getSimpleName();

    public List<RecommandApp> message;

    public class RecommandApp {
        public int idx;
        public String title;
        public String app_url;
        public String image_url;
    }
}
