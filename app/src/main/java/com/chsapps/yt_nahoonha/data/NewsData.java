package com.chsapps.yt_nahoonha.data;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsData implements Parcelable {
    private final static String TAG = NewsData.class.getSimpleName();

    protected NewsData(Parcel in) {
        title = in.readString();
        link = in.readString();
        desc = in.readString();
        like_cnt = in.readInt();
        comment_cnt = in.readInt();
        image_url = in.readString();
    }

    public static final Creator<NewsData> CREATOR = new Creator<NewsData>() {
        @Override
        public NewsData createFromParcel(Parcel in) {
            return new NewsData(in);
        }

        @Override
        public NewsData[] newArray(int size) {
            return new NewsData[size];
        }
    };

    public String getString(String value) {
        if(value == null)
            return "";
        return value;
    }


    public String getTitle() {
        return getString(title);
    }

    public String getLink() {
        return getString(link);
    }

    public String getDesc() {
        return getString(desc);
    }

    public String getDate() {
        return getString(date);
    }

    public int getLike_cnt() {
        return like_cnt;
    }

    public int getComment_cnt() {
        return comment_cnt;
    }

    public String getImage_url() {
        return getString(image_url);
    }

    public String title;
    public String link;
    public String desc;
    public String date;
    public int like_cnt;
    public int comment_cnt;
    public String image_url;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(desc);
        dest.writeInt(like_cnt);
        dest.writeInt(comment_cnt);
        dest.writeString(image_url);
    }
}
