package com.netcosports.rectangleview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by trung on 30/01/15.
 */
public class RectangleLayoutManager extends RecyclerView.LayoutManager {

    private Context mContext;
    private ArrayList<Channel> mChannels;
    public ArrayList<Program> mPrograms;
    private int[] sizes;

    /**
     * This element allows us to know if we reach some news Childs View
     */
    public int horizontalScrollingDistance = 0;
    public int verticalScrollingDistance = 0;

    private int width;
    private int height;

    /**
     * Number of items
     */
    private int mCount;

    /**
     * First Next Invisible position when scrolling from right to left (in the Recycle-Pool)
     */
    private int mNextIndex = 0;

    /**
     * First Next Invisible position scrolling from left to right (in the Recycle-Pool)
     */
    private int mNextIndexInverse = 0;

    private int[] top;
    private int[] bottom;

    public RectangleLayoutManager(Context context, ArrayList<Channel> channels) {
        mContext = context;
        mChannels = channels;

        sizes = new int[channels.size()];

        mPrograms = new ArrayList<>();

        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = mChannels.get(i).mPrograms.size();
            mPrograms.addAll(mChannels.get(i).mPrograms);
        }

        top = new int[channels.size()];
        bottom = new int[channels.size()];

        Program.sortProgram(mPrograms);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Draw all Visible Childs
     * Pay Attention : there are two main cases :
     * 1/. 1st time filling (from scratch)
     * 2/. DataSet is changing (not for the moment, we take care of this case after)
     *
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        //We have nothing to show for an empty data set but clear any existing views
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        width = getWidth();
        height = getHeight();

