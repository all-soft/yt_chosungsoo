package com.chsapps.yt_hongjinyoung.api;

import android.app.Activity;
import android.content.DialogInterface;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.model.BaseAPIData;
import com.chsapps.yt_hongjinyoung.api.model.HomeData;
import com.chsapps.yt_hongjinyoung.api.model.NewsAPIData;
import com.chsapps.yt_hongjinyoung.api.model.RecommAppAPIData;
import com.chsapps.yt_hongjinyoung.api.model.SingersAPIData;
import com.chsapps.yt_hongjinyoung.api.model.SongAPIData;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.ui.view.popup.CommonPopup;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RequestUtils {
    private static final String TAG = RequestUtils.class.getSimpleName();

    private static final RequestUtils instanse = new RequestUtils();
    public static RequestUtils getInstanse() {
        return instanse;
    }


    private boolean isSuccessResponse(BaseAPIData response) {
        return response != null && response.isSuccess();
    }

    private void showErrorDlg(Activity activity, BaseAPIData response) {
        showErrorDlg(activity, response == null ? "Error) Response is null." : response.getErrorMessage());
    }

    private void showErrorDlg(Activity activity, String message) {
        final CommonPopup dlg = new CommonPopup(activity);
        dlg.setMessageString(message);
        dlg.setBtn_positive(true, R.string.Done)
                .setBtn_negative(false, null)
                .setActionListener(new CommonPopup.CommonPopupActionListener() {
                    @Override
                    public void onActionPositiveBtn() {
                        dlg.dismiss();
                    }

                    @Override
                    public void onActionNegativeBtn() {

                    }
                }).show();
    }



    /***
     * API Request func.
     *
     *
     * **/
    private final static int REQUEST_ITEM_COUNT = 100;
    public boolean requestHome(final Activity activity, CompositeDisposable subscription, final RequestServiceListener listener) {
        if (Utils.unableRequestAPI(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_home(Constants.APP_ID, Utils.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(listener != null){
                            listener.complete();
                        }
                    }
                })
                .subscribe(new Consumer<HomeData>() {
                    @Override
                    public void accept(HomeData homeData) throws Exception {
                        if(listener != null) {
                            listener.response(isSuccessResponse(homeData), homeData);
                        }

                        if(homeData != null && !isSuccessResponse(homeData)) {
                            showErrorDlg(activity, homeData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }

    public boolean requestSinger(final Activity activity, CompositeDisposable subscription, final RequestServiceListener listener) {
        if (Utils.unableRequestAPI(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_singers(Constants.APP_ID, Utils.getLanguage(), 100, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                if(listener != null){
                                    listener.complete();
                                }
                            }
                        })
                .subscribe(new Consumer<SingersAPIData>() {
                    @Override
                    public void accept(SingersAPIData singersAPIData) throws Exception {

                        if (listener != null) {
                            listener.response(isSuccessResponse(singersAPIData), singersAPIData);
                        }

                        if (singersAPIData != null && !isSuccessResponse(singersAPIData)) {
                            showErrorDlg(activity, singersAPIData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }

    public boolean requestSongList(final Activity activity, CompositeDisposable subscription, int category_idx, int page, int song_list, final RequestServiceListener listener) {
        if (Utils.unableRequestAPI(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_song_list(Constants.APP_ID, Utils.getLanguage(), REQUEST_ITEM_COUNT, page, category_idx, song_list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(listener != null){
                            listener.complete();
                        }
                    }
                })
                .subscribe(new Consumer<SongAPIData>() {
                    @Override
                    public void accept(SongAPIData songAPIData) throws Exception {

                        if (listener != null) {
                            listener.response(isSuccessResponse(songAPIData), songAPIData);
                        }

                        if (songAPIData != null && !isSuccessResponse(songAPIData)) {
                            showErrorDlg(activity, songAPIData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }

    public boolean requestPlaySong(final Activity activity, CompositeDisposable subscription, String videoid, final RequestServiceListener listener) {
        if (Utils.unableRequestAPI(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_play_song(Constants.APP_ID, Utils.getLanguage(), videoid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(listener != null){
                            listener.complete();
                        }
                    }
                })
                .subscribe(new Consumer<BaseAPIData>() {
                    @Override
                    public void accept(BaseAPIData baseAPIData) throws Exception {

                        if (listener != null) {
                            listener.response(isSuccessResponse(baseAPIData), baseAPIData);
                        }

                        if (baseAPIData != null && !isSuccessResponse(baseAPIData)) {
                            showErrorDlg(activity, baseAPIData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }

    public boolean requestSongsSearch(final Activity activity, CompositeDisposable subscription, String keyword, int page, final RequestServiceListener listener) {
        if (Utils.unableRequestAPI(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_songs_search(Constants.APP_ID, Utils.getLanguage(), keyword, REQUEST_ITEM_COUNT, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(listener != null){
                            listener.complete();
                        }
                    }
                })
                .subscribe(new Consumer<SongAPIData>() {
                    @Override
                    public void accept(SongAPIData songAPIData) throws Exception {

                        if (listener != null) {
                            listener.response(isSuccessResponse(songAPIData), songAPIData);
                        }

                        if (songAPIData != null && !isSuccessResponse(songAPIData)) {
                            showErrorDlg(activity, songAPIData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }

    public boolean requestSingersSearch(final Activity activity, CompositeDisposable subscription, String keyword, final RequestServiceListener listener) {
        if (Utils.unableRequestAPI(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_singers_search(Constants.APP_ID, Utils.getLanguage(), keyword, 100, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(listener != null){
                            listener.complete();
                        }
                    }
                })
                .subscribe(new Consumer<SingersAPIData>() {
                    @Override
                    public void accept(SingersAPIData singersAPIData) throws Exception {

                        if (listener != null) {
                            listener.response(isSuccessResponse(singersAPIData), singersAPIData);
                        }

                        if (singersAPIData != null && !isSuccessResponse(singersAPIData)) {
                            showErrorDlg(activity, singersAPIData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }

    public boolean requestNews(final Activity activity, CompositeDisposable subscription, String keyword, int page, final RequestServiceListener listener) {
        if (Utils.unableRequestAPI(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }
        subscription.add(APIUtils.getInstanse().getApiService()
                .request_news(Constants.APP_ID, Utils.getLanguage(), keyword, REQUEST_ITEM_COUNT, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(listener != null){
                            listener.complete();
                        }
                    }
                })
                .subscribe(new Consumer<NewsAPIData>() {
                    @Override
                    public void accept(NewsAPIData newsAPIData) throws Exception {

                        if (listener != null) {
                            listener.response(isSuccessResponse(newsAPIData), newsAPIData);
                        }

                        if (newsAPIData != null && !isSuccessResponse(newsAPIData)) {
                            showErrorDlg(activity, newsAPIData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }

    public boolean requestVideoSong(final Activity activity, CompositeDisposable subscription, String keyword, int page, final RequestServiceListener listener) {
        if (Utils.unableRequestAPI(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_videos(Constants.APP_ID, Utils.getLanguage(), keyword, REQUEST_ITEM_COUNT, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(listener != null){
                            listener.complete();
                        }
                    }
                })
                .subscribe(new Consumer<SongAPIData>() {
                    @Override
                    public void accept(SongAPIData songAPIData) throws Exception {

                        if (listener != null) {
                            listener.response(isSuccessResponse(songAPIData), songAPIData);
                        }

                        if (songAPIData != null && !isSuccessResponse(songAPIData)) {
                            showErrorDlg(activity, songAPIData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }

    public boolean requestRecommendSong(final Activity activity, CompositeDisposable subscription, int category_idx, final RequestServiceListener listener) {
        if (Utils.unableRequestAPI(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_recommend_song_list(Constants.APP_ID, Utils.getLanguage(), REQUEST_ITEM_COUNT, 0, category_idx)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(listener != null){
                            listener.complete();
                        }
                    }
                })
                .subscribe(new Consumer<SongAPIData>() {
                    @Override
                    public void accept(SongAPIData songAPIData) throws Exception {

                        if (listener != null) {
                            listener.response(isSuccessResponse(songAPIData), songAPIData);
                        }

                        if (songAPIData != null && !isSuccessResponse(songAPIData)) {
                            showErrorDlg(activity, songAPIData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }

    public boolean requestrequestRecomAppRecomApp(final Activity activity, CompositeDisposable subscription, final RequestServiceListener listener) {
        if (Utils.unableRequestAPI(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_recommend_app_list(Constants.APP_ID, Utils.getLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(listener != null){
                            listener.complete();
                        }
                    }
                })
                .subscribe(new Consumer<RecommAppAPIData>() {
                    @Override
                    public void accept(RecommAppAPIData appDatas) throws Exception {
                        if (listener != null) {
                            listener.response(isSuccessResponse(appDatas), appDatas);
                        }

                        if (appDatas != null && !isSuccessResponse(appDatas)) {
                            showErrorDlg(activity, appDatas);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }
}
