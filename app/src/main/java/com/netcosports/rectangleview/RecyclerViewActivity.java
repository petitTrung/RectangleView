package com.netcosports.rectangleview;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class RecyclerViewActivity extends ActionBarActivity implements RecyclerView.OnScrollListener,
        VerticalScrollingDistanceCalculator.VerticalScrollDistanceListener {

    private android.support.v7.widget.RecyclerView mHorizontalRecyclerView;
    private HorizontalRecyclerViewAdapter mHorizontalRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private RecyclerView mRecyclerView;
    private RectangleLayoutManager mLayoutManager;

    private ListView mVListView;
    private VerticalListAdapter mVerticalListAdapter;
    private VerticalScrollingDistanceCalculator mVerticalScrollingDistanceCalculator;

    private ArrayList<Channel> mChannels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_activity_recycler);

        mChannels = new ArrayList<>();
        mChannels.add(new Channel(0, Program.generateFirstDummyPrograms()));
        mChannels.add(new Channel(1, Program.generateSecondDummyPrograms()));
        mChannels.add(new Channel(2, Program.generateThirdDummyPrograms()));
//        mChannels.add(new Channel(3, Program.generateFourthDummyPrograms()));
//        mChannels.add(new Channel(4, Program.generateFifthDummyPrograms()));
//        mChannels.add(new Channel(5, Program.generateSixthDummyPrograms()));
//        mChannels.add(new Channel(6, Program.generateSeventhDummyPrograms()));

        SimpleAdapter adapter = new SimpleAdapter(this, mChannels);
        mLayoutManager = new RectangleLayoutManager(this, mChannels);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnScrollListener(this);

        ArrayList<Integer> mData = new ArrayList<>();

        for (int i = 0; i < 106; i++) {
            mData.add(100);
        }

        mHorizontalRecyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.horizontalRecyclerView);
        mHorizontalRecyclerViewAdapter = new HorizontalRecyclerViewAdapter(this, mData);
        mLinearLayoutManager = new LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false);
        mHorizontalRecyclerView.setLayoutManager(mLinearLayoutManager);
        mHorizontalRecyclerView.setAdapter(mHorizontalRecyclerViewAdapter);
        mHorizontalRecyclerView.setOnScrollListener(new android.support.v7.widget.RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
                mRecyclerView.scrollBy(dx, dy);
            }
        });

        mVListView = (ListView) findViewById(R.id.vListView);
        ArrayList<Integer> mData1 = new ArrayList<>();

        for (int i = 0; i < mChannels.size(); i++) {
            mData1.add(100);
        }
        mVerticalListAdapter = new VerticalListAdapter(this, mData1);
        mVListView.setAdapter(mVerticalListAdapter);
        mVerticalScrollingDistanceCalculator = new VerticalScrollingDistanceCalculator();
        mVerticalScrollingDistanceCalculator.setVerticalScrollDistanceListener(this);
        mVListView.setOnScrollListener(mVerticalScrollingDistanceCalculator);


//        new CountDownTimer(3000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            public void onFinish() {
//                mLayoutManager.setData(mChannels);
//            }
//        }.start();
//
//        new CountDownTimer(6000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            public void onFinish() {
//                mChannels.add(new Channel(7, Program.generateSeventhDummyPrograms()));
//                mLayoutManager.setData(mChannels);
//                mLinearLayoutManager.scrollToPositionWithOffset(0, 0);
//                mVListView.smoothScrollToPositionFromTop(0, 0);
//            }
//        }.start();
    }

    /**
     * Recycler scroll listener
     */
    @Override
    public void onScrollStateChanged(int newState) {

    }

    @Override
    public void onScrolled(int dx, int dy) {
        /**
         * Scrolling Horizontal ListView
         */
        int x = mLayoutManager.horizontalScrollingDistance - mRecyclerView.getWidth();

        int x1 = x / 100;
        int y1 = x1 * 100 - x;
        mLinearLayoutManager.scrollToPositionWithOffset(x1, y1);


        /**
         * Scrolling Vertical ListView
         */

        int y = mLayoutManager.verticalScrollingDistance - mRecyclerView.getHeight();
//
//        int x2 = y/ConvertHelper.toPixels(200, getResources().getDisplayMetrics());
//        int y2 = x2*ConvertHelper.toPixels(200, getResources().getDisplayMetrics()) - y;
//
//        mVListView.setSelectionFromTop(x2, y2);

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

    @Override
    public void onVerticalScrollDistanceChanged(int delta, int total) {
        mRecyclerView.scrollBy(0, -delta);
    }
}