        /**
         * Initialize top & bottom of view for a given channel
         */
        for (int i = 0; i < top.length; i++) {
            top[i] = i * height / 3;
            bottom[i] = (i + 1) * height / 3;
        }

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
        mCount = mPrograms.size();


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
        while (i < mCount && needFill) {

            /**
             * Retrieving from the Recycler-Pool or Creating a new fresh View for the position (mFirstPosition + i)
             *
             * Priority :
             *
             * Recycler-Pool > Creating a fresh View
             */
            View v = recycler.getViewForPosition(i);

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
             * Verify if there is still some views to be added
             */
            if (i < mCount && mPrograms.get(i).start_time < width) {
                needFill = true;
            } else {
                needFill = false;

                horizontalScrollingDistance = width;
                verticalScrollingDistance = height;

                mNextIndex = i;
                mNextIndexInverse = getItemCount();
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
        //We have nothing to show for an empty data set but clear any existing views
        if (getChildCount() == 0) {
            return 0;
        }

        int scrolledX = 0;

        if (dx < 0) {

            /**
             * We determine when we need to fill the left gap
             *
             * Suppose that initial horizontalScrollingDistance = 1550 (720 + 830), the first two items (A1 & B1) were in Recycle-Pool
             *
             * 2 CASES :
             *
             * CASE A/. Assume that dx = 10 so horizontalScrollingDistance will be 1550 - 720 - 10 = 820 > 800 (NOT NEED to fill)
             * Scroll all child by 10
             *
             * CASE B/. Assume that dx = 100 so horizontalScrollingDistance will be 1550 - 720 - 100 = 730 < 800 (NEED to fill, B1 and A1, remember that B1 take the priority).
             * There are 2 steps :
             *
             * 1/. Fill the left gap
             *
             * 2/. Scroll all by dx
             *
             * 3/. Remove possible useless ChildView
             *
             * Remember that if the current index is the last index, we must to compare the last Child's end_time with the total Scrolling (not the start_time)
             *
             */

            if (horizontalScrollingDistance == width) {
                return 0;
            }

            if (mNextIndexInverse == mCount) {
                int distanceAfterLast = mPrograms.get(0).start_time - horizontalScrollingDistance + width;

                if (dx > distanceAfterLast) {

                    horizontalScrollingDistance += dx;

                    offsetChildrenHorizontal(-dx);

                    scrolledX = -dx;
                } else {
                    horizontalScrollingDistance += distanceAfterLast;

                    offsetChildrenHorizontal(-distanceAfterLast);

                    scrolledX = -distanceAfterLast;
                }
            } else if (mNextIndexInverse < mCount) {
                int distanceAfterLast = getProgramByIndexInverse().end_time - horizontalScrollingDistance + width;

                /**
                 * in this case all views will be scrolled by dx because there is no view which will be added
                 */
                if (dx > distanceAfterLast) {
                    horizontalScrollingDistance += dx;

                    offsetChildrenHorizontal(-dx);
                }

                /**
                 * in this case we check if there are possible next childs view
                 *
                 * if there is no child view, return 0
                 * else we have to create all next childs before scrolling operation
                 *
                 */
                else {
                    /**
                     * Initialize all possible childs
                     */
                    while (mNextIndexInverse < mCount &&
                            getProgramByIndexInverse().end_time - horizontalScrollingDistance + width >= dx) {

                        View leftView = recycler.getViewForPosition(getProgramByIndexInverse().positionOrderedByStartTime);

                        addView(leftView, 0);

                        measureChildWithMargins(leftView, 0, 0);

                        //Log.i("add view at index", "" + getProgramByIndexInverse().positionOrderedByStartTime);

                        onLayoutLeftView(leftView);

                        mNextIndexInverse++;
                    }

                    horizontalScrollingDistance += dx;
                    offsetChildrenHorizontal(-dx);
                }

                scrolledX = -dx;
            }
        } else if (dx > 0) {

            /**
             * We determine when we need to fill the right gap
             *
             * initial horizontalScrollingDistance = 720
             *
             * 2 CASES :
             *
             * CASE A/. Assume that dx = 10 so horizontalScrollingDistance will be 730 < 800 (NOT NEED to fill)
             * Scroll all child by 10
             *
             * CASE B/. Assume that dx = 100 so horizontalScrollingDistance will be 820 > 800 (NEED to fill, A2 and B2, remember that A2 take the priority).
             * There are 2 steps :
             *
             * 1/. Fill the right gap
             *
             * 2/. Scroll all by dx
             *
             * 3/. Remove possible useless ChildView
             *
             * Remember that if the current index is the last index, we must to compare the last Child's end_time with the total Scrolling (not the start_time)
             *
             */

            if (horizontalScrollingDistance == mPrograms.get(mPrograms.size() - 1).end_time) {
                return 0;
            }

            if (mNextIndex == mCount) {
                int distanceBeforeNext = mPrograms.get(mNextIndex - 1).end_time - horizontalScrollingDistance;

                if (distanceBeforeNext > dx) {
                    horizontalScrollingDistance += dx;

                    offsetChildrenHorizontal(-dx);

                    scrolledX = -dx;
                } else {
                    horizontalScrollingDistance += distanceBeforeNext;
                    offsetChildrenHorizontal(-distanceBeforeNext);

                    scrolledX = -distanceBeforeNext;
                }

            } else if (mNextIndex < mCount) {
                int distanceBeforeNext = mPrograms.get(mNextIndex).start_time - horizontalScrollingDistance;

                /**
                 * in this case all views will be scrolled by dx because there is no view which will be added
                 */
                if (distanceBeforeNext > dx) {
                    horizontalScrollingDistance += dx;

                    offsetChildrenHorizontal(-dx);
                }
                /**
                 * in this case we check if there are possible next childs view
                 *
                 * if there is no child view, return 0
                 * else we have to create all next childs before scrolling operation
                 *
                 */
                else {
                    /**
                     * Initialize all possible childs
                     */
                    while (mNextIndex < mCount && mPrograms.get(mNextIndex).start_time <= dx + horizontalScrollingDistance) {
                        View rightView = recycler.getViewForPosition(mNextIndex);

                        addView(rightView);

                        measureChildWithMargins(rightView, 0, 0);

                        Log.i("add view at index", "" + mNextIndex);

                        onLayoutRightView(rightView);

                        mNextIndex++;
                    }

                    horizontalScrollingDistance += dx;
                    offsetChildrenHorizontal(-dx);
                }

                scrolledX = -dx;
            }
        }

        /**
         * put All Useless Views to Recycle-Pool
         */
        recycleViewsOutOfBounds(recycler);

        return -scrolledX;
    }

    private Program getProgramByIndexInverse() {
        for (Program program : mPrograms) {
            if (program.positionOrderedByEndTime == mNextIndexInverse) {
                return program;
            }
        }
        return null;
    }

    private void onInitialLayout(final View v) {
        if (v.getTag() != null) {
            Program program = (Program) v.getTag();

            if (program != null) {
                layoutDecorated(v, program.start_time, top[program.channel - 1], program.end_time, bottom[program.channel - 1]);
            }
        }
    }

    private void onLayoutRightView(final View v) {
        if (mNextIndex < mCount) {
            Program program = mPrograms.get(mNextIndex);

            int left = (program.start_time - horizontalScrollingDistance) + width;
            int right = left + program.duration;

            if (program != null) {
                layoutDecorated(v, left, top[program.channel - 1], right, bottom[program.channel - 1]);
            }
        }
    }

    private void onLayoutLeftView(final View v) {
        if (mNextIndexInverse < mCount) {
            Program program = getProgramByIndexInverse();

            int right = program.end_time - horizontalScrollingDistance + width;
            int left = right - program.duration;

            if (program != null) {
                layoutDecorated(v, left, top[program.channel - 1], right, bottom[program.channel - 1]);
            }
        }
    }

    @Override
    public boolean isSmoothScrolling() {
        return true;
    }

    public void recycleViewsOutOfBounds(RecyclerView.Recycler recycler) {
        final int childCount = getChildCount();
        boolean foundFirst = false;
        int first = 0;
        int last = 0;

//        ArrayList<Program> childsOrdered = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            final View v = getChildAt(i);

//            Program program = (Program) v.getTag();

            if (getDecoratedRight(v) < 0) {
//                mNextIndexInverse = program.positionOrderedByEndTime;
            } else if (getDecoratedRight(v) >= 0 &&
                    getDecoratedLeft(v) <= width) {
                if (!foundFirst) {
                    first = i;
                    foundFirst = true;
                }
                last = i;
//                childsOrdered.add(program);
            }
        }

        mNextIndex = ((Program) getChildAt(last).getTag()).positionOrderedByStartTime + 1;
        mNextIndexInverse = ((Program) getChildAt(0).getTag()).positionOrderedByEndTime + 1;

//        Program program = getProgramByIndexInverse();
//        if (program == null) {
//            mNextIndex = getChildCount();
//        }
//        else {
//            mNextIndex = program.positionOrderedByStartTime + getChildCount() + 1;
//        }


//        if (childsOrdered.size() > 0) {
//            Collections.sort(childsOrdered, new Comparator<Program>() {
//                @Override
//                public int compare(Program lhs, Program rhs) {
//                    return (lhs.positionOrderedByStartTime - rhs.positionOrderedByStartTime);
//                }
//            });
//            mNextIndex = childsOrdered.get(childsOrdered.size() - 1).positionOrderedByStartTime + 1;
//        }

//        mNextIndex = ((Program)getChildAt(last).getTag()).positionOrderedByStartTime + 1;

        Log.i("childCount", "" + childCount);
        Log.i("last", "" + last);
        Log.i("first", "" + first);

        /**
         * when right item disappear
         //         */
        for (int i = childCount - 1; i > last; i--) {
            removeAndRecycleViewAt(i, recycler);
        }

        /**
         * when left item disappear
         */
        for (int i = first - 1; i >= 0; i--) {
            removeAndRecycleViewAt(i, recycler);
        }

        Log.i("mNextIndex", "" + mNextIndex);
        Log.i("mNextIndexInverse", "" + mNextIndexInverse);
    }


