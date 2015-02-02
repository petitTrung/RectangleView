package com.netcosports.rectangleview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


public class RecyclerViewActivity extends ActionBarActivity {

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SimpleAdapter adapter = new SimpleAdapter(this, Program.generateDummyPrograms());

        rv = new RecyclerView(this);
        rv.setLayoutManager(new RectangleLayoutManager(this, adapter.getData()));
        rv.setHasFixedSize(true);
        rv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rv.setAdapter(adapter);
        setContentView(rv);

    }
}
