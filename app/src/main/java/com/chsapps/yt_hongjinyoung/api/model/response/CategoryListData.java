package com.chsapps.yt_hongjinyoung.api.model.response;

import com.chsapps.yt_hongjinyoung.data.CategoryData;

import java.util.List;

public class CategoryListData extends BaseAPIData {
    private final static String TAG = CategoryListData.class.getSimpleName();

    public List<CategoryData> message;
}
