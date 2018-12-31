package com.chsapps.yt_nahoonha.api;


import com.chsapps.yt_nahoonha.api.model.BaseAPIData;

public interface RequestServiceListener {
    void response(boolean is_success, BaseAPIData response);
    void complete();
}
