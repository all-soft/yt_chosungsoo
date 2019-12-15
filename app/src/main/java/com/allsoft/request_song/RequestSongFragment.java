package com.allsoft.request_song;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.APIUtils;
import com.chsapps.yt_hongjinyoung.api.model.response.BaseAPIData;
import com.chsapps.yt_hongjinyoung.constants.APIConstants;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.ui.base.BaseFragment;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class RequestSongFragment extends BaseFragment {
    public final static String TAG = RequestSongFragment.class.getSimpleName();

    @BindView(R.id.et_input_singer)
    EditText et_input_singer;
    @BindView(R.id.et_input_song)
    EditText et_input_song;

    @BindView(R.id.btn_request_song)
    TextView btn_request_song;

    public static RequestSongFragment newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        RequestSongFragment fragment = new RequestSongFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RequestSongFragment newInstance() {
        return newInstance(null);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mainView = inflater.inflate(R.layout.func_fragment_request_song, null);
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
        setTitle(R.string.main_menu_request_song);
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
        et_input_singer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    et_input_singer.setBackgroundResource(R.drawable.func_request_song_edittext_selected_bg);
                }else {
                    et_input_singer.setBackgroundResource(R.drawable.func_request_song_edittext_bg);
                }
            }
        });
        et_input_song.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    et_input_song.setBackgroundResource(R.drawable.func_request_song_edittext_selected_bg);
                }else {
                    et_input_song.setBackgroundResource(R.drawable.func_request_song_edittext_bg);
                }
            }
        });
        et_input_singer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_input_song.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void updateButton() {
        if(TextUtils.isEmpty(et_input_singer.getText())
                && TextUtils.isEmpty(et_input_song.getText())) {
            btn_request_song.setBackgroundResource(R.drawable.func_request_song_btn_disable);
        } else {
            btn_request_song.setBackgroundResource(R.drawable.func_request_song_button_enable);
        }

    }

    @OnClick(R.id.btn_request_song)
    void onClick_btn_request_song() {
        String singer_name = et_input_singer.getText().toString();
        String song_title = et_input_song.getText().toString();
        
        if(TextUtils.isEmpty(singer_name) || TextUtils.isEmpty(song_title)) {
            Toast.makeText(parentActivity, "가수 이름 및 노래 제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        requestSong(song_title, singer_name);
    }

    private boolean requestSong(String songName, String singerName) {
        if (Utils.unableRequestAPI(parentActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })) {
            return false;
        }

        showLoading();
        subscription.add(APIUtils.getInstanse().getApiService()
                .request_song_request(
                        APIConstants.API_KEY,
                        Utils.getLanguage(),
                        Constants.APPID,
                        songName,
                        "",
                        singerName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .subscribe(new Consumer<BaseAPIData>() {
                    @Override
                    public void accept(BaseAPIData appDatas) throws Exception {
                        if(appDatas.isSuccess()) {
                            Toast.makeText(parentActivity, "요청하신 노래가 정상적으로 배달되었습니다.\n곧 리스트에서 노래를 확인해보세요!", Toast.LENGTH_SHORT).show();
                            parentActivity.finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
        return true;
    }



}
