package com.netcosports.rectangleview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import java.util.ArrayList;

import it.sephiroth.android.library.widget.HListView;


public class RecyclerViewActivity extends ActionBarActivity implements RecyclerView.OnScrollListener, View.OnTouchListener {

    private RecyclerView mRecyclerView;
    private RectangleLayoutManager mLayoutManager;

    private ListView mVListView;
    private VerticalListAdapter mVerticalListAdapter;

    private HListView mHListView;
    private HorizontalListAdapter mHorizontalListAdapter;

    private ArrayList<Channel> mChannels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_activity_recycler);

        mChannels = new ArrayList<>();
        mChannels.add(new Channel(1, Program.generateFirstDummyPrograms()));
        mChannels.add(new Channel(2, Program.generateSecondDummyPrograms()));
        mChannels.add(new Channel(3, Program.generateThirdDummyPrograms()));
        mChannels.add(new Channel(4, Program.generateFourthDummyPrograms()));
        mChannels.add(new Channel(5, Program.generateFifthDummyPrograms()));
        mChannels.add(new Channel(6, Program.generateSixthDummyPrograms()));

        SimpleAdapter adapter = new SimpleAdapter(this, mChannels);
        mLayoutManager = new RectangleLayoutManager(this, mChannels);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnScrollListener(this);

        mHListView = (HListView) findViewById(R.id.hListView);

        ArrayList<Integer> mData = new ArrayList<>();

        for (int i = 0; i < 106; i++) {
            mData.add(100);
        }

        mHorizontalListAdapter = new HorizontalListAdapter(this, mData);
        mHListView.setAdapter(mHorizontalListAdapter);
        mHListView.setOnTouchListener(this);

        mVListView = (ListView) findViewById(R.id.vListView);
        ArrayList<Integer> mData1 = new ArrayList<>();

        for (int i = 0; i < mChannels.size(); i++) {
            mData1.add(100);
        }
        mVerticalListAdapter = new VerticalListAdapter(this, mData1);
        mVListView.setAdapter(mVerticalListAdapter);
        mVListView.setOnTouchListener(this);


        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Remove it here unless you want to get this callback for EVERY
                //layout pass, which can get you into infinite loops if you ever
                //modify the layout from within this method.
                mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                mVerticalListAdapter.setItemHeight(mRecyclerView.getHeight()/3);
                //Now you can get the width and height from content
            }
        });

    }

    /**
     * Recycler scroll listener
     *
     */
    @Override
    public void onScrollStateChanged(int newState) {

    }

    @Override
    public void onScrolled(int dx, int dy) {
        /**
         * Horizontal ListView
         */

        int x = mLayoutManager.horizontalScrollingDistance - mRecyclerView.getWidth();

        int left = getListLeftPosition();
        if (left != x) {
            View child = mHListView.getChildAt(mHListView.getHeaderViewsCount());
            int width = 1;
            if (child != null) {
                width = child.getWidth() + mHListView.getDividerWidth();
            }
            int position = x / width;
            int offset = (position * width) - x;
            mHListView.setSelectionFromLeft(position, offset);
        }



        int y = mLayoutManager.verticalScrollingDistance - mRecyclerView.getHeight();

        int top = getListTopPosition();
        if (top != y) {
            View child = mVListView.getChildAt(mVListView.getHeaderViewsCount());
            int height = 1;
            if (child != null) {
                height = child.getHeight() + mVListView.getDividerHeight();
            }
            int position = y / height;
            int offset = (position * height) - y;
            mVListView.setSelectionFromTop(position, offset);
        }

    }

    private int getListTopPosition() {
        int firstVisibleItem = mVListView.getFirstVisiblePosition();
        View child = mVListView.getChildAt(mVListView.getHeaderViewsCount());
        int position = 0;
        if (child != null) {
            position = (firstVisibleItem * (child.getMeasuredHeight() + mVListView.getDividerHeight()))
                    - child.getTop();
        }
        return position;
    }

    private int getListLeftPosition() {
        int firstVisibleItem = mHListView.getFirstVisiblePosition();
        View child = mHListView.getChildAt(mHListView.getHeaderViewsCount());
        int position = 0;
        if (child != null) {
            position = (firstVisibleItem * (child.getMeasuredWidth() + mHListView.getDividerWidth()))
                    - child.getLeft();
        }
        return position;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        mRecyclerView.dispatchTouchEvent(event);

        return false;
    }
}
