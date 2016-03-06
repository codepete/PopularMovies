package com.peterung.popularmovies.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomImageView extends ImageView {
    private final float IMAGE_RATIO = 2f/3;

    public CustomImageView(Context context) {
        super(context);
    }


    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float newMeasuredHeight = getMeasuredWidth() / IMAGE_RATIO;
        setMeasuredDimension(getMeasuredWidth(), (int)newMeasuredHeight);
    }
}
