package com.netcosports.rectangleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mrgiua on 08/02/2015.
 */
public class HorizontalRecyclerViewAdapter extends android.support.v7.widget.RecyclerView.Adapter<HorizontalRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Integer> mData;
    private LayoutInflater mInflater;

    public HorizontalRecyclerViewAdapter(Context context, ArrayList<Integer> list) {
        mContext = context;
        mData = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_hlist, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(String.valueOf(position));
        holder.textView.setWidth(100);
        holder.textView.setHeight(30);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

        public TextView textView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            textView = (TextView) itemLayoutView.findViewById(R.id.text);
        }
    }
}
