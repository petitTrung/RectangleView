package com.netcosports.rectangleview;

import android.view.View;

import it.sephiroth.android.library.widget.AbsHListView;

/**
 * Created by trung on 06/02/15.
 */
public class HorizontalScrollingDistanceCalculator implements AbsHListView.OnScrollListener {

    private HorizontalScrollDistanceListener mScrollDistanceListener;

    private boolean mListScrollStarted;
    private int mFirstVisibleItem;
    private int mFirstVisibleWidth;
    private int mFirstVisibleLeft, mFirstVisibleRight;
    private int mTotalScrollDistance;

    @Override
    public void onScrollStateChanged(AbsHListView view, int scrollState) {
        if (view.getCount() == 0) return;
        switch (scrollState) {
            case SCROLL_STATE_IDLE: {
                mListScrollStarted = false;
                break;
            }
            case SCROLL_STATE_TOUCH_SCROLL: {
                final View firstChild = view.getChildAt(0);
                mFirstVisibleItem = view.getFirstVisiblePosition();
                mFirstVisibleLeft = firstChild.getLeft();
                mFirstVisibleRight = firstChild.getRight();
                mFirstVisibleWidth = firstChild.getWidth();
                mListScrollStarted = true;
                mTotalScrollDistance = 0;
                break;
            }
        }
    }

    public int getTotalScrollDistance() {
        return mTotalScrollDistance;
    }

    @Override
    public void onScroll(AbsHListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount == 0 || !mListScrollStarted) return;
        final View firstChild = view.getChildAt(0);
        final int firstVisibleLeft = firstChild.getLeft(), firstVisibleRight = firstChild.getRight();
        final int firstVisibleWidth = firstChild.getWidth();
        final int delta;
        if (firstVisibleItem > mFirstVisibleItem) {
            mFirstVisibleLeft += mFirstVisibleWidth;
            delta = firstVisibleLeft - mFirstVisibleLeft;
        } else if (firstVisibleItem < mFirstVisibleItem) {
            mFirstVisibleRight -= mFirstVisibleWidth;
            delta = firstVisibleRight - mFirstVisibleRight;
        } else {
            delta = firstVisibleRight - mFirstVisibleRight;
        }
        mTotalScrollDistance += delta;
        if (mScrollDistanceListener != null) {
            mScrollDistanceListener.onHorizontalScrollDistanceChanged(delta, mTotalScrollDistance);
        }
        mFirstVisibleLeft = firstVisibleLeft;
        mFirstVisibleRight = firstVisibleRight;
        mFirstVisibleWidth = firstVisibleWidth;
        mFirstVisibleItem = firstVisibleItem;
    }

    public void setHorizontalScrollDistanceListener(HorizontalScrollDistanceListener listener) {
        mScrollDistanceListener = listener;
    }

    public static interface HorizontalScrollDistanceListener {
        void onHorizontalScrollDistanceChanged(int delta, int total);
    }
}
