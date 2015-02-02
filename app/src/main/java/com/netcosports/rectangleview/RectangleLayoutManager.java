package com.netcosports.rectangleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by trung on 30/01/15.
 */
public class RectangleLayoutManager extends RecyclerView.LayoutManager {

    private Context mContext;
    private ArrayList<Program> mData;

    private int screenWidth;

    /**
     * This element allows us to know if we reach some news Childs View
     */
    private int verticalScrollingDistance = 0;


    /**
     * First Visible position (which is changed on scroll)
     */
    private int mFirstPosition;

    /**
     * First Next Invisible position
     */
    private int nextIndex = 0;

    private int top1;
    private int bottom1;

    private int top2;
    private int bottom2;

    private int top3;
    private int bottom3;

    private int top4;
    private int bottom4;

    public RectangleLayoutManager(Context context, ArrayList<Program> data) {
        mContext = context;
        mData = data;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Draw all Visible Childs
     *
     * Pay Attention : there are two main cases :
     * 1/. 1st time filling (from scratch)
     * 2/. DataSet is changing (not for the moment, we take care of this case before)
     *
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        top1 = 0;
        bottom1 = getHeight() / 3;

        top2 = getHeight() / 3;
        bottom2 = 2 * getHeight() / 3;

        top3 = 2 * getHeight() / 3;
        bottom3 = getHeight();

        top4 = getHeight();
        bottom4 = 4 * getHeight() / 3;

        /**
         *
         * 1/. If we start filling from scratch (1st time - filling), top = 0
         *
         * 2/. In case of changing DataSet (getChildCount() > 0), we do not initialize all of views from 0 and
         * we do not scroll to position [0]; we re-initialize all of views from the "actual first visible position"
         * that means we re-initialize from the "mFirstPosition", top = top of the "actual first visible position"
         *
         */
//        final View oldFirstView = getChildCount() > 0 ? getChildAt(0) : null;
//        int oldTop = 0;
//        int oldLeft = 0;
//        if (oldFirstView != null) {
//            oldTop = oldFirstView.getTop();
//            oldLeft = oldFirstView.getLeft();
//        }

        /**
         * Temporarily detach and scrap all currently attached child views if existed.
         *
         * 1/. For the 1st time-filling, this operation is useless
         *
         * 2/. In case of changing DataSet, this operation detach and move all existing views to the Recycler-Pool
         *
         */
        detachAndScrapAttachedViews(recycler);

//        int top = oldTop;
//        int left = oldLeft;
//        int right = 0;
//        int bottom = 0;

        /**
         * Returns the number of items in the adapter bound to the parent RecyclerView.
         *
         * Remember : getItemCount() return the number of all views or = mData.size()
         *            getChildCount() return the number of all actuals Childs in RecyclerViews - not necessarily
         *
         * We should not use getChildCount() here because for the 1st time filling; it does not know how many child is
         * has to fill. We start filling
         *
         */
        final int count = state.getItemCount();


        /**
         * top < parentBottom or right < parentRight : Screen is not filled
         * top >= parentBottom and right >= parentRight: Screen is fully filled, we quit the loop
         *
         * mFirstPosition : mFirstPosition represent the index of the first visible child,
         *
         * mFirstPosition : take initially value of 0 of course
         *
         */
        int i = 0;
        boolean needFill = true;
        while (mFirstPosition + i < count && needFill) {

            /**
             * Retrieving from the Recycler-Pool or Creating a new fresh View for the position (mFirstPosition + i)
             *
             * Priority :
             *
             * Recycler-Pool > Creating a fresh View
             */
            View v = recycler.getViewForPosition(mFirstPosition + i);

            /**
             * Adding this View to current RecyclerView for the i-th position
             */
            addView(v, i);

            /**
             * Mesuring this View
             *
             * If the RecyclerView can be scrolled in either dimension the caller may
             * pass 0 as the widthUsed or heightUsed parameters as they will be irrelevant.
             *
             */
            measureChildWithMargins(v, 0, 0);

            /**
             * Layouting this View's position
             *
             * Remember that top & left of childView were fixed by it's channel
             *
             * more Detail : onInitialLayout(int, int)
             */
            onInitialLayout(v);

            i++;

            /**
             * Verify "screenWidth"
             */
            if (mFirstPosition + i < count && mData.get(mFirstPosition + i).start_time < screenWidth) {
                needFill = true;
            } else {
                needFill = false;

                verticalScrollingDistance = screenWidth;
                nextIndex = mFirstPosition + i;
            }
        }
    }


