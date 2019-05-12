package com.chsapps.yt_hongjinyoung.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.RequestServiceListener;
import com.chsapps.yt_hongjinyoung.api.RequestUtils;
import com.chsapps.yt_hongjinyoung.api.model.BaseAPIData;
import com.chsapps.yt_hongjinyoung.api.model.RecommAppAPIData;
import com.chsapps.yt_hongjinyoung.common.BaseFragment;
import com.chsapps.yt_hongjinyoung.ui.adapter.AppStoreAdapter;
import com.chsapps.yt_hongjinyoung.ui.adapter.holder.AppStoreAdapterHolder;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import javax.annotation.Nullable;

import butterknife.BindView;

public class RecommendAppStoreFragment extends BaseFragment {
    public final static String TAG = RecommendAppStoreFragment.class.getSimpleName();

    @BindView(R.id.list_view)
    RecyclerView list_view;

    private AppStoreAdapter adapter;
    public static RecommendAppStoreFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        RecommendAppStoreFragment fragment = new RecommendAppStoreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RecommendAppStoreFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_appstore, null);
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
        setBackKey(true);
//        setTitle(R.string.menu_additional_music);
        initLayout();
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

    private void initLayout() {
        adapter = new AppStoreAdapter(parentActivity, new AppStoreAdapterHolder.AppStoreAdapterListener() {
            @Override
            public void onClick(String url) {
                if(TextUtils.isEmpty(url)) {
                    return;
                }
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        list_view.setLayoutManager(manager);
        list_view.setHasFixedSize(true);
        list_view.setAdapter(adapter);

        requestRecommendApp();
    }

    private void requestRecommendApp() {
        RequestUtils.getInstanse().requestrequestRecomAppRecomApp(parentActivity, subscription, new RequestServiceListener() {
            @Override
            public void response(boolean is_success, BaseAPIData response) {
                if (is_success) {
                    if (response instanceof RecommAppAPIData) {
                        adapter.addAll(((RecommAppAPIData) response).message);
                    }
                }
            }

            @Override
            public void complete() {

            }
        });
        if (Utils.unableRequestAPI(parentActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return;
        }
    }


}
