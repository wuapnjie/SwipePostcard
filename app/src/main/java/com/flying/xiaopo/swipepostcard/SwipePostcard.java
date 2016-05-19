package com.flying.xiaopo.swipepostcard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class SwipePostcard extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private Adapter mAdapter;
    private int mItemCount;
    private int mCurrentPosition = 0;
    private float startX;
    private float startY;
    private int mMinDistance = 200;
    private int mDropNum = 0;
    private int mMaxPostcardNum = 3;
    private boolean mIsBacked = true;
    private int mDropDirection;
    private int mOffsetY = 40;

    public static final int DIRECTION_LEFT = 1 << 1;
    public static final int DIRECTION_RIGHT = 1 << 2;

    private OnPostcardRunOutListener mOnPostcardRunOutListener;
    private OnPostcardDismissListener mOnPostcardDismissListener;

    public SwipePostcard(Context context) {
        super(context);
        init(context);
    }

    public SwipePostcard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public SwipePostcard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PoiSwipePostcard, defStyleAttr, 0);
        setMaxPostcardNum(a.getInt(R.styleable.PoiSwipePostcard_maxCount, 3));
        setOffsetY(a.getInt(R.styleable.PoiSwipePostcard_offsetY, 40));
        setMinDistance(a.getInt(R.styleable.PoiSwipePostcard_minDistance, 200));
        a.recycle();
        init(context);
    }

    @TargetApi(21)
    public SwipePostcard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {

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
                if (mIsBacked) {
                    startX = capturedChild.getLeft();
                    startY = capturedChild.getTop();
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                float x = releasedChild.getX();
                float y = releasedChild.getY();

                if (Math.abs(x - startX) > mMinDistance) {
                    if (x > startX)
                        mDropDirection = DIRECTION_RIGHT;
                    else
                        mDropDirection = DIRECTION_LEFT;


                    dropPostcard(mDropDirection);
                } else {
                    mIsBacked = false;
                    mViewDragHelper.settleCapturedViewAt((int) startX, (int) startY);
                    invalidate();
                }
            }
        });
    }

    private void dropPostcard(int dropDirection) {
        removeViewAt(getChildCount() - 1);

        View second = getChildAt(getChildCount() - 1);
        View third = getChildAt(getChildCount() - 2);
        if (second != null)
            second.animate().scaleX(1f).scaleY(1f).translationYBy(-mOffsetY).setDuration(100).start();
        if (third != null)
            third.animate().scaleX(0.95f).scaleY(0.95f).translationYBy(-mOffsetY).setDuration(100).start();

        if (mCurrentPosition < mItemCount) {
            addPostcards(mCurrentPosition);
        }
        mDropNum++;
        if (mDropNum == mItemCount && mOnPostcardRunOutListener != null) {
            mOnPostcardRunOutListener.onPostcardRunOut();
        }

        if (mOnPostcardDismissListener != null) {
            mOnPostcardDismissListener.onPostcardDismiss(dropDirection);
        }
    }

    public int getOffsetY() {
        return mOffsetY;
    }

    public void setOffsetY(int offsetY) {
        mOffsetY = offsetY;
        relayout();
    }

    public int getMaxPostcardNum() {
        return mMaxPostcardNum;
    }

    public void setMaxPostcardNum(int maxPostcardNum) {
        if (maxPostcardNum < 3)
            throw new IllegalArgumentException("the maxPostcardNum can not smaller than 3");
        mMaxPostcardNum = maxPostcardNum;

        relayout();
    }

    private void relayout() {
        mCurrentPosition = 0;
        removeAllViews();
        layoutPostcards();
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

    public void setOnPostcardDismissListener(OnPostcardDismissListener onPostcardDismissListener) {
        mOnPostcardDismissListener = onPostcardDismissListener;
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
        if (position == 0) {
            postCard.setScaleX(1);
            postCard.setScaleY(1);
            postCard.setTranslationY(0);
        } else if (position == 1) {
            postCard.setScaleX(0.95f);
            postCard.setScaleY(0.95f);
            postCard.setTranslationY(mOffsetY);
        } else {
            postCard.setScaleX(0.5f);
            postCard.setScaleY(0.5f);
            postCard.setTranslationY(2 * mOffsetY);
            postCard.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
        }
        addView(postCard, 0);
        mCurrentPosition++;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true))
            invalidate();
        if (getChildAt(0) != null && getChildAt(0).getLeft() == startX && getChildAt(0).getTop() == startY)
            mIsBacked = true;
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

    public interface OnPostcardDismissListener {
        void onPostcardDismiss(int direction);
    }
}



















