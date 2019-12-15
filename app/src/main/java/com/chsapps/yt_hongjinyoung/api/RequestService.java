package com.chsapps.yt_hongjinyoung.api;

import com.chsapps.yt_hongjinyoung.api.model.response.BaseAPIData;
import com.chsapps.yt_hongjinyoung.api.model.response.CategoryListData;
import com.chsapps.yt_hongjinyoung.api.model.response.HomeData;
import com.chsapps.yt_hongjinyoung.api.model.response.RecommandAppAPIData;
import com.chsapps.yt_hongjinyoung.api.model.response.SearchData;
import com.chsapps.yt_hongjinyoung.api.model.response.SongListData;
import com.chsapps.yt_hongjinyoung.event.data.AppNoticeAPIData;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import io.reactivex.Observable;

public interface RequestService {
    /**
        token	        String	푸시 토큰
        uuid	        String	단말 UUID
        app_type	    Int	    50 으로 고정
        app_ver	        String	"버전 문자열 예) 1.0 "
        lang	        String	"단말의 설정된 언어 예) ko, fr, en "
        cc	            String	"국가 코드 예) US, KR, MX"
    * */
    @FormUrlEncoded
    @POST("/push/token2")
    Observable<BaseAPIData> request_token(@Header("applicationkey") String apiKey,
                                          @Field("uid") String uuid,
                                          @Field("app_type") String app_type,
                                          @Field("app_ver") String app_ver,
                                          @Field("cc") String cc,
                                          @Field("lang") String lang,
                                          @Field("token") String token);

    /**
        type	Int	    1 : 장르별 요청시
		                2 : 인기순 요청시
		                3 : 최신순 요청시
    * */
    @FormUrlEncoded
    @POST("/yt/home")
    Observable<HomeData> request_home(@Header("applicationkey") String apiKey,
                                      @Field("lang") String lang,
                                      @Field("app_id") int app_id,
                                      @Field("type") int type);

    @FormUrlEncoded
    @GET("/yt/version")
    Observable<HomeData> request_version(@Header("applicationkey") String apiKey,
                                      @Field("lang") String lang,
                                      @Field("app_id") int app_id,
                                      @Field("type") int type);

    /**
        category_idx	int	yt7080/home에서 response 로 받은 category_idx
        max		            한번에 가져올 개수
        page		        페이지 개수 (더보기시 사용)
    * */
    @FormUrlEncoded
    @POST("/yt/song_list")
    Observable<SongListData> request_song_list(@Header("applicationkey") String apiKey,
                                               @Field("lang") String lang,
                                               @Field("app_id") int appid,
                                               @Field("category_idx") int category_idx,
                                               @Field("max") int max,
                                               @Field("page") int page);

    /**
        song_idx	int	노래 index
    * */
    @FormUrlEncoded
    @POST("/yt/play_song")
    Observable<BaseAPIData> request_play_song(@Header("applicationkey") String apiKey,
                                              @Field("song_idx") int song_idx);

    /**
        keyword	    String	검색어
        max		            한번에 가져올 개수
        page		        페이지 개수 (더보기시 사용)
    * */
    @FormUrlEncoded
    @POST("/yt/search")
    Observable<SearchData> request_search(@Header("applicationkey") String apiKey,
                                          @Field("lang") String lang,
                                          @Field("app_id") int appid,
                                          @Field("keyword") String keyword,
                                          @Field("max") int max,
                                          @Field("page") int page);

    /**
        max		    한번에 가져올 개수
        page		페이지 개수 (더보기시 사용)
    * */
    @FormUrlEncoded
    @POST("/yt/rank")
    Observable<SearchData> request_rank(@Header("applicationkey") String apiKey,
                                        @Field("lang") String lang,
                                        @Field("app_id") int appid,
                                        @Field("max") int max,
                                        @Field("page") int page);

    /**
        없음.
    * */
    @FormUrlEncoded
    @POST("/yt/category")
    Observable<CategoryListData> request_category(@Header("applicationkey") String apiKey,
                                                  @Field("lang") String lang,
                                                  @Field("app_id") int appid,
                                                  @Field("max") int max,
                                                  @Field("type") int type,
                                                  @Field("page") int page);

    /**
        max		    한번에 가져올 개수
        page		페이지 개수 (더보기시 사용)
    * */
    @FormUrlEncoded
    @POST("/yt/recent")
    Observable<SearchData> request_recent(@Header("applicationkey") String apiKey,
                                          @Field("lang") String lang,
                                          @Field("app_id") int appid,
                                          @Field("max") int max,
                                          @Field("page") int page);

    @FormUrlEncoded
    @POST("/app/v2/recommend")
    Observable<RecommandAppAPIData> request_recommand_app(@Header("applicationkey") String apiKey,
                                                          @Field("lang") String lang,
                                                          @Field("app_id") int app_id);



    /*****
     *
     *
     *
     * About Event API.
     *
     *
     *
     *
     * ***/
    @FormUrlEncoded
    @POST("/app/v3/notice")
    Observable<AppNoticeAPIData> request_app_notice(@Header("applicationkey") String apiKey,
                                                    @Field("app_id") int appid,
                                                    @Field("lang") String lang);

    @FormUrlEncoded
    @POST("/app/event")
    Observable<BaseAPIData> request_app_event(@Header("applicationkey") String apiKey,
                                              @Field("lang") String lang,
                                              @Field("app_id") int appid,
                                              @Field("user_idx") int user_idx,
                                              @Field("token") String token,
                                              @Field("event_idx") int event_idx,
                                              @Field("layout") int layout);

    @FormUrlEncoded
    @POST("/app/event/register")
    Observable<BaseAPIData> request_app_event_register(@Header("applicationkey") String apiKey,
                                                       @Field("lang") String lang,
                                                       @Field("app_id") int appid,
                                                       @Field("user_idx") int user_idx,
                                                       @Field("token") String token,
                                                       @Field("event_idx") int event_idx,
                                                       @Field("user_name") String user_name,
                                                       @Field("user_phone") String user_phone,
                                                       @Field("layout") int layout);

    @FormUrlEncoded
    @POST("/app/v2/recommend/log")
    Observable<BaseAPIData> request_recommend_log(@Header("applicationkey") String apiKey,
                                                  @Field("lang") String lang,
                                                  @Field("app_id") int app_id,
                                                  @Field("app_idx") int idx);


    /*
    * Add Request song api.
    * */
    @FormUrlEncoded
    @POST("/yt/song/request")
    Observable<BaseAPIData> request_song_request(@Header("applicationkey") String apiKey,
                                                 @Field("lang") String lang,
                                                 @Field("appid") int app_id,
                                                 @Field("song_name") String song_name,
                                                 @Field("uuid") String uuid,
                                                 @Field("singer_name") String singer_name);
}
