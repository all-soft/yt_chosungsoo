package com.chsapps.yt_hongjinyoung.api.model.response;

import com.chsapps.yt_hongjinyoung.constants.APIConstants;

public class BaseAPIData {
    private static final String TAG = BaseAPIData.class.getSimpleName();

    public String status;
    public int code;


    public boolean isSuccess() {
        if(status == null) {
            return false;
        }
        if(status.equals("OK") && APIConstants.RESPONSE_SUCCESS == code) {
            return true;
        }
        return false;
    }

    public String getString(String value) {
        if(value == null)
            return "";
        return value;
    }

}