    /**
     * HORIZONTAL CONFIG
     */

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    /**
     * This method describes how far RecyclerView thinks the contents should scroll horizontally.
     * You are responsible for verifying edge boundaries, and determining if this scroll
     * event somehow requires that new views be added or old views get recycled.
     * <p/>
     * dx < 0 : Contents are pulled or flinged from left to right. Ex : index go from 56 to 45
     * dx > 0 : Contents are pulled or flinged from right to left. Ex : index go from 45 to 56
     * <p/>
     * Pay attention : when we pull or fling the view (even if there is no possible additional view : dx take a value # 0)
     * <p/>
     * in all two cases : we have to determine which view must be remove and put to Recycle-Pool and creating or reusing a view to fill in the gap
     * the gap maybe in right : dx > 0
     * or left : dx < 0
     *
     * @param dx
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }


        int scrolled = 0;

        if (dx < 0) {

            /**
             * We determine when we need to fill the left gap
             *
             * Suppose that initial verticalScrollingDistance = 830, the first two items were in Recycle-Pool
             *
             * 2 CASES :
             *
             * CASE A/. Assume that dx = 10 so verticalScrollingDistance will be 820 < 800 (NOT NEED to fill)
             * Scroll all child by 10
             *
             * CASE B/. Assume that dx = 100 so verticalScrollingDistance will be 730 < 800 (NEED to fill, A1 and B1, remember that B1 take the priority). There are 2 steps :
             *
             * 1/. Fill the left gap
             *
             * 2/. Remove possible useless ChildView
             *
             * a/. We scroll firstly by 30 and we initialize B1 & A1
             *
             * b/. We continue scrolling secondly by the rest of dx or by 70
             *
             */

