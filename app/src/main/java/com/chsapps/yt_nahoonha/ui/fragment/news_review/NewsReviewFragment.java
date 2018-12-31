package com.chsapps.yt_nahoonha.ui.fragment.news_review;

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
import com.chsapps.yt_nahoonha.data.NewsReviewData;
import com.chsapps.yt_nahoonha.ui.adapter.NewsReviewAdapter;
import com.chsapps.yt_nahoonha.utils.DelayListenerListener;
import com.chsapps.yt_nahoonha.utils.Utils;

import butterknife.BindView;

public class NewsReviewFragment extends BaseFragment {
    public final static String TAG = NewsReviewFragment.class.getSimpleName();

    @BindView(R.id.list_view)
    RecyclerView list_view;

    @BindView(R.id.view_empty)
    ViewGroup view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    @BindView(R.id.tv_comments)
    TextView tv_comments;
    @BindView(R.id.tv_create_at)
    TextView tv_create_at;
    @BindView(R.id.tv_like_cnt)
    TextView tv_like_cnt;
    @BindView(R.id.tv_unlike_cnt)
    TextView tv_unlike_cnt;

    @BindView(R.id.et_comments)
    EditText et_comments;

    NewsReviewData newsReviewData;

    NewsReviewAdapter adapter;

    public static NewsReviewFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        NewsReviewFragment fragment = new NewsReviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static NewsReviewFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_singer_news, null);
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
        setHasOptionsMenu(true);
        newsReviewData = getArguments().getParcelable(ParamConstants.PARAM_NEWS_REVIEW_DATA);

        setTitle(newsReviewData.getComment());
        tv_empty_title.setText(R.string.no_songs);
        Utils.initLayoutListView(parentActivity, list_view);
        adapter = new NewsReviewAdapter(parentActivity);
        list_view.setAdapter(adapter);

        tv_comments.setText(newsReviewData.getComment());
        tv_create_at.setText(newsReviewData.getCreate_at());
        tv_like_cnt.setText(String.valueOf(newsReviewData.like_cnt));
        tv_unlike_cnt.setText(String.valueOf(newsReviewData.comment_cnt));

        request_review();
        Utils.delay(subscription, 1000, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                Utils.showSoftKeyboard(et_comments);
            }
        });
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
        parentActivity.getMenuInflater().inflate(R.menu.news_review, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.btn_like) {

        }
        return super.onOptionsItemSelected(item);
    }

    public void request_review() {

    }
}
