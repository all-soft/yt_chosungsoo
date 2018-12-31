package com.chsapps.yt_nahoonha.ui.view.popup;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chsapps.yt_nahoonha.R;

import butterknife.BindView;
import butterknife.OnClick;

public class CommonPopup extends BaseDialog {

    private final static String TAG = CommonPopup.class.getSimpleName();

    private Context context;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.btn_negative)
    Button btn_negative;
    @BindView(R.id.btn_positive)
    Button btn_positive;
    @BindView(R.id.layer_input)
    ViewGroup layer_input;
    @BindView(R.id.et_input)
    EditText et_input;

    private CommonPopupActionListener actionListener;

    public interface CommonPopupActionListener {
        void onActionPositiveBtn();
        void onActionNegativeBtn();
    }

    public CommonPopup(Context context) {
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

    public CommonPopup setActionListener(CommonPopupActionListener listener) {
        actionListener = listener;
        return this;
    }

    public CommonPopup setTitleString(String text) {
        if (TextUtils.isEmpty(text)) {
            tv_title.setVisibility(View.GONE);
        }
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(text);
        return this;
    }

    public CommonPopup setTitleString(@StringRes int text_id) {
        String text = context.getResources().getString(text_id);
        if (TextUtils.isEmpty(text)) {
            tv_title.setVisibility(View.GONE);
        }
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(text);
        return this;
    }

    public CommonPopup setMessageString(String text) {
        if (TextUtils.isEmpty(text)) {
            tv_message.setVisibility(View.GONE);
        }
        tv_message.setVisibility(View.VISIBLE);
        tv_message.setText(text);
        return this;
    }

    public CommonPopup setMessageString(@StringRes int text_id) {
        String text = context.getResources().getString(text_id);
        if (TextUtils.isEmpty(text)) {
            tv_message.setVisibility(View.GONE);
        }
        tv_message.setVisibility(View.VISIBLE);
        tv_message.setText(text);
        return this;
    }

    public CommonPopup setBtn_negative(boolean isShow, String text) {
        btn_negative.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btn_negative.setText(text);
        return this;
    }

    public CommonPopup setBtn_positive(boolean isShow, String text) {
        btn_positive.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btn_positive.setText(text);
        return this;
    }

    public CommonPopup setBtn_negative(boolean isShow, @StringRes int text_id) {
        btn_negative.setVisibility(isShow ? View.VISIBLE : View.GONE);
        String text = context.getResources().getString(text_id);
        btn_negative.setText(text);
        return this;
    }

    public CommonPopup setBtn_positive(boolean isShow, @StringRes int text_id) {
        btn_positive.setVisibility(isShow ? View.VISIBLE : View.GONE);
        String text = context.getResources().getString(text_id);
        btn_positive.setText(text);
        return this;
    }

    public CommonPopup setET_Input(boolean isShow, String hint) {
        if(isShow) {
            layer_input.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(hint)) {
                et_input.setHint(hint);
            }
        } else {
            layer_input.setVisibility(View.GONE);
        }
        return this;
    }

    public CommonPopup setET_Input(boolean isShow, @StringRes int hint_id) {
        String text = context.getResources().getString(hint_id);
        return setET_Input(isShow, text);
    }

    public String getET_InputText() {
        return et_input.getText().toString();
    }

    private void initialize() {
        setContentView(R.layout.view_common_popup);
    }


    @OnClick(R.id.btn_negative)
    public void onClick_btn_negative() {
        if(actionListener != null) {
            actionListener.onActionNegativeBtn();
        }
        dismiss();
    }

    @OnClick(R.id.btn_positive)
    public void onClick_btn_positive() {
        if(actionListener != null) {
            actionListener.onActionPositiveBtn();
        }
        dismiss();
    }
}
