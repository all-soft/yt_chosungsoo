package com.chsapps.yt_hongjinyoung.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class SongData implements Parcelable {
    private final static String TAG = SongData.class.getSimpleName();

    public SongData() {
    }

    protected SongData(Parcel in) {
        song_idx = in.readInt();
        thumbnail = in.readString();
        title = in.readString();
        videoid = in.readString();
        duration = in.readString();
        date_reg = in.readLong();
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
    public String title;
    public String videoid;
    public String duration;
    public long date_reg;
    public String long_yn;
    public int play_cnt;

    private int total_sec = -1;

    public String getThumbnail() {
        return getString(thumbnail);
    }

    public String getTitle() {
        return getString(title);
    }

    public String getVideoid() {
        return getString(videoid);
    }

    public String getDuration() {
        return getString(duration);
    }

    public String getLong_yn() {
        return getString(long_yn);
    }

    public String getDurationTime() {
        if(duration == null) {
            return "";
        }

        String time = duration.replace("PT", "");
        String[] hour = time.split("H");

        String before = "";
        StringBuilder stringBuilder = new StringBuilder();
        if(time.contains("H") && hour.length > 0) {
            try {
                stringBuilder.append(hour[0]);
            } catch (Exception e) {
                stringBuilder.append("00");
            }
            stringBuilder.append(":");

            before = hour[0] + "H";
        }
        if(time.contains("M")) {
            boolean isEmptyBefore = true;
            String minTime = time;
            if(!TextUtils.isEmpty(before)) {
                isEmptyBefore = false;
                minTime = time.replace(before, "");
            }
            String[] min = minTime.split("M");
            try {
                stringBuilder.append(min[0].length() == 2 ? "" : "0").append(min[0]);
            } catch (Exception e) {
                stringBuilder.append("00");
            }
            before = (!isEmptyBefore ? before : "") + (min[0] + "M");
        } else {
            stringBuilder.append("00");
        }

        stringBuilder.append(":");
        if(time.contains("S")) {
            String secTime = time;
            if(!TextUtils.isEmpty(before)) {
                secTime = time.replace(before, "");
            }
            String[] sec = secTime.split("S");
            try {
                stringBuilder.append(sec[0].length() == 2 ? "" : "0").append(sec[0]);
            } catch (Exception e) {
                stringBuilder.append("00");
            }
        } else {
            stringBuilder.append("00");
        }
        return stringBuilder.toString();
    }
    public int getDurationSec() {
        if(duration == null) {
            return 0;
        }


        if(total_sec > 0) {
//            LogUtil.e("PLAY.", "getDurationSec : " + title + " / " + total_sec);
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
        dest.writeString(title);
        dest.writeString(videoid);
        dest.writeString(duration);
        dest.writeLong(date_reg);
        dest.writeString(long_yn);
        dest.writeInt(play_cnt);
    }
}
