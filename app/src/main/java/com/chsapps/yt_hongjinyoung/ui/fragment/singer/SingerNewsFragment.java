package com.chsapps.yt_hongjinyoung.ui.fragment.singer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.RequestServiceListener;
import com.chsapps.yt_hongjinyoung.api.RequestUtils;
import com.chsapps.yt_hongjinyoung.api.model.BaseAPIData;
import com.chsapps.yt_hongjinyoung.api.model.NewsAPIData;
import com.chsapps.yt_hongjinyoung.common.BaseFragment;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.data.NewsData;
import com.chsapps.yt_hongjinyoung.data.SingersData;
import com.chsapps.yt_hongjinyoung.ui.activity.NewsWebActivity;
import com.chsapps.yt_hongjinyoung.ui.adapter.NewsAdapter;
import com.chsapps.yt_hongjinyoung.ui.adapter.listener.NewsAdapterHolderListener;
import com.chsapps.yt_hongjinyoung.ui.view.EndlessRecyclerOnScrollListener;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SingerNewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    public final static String TAG = SingerNewsFragment.class.getSimpleName();

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;
    @BindView(R.id.list_view)
    RecyclerView list_view;

    @BindView(R.id.view_empty)
    ViewGroup view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    private SingersData singersData;
    private NewsAdapter adapter;

    private static List<NewsData> responseData = new ArrayList<>();

    private EndlessRecyclerOnScrollListener scrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore(int currentPage) {
            if(currentPage - 1 != adapter.getCurrentPage()) {
                adapter.setCurrentPage(currentPage - 1);
                requestNewsList(true);
            }
        }
    };

    public static void clearData() {
        responseData.clear();
    }

    public static SingerNewsFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        SingerNewsFragment fragment = new SingerNewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SingerNewsFragment newInstance() {
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
        tv_empty_title.setText(R.string.no_songs);
        singersData = getArguments().getParcelable(ParamConstants.PARAM_SINGER_DATA);
        Utils.initLayoutListView(parentActivity, list_view);
        adapter = new NewsAdapter(parentActivity, new NewsAdapterHolderListener() {
            @Override
            public void selected(NewsData news) {
                Intent intent = new Intent(parentActivity, NewsWebActivity.class);
                intent.putExtra(ParamConstants.PARAM_NEWS_TITLE, news.getTitle());
                intent.putExtra(ParamConstants.PARAM_NEWS_URL, news.getLink());
                startActivity(intent);
//                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(news.getLink()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            }

            @Override
            public void selectedHearts(NewsData news) {

            }

            @Override
            public void selectedComments(NewsData news) {

            }

            @Override
            public void selectedShare(NewsData news) {
                Utils.showShare(parentActivity, news.getTitle(), news.getImage_url());
            }
        });
        list_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(this);

        scrollListener.initialize();
        list_view.addOnScrollListener(scrollListener);

        requestNewsList(false);
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

    private void requestNewsList(boolean isForce) {
        if(!isForce && responseData.size() > 0) {
            adapter.insert(responseData);
            return;
        }
        if(RequestUtils.getInstanse().requestNews(parentActivity, subscription, singersData.getNews_keyword(), adapter.getCurrentPage() - 1, new RequestServiceListener() {
            @Override
            public void response(boolean is_success, BaseAPIData response) {
                if(is_success) {
                    if(response instanceof NewsAPIData) {
                        responseData.addAll(((NewsAPIData) response).message);
                        adapter.insert(((NewsAPIData) response).message);
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


    @Override
    public void onRefresh() {
        responseData.clear();
        refresh_layout.setRefreshing(false);
        scrollListener.initialize();

        adapter.clear();
        requestNewsList(true);
    }
}
