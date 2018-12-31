package com.chsapps.yt_nahoonha.ui.view.popup;

import android.content.Context;

import com.airbnb.lottie.LottieAnimationView;
import com.chsapps.yt_nahoonha.R;

public class LoadingAdPopup extends BaseDialog {

    private final static String TAG = LoadingAdPopup.class.getSimpleName();

    private Context context;

    public LoadingAdPopup(Context context) {
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
        setContentView(R.layout.view_loading_ad);

        LottieAnimationView lottieView = (LottieAnimationView)findViewById(R.id.progress_bar);
        lottieView.playAnimation();
        lottieView.loop(true);
    }
}