    /**
     * VERTICAL CONFIG
     */
    @Override
    public boolean canScrollVertically() {
        return true;
    }

    /**
     * We distinct two cases :
     * <p/>
     * dy < 0 : RecyclerView is pulling down or flinging down (pull or fling from top to bottom) : all views are scrolling up (ex : index 50 -> 40)
     * dy > 0 : RecyclerView is pulling up or flinging up (pull or fling from bottom to top) : all views are scrolling down (ex : index 40 -> 50)
     * <p/>
     * Pay attention : when we pull or fling the view (even if there is no possible additional view : dy take a value # 0)
     *
     * @param dy
     * @param recycler
     * @param state
     * @return
     */

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        /**
         * Must modify values top[i] & bottom[i]
         */
        int scrolledY;

        if (dy > 0) { //Up
            int distance = mChannels.size() * height / 3 - verticalScrollingDistance;
            if (dy < distance) {
                verticalScrollingDistance += dy;

                offsetChildrenVertical(-dy);

                scrolledY = -dy;
            } else {
                verticalScrollingDistance += distance;

                offsetChildrenVertical(-distance);

                scrolledY = -distance;
            }

        } else { //Down
            int distance = height - verticalScrollingDistance;

            if (dy > distance) {
                verticalScrollingDistance += dy;

                offsetChildrenVertical(-dy);

                scrolledY = -dy;
            } else {
                verticalScrollingDistance += distance;

                offsetChildrenVertical(-distance);

                scrolledY = -distance;
            }
        }

        for (int i = 0; i < top.length; i++) {
            top[i] += scrolledY;
            bottom[i] += scrolledY;
        }

        /**
         * put All Useless Views to Recycle-Pool
         */
        //recycleViewsOutOfBounds(recycler);

        return -scrolledY;
//        return dy;
    }
}
