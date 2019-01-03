package com.chsapps.yt_hongjinyoung.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CicleDialog extends Dialog {
    @BindView(R.id.progress_text)
    TextView progressText;

    Unbinder unbinder;

    public CicleDialog(Context context) {
        super(context, R.style.popup_dailog);
        setContentView(R.layout.view_loading);
        unbinder = ButterKnife.bind(this);
        initLayout();
    }

    private void initLayout() {
    }

    @Override
    public void show() {
        super.show();
    }

    public void setMessage(CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            progressText.setText(message);
            progressText.setVisibility(View.VISIBLE);
        }
    }
}
