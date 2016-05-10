package com.businessmonk.pharmameter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ahmed on 08/05/16.
 */
public class ProductAdapter extends ArrayAdapter {
    Context mContext;
    ArrayList<String> id, name, price, img,product_indication ;
    public ProductAdapter(Context context,ArrayList<String> id,ArrayList<String> name,ArrayList<String> price,ArrayList<String> img,ArrayList<String> product_indication) {
        super(context, R.layout.custom_products);
        this.id = id;
        this.name= name;
        this.price = price;
        this.img = img;
        this.product_indication = product_indication;
        mContext = context;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflator = ((Activity)mContext).getLayoutInflater();
            convertView = inflator.inflate(R.layout.custom_products, null);
             holder = new ViewHolder();
           holder.product_name_view = (TextView) convertView.findViewById(R.id.product_name);
            holder.product_price_view = (TextView) convertView.findViewById(R.id.product_price);
            holder.product_pic_view = (ImageView) convertView.findViewById(R.id.product_pic);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.product_name_view.setText(name.get(position));
        holder.product_price_view.setText(price.get(position)+" L.E");
        holder.product_pic_view.setImageBitmap(decode64(img.get(position)));
        return convertView;
    }
    static class ViewHolder {
        protected TextView product_name_view;
        protected TextView product_price_view;
        protected ImageView product_pic_view;
    }
    public Bitmap decode64(String hash){
        hash = hash.substring(22);
        Log.e("has",hash);
        byte[] decodedString = Base64.decode(hash, 0);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
