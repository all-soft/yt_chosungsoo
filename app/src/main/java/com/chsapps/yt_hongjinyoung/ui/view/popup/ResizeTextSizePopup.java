package com.chsapps.yt_hongjinyoung.ui.view.popup;

import android.content.Context;
import android.widget.SeekBar;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;

import butterknife.BindView;
import butterknife.OnClick;

public class ResizeTextSizePopup extends BaseDialog {

    private final static String TAG = ResizeTextSizePopup.class.getSimpleName();

    public interface  ResizeTextSizePopupListener {
        void onChangedTextSize(int size);
    }

    private Context context;

    private ResizeTextSizePopupListener listener;
    private int resizeTextSize = 5;

    @BindView(R.id.seekbar)
    SeekBar seekbar;

    public ResizeTextSizePopup(Context context, ResizeTextSizePopupListener listener) {
        super(context, true, null);
        this.context = context;
        this.listener = listener;
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
        setContentView(R.layout.view_resize_textsize);

        resizeTextSize = Global.getInstance().getResizeTextSize();
        seekbar.setMax(10);
        seekbar.setProgress(resizeTextSize);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                resizeTextSize = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.btn_cancel)
    public void onClick_btn_cancel() {
        dismiss();
    }

    @OnClick(R.id.btn_apply)
    public void onClick_btn_apply() {
        Global.getInstance().setResizeTextSize(resizeTextSize);
        if(listener != null) {
            listener.onChangedTextSize(resizeTextSize);
        }
        dismiss();
    }
}
