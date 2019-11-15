package com.chsapps.yt_hongjinyoung.ui.view.popup;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class RemovedAppPopup extends BaseDialog {

    private final static String TAG = RemovedAppPopup.class.getSimpleName();

    private Context context;

    @BindView(R.id.tv_comments)
    TextView tv_comments;

    public interface OnRemovedAppPopupListener {
        public void onClick_Closed();
        public void onClick_MoveAppStore();
    }

    private OnRemovedAppPopupListener listener;

    public RemovedAppPopup(Context context) {
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
        setContentView(R.layout.view_dlg_removed_app);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        tv_comments.setText(Html.fromHtml(Utils.getString(R.string.comment_removed_app)));
    }

    public void setListener(OnRemovedAppPopupListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.btn_close)
    public void onClick_btn_close() {
        if(listener != null)
            listener.onClick_Closed();
    }

    @OnClick(R.id.btn_new)
    public void onClick_btn_new() {
        if(listener != null)
            listener.onClick_MoveAppStore();
    }


}
