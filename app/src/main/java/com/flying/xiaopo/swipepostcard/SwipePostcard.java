package com.flying.xiaopo.swipepostcard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动卡片，继承自FrameLayout
 * Created by Flying SnowBean on 2016/1/24.
 */
public class SwipePostcard extends FrameLayout {
    private final String TAG = SwipePostcard.class.getSimpleName();

    private ViewDragHelper mViewDragHelper;
    private Adapter mAdapter;
    private int mItemCount;
    private int mCurrentPosition = 0;
    private float startX;
    private float startY;
    private int mMinDistance = 200;
    private int mDropNum = 0;
    private int mMaxPostcardNum = 3;

    private OnPostcardRunOutListener mOnPostcardRunOutListener;

    public SwipePostcard(Context context) {
        super(context);
        init();
    }

    public SwipePostcard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipePostcard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public SwipePostcard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int getOrderedChildIndex(int index) {
                return getChildCount() - 1;
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                startX = capturedChild.getLeft();
                startY = capturedChild.getTop();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                float x = releasedChild.getX();
                float y = releasedChild.getY();

                if (Math.abs(x - startX) > mMinDistance) {
                    dropPostcard();
                } else {
                    mViewDragHelper.settleCapturedViewAt((int) startX, (int) startY);
                    invalidate();
                }
            }
        });
    }

    private void dropPostcard() {
        removeViewAt(getChildCount() - 1);
        if (mCurrentPosition < mItemCount) {
            addPostcards(mCurrentPosition);
        }
        mDropNum++;
        if (mDropNum == mItemCount && mOnPostcardRunOutListener != null) {
            mOnPostcardRunOutListener.onPostcardRunOut();
        }
    }

    public int getMaxPostcardNum() {
        return mMaxPostcardNum;
    }

    public void setMaxPostcardNum(int maxPostcardNum) {
        if (maxPostcardNum<3) throw new IllegalArgumentException("the maxPostcardNum can not smaller than 3");
        mMaxPostcardNum = maxPostcardNum;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        mItemCount = mAdapter.getItemCount();
        layoutPostcards();
    }

    public void setMinDistance(int minDistance) {
        mMinDistance = minDistance;
    }

    public int getMinDistance() {
        return mMinDistance;
    }

    public void setOnPostcardRunOutListener(OnPostcardRunOutListener onPostcardRunOutListener) {
        mOnPostcardRunOutListener = onPostcardRunOutListener;
    }

    private void layoutPostcards() {
        int preCount = mItemCount < mMaxPostcardNum ? mItemCount : mMaxPostcardNum;
        for (int i = 0; i < preCount; i++) {
            addPostcards(i);
        }
    }

    private void addPostcards(int position) {
        View postCard = getView(position);
        addView(postCard, 0);
        mCurrentPosition++;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true))
            invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;

    }

    private View getView(int position) {
        int type = position % mMaxPostcardNum;
        View view = null;
        if (mAdapter.mViewList.size() < mMaxPostcardNum) {
            view = mAdapter.createView(this);
            mAdapter.mViewList.add(view);
        } else {
            view = mAdapter.mViewList.get(type);
            if (view == null) {
                view = mAdapter.createView(this);
                mAdapter.mViewList.add(view);
            }
        }
        mAdapter.bindView(view, position);
        return view;
    }

    public static abstract class Adapter {
        List<View> mViewList = new ArrayList<>();

        public abstract View createView(ViewGroup parent);

        public abstract void bindView(View view, int position);

        public abstract int getItemCount();

    }

    public interface OnPostcardRunOutListener {
        void onPostcardRunOut();
    }

}



















