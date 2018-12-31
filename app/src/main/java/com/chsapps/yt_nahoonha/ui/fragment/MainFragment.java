package com.chsapps.yt_nahoonha.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.api.RequestServiceListener;
import com.chsapps.yt_nahoonha.api.RequestUtils;
import com.chsapps.yt_nahoonha.api.model.BaseAPIData;
import com.chsapps.yt_nahoonha.api.model.SingersAPIData;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseFragment;
import com.chsapps.yt_nahoonha.constants.ParamConstants;
import com.chsapps.yt_nahoonha.data.SingersData;
import com.chsapps.yt_nahoonha.ui.activity.SingerMainActivity;
import com.chsapps.yt_nahoonha.ui.adapter.SingersAdapter;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SingerAdapterHolderListener;
import com.chsapps.yt_nahoonha.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment {
    public final static String TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.list_view)
    RecyclerView list_view;
    @BindView(R.id.view_empty)
    View view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;
    @BindView(R.id.btn_selected)
    TextView btn_selected;

    private SingersData selectedData;
    private SingersAdapter adapter;

    public static MainFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_main, null);
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
        btn_selected.setVisibility(selectedData == null ? View.GONE : View.VISIBLE);
        adapter.selectedSinger(selectedData);
    }

    @Override
    public void initialize() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                onClick_btn_search();
                return true;
            }
        });
        adapter = new SingersAdapter(parentActivity, false, new SingerAdapterHolderListener() {
            @Override
            public void selected(SingersData singer) {
                selectedData = singer;
                adapter.selectedSinger(selectedData);
                btn_selected.setVisibility(selectedData == null ? View.GONE : View.VISIBLE);
            }
        });
        adapter.setIsAddedAd(false);
        Utils.initLayoutListView(parentActivity, list_view);
        list_view.setAdapter(adapter);

        requestSinger();
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

    private void requestSinger() {
        //onClick_btn_search();
        if (RequestUtils.getInstanse().requestSinger(parentActivity, subscription, new RequestServiceListener() {
            @Override
            public void response(boolean is_success, BaseAPIData response) {
                if (is_success) {
                    if (response instanceof SingersAPIData) {
                        adapter.insert(((SingersAPIData) response).message);
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
        String keyword = et_search.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            requestSinger();
            return;
        }
        if (RequestUtils.getInstanse().requestSingersSearch(parentActivity, subscription, keyword, new RequestServiceListener() {
            @Override
            public void response(boolean is_success, final BaseAPIData response) {
                if (is_success) {
                    if (response instanceof SingersAPIData) {
                        adapter.insert(((SingersAPIData) response).message);
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

    @OnClick(R.id.btn_selected)
    public void onClick_btn_selected() {
        if (selectedData != null) {
            Global.getInstance().setSingersData(selectedData);

            Intent intent = new Intent(parentActivity, SingerMainActivity.class);
            intent.putExtra(ParamConstants.PARAM_SINGER_DATA, selectedData);
            startActivity(intent);

            selectedData = null;
        }
    }
}
