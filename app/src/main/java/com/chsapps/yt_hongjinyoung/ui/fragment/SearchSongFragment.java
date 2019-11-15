package com.chsapps.yt_hongjinyoung.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.APIUtils;
import com.chsapps.yt_hongjinyoung.api.model.response.SearchData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.APIConstants;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.db.KeywordDBHelper;
import com.chsapps.yt_hongjinyoung.db.StorageDBHelper;
import com.chsapps.yt_hongjinyoung.ui.activity.PlaySongActivity;
import com.chsapps.yt_hongjinyoung.ui.adapter.KeywordAdapter;
import com.chsapps.yt_hongjinyoung.ui.adapter.MainPapularAdapter;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.KeywordListHolderListener;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.SongListHolderListener;
import com.chsapps.yt_hongjinyoung.ui.base.BaseFragment;
import com.chsapps.yt_hongjinyoung.ui.view.popup.YoutubePolicyPopup;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchSongFragment extends BaseFragment implements KeywordListHolderListener {
    public final static String TAG = SearchSongFragment.class.getSimpleName();

    private MainPapularAdapter adapter;
    private KeywordAdapter keywordAdapter;
    private MainPapularAdapter popularAdapter;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.list_view)
    RecyclerView list_view;

    @BindView(R.id.tv_recent)
    TextView tv_recent;
    @BindView(R.id.tv_popular)
    TextView tv_popular;

    @BindView(R.id.line_recent)
    View line_recent;
    @BindView(R.id.line_popular)
    View line_popular;

    @BindView(R.id.recent_search_list_view)
    RecyclerView recent_search_list_view;
    @BindView(R.id.realtime_list_view)
    RecyclerView realtime_list_view;

    @BindView(R.id.view_empty)
    View view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    @BindView(R.id.layer_tab)
    ViewGroup layer_tab;

    @BindView(R.id.tv_select_all)
    TextView tv_select_all;
    @BindView(R.id.layer_bottom)
    ViewGroup layer_bottom;

    private int currentTabIndex = 0;

    public static SearchSongFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        SearchSongFragment fragment = new SearchSongFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SearchSongFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_search_song, null);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initialize() {
        setTitle(R.string.Search);
        setBackKey(true);

        view_empty.setVisibility(View.GONE);

        initListView();
        et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onClick_btn_search();
                }
                return false;
            }
        });
        updateTab();
    }

    @Override
    public void clearMemory() {

    }

    @Override
    public boolean onBackPressed() {
        if (list_view.getVisibility() == View.VISIBLE) {
            list_view.setVisibility(View.GONE);
            updateTab();
            return true;
        }

        return super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initListView() {
        if (adapter == null) {
            adapter = new MainPapularAdapter(parentActivity, new SongListHolderListener() {
                @Override
                public void playSong(SongData song) {
//                    Global.getInstance().setPlaySongListData(adapter.getItemsList());
//
//                    Intent intent = new Intent(parentActivity, PlaySongActivity.class);
//                    intent.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, song.song_idx);
//                    parentActivity.startActivity(intent);
//
//                    parentActivity.finish();
                    adapter.addSelectedSongData(song);
                    List<SongData> arrayList = adapter.getSelectedSongDataList();
                    try {
                        if(arrayList.size() == adapter.getSongDataList().size()) {
                            tv_select_all.setText(R.string.all_deselect);
                        } else {
                            tv_select_all.setText(R.string.all_select);
                        }
                    } catch (Exception e) {
                        tv_select_all.setText(R.string.all_select);
                    }
                }

                @Override
                public void deleteSong(SongData song) {

                }

                @Override
                public void shareSong(SongData song) {

                }

                @Override
                public void saveSong(SongData song) {
                    StorageDBHelper.getInstance().addData(song);
                    Toast.makeText(parentActivity, "보관함에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (keywordAdapter == null) {
            keywordAdapter = new KeywordAdapter(parentActivity, this);
        }
        if (popularAdapter == null) {
            popularAdapter = new MainPapularAdapter(parentActivity, new SongListHolderListener() {
                @Override
                public void playSong(SongData song) {
//                    Global.getInstance().setPlaySongListData(popularAdapter.getSongDataList());
//
//                    Intent intent = new Intent(parentActivity, PlaySongActivity.class);
//                    intent.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, song.song_idx);
//                    parentActivity.startActivity(intent);
//
//                    parentActivity.finish();

                    popularAdapter.addSelectedSongData(song);

                    List<SongData> arrayList = popularAdapter.getSelectedSongDataList();
                    try {
                        if(arrayList.size() == popularAdapter.getSongDataList().size()) {
                            tv_select_all.setText(R.string.all_deselect);
                        } else {
                            tv_select_all.setText(R.string.all_select);
                        }
                    } catch (Exception e) {
                        tv_select_all.setText(R.string.all_select);
                    }
                }

                @Override
                public void deleteSong(SongData song) {

                }

                @Override
                public void shareSong(SongData song) {

                }

                @Override
                public void saveSong(SongData song) {
                    StorageDBHelper.getInstance().addData(song);
                    Toast.makeText(parentActivity, "보관함에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        list_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        list_view.setHasFixedSize(true);
        list_view.setAdapter(adapter);

        recent_search_list_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recent_search_list_view.setHasFixedSize(true);
        recent_search_list_view.setAdapter(keywordAdapter);

        realtime_list_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        realtime_list_view.setHasFixedSize(true);
        realtime_list_view.setAdapter(popularAdapter);

        refreshKeyword();
        requestAPISongList();
    }

    @OnClick(R.id.btn_search)
    public void onClick_btn_search() {
        KeywordDBHelper.getInstance().addData(et_search.getText().toString());
        reqSearchSong(et_search.getText().toString());
    }

    public void reqSearchSong(String kewords) {
        if (TextUtils.isEmpty(kewords) || kewords.length() < 2) {
            Toast.makeText(parentActivity, "두 글자 이상의 검색어를 입력 해주세요.", Toast.LENGTH_SHORT).show();
        }

        if (Utils.unableRequestAPI(parentActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return;
        }

        showLoading();
        subscription.add(APIUtils.getInstanse().getApiService()
                .request_search(APIConstants.API_KEY, Utils.getLanguage(), Constants.APPID, kewords, Constants.CATEGORY_MAX, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .subscribe(new Consumer<SearchData>() {
                    @Override
                    public void accept(SearchData response) throws Exception {
                        if (response != null) {
                            if (response.isSuccess()) {
                                layer_tab.setVisibility(View.GONE);
                                if (response.message != null && response.message.size() > 0)
                                    realtime_list_view.setVisibility(View.GONE);
                                recent_search_list_view.setVisibility(View.GONE);
                                Utils.hideSoftKeyboard(et_search);
                                list_view.setVisibility(View.VISIBLE);
                                updateLayerBottom();
                                layer_bottom.setVisibility(View.VISIBLE);

                                adapter.insertSongDataList(response.message);
                                if (adapter.getItemCount() == 0) {
                                    view_empty.setVisibility(View.VISIBLE);
                                    tv_empty_title.setText(R.string.message_empty_search_keyword);
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

    private void requestAPISongList() {
        if (Utils.unableRequestAPI(parentActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return;
        }

        showLoading();
        subscription.add(APIUtils.getInstanse().getApiService()
                .request_rank(APIConstants.API_KEY, Utils.getLanguage(), Constants.APPID, Constants.CATEGORY_MAX, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .subscribe(new Consumer<SearchData>() {
                    @Override
                    public void accept(SearchData response) throws Exception {
                        if (response != null) {
                            if (response.isSuccess()) {
                                popularAdapter.insertSongDataList(response.message);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    @OnClick(R.id.tab_recent)
    public void onClick_tab_recent() {
        if (currentTabIndex == 0) {
            return;
        }

        currentTabIndex = 0;
        updateTab();
    }

    @OnClick(R.id.tab_popular)
    public void onClick_tab_popular() {
        if (currentTabIndex == 1) {
            return;
        }

        currentTabIndex = 1;
        updateTab();
    }

    private void updateLayerBottom() {
        if(list_view.getVisibility() == View.VISIBLE) {
            List<SongData> arrayList = adapter == null ? new ArrayList<>() :adapter.getSelectedSongDataList();
            if (arrayList != null && arrayList.size() > 0) {
                tv_select_all.setText(R.string.all_select);
            } else {
                tv_select_all.setText(R.string.all_deselect);
            }
        } else {
            List<SongData> arrayList = popularAdapter == null ? new ArrayList<>() : popularAdapter.getSelectedSongDataList();
            if (arrayList != null && arrayList.size() > 0) {
                tv_select_all.setText(R.string.all_select);
            } else {
                tv_select_all.setText(R.string.all_deselect);
            }
        }
    }
    public void updateTab() {
        layer_tab.setVisibility(View.VISIBLE);
        if (currentTabIndex == 0) {
            layer_bottom.setVisibility(View.GONE);
            tv_recent.setTextColor(
                    getResources().getColor(R.color.White));
            tv_popular.setTextColor(
                    getResources().getColor(R.color.color_cccccc));
            line_recent.setVisibility(View.VISIBLE);
            line_popular.setVisibility(View.GONE);

            list_view.setVisibility(View.GONE);
            recent_search_list_view.setVisibility(View.VISIBLE);
            realtime_list_view.setVisibility(View.GONE);

            refreshKeyword();
        } else {
            layer_bottom.setVisibility(View.VISIBLE);
            view_empty.setVisibility(View.GONE);
            tv_recent.setTextColor(
                    getResources().getColor(R.color.color_cccccc));
            tv_popular.setTextColor(
                    getResources().getColor(R.color.White));
            line_recent.setVisibility(View.GONE);
            line_popular.setVisibility(View.VISIBLE);

            list_view.setVisibility(View.GONE);
            realtime_list_view.setVisibility(View.VISIBLE);
            recent_search_list_view.setVisibility(View.GONE);
            updateLayerBottom();
        }
    }

    private void refreshKeyword() {
        keywordAdapter.addAll(KeywordDBHelper.getInstance().getList());
        if (keywordAdapter.getItemCount() == 0) {
            view_empty.setVisibility(View.VISIBLE);
            tv_empty_title.setText(R.string.message_empty_search_recent_keyword);
        } else {
            view_empty.setVisibility(View.GONE);
        }
    }

    @Override
    public void deleteKeyword(String keyword) {
        KeywordDBHelper.getInstance().removeData(keyword);
        refreshKeyword();
    }

    @Override
    public void search(String keyword) {
        reqSearchSong(keyword);
    }

    @OnClick(R.id.btn_select_all)
    public void onClick_btn_select_all() {
        if(list_view.getVisibility() == View.VISIBLE) {
            if (tv_select_all.getText().toString().equals(Utils.getString(R.string.all_select))) {
                adapter.addAllSelected();
                tv_select_all.setText(R.string.all_deselect);
            } else {
                adapter.addAllDeSelected();
                tv_select_all.setText(R.string.all_select);
            }
        } else {
            if (tv_select_all.getText().toString().equals(Utils.getString(R.string.all_select))) {
                popularAdapter.addAllSelected();
                tv_select_all.setText(R.string.all_deselect);
            } else {
                popularAdapter.addAllDeSelected();
                tv_select_all.setText(R.string.all_select);
            }
        }

    }

    @OnClick(R.id.btn_play)
    public void onClick_btn_play() {
        if(list_view.getVisibility() == View.VISIBLE) {
            List<SongData> arrayList = adapter.getSelectedSongDataList();
            if (arrayList != null && arrayList.size() > 0) {
                if(Global.getInstance().isShowYoutubePlayPolicy()) {
                    YoutubePolicyPopup dlg = new YoutubePolicyPopup(parentActivity);
                    dlg.setListener(new YoutubePolicyPopup.onPlayListener() {
                        @Override
                        public void play() {
                            Global.getInstance().setPlaySongListData(arrayList);
                            Intent intent = new Intent(parentActivity, PlaySongActivity.class);
                            intent.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, arrayList.get(0).song_idx);
                            parentActivity.startActivity(intent);
                            parentActivity.finish();
                        }
                    });
                    dlg.show();
                } else {
                    Global.getInstance().setPlaySongListData(arrayList);
                    Intent intent = new Intent(parentActivity, PlaySongActivity.class);
                    intent.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, arrayList.get(0).song_idx);
                    parentActivity.startActivity(intent);
                    parentActivity.finish();
                }


            } else {
                Toast.makeText(parentActivity, "재생할 곡을 선택하십시오.", Toast.LENGTH_SHORT).show();
            }
        } else {
            List<SongData> arrayList = popularAdapter.getSelectedSongDataList();
            if (arrayList != null && arrayList.size() > 0) {
                if(Global.getInstance().isShowYoutubePlayPolicy()) {
                    YoutubePolicyPopup dlg = new YoutubePolicyPopup(parentActivity);
                    dlg.setListener(new YoutubePolicyPopup.onPlayListener() {
                        @Override
                        public void play() {
                            Global.getInstance().setPlaySongListData(arrayList);
                            Intent intent = new Intent(parentActivity, PlaySongActivity.class);
                            intent.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, arrayList.get(0).song_idx);
                            parentActivity.startActivity(intent);
                            parentActivity.finish();
                        }
                    });
                    dlg.show();
                } else {
                    Global.getInstance().setPlaySongListData(arrayList);
                    Intent intent = new Intent(parentActivity, PlaySongActivity.class);
                    intent.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, arrayList.get(0).song_idx);
                    parentActivity.startActivity(intent);
                    parentActivity.finish();
                }
            } else {
                Toast.makeText(parentActivity, "재생할 곡을 선택하십시오.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.btn_save_storage)
    public void onClick_btn_save_storage() {
        if(list_view.getVisibility() == View.VISIBLE) {
            List<SongData> arrayList = adapter.getSelectedSongDataList();
            if (arrayList != null && arrayList.size() > 0) {
                for (SongData song : arrayList) {
                    StorageDBHelper.getInstance().addData(song);
                }
                Toast.makeText(parentActivity, "보관함에 추가하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(parentActivity, "보관함에 추가할 곡을 선택하십시오.", Toast.LENGTH_SHORT).show();
            }
        } else {
            List<SongData> arrayList = popularAdapter.getSelectedSongDataList();
            if (arrayList != null && arrayList.size() > 0) {
                for (SongData song : arrayList) {
                    StorageDBHelper.getInstance().addData(song);
                }
                Toast.makeText(parentActivity, "보관함에 추가하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(parentActivity, "보관함에 추가할 곡을 선택하십시오.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
