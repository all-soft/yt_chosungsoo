package com.chsapps.yt_nahoonha.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.chsapps.yt_nahoonha.utils.LogUtil;

public class SingersData implements Parcelable {
    private final static String TAG = SingersData.class.getSimpleName();

    int category_idx;
    String category_name;
    String category_image_url;
    String category_share_url;
    String video_keyword;
    String news_keyword;

    public static final Creator<SingersData> CREATOR = new Creator<SingersData>() {
        @Override
        public SingersData createFromParcel(Parcel in) {
            return new SingersData(in);
        }

        @Override
        public SingersData[] newArray(int size) {
            return new SingersData[size];
        }
    };

    private String getString(String value) {
        if(value == null)
            return "";
        return value;
    }

    public int getCategory_idx() {
        return category_idx;
    }
    public String getCategory_name() {
        return getString(category_name);
    }
    public String getCategory_image_url() {
        return getString(category_image_url);
    }
    public String getCategory_share_url() {
        return getString(category_share_url);
    }
    public String getVideo_keyword() {
        return getString(video_keyword);
    }

    public String getNews_keyword() {
        return getString(news_keyword);
    }

    public SingersData(int category_idx, String category_name, String category_image_url, String category_share_url, String video_keyword, String news_keyword) {
        this.category_idx = category_idx;
        this.category_name = category_name;
        this.category_image_url = category_image_url;
        this.category_share_url = category_share_url;
        this.video_keyword = video_keyword;
        this.news_keyword = news_keyword;
    }

    public SingersData(Parcel in) {
        category_idx = in.readInt();
        category_name = in.readString();
        category_image_url = in.readString();
        category_share_url = in.readString();
        video_keyword = in.readString();
        news_keyword = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(category_idx);
        parcel.writeString(category_name);
        parcel.writeString(category_image_url);
        parcel.writeString(category_share_url);
        parcel.writeString(video_keyword);
        parcel.writeString(news_keyword);
    }

    public void Log() {
        LogUtil.d(TAG, "index : " + category_idx);
        LogUtil.d(TAG, "name : " + category_name);
        LogUtil.d(TAG, "image_url : " + category_image_url);
        LogUtil.d(TAG, "share_url : " + category_share_url);
        LogUtil.d(TAG, "video_keyword : " + video_keyword);
        LogUtil.d(TAG, "news_keyword : " + news_keyword);
    }
}
