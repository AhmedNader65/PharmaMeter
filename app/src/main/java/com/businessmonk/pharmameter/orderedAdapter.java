package com.businessmonk.pharmameter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;

/**
 * Created by ahmed on 09/05/16.
 */
public class orderedAdapter extends BaseSwipeAdapter {
    ArrayList<String> ordered;
    private Context mContext;
     SwipeLayout swipeLayout;
    public orderedAdapter(Context mContext,ArrayList<String> ordered) {
        this.ordered = ordered;
        this.mContext = mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.ordered_item, null);
         swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));

        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordered.remove(position);
                Order.ordered_prices.remove(position);
                Order.totPrice.setText(String.valueOf(Order.calcPric(Order.ordered_prices))+" L.E");

                Order.adapter2.notifyDataSetChanged();
            }
        });
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.e("hi","changed");
    }

    @Override
    public void fillValues(int position, View convertView) {
        closeAllItems();
        if(ordered.size()>0) {

            TextView t = (TextView) convertView.findViewById(R.id.position);
            t.setText(ordered.get(position));
        }
    }

    @Override
    public int getCount() {
        return ordered.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
