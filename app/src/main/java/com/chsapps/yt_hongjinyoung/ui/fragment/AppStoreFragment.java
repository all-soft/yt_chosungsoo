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
import com.chsapps.yt_hongjinyoung.api.APIUtils;
import com.chsapps.yt_hongjinyoung.api.model.response.BaseAPIData;
import com.chsapps.yt_hongjinyoung.api.model.response.RecommandAppAPIData;
import com.chsapps.yt_hongjinyoung.constants.APIConstants;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.ui.adapter.AppStoreAdapter;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.AppStoreAdapterHolder;
import com.chsapps.yt_hongjinyoung.ui.base.BaseFragment;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import javax.annotation.Nullable;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class AppStoreFragment extends BaseFragment {
    public final static String TAG = AppStoreFragment.class.getSimpleName();

    @BindView(R.id.list_view)
    RecyclerView list_view;

    private AppStoreAdapter adapter;
    public static AppStoreFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        AppStoreFragment fragment = new AppStoreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AppStoreFragment newInstance() {
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
        setTitle(R.string.main_menu_another_apps);
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
                RecommandAppAPIData.RecommandApp data = adapter.getData(url);
                requestRecomAppLog(data);
            }
        });
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        list_view.setLayoutManager(manager);
        list_view.setHasFixedSize(true);
        list_view.setAdapter(adapter);

        requestRecommendApp();
    }

    private void requestRecommendApp() {
        if (Utils.unableRequestAPI(parentActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_recommand_app(APIConstants.API_KEY, Utils.getLanguage(), Constants.APPID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new Consumer<RecommandAppAPIData>() {
                    @Override
                    public void accept(RecommandAppAPIData data) throws Exception {
                        if (data != null && data.isSuccess()) {
                            adapter.addAll(data.message);
                        } else {
                            parentActivity.finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    public boolean requestRecomAppLog(final RecommandAppAPIData.RecommandApp data) {
        if (Utils.unableRequestAPI(parentActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_recommend_log(APIConstants.API_KEY, Utils.getLanguage(), Constants.APPID, data.app_idx)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        final String appPackageName = data.app_package_name; // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })
                .subscribe(new Consumer<BaseAPIData>() {
                    @Override
                    public void accept(BaseAPIData appDatas) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        return true;
    }


}
