package com.chsapps.yt_hongjinyoung.event;

import android.app.Activity;
import android.text.TextUtils;

import com.chsapps.yt_hongjinyoung.event.data.AppEventData;
import com.chsapps.yt_hongjinyoung.event.data.AppEventDatas;
import com.chsapps.yt_hongjinyoung.event.ui.popup.MainEventPopup;
import com.chsapps.yt_hongjinyoung.preference.Preference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AppEventManager {

    private final static String TAG = AppEventManager.class.getSimpleName();

    private static AppEventManager instance;

    private AppEventDatas eventDatas;

    public static AppEventManager getInstance() {
        if(instance == null) {
            instance = new AppEventManager();
        }
        return instance;
    }

    public void setAppNoticeAPIData(AppEventDatas noticeData) {
        this.eventDatas = noticeData;
    }
    public ArrayList<AppEventData> getEventListData() {
        try {
            return eventDatas.event_list;
        } catch (Exception e) {
            return null;
        }
    }
    public ArrayList<AppEventData> getEventListData(int layoutType) {
        try {
            ArrayList<AppEventData> list = new ArrayList<>();
            for(AppEventData eventData : eventDatas.event_list) {
                boolean isAdd = false;
                if(eventData.getLayout() == layoutType) {
                    isAdd = true;
                } else if((eventData.getLayout() < 1 || eventData.getLayout() > 2) && layoutType == 1) {
                    isAdd = true;
                }
                if(isAdd) {
                    list.add(eventData);
                }
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }
    public int getRollingTime() {
        try {
            return eventDatas.rolling_sec;
        } catch (Exception e) {
            return 3;
        }
    }
    public AppEventData getNoticeData() {
        try {
            return eventDatas.notice;
        } catch (Exception e) {
            return null;
        }
    }
    public int getAppNoticeEventIdx() {
        try {
            return eventDatas.notice.getEvent_idx();
        } catch (Exception e) {
            return 0;
        }
    }
    public String getAppNoticeEventURL() {
        try {
            return eventDatas.notice.getEvent_url();
        } catch (Exception e) {
            return "";
        }
    }
    public String getAppNoticeDetailURL() {
        try {
            return eventDatas.notice.getDetail_url();
        } catch (Exception e) {
            return "";
        }
    }
    public String getAppNoticeSubmitURL() {
        try {
            return eventDatas.notice.getSubmit_url();
        } catch (Exception e) {
            return "";
        }
    }
    public String getAppNoticeCheckURL() {
        try {
            return eventDatas.notice.getCheck_url();
        } catch (Exception e) {
            return "";
        }
    }


    public boolean isShowMainPageEventPopup() {
        if(eventDatas == null || TextUtils.isEmpty(eventDatas.notice.getEvent_url())) {
            return false;
        }
        long setTime = getDoNotSeeTodaySetTime();
        if(setTime > -1) {
            String setTimeDate = getDateString(setTime, "yyyy.MM.dd");
            String curTimeDate = getDateString(System.currentTimeMillis(), "yyyy.MM.dd");
            if(setTimeDate.equals(curTimeDate)) {
                return false;
            }
        }
        return true;
    }

    public void setDoNotSeeTodaySetTime() {
        Preference.getInstance().putLong(AppEventConstants.PREFS_PARAM_EVENT_DONOT_SEE_TODAY_TIME, System.currentTimeMillis());
    }
    public long getDoNotSeeTodaySetTime() {
        return Preference.getInstance().getLong(AppEventConstants.PREFS_PARAM_EVENT_DONOT_SEE_TODAY_TIME, -1);
    }

    public String getDateString(long time, String datePattern) {
        return new SimpleDateFormat(datePattern).format(new Date(time));
    }

    public void showEventNoticePopup(Activity activity) {
        MainEventPopup mainEventPopup = new MainEventPopup(activity, new MainEventPopup.onMainEventPopupListener() {
            @Override
            public void closePopup() {

            }

            @Override
            public void joinEvent() {

            }
        });
        mainEventPopup.show();
    }
}
