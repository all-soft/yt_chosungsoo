package com.chsapps.yt_hongjinyoung.ui.fragment.singer;

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
import android.widget.Toast;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.RequestServiceListener;
import com.chsapps.yt_hongjinyoung.api.RequestUtils;
import com.chsapps.yt_hongjinyoung.api.model.BaseAPIData;
import com.chsapps.yt_hongjinyoung.api.model.SongAPIData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseFragment;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.data.SingersData;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.db.StorageDBHelper;
import com.chsapps.yt_hongjinyoung.ui.activity.PlayerActivity;
import com.chsapps.yt_hongjinyoung.ui.adapter.SongAdapter;
import com.chsapps.yt_hongjinyoung.ui.adapter.listener.SongAdapterHolderListener;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;

public class SingerStorageFragment extends BaseFragment {
    public final static String TAG = SingerStorageFragment.class.getSimpleName();

    @BindView(R.id.list_view)
    RecyclerView list_view;

    @BindView(R.id.view_empty)
    ViewGroup view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    @BindView(R.id.layer_recomment_empty)
    ViewGroup layer_recomment_empty;

    private SingersData singersData;
    private SongAdapter adapter;

    private static SongAPIData responseData;

    public static SingerStorageFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        SingerStorageFragment fragment = new SingerStorageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SingerStorageFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_singer_storage, null);
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
                if(layer_recomment_empty.getVisibility() == View.VISIBLE) {
                    if(StorageDBHelper.getInstance().addData(song)) {
                        Toast.makeText(parentActivity, R.string.success_to_save_storage, Toast.LENGTH_SHORT).show();
                        refreshData();
                    }
                } else {
                    if(StorageDBHelper.getInstance().removeData(song)) {
                        Toast.makeText(parentActivity, R.string.success_to_remove_storage, Toast.LENGTH_SHORT).show();
                        refreshData();
                    }
                }
            }
        });
        adapter.setIsDeletedItem(true);
        list_view.setAdapter(adapter);

        refreshData();
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

    private void refreshData() {

        layer_recomment_empty.setVisibility(View.GONE);
        ArrayList<SongData> listData = StorageDBHelper.getInstance().getList();
        if(listData.size() == 0) {
            adapter.setIsDeletedItem(false);
            view_empty.setVisibility(View.GONE);
            list_view.setVisibility(View.VISIBLE);
            requestSongList();
        } else {
            adapter.setIsDeletedItem(true);
            adapter.insert(listData);
            view_empty.setVisibility(View.GONE);
            list_view.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    private void requestSongList() {
        layer_recomment_empty.setVisibility(View.VISIBLE);

        if(responseData != null) {
            adapter.insert(responseData.message);
            return;
        }
        if(RequestUtils.getInstanse().requestRecommendSong(parentActivity, subscription, singersData.getCategory_idx(), new RequestServiceListener() {
            @Override
            public void response(boolean is_success, BaseAPIData response) {
                if(is_success) {
                    if(response instanceof SongAPIData) {
                        responseData = (SongAPIData)response;
                        adapter.setIsDeletedItem(false);
                        adapter.insert(((SongAPIData) response).message);
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
}
