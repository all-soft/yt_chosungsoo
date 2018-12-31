package com.chsapps.yt_nahoonha.api;


import com.chsapps.yt_nahoonha.api.model.BaseAPIData;
import com.chsapps.yt_nahoonha.api.model.HomeData;
import com.chsapps.yt_nahoonha.api.model.NewsAPIData;
import com.chsapps.yt_nahoonha.api.model.SingersAPIData;
import com.chsapps.yt_nahoonha.api.model.SongAPIData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RequestService {

    @FormUrlEncoded
    @POST("/push/token2")
    Observable<BaseAPIData> request_token(
            @Field("app_type") String app_type,
            @Field("app_ver") String app_ver,
            @Field("cc") String cc,
            @Field("lang") String lang,
            @Field("token") String token,
            @Field("uid") String uid
    );


    //lang
    @FormUrlEncoded
    @POST("/alltrot/home")
    Observable<HomeData> request_home(
            @Field("app_id") int app_id,
            @Field("lang") String lang
    );

    //lang, max, page
    @FormUrlEncoded
    @POST("/alltrot/singers")
    Observable<SingersAPIData> request_singers(
            @Field("app_id") int app_id,
            @Field("lang") String lang,
            @Field("max") int max,
            @Field("page") int page
    );

    //lang, max, page, category_idx
    @FormUrlEncoded
    @POST("/alltrot/song_list")
    Observable<SongAPIData> request_song_list(
            @Field("app_id") int app_id,
            @Field("lang") String lang,
            @Field("max") int max,
            @Field("page") int page,
            @Field("category_idx") int category_idx,
            @Field("type") int type
    );

    //lang, videoid
    @FormUrlEncoded
    @POST("/alltrot/play_song/v2")
    Observable<BaseAPIData> request_play_song(
            @Field("app_id") int app_id,
            @Field("lang") String lang,
            @Field("videoid") String videoid
    );

    //lang,
    @FormUrlEncoded
    @POST("/alltrot/search")
    Observable<SongAPIData> request_songs_search(
            @Field("app_id") int app_id,
            @Field("lang") String lang,
            @Field("keyword") String keyword,
            @Field("max") int max,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("/alltrot/singers/search")
    Observable<SingersAPIData> request_singers_search(
            @Field("app_id") int app_id,
            @Field("lang") String lang,
            @Field("keyword") String keyword,
            @Field("max") int max,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("/alltrot/news")
    Observable<NewsAPIData> request_news(
            @Field("app_id") int app_id,
            @Field("lang") String lang,
            @Field("keyword") String keyword,
            @Field("max") int max,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("/alltrot/videos")
    Observable<SongAPIData> request_videos(
            @Field("app_id") int app_id,
            @Field("lang") String lang,
            @Field("keyword") String keyword,
            @Field("max") int max,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("/alltrot/recommend_song_list")
    Observable<SongAPIData> request_recommend_song_list(
            @Field("app_id") int app_id,
            @Field("lang") String lang,
            @Field("max") int max,
            @Field("page") int page,
            @Field("category_idx") int category_idx
    );
}
