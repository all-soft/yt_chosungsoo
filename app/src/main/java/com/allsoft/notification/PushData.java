package com.allsoft.notification;

import java.io.Serializable;

public class PushData implements Serializable {
    private final static String TAG = PushData.class.getSimpleName();


    //공통.
    public String title;
    public String message;

    /**
     * 1 : 앱 메인으로 이동
     * 2 : 웹 뷰로 컨텐츠 노출
     * 3 : 앱 내의 컨텐츠로 이동
     * */
    public int click_type;

    public String img_url;

    public boolean is_custom;
    public int custom_type;

    //click_type case 1 :

    //click_type case 2 :
    public String page_title;
    public String page_url;

    //click_type case 3 :
    public int contents_idx;
    public String contents_title;
    public String contents_thumbnail;
    public String contents_video_id;
    public String contents_duration;

    public String log() {
        return "" + title + "\n"
                + message + "\n"
                + click_type + "\n"
                + img_url + "\n"
                + is_custom + "\n"
                + custom_type + "\n"
                + page_title + "\n"
                + page_url + "\n"
                + contents_title + "\n"
                + contents_idx + "\n";
    }
}
