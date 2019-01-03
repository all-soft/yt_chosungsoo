package com.chsapps.yt_hongjinyoung.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.chsapps.yt_hongjinyoung.utils.LogUtil;

public class SongData implements Parcelable {
    private final static String TAG = SongData.class.getSimpleName();

    public SongData() {
    }

    protected SongData(Parcel in) {
        song_idx = in.readInt();
        thumbnail = in.readString();
        thumbnails = in.readString();
        title = in.readString();
        videoid = in.readString();
        videoId = in.readString();
        duration = in.readString();
        date_reg = in.readString();
        long_yn = in.readString();
        play_cnt = in.readInt();
    }

    public SongData(int song_idx, String thumbnail, String title, String videoid, int play_cnt, String duration) {
        this.song_idx = song_idx;
        this.thumbnail = thumbnail;
        this.title = title;
        this.videoid = videoid;
        this.play_cnt = play_cnt;
        this.duration = duration;
    }

    public static final Creator<SongData> CREATOR = new Creator<SongData>() {
        @Override
        public SongData createFromParcel(Parcel in) {
            return new SongData(in);
        }

        @Override
        public SongData[] newArray(int size) {
            return new SongData[size];
        }
    };

    public String getString(String value) {
        if(value == null)
            return "";
        return value;
    }

    public int song_idx;
    public String thumbnail;
    public String thumbnails;
    public String title;
    public String videoid;
    public String videoId;
    public String duration;
    public String date_reg;
    public String long_yn;
    public int play_cnt;

    private int total_sec = -1;

    public String getThumbnail() {
        if(!TextUtils.isEmpty(thumbnail)) {
            return thumbnail;
        } else if(!TextUtils.isEmpty(thumbnails)) {
            return thumbnails;
        }
        return "";
    }

    public String getTitle() {
        return getString(title);
    }

    public String getRealVideoId() {
        if(TextUtils.isEmpty(videoId)) {
            return getVideoid();
        }
        return getVideoId();
    }
    public String getVideoid() {
        return getString(videoid);
    }
    public String getVideoId() {
        return getString(videoId);
    }

    public String getDuration() {
        return getString(duration);
    }

    public String getLong_yn() {
        return getString(long_yn);
    }

    public int getDurationSec() {
        if(duration == null) {
            return 0;
        }


        if(total_sec > 0) {
            return total_sec;
        }

        String time = duration.replace("PT", "");
        String[] hour = time.split("H");
        String[] min = time.split("M");
        String[] sec = time.split("S");

        if(hour.length > 0) {
            try {
                total_sec += Integer.parseInt(hour[0]) * 60 * 60;
            } catch (Exception e) {

            }
        }
        if(min.length > 0) {
            try {
                if(time.contains("H")) {
                    min = hour[1].split("M");
                }
                total_sec += Integer.parseInt(min[0]) * 60;
            } catch (Exception e) {

            }
        }
        if(sec.length > 0) {
            try {
                if(time.contains("M")) {
                    sec = min[1].split("S");
                }
                total_sec += Integer.parseInt(sec[0]);
            } catch (Exception e) {

            }
        }

//        LogUtil.e("PLAY.", "getDurationSec : " + title + " / " + total_sec);
        return total_sec;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(song_idx);
        dest.writeString(thumbnail);
        dest.writeString(thumbnails);
        dest.writeString(title);
        dest.writeString(videoid);
        dest.writeString(videoId);
        dest.writeString(duration);
        dest.writeString(date_reg);
        dest.writeString(long_yn);
        dest.writeInt(play_cnt);
    }

    public void log() {
        LogUtil.i(TAG, "titlde : " + title);
        LogUtil.i(TAG, "videoid : " + videoid);
        LogUtil.i(TAG, "videoId : " + videoId);
    }
}
