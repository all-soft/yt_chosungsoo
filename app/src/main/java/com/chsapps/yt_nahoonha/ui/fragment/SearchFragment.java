package com.chsapps.yt_nahoonha.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.api.RequestServiceListener;
import com.chsapps.yt_nahoonha.api.RequestUtils;
import com.chsapps.yt_nahoonha.api.model.BaseAPIData;
import com.chsapps.yt_nahoonha.api.model.SongAPIData;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseFragment;
import com.chsapps.yt_nahoonha.constants.ParamConstants;
import com.chsapps.yt_nahoonha.data.SongData;
import com.chsapps.yt_nahoonha.db.StorageDBHelper;
import com.chsapps.yt_nahoonha.ui.activity.PlayerActivity;
import com.chsapps.yt_nahoonha.ui.adapter.SongAdapter;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SongAdapterHolderListener;
import com.chsapps.yt_nahoonha.ui.view.EndlessRecyclerOnScrollListener;
import com.chsapps.yt_nahoonha.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    public final static String TAG = SearchFragment.class.getSimpleName();

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.list_view)
    RecyclerView list_view;
    @BindView(R.id.view_empty)
    View view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    private SongAdapter adapter;

    private EndlessRecyclerOnScrollListener scrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore(int currentPage) {
            if(currentPage - 1 != adapter.getCurrentPage()) {
                adapter.setCurrentPage(currentPage - 1);
                requestSearch();
            }
        }
    };

    public static SearchFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SearchFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_search, null);
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
        setBackKey(true);
        setTitle(R.string.Search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                onClick_btn_search();
                return true;
            }
        });
        adapter = new SongAdapter(parentActivity, new SongAdapterHolderListener() {
            @Override
            public void selected(SongData song) {
                Global.getInstance().setPlaySongListData(adapter.getSongDataList());

                Intent intent = new Intent(parentActivity, PlayerActivity.class);
                intent.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, song.song_idx);
                parentActivity.startActivity(intent);
            }

            @Override
            public void save(SongData song) {
                if(StorageDBHelper.getInstance().addData(song)) {
                    Toast.makeText(parentActivity, R.string.success_to_save_storage, Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
        Utils.initLayoutListView(parentActivity, list_view);
        list_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(this);

        scrollListener.initialize();
        list_view.addOnScrollListener(scrollListener);

        requestSearch();
    }

    @Override
    public void clearMemory() {
    }

    @Override
    public boolean onBackPressed() {
        String keyword = et_search.getText().toString();
        if(!TextUtils.isEmpty(keyword)) {
            et_search.setText("");
            return false;
        }

        return super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        parentActivity.getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void requestSearch() {
        //onClick_btn_search();
        String keyword = et_search.getText().toString();
        if(TextUtils.isEmpty(keyword) || keyword.length() < 2) {
            Toast.makeText(parentActivity, R.string.must_have_2_character_keyword, Toast.LENGTH_SHORT).show();
            return;
        }
        if (RequestUtils.getInstanse().requestSongsSearch(parentActivity, subscription, keyword, adapter.getCurrentPage() - 1, new RequestServiceListener() {
            @Override
            public void response(boolean is_success, BaseAPIData response) {
                if (is_success) {
                    if (response instanceof SongAPIData) {
                        List<SongData> list = ((SongAPIData) response).message;
                        if(list.size() > 0) {
                            list.remove(0);
                        }
                        adapter.insert(list);
                    }
                }
            }

            @Override
            public void complete() {
                dismissLoading();
                view_empty.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                list_view.setVisibility(adapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
            }
        })) {
            showLoading();
        }
    }

    @OnClick(R.id.btn_search)
    public void onClick_btn_search() {
        requestSearch();
    }

    @Override
    public void onRefresh() {
        refresh_layout.setRefreshing(false);
        scrollListener.initialize();

        adapter.clear();
        requestSearch();
    }
}
