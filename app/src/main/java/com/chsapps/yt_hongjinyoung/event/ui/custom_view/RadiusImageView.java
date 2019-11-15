package com.chsapps.yt_hongjinyoung.event.ui.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RadiusImageView extends android.support.v7.widget.AppCompatImageView {

    // 라운드처리 강도 값을 크게하면 라운드 범위가 커짐
    public float radius = 20.0f;

    public RadiusImageView(Context context) {
        super(context);
    }

    public RadiusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadiusImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
