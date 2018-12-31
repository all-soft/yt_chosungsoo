package com.chsapps.yt_nahoonha.ui.fragment.play_song;

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
import android.widget.TextView;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.common.BaseFragment;
import com.chsapps.yt_nahoonha.data.ReviewData;
import com.chsapps.yt_nahoonha.ui.adapter.ReviewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaySongReviewFragment extends BaseFragment {
    public final static String TAG = PlaySongReviewFragment.class.getSimpleName();

    @BindView(R.id.list_view)
    RecyclerView list_view;

    @BindView(R.id.view_empty)
    ViewGroup view_empty;

    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    @BindView(R.id.tv_like_cnt)
    TextView tv_like_cnt;
    @BindView(R.id.tv_comments_cnt)
    TextView tv_comments_cnt;

    private ReviewAdapter adapter;
    private List<ReviewData> arrSongs = new ArrayList<>();

    public static PlaySongReviewFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        PlaySongReviewFragment fragment = new PlaySongReviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlaySongReviewFragment newInstance() {
        return newInstance(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_review_play_song, null);
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
        tv_empty_title.setText(R.string.no_songs);
//        Utils.initLayoutListView(parentActivity, list_view);
//        adapter = new ReviewAdapter(parentActivity, new ReviewAdapterHolderListener() {
//            @Override
//            public void giveLike(ReviewData reviewData) {
//
//            }
//
//            @Override
//            public void writeComment(ReviewData reviewData) {
//
//            }
//        });
//        list_view.setAdapter(adapter);
//        arrSongs = getArguments().getParcelableArrayList(ParamConstants.PARAM_ARRAY_SONG_DATA);
//        if(!adapter.insert(arrSongs)) {
//            list_view.setVisibility(View.GONE);
//            view_empty.setVisibility(View.VISIBLE);
//        } else {
//            list_view.setVisibility(View.VISIBLE);
//            view_empty.setVisibility(View.GONE);
//        }
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

    @OnClick(R.id.btn_like)
    public void onClick_btn_like() {

    }
}
