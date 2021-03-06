package com.netcosports.rectangleview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
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
        View v = LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.mTextView.setText("Position : " + position +
                "\nStart : " + mData.get(position).start_time +
                "\nDuration : " + mData.get(position).duration +
                "\n" + mData.get(position).name +
                "\n" + mData.get(position).positionOrderedByStartTime +
                "\n" + mData.get(position).positionOrderedByEndTime);
        viewHolder.mParent.setTag(mData.get(position));
//        viewHolder.mTextView.setWidth(mData.get(position).duration);
        viewHolder.mTextView.setTextColor(mData.get(position).color);

        viewHolder.mParent.setOnClickListener(new View.OnClickListener() {
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


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mParent;
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mParent = view.findViewById(R.id.parent);
            mTextView = (TextView) view.findViewById(R.id.text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }
}
