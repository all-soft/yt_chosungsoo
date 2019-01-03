package com.chsapps.yt_hongjinyoung.api.model;

import com.chsapps.yt_hongjinyoung.constants.APIConstants;

public class BaseAPIData {
    private static final String TAG = BaseAPIData.class.getSimpleName();

    public String status;
    public int code;

    public boolean isSuccess() {
        if(status == null) {
            return false;
        }
        if(status.equals("OK") && (APIConstants.RESPONSE_SUCCESS == code || APIConstants.RESPONSE_ANOTHER_SUCCESS == code)) {
            return true;
        }
        return false;
    }

    public String getErrorMessage() {
        return "[" + String.valueOf(code) + "] " + getString(status);
    }

    public String getString(String value) {
        if(value == null)
            return "";
        return value;
    }

}
