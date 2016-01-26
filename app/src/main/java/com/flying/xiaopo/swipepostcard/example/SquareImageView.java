package com.flying.xiaopo.swipepostcard.example;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Flying SnowBean on 2016/1/25.
 */
public class SquareImageView extends ImageView {
    private final String TAG = SquareImageView.class.getSimpleName();


    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int length = width > height ? height : width;
        setMeasuredDimension(length,length);
    }
}
