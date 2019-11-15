package com.chsapps.yt_hongjinyoung.event.ui.popup;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.ui.view.popup.BaseDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class AppEventEnterPopup extends BaseDialog {

    private final static String TAG = AppEventEnterPopup.class.getSimpleName();

    public interface AppEventEnterPopupListener {
        void onClickEnter(String name, String phone);
    }

    private Context context;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone_1)
    EditText et_phone_1;
    @BindView(R.id.et_phone_2)
    EditText et_phone_2;
    @BindView(R.id.et_phone_3)
    EditText et_phone_3;

    private AppEventEnterPopupListener listener;
    public AppEventEnterPopup(Context context, AppEventEnterPopupListener listener) {
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
        setContentView(R.layout.view_event_user_info_dlg);

        et_name.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_phone_1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_phone_2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_phone_3.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                onClick_btn_enter_event();
                return false;
            }
        });
        et_phone_3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                onClick_btn_enter_event();
                return false;
            }
        });
    }

    @OnClick(R.id.btn_close)
    public void onClick_btn_close() {
        dismiss();
    }

    @OnClick(R.id.btn_enter_event)
    public void onClick_btn_enter_event() {
        String name = et_name.getText().toString();
        String phone1 = et_phone_1.getText().toString();
        String phone2 = et_phone_2.getText().toString();
        String phone3 = et_phone_3.getText().toString();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(phone1) || TextUtils.isEmpty(phone2) || TextUtils.isEmpty(phone3)) {
            Toast.makeText(context, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if(listener != null){
            listener.onClickEnter(name, phone1 + "-" + phone2 + "-" + phone3);
            dismiss();
        }
    }
}
