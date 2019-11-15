package com.chsapps.yt_hongjinyoung.data;

public class CategoryData {
    private final static String TAG = CategoryData.class.getSimpleName();

    public String getString(String value) {
        if(value == null)
            return "";
        return value;
    }

    public String getCategory_name() {
        return getString(category_name);
    }

    public String getCategory_url() {
        return getString(category_url);
    }

    public String getCategory_image_url() {
        return getString(category_image_url);
    }

    public long getUpdate_at() {
        return update_at;
    }

    public int category_idx;
    public String category_name;
    public String category_url;
    public String category_image_url;
    public long update_at;
}