            if (mFirstPosition >= 0) {

                while (dx < scrolled && mFirstPosition >= 0) { //-100 < 0; -100 < -30

                    if (mFirstPosition == 0) {
                        int distanceAfterLast = mData.get(0).end_time - verticalScrollingDistance; // -30; 0
                        Log.i("distanceAfterLast", "" + distanceAfterLast);

                        Log.i("dx", "" + dx);
                        Log.i("scrolled before", "" + scrolled);

                        int scrollBy = -Math.min(-distanceAfterLast, scrolled - dx); //-min(30, 0 - (-100) = 100) = -30; -min(0, -30 - (-100) = 70) = -70

                        Log.i("scrollBy", "" + scrollBy);

                        if (distanceAfterLast != 0) {

                            scrolled += scrollBy; // = -30; -100

                            Log.i("scrolled after", "" + scrolled);

                            /**
                             * Step a/. b/.
                             */
                            offsetChildrenHorizontal(-scrollBy); //30, 70

                            verticalScrollingDistance += scrollBy;
                        } else {
                            break;
                        }
                    } else {
                        Log.i("mFirstPosition", "" + mFirstPosition);

                        int distanceAfterLast = mData.get(mFirstPosition - 1).end_time - verticalScrollingDistance; // -30; 0
                        Log.i("distanceAfterLast", "" + distanceAfterLast);

                        Log.i("dx", "" + dx);
                        Log.i("scrolled before", "" + scrolled);

                        int scrollBy = -Math.min(-distanceAfterLast, scrolled - dx); //-min(30, 0 - (-100) = 100) = -30; -min(0, -30 - (-100) = 70) = -70

                        Log.i("scrollBy", "" + scrollBy);

                        scrolled += scrollBy; // = 80; 100

                        Log.i("scrolled after", "" + scrolled);

                        /**
                         * Step a/. b/.
                         */
                        offsetChildrenHorizontal(-scrollBy); //-80, -20

                        if (dx > scrolled) {

                            while (mFirstPosition - 1 > 0 && mData.get(mFirstPosition - 1).end_time > verticalScrollingDistance - dx) {
                                View rightView = recycler.getViewForPosition(mFirstPosition -1);

                                addView(rightView);

                                measureChildWithMargins(rightView, 0, 0);

                                Log.i("add view at index", "" + (mFirstPosition - 1));

                                onLayoutLeftView(rightView);

                                mFirstPosition--;
                            }

                            verticalScrollingDistance += scrollBy;

                            Log.i("verticalScrollingDistance", "" + verticalScrollingDistance);

                        } else {

                            verticalScrollingDistance += scrollBy;

                            Log.i("verticalScrollingDistance", "" + verticalScrollingDistance);
                            break;
                        }
                    }
                }
                Log.i("TAG", "**********************");
            }

        } else if (dx > 0) {

            /**
             * We determine when we need to fill the right gap
             *
             * initial verticalScrollingDistance = 720
             *
             * 2 CASES :
             *
             * CASE A/. Assume that dx = 10 so verticalScrollingDistance will be 730 < 800 (NOT NEED to fill)
             * Scroll all child by 10
             *
             * CASE B/. Assume that dx = 100 so verticalScrollingDistance will be 820 > 800 (NEED to fill, A2 and B2, remember that A2 take the priority). There are 2 steps :
             *
             * 1/. Fill the right gap
             *
             * 2/. Remove possible useless ChildView
             *
             * a/. We scroll firstly by 80 and we initialize A2 & B2
             *
             * b/. We continue scrolling secondly by the rest of dx or by 20
             *
             */

            if (nextIndex <= getItemCount()) {

                while (dx > scrolled && nextIndex <= getItemCount()) {

                    if (nextIndex == getItemCount()) {
                        int distanceBeforeNext = mData.get(nextIndex - 1).end_time - verticalScrollingDistance;
                        Log.i("distanceBeforeNext", "" + distanceBeforeNext);

                        Log.i("dx", "" + dx);
                        Log.i("scrolled before", "" + scrolled);

                        int scrollBy = Math.min(distanceBeforeNext, dx - scrolled);

                        Log.i("scrollBy", "" + scrollBy);

                        if (scrollBy > 0) {

                            scrolled += scrollBy; // = 80; 100

                            Log.i("scrolled after", "" + scrolled);

                            /**
                             * Step a/.
                             */
                            offsetChildrenHorizontal(-scrollBy); //-80, -20

                            verticalScrollingDistance += scrollBy;
                        } else {
                            break;
                        }
                    } else {
                        Log.i("index", "" + nextIndex);

                        int distanceBeforeNext = mData.get(nextIndex).start_time - verticalScrollingDistance;
                        Log.i("distanceBeforeNext", "" + distanceBeforeNext);

                        Log.i("dx", "" + dx);
                        Log.i("scrolled before", "" + scrolled);

                        int scrollBy = Math.min(distanceBeforeNext, dx - scrolled);//min(80, 100) = 80; min(80,20) = 20

                        Log.i("scrollBy", "" + scrollBy);

                        scrolled += scrollBy; // = 80; 100

                        Log.i("scrolled after", "" + scrolled);

                        /**
                         * Step a/.
                         */
                        offsetChildrenHorizontal(-scrollBy); //-80, -20

                        if (dx > scrolled) {

                            while (nextIndex < getItemCount() && mData.get(nextIndex).start_time < dx + verticalScrollingDistance) {
                                View rightView = recycler.getViewForPosition(nextIndex);

                                addView(rightView);

                                measureChildWithMargins(rightView, 0, 0);

                                Log.i("add view at index", "" + nextIndex);

                                onLayoutRightView(rightView);

                                nextIndex++;
                            }

                            verticalScrollingDistance += scrollBy;

                            Log.i("verticalScrollingDistance", "" + verticalScrollingDistance);

                        } else {

                            verticalScrollingDistance += scrollBy;

                            Log.i("verticalScrollingDistance", "" + verticalScrollingDistance);
                            break;
                        }
                    }
                }
                Log.i("TAG", "**********************");
            }
        }

        /**
         * put All Useless Views to Recycle-Pool
         */
        recycleViewsOutOfBounds(recycler);

        return -scrolled;
    }

    private void onInitialLayout(final View v) {
        if (v.getTag() != null) {
            Program program = (Program) v.getTag();
            Log.i("ADD PROGRAM", program.name);

            if (program != null) {
                if (program.channel == 2) {
                    layoutDecorated(v, program.start_time, top2, program.end_time, bottom2);
                } else if (program.channel == 3) {
                    layoutDecorated(v, program.start_time, top3, program.end_time, bottom3);
                } else if (program.channel == 4) {
                    layoutDecorated(v, program.start_time, top4, program.end_time, bottom4);
                } else {
                    layoutDecorated(v, program.start_time, top1, program.end_time, bottom1);
                }
            }
        }
    }

    private void onLayoutRightView(final View v) {
        if (nextIndex < mData.size()) {
            Program program = mData.get(nextIndex);
            Log.i("add program", program.name);
            Log.i("left", "" + screenWidth);
            Log.i("right", "" + program.duration);

            if (program != null) {
                if (program.channel == 2) {
                    layoutDecorated(v, screenWidth, top2, screenWidth + program.duration, bottom2);
                } else if (program.channel == 3) {
                    layoutDecorated(v, screenWidth, top3, screenWidth + program.duration, bottom3);
                } else if (program.channel == 4) {
                    layoutDecorated(v, screenWidth, top4, screenWidth + program.duration, bottom4);
                } else {
                    layoutDecorated(v, screenWidth, top1, screenWidth + program.duration, bottom1);
                }
            }
        }
    }

    private void onLayoutLeftView(final View v) {
        if (mFirstPosition > 0) {
            Program program = mData.get(mFirstPosition);
            Log.i("add program", program.name);
            Log.i("left", "-" + program.duration);
            Log.i("right", "" + 0);

            if (program != null) {
                if (program.channel == 2) {
                    layoutDecorated(v, -1 * program.duration, top2, 0, bottom2);
                } else if (program.channel == 3) {
                    layoutDecorated(v, -1 * program.duration, top3, 0, bottom3);
                } else if (program.channel == 4) {
                    layoutDecorated(v, -1 * program.duration, top4, 0, bottom4);
                } else {
                    layoutDecorated(v, -1 * program.duration, top1, 0, bottom1);
                }
            }
        }
    }

    public void recycleViewsOutOfBounds(RecyclerView.Recycler recycler) {
        final int childCount = getChildCount();
        final int parentWidth = getWidth();
        final int parentHeight = getHeight();
        boolean foundFirst = false;
        int first = 0;
        int last = 0;
        for (int i = 0; i < childCount; i++) {
            final View v = getChildAt(i);
            if (v.hasFocus() || (getDecoratedRight(v) >= 0 &&
                    getDecoratedLeft(v) <= parentWidth &&
                    getDecoratedBottom(v) >= 0 &&
                    getDecoratedTop(v) <= parentHeight)) {
                if (!foundFirst) {
                    first = i;
                    foundFirst = true;
                }
                last = i;
            }
        }

        /**
         * when right item disappear
         */
        for (int i = childCount - 1; i > last; i--) {
            removeAndRecycleViewAt(i, recycler);
        }

        /**
         * when left item disappear
         */
        for (int i = first - 1; i >= 0; i--) {
            removeAndRecycleViewAt(i, recycler);
        }
        if (getChildCount() == 0) {
            mFirstPosition = 0;
        } else {
            mFirstPosition += first;
        }

        Log.i("mFirstPosition", "" + mFirstPosition);
    }


    /**
     * VERTICAL CONFIG
     */
    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return dy;
    }
}
