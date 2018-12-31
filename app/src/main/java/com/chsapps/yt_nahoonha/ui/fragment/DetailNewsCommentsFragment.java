package com.chsapps.yt_nahoonha.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.common.BaseFragment;
import com.chsapps.yt_nahoonha.constants.ParamConstants;
import com.chsapps.yt_nahoonha.data.NewsData;
import com.chsapps.yt_nahoonha.data.SingersData;
import com.chsapps.yt_nahoonha.data.SongData;
import com.chsapps.yt_nahoonha.ui.adapter.VideoAdapter;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SongAdapterHolderListener;
import com.chsapps.yt_nahoonha.utils.Utils;

import butterknife.BindView;

public class DetailNewsCommentsFragment extends BaseFragment {
    public final static String TAG = DetailNewsCommentsFragment.class.getSimpleName();

    @BindView(R.id.list_view)
    RecyclerView list_view;

    @BindView(R.id.view_empty)
    ViewGroup view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    @BindView(R.id.et_comments)
    EditText et_comments;
    @BindView(R.id.tv_comments)
    TextView tv_comments;
    @BindView(R.id.tv_create_at)
    TextView tv_create_at;
    @BindView(R.id.tv_like_cnt)
    TextView tv_like_cnt;
    @BindView(R.id.tv_comments_cnt)
    TextView tv_comments_cnt;

    private SingersData singersData;
    private NewsData newsData;
    private VideoAdapter adapter;

    public static DetailNewsCommentsFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        DetailNewsCommentsFragment fragment = new DetailNewsCommentsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static DetailNewsCommentsFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_singer_video, null);
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
        singersData = getArguments().getParcelable(ParamConstants.PARAM_SINGER_DATA);
        Utils.initLayoutListView(parentActivity, list_view);
        adapter = new VideoAdapter(parentActivity, new SongAdapterHolderListener() {
            @Override
            public void selected(SongData song) {

            }

            @Override
            public void save(SongData song) {

            }
        });
        list_view.setAdapter(adapter);

        requestSongList();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        parentActivity.getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void requestSongList() {

    }
}
