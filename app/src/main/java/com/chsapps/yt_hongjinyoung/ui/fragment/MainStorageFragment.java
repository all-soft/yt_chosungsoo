package com.chsapps.yt_hongjinyoung.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.db.StorageDBHelper;
import com.chsapps.yt_hongjinyoung.ui.activity.PlaySongActivity;
import com.chsapps.yt_hongjinyoung.ui.adapter.MainStorageAdapter;
import com.chsapps.yt_hongjinyoung.ui.adapter.viewholder.SongListHolderListener;
import com.chsapps.yt_hongjinyoung.ui.base.BaseFragment;
import com.chsapps.yt_hongjinyoung.ui.view.popup.YoutubePolicyPopup;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;

public class MainStorageFragment extends BaseFragment implements SongListHolderListener {
    public final static String TAG = MainStorageFragment.class.getSimpleName();

    @BindView(R.id.list_view)
    RecyclerView list_view;

    private MainStorageAdapter adapter;
    private static HashMap<Integer, SongData> selectedAdapter = new HashMap<>();

    @BindView(R.id.view_empty)
    View view_empty;
    @BindView(R.id.tv_empty_title)
    TextView tv_empty_title;

    @BindView(R.id.tv_select_all)
    TextView tv_select_all;

    public static MainStorageFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        MainStorageFragment fragment = new MainStorageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainStorageFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.fragment_main_storage, null);
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

        refreshListView();
    }

    @Override
    public void initialize() {
        initListView();
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

    private void initListView() {
        tv_empty_title.setText(R.string.empty_storage_list);
        if (adapter == null) {
            adapter = new MainStorageAdapter(parentActivity, this);
        }
        list_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        list_view.setHasFixedSize(true);
        list_view.setAdapter(adapter);
    }

    private void refreshListView() {
        adapter.insertSongDataList(StorageDBHelper.getInstance().getList());
        if(adapter.getItemCount() == 0) {
            view_empty.setVisibility(View.VISIBLE);
        } else {
            view_empty.setVisibility(View.GONE);
        }
    }

    @Override
    public void playSong(SongData song) {
//        Global.getInstance().setPlaySongListData(adapter.getSongDataList());
//
//        Intent intent = new Intent(parentActivity, PlaySongActivity.class);
//        intent.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, song.song_idx);
//        parentActivity.startActivity(intent);
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
        setSelectedAdapter();
    }

    @Override
    public void deleteSong(SongData song) {
        StorageDBHelper.getInstance().removeData(song);
        refreshListView();
    }

    @Override
    public void shareSong(SongData song) {
        Utils.showShare(parentActivity, Utils.getString(R.string.title_share_application), Utils.getString(R.string.message_share_application));
    }

    @Override
    public void saveSong(SongData song) {
        StorageDBHelper.getInstance().addData(song);
        Toast.makeText(parentActivity, "보관함에 추가하였습니다.", Toast.LENGTH_SHORT).show();
    }

    private void setSelectedAdapter() {
        selectedAdapter = adapter.getSelectedSongDataMap();
    }

    @OnClick(R.id.btn_select_all)
    public void onClick_btn_select_all() {
        if (tv_select_all.getText().toString().equals(Utils.getString(R.string.all_select))) {
            adapter.addAllSelected();
            tv_select_all.setText(R.string.all_deselect);
        } else {
            adapter.addAllDeSelected();
            tv_select_all.setText(R.string.all_select);
        }
        setSelectedAdapter();
    }

    @OnClick(R.id.btn_play)
    public void onClick_btn_play() {
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
                    }
                });
                dlg.show();
            } else {
                Global.getInstance().setPlaySongListData(arrayList);
                Intent intent = new Intent(parentActivity, PlaySongActivity.class);
                intent.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, arrayList.get(0).song_idx);
                parentActivity.startActivity(intent);
            }
        } else {
            Toast.makeText(parentActivity, "재생할 곡을 선택하십시오.", Toast.LENGTH_SHORT).show();
        }
        setSelectedAdapter();
    }

    @OnClick(R.id.btn_save_storage)
    public void onClick_btn_save_storage() {
        List<SongData> arrayList = adapter.getSelectedSongDataList();
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
