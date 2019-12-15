package com.chsapps.yt_hongjinyoung.event.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.utils.Utils;

public class AppEventData implements Parcelable {
    private final static String TAG = AppEventData.class.getSimpleName();

    protected AppEventData(Parcel in) {
        event_idx = in.readInt();
        type = in.readInt();
        event_url = in.readString();
        detail_url = in.readString();
        submit_url = in.readString();
        check_url = in.readString();
        title = in.readString();
        banner = in.readString();
        share_msg = in.readString();
        share_title = in.readString();
        layout = in.readInt();
        p_name = in.readString();
    }

    public static final Creator<AppEventData> CREATOR = new Creator<AppEventData>() {
        @Override
        public AppEventData createFromParcel(Parcel in) {
            return new AppEventData(in);
        }

        @Override
        public AppEventData[] newArray(int size) {
            return new AppEventData[size];
        }
    };

    public int getEvent_idx() {
        return event_idx;
    }

    public int getType() {
        return type;
    }

    public String getEvent_url() {
        if(event_url == null) {
            return "";
        }
        return event_url;
    }

    public String getDetail_url() {
        if(detail_url == null) {
            return "";
        }
        return detail_url;
    }

    public String getSubmit_url() {
        if(submit_url == null) {
            return "";
        }
        return submit_url;
    }

    public String getCheck_url() {
        if(check_url == null) {
            return "";
        }
        return check_url;
    }

    public String getTitle() {
        if(title == null) {
            return "";
        }
        return title;
    }

    public String getBanner() {
        if(banner == null) {
            return "";
        }
        return banner;
    }

    public int getLayout() {
        return layout;
    }

    public String getShare_title() {
        if(share_title == null) {
            return Utils.getString(R.string.title_share_application);
        }
        return share_title;
    }

    public String getShare_msg() {
        if(share_msg == null) {
            return Utils.getString(R.string.message_share_application);
        }
        return share_msg;
    }

    public String getP_name() {
        if (p_name == null) {
            return "";
        }
        return p_name;
    }

    public int event_idx;
    public int type;
    public String event_url;
    public String detail_url;
    public String submit_url;
    public String check_url;
    public String title;
    public String banner;
    public String share_msg;
    public String share_title;
    public int layout;
    public String p_name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(event_idx);
        parcel.writeInt(type);
        parcel.writeString(event_url);
        parcel.writeString(detail_url);
        parcel.writeString(submit_url);
        parcel.writeString(check_url);
        parcel.writeString(title);
        parcel.writeString(banner);
        parcel.writeString(share_msg);
        parcel.writeString(share_title);
        parcel.writeInt(layout);
        parcel.writeString(p_name);
    }
}
