package com.chsapps.yt_hongjinyoung.api;


import com.chsapps.yt_hongjinyoung.api.model.BaseAPIData;

public interface RequestServiceListener {
    void response(boolean is_success, BaseAPIData response);
    void complete();
}
