package com.netcosports.rectangleview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;


public class RecyclerViewActivity extends ActionBarActivity {

    private RecyclerView rv;
    private ArrayList<Channel> mChannels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChannels = new ArrayList<>();
        mChannels.add(new Channel(1, Program.generateFirstDummyPrograms()));
        mChannels.add(new Channel(2, Program.generateSecondDummyPrograms()));
        mChannels.add(new Channel(3, Program.generateThirdDummyPrograms()));
        mChannels.add(new Channel(4, Program.generateFourthDummyPrograms()));
        //mChannels.add(new Channel(5, Program.generateFifthDummyPrograms()));

        SimpleAdapter adapter = new SimpleAdapter(this, mChannels);
        RectangleLayoutManager layoutManager = new RectangleLayoutManager(this, mChannels);
        rv = new RecyclerView(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setClipToPadding(false);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rv.setAdapter(adapter);
        setContentView(rv);

    }
}
