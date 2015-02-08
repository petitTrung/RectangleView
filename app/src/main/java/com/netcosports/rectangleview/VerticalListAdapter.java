package com.netcosports.rectangleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by trung on 05/02/15.
 */
public class VerticalListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Integer> mData;
    private LayoutInflater mInflater;
    private int itemHeight;


    public VerticalListAdapter(Context context, ArrayList<Integer> list) {
        mContext = context;
        mData = list;
        mInflater = LayoutInflater.from(context);
        itemHeight = ConvertHelper.toPixels(mContext.getResources().getInteger(R.integer.rectangle_item_height), mContext.getResources().getDisplayMetrics());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_hlist, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(String.valueOf(position));
        textView.setHeight(itemHeight);

        return convertView;
    }
}
