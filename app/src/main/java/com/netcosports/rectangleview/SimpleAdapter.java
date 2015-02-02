package com.netcosports.rectangleview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by trung on 30/01/15.
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    private ArrayList<Program> mData;
    private Context mContext;

    public SimpleAdapter(Context context, ArrayList<Program> data) {
        mData = data;
        mContext = context;
    }

    public ArrayList<Program> getData() {
        return mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final ViewHolder viewHolder = new ViewHolder(new TextView(mContext));
        viewHolder.mTextView.setMinimumHeight(128);
//        h.mTextView.setPadding(20, 0, 20, 0);
        viewHolder.mTextView.setFocusable(true);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.leftMargin = 10;
//        lp.rightMargin = 5;
//        lp.topMargin = 20;
//        lp.bottomMargin = 15;
        viewHolder.mTextView.setLayoutParams(lp);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.mTextView.setText("Position : " + position + "\nStart : " + mData.get(position).start_time + "\nDuration : " + mData.get(position).duration + "\n" + mData.get(position).name);
        viewHolder.mTextView.setTag(mData.get(position));
        viewHolder.mTextView.setWidth(mData.get(position).duration);
//        viewHolder.mTextView.setMinWidth((mData.get(position).duration));
        viewHolder.mTextView.setBackgroundColor(generateDummyColor());

        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mData.get(position).name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData != null && mData.size() > 0) {
            return mData.size();
        }
        return 0;
    }


    public int generateDummyColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }
}
