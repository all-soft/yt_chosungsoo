package com.chsapps.yt_hongjinyoung.ui.view.popup;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class YoutubePolicyPopup extends BaseDialog {

    private final static String TAG = YoutubePolicyPopup.class.getSimpleName();

    private Context context;

    @BindView(R.id.tv_comments)
    TextView tv_comments;

    public interface onPlayListener {
        public void play();
    }

    onPlayListener listener;
    public YoutubePolicyPopup(Context context) {
        super(context, true, null);
        this.context = context;
        initialize();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initialize() {
        setContentView(R.layout.view_youtubbe_policy_dlg);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        tv_comments.setText(Html.fromHtml(Utils.getString(R.string.youtube_policy)));
    }

    public void setListener(onPlayListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.btn_play)
    public void onClick_btn_play() {
        if(listener != null)
            listener.play();

        dismiss();
    }

    @OnClick(R.id.btn_dont_see)
    public void onClick_btn_dont_see() {
        Global.getInstance().setShowYoutubePlayPolicy();

        if(listener != null)
            listener.play();
        dismiss();
    }


}
