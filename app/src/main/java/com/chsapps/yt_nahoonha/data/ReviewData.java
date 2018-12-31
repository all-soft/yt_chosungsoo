package com.chsapps.yt_nahoonha.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewData implements Parcelable {
    private final static String TAG = ReviewData.class.getSimpleName();

    public int idx;
    public int like_cnt;
    public int comment_cnt;

    public String comment;
    public String create_at;

    public ReviewData(int idx, int like_cnt, int comment_cnt, String comment, String create_at) {
        this.idx = idx;
        this.like_cnt = like_cnt;
        this.comment_cnt = comment_cnt;
        this.comment = comment;
        this.create_at = create_at;
    }

    protected ReviewData(Parcel in) {
        idx = in.readInt();
        like_cnt = in.readInt();
        comment_cnt = in.readInt();
        comment = in.readString();
        create_at = in.readString();
    }

    public static final Creator<ReviewData> CREATOR = new Creator<ReviewData>() {
        @Override
        public ReviewData createFromParcel(Parcel in) {
            return new ReviewData(in);
        }

        @Override
        public ReviewData[] newArray(int size) {
            return new ReviewData[size];
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
