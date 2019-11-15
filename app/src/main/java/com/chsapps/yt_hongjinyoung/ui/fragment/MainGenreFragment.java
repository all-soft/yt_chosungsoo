package com.chsapps.yt_hongjinyoung.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.APIUtils;
import com.chsapps.yt_hongjinyoung.api.model.response.CategoryListData;
import com.chsapps.yt_hongjinyoung.api.model.response.SongListData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.APIConstants;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.data.CategoryData;
import com.chsapps.yt_hongjinyoung.ui.activity.CategoryPlaySongActivity;
import com.chsapps.yt_hongjinyoung.ui.adapter.MainGenreAdapter;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.CategoryListHolderListener;
import com.chsapps.yt_hongjinyoung.ui.base.BaseFragment;
import com.chsapps.yt_hongjinyoung.ui.view.player.WebPlayer;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import java.util.ArrayList;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainGenreFragment extends BaseFragment implements CategoryListHolderListener {
    public final static String TAG = MainGenreFragment.class.getSimpleName();

    private MainGenreAdapter adapter;

    private static CategoryListData responseData;

    @BindView(R.id.genre_list_view)
    RecyclerView genre_list_view;

    @BindView(R.id.view_empty)
    View view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    private static int type = 0;

    @BindView(R.id.btn_by_genre)
    ViewGroup btn_by_genre;
    @BindView(R.id.tv_by_genre)
    TextView tv_by_genre;
    @BindView(R.id.view_line_by_genre)
    View view_line_by_genre;
    @BindView(R.id.btn_by_singer)
    ViewGroup btn_by_singer;
    @BindView(R.id.tv_by_singer)
    TextView tv_by_singer;
    @BindView(R.id.view_line_by_singer)
    View view_line_by_singer;

    @BindView(R.id.iv_new_genre)
    ImageView iv_new_genre;
    @BindView(R.id.iv_new_singer)
    ImageView iv_new_singer;

    @BindView(R.id.layer_type)
    ViewGroup layer_type;

    public static MainGenreFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        MainGenreFragment fragment = new MainGenreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainGenreFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_main_genre, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void initialize() {
        if(Global.getInstance().isHaveNotSinger) {
            layer_type.setVisibility(View.GONE);
        } else {
            layer_type.setVisibility(View.VISIBLE);
        }
        updateTypeLayer(type);
        initListView();

        if(responseData != null) {
            adapter.insertCategoryDataList(responseData.message);
        } else {
            requestCategory();
        }
    }

    @Override
    public void clearMemory() {

    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void selectedCategory(CategoryData category) {
        if(category == null)
            return;

        requestSongList(category);
    }

    public boolean setType(int type) {
        if(type != this.type) {
            this.type = type;

            updateTypeLayer(type);
            return true;
        }
        return false;
    }

    private void initListView() {
        tv_empty_title.setText(R.string.empty_list);
        if (adapter == null) {
            adapter = new MainGenreAdapter(parentActivity, this);
        }

        if(Global.getInstance().getGenreCategoryLayout() == 2) {
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(WebPlayer.getPlayer() != null) {
                        if(!Global.getInstance().isPlayerClosed) {
                            return 1;
                        }
                    }

                    if(adapter.isPlayerAd && position % 13 == 4)
                        return 2;
                    return 1;
                }
            });
            genre_list_view.setLayoutManager(manager);
            adapter.setAdTerm(13);
        } else {
            genre_list_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        }

        genre_list_view.setHasFixedSize(true);
        genre_list_view.setAdapter(adapter);
    }

    public void requestCategory() {
        if (Utils.unableRequestAPI(parentActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return;
        }

        showLoading();
        subscription.add(APIUtils.getInstanse().getApiService()
                .request_category(APIConstants.API_KEY, Utils.getLanguage(), Constants.APPID, Constants.CATEGORY_MAX, type+1, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .subscribe(new Consumer<CategoryListData>() {
                    @Override
                    public void accept(CategoryListData response) throws Exception {
                        if (response != null) {
                            if (response.isSuccess()) {

                                responseData = response;
                                adapter.insertCategoryDataList(response.message);
                                adapter.setGenreType(type);
                                if(adapter.getItemCount() == 0) {
                                    view_empty.setVisibility(View.VISIBLE);
                                } else {
                                    view_empty.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void requestSongList(final CategoryData category) {
        if (Utils.unableRequestAPI(parentActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return;
        }

        showLoading();
        subscription.add(APIUtils.getInstanse().getApiService()
                .request_song_list(APIConstants.API_KEY, Utils.getLanguage(), Constants.APPID, category.category_idx, Constants.CATEGORY_MAX, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .subscribe(new Consumer<SongListData>() {
                    @Override
                    public void accept(SongListData response) throws Exception {
                        if (response != null) {
                            if (response.isSuccess()) {
                                Intent intent = new Intent(parentActivity, CategoryPlaySongActivity.class);
                                intent.putExtra(ParamConstants.PARAM_TITLE, category.getCategory_name());
                                intent.putParcelableArrayListExtra(ParamConstants.PARAM_SONG_LIST, (ArrayList<? extends Parcelable>) response.message);
                                parentActivity.startActivity(intent);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void updateTypeLayer(int type) {
        this.type = type;

        iv_new_genre.setVisibility(Global.getInstance().isNewGenreTypeGenre ? View.VISIBLE : View.GONE);
        iv_new_singer.setVisibility(Global.getInstance().isNewGenreTypeSinger? View.VISIBLE : View.GONE);
        switch (type) {
            case 0:
                tv_by_genre.setTextColor(getResources().getColor(R.color.colorPrimary));;
                tv_by_singer.setTextColor(getResources().getColor(R.color.color_acacac));;
                view_line_by_genre.setVisibility(View.VISIBLE);
                view_line_by_singer.setVisibility(View.GONE);
                break;
            case 1:
                tv_by_genre.setTextColor(getResources().getColor(R.color.color_acacac));;
                tv_by_singer.setTextColor(getResources().getColor(R.color.colorPrimary));;
                view_line_by_genre.setVisibility(View.GONE);
                view_line_by_singer.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.btn_by_genre)
    public void onClick_btn_by_genre() {
        if(type == 0)
            return;
        updateTypeLayer(0);
        requestCategory();

        Global.getInstance().isGenreTypeUpdate0 = true;
    }

    @OnClick(R.id.btn_by_singer)
    public void onClick_btn_by_singer() {
        if(type == 1)
            return;
        updateTypeLayer(1);
        requestCategory();

        Global.getInstance().isGenreTypeUpdate1 = true;
    }




}
