package com.netcosports.rectangleview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import it.sephiroth.android.library.widget.AbsHListView;
import it.sephiroth.android.library.widget.HListView;


public class RecyclerViewActivity extends ActionBarActivity implements AbsHListView.OnScrollListener, RecyclerView.OnScrollListener, AbsListView.OnScrollListener {

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

        for (int i = 0; i < 1000; i++) {
            mData.add(100);
        }

        mHorizontalListAdapter = new HorizontalListAdapter(this, mData);
        mHListView.setAdapter(mHorizontalListAdapter);
        mHListView.setOnScrollListener(this);

        mVListView = (ListView) findViewById(R.id.vListView);
        mVerticalListAdapter = new VerticalListAdapter(this, mData);
        mVListView.setAdapter(mVerticalListAdapter);
        mVListView.setOnScrollListener(this);

    }

    /**
     * Horizontal ListView scroll listener
     */

    @Override
    public void onScrollStateChanged(AbsHListView absHListView, int scrollState) {
        
    }

    @Override
    public void onScroll(AbsHListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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


    /**
     * Vertical ListView scroll listener
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
