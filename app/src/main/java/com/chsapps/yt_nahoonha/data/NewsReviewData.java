package com.chsapps.yt_nahoonha.data;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsReviewData implements Parcelable {
    private final static String TAG = NewsReviewData.class.getSimpleName();

    public int idx;
    public int like_cnt;
    public int comment_cnt;

    public String comment;
    public String create_at;

    public NewsReviewData(int idx, int like_cnt, int comment_cnt, String comment, String create_at) {
        this.idx = idx;
        this.like_cnt = like_cnt;
        this.comment_cnt = comment_cnt;
        this.comment = comment;
        this.create_at = create_at;
    }

    protected NewsReviewData(Parcel in) {
        idx = in.readInt();
        like_cnt = in.readInt();
        comment_cnt = in.readInt();
        comment = in.readString();
        create_at = in.readString();
    }

    public static final Creator<NewsReviewData> CREATOR = new Creator<NewsReviewData>() {
        @Override
        public NewsReviewData createFromParcel(Parcel in) {
            return new NewsReviewData(in);
        }

        @Override
        public NewsReviewData[] newArray(int size) {
            return new NewsReviewData[size];
        }
    };

    public String getString(String value) {
        if(value == null)
            return "";
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeInt(like_cnt);
        dest.writeInt(comment_cnt);
        dest.writeString(comment);
        dest.writeString(create_at);
    }

    public String getComment() {
        return getString(comment);
    }

    public String getCreate_at() {
        return getString(create_at);
    }
}
