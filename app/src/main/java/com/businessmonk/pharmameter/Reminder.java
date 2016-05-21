package com.businessmonk.pharmameter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Reminder extends AppCompatActivity {
    ListView listView ;
    public static ArrayList<String> med_name;
    public static ArrayList<String> interval;
    public static rem_customAdapter adapter;
    FloatingActionButton fab;

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        listView = (ListView)findViewById(R.id.reminder_list);
        med_name = new ArrayList<>();
        interval = new ArrayList<>();
        med_name.add("Banadol");
        interval.add("3 times per day");
        adapter = new rem_customAdapter(Reminder.this,R.id.reminder_list,med_name,interval);
        listView.setAdapter(adapter);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Reminder.this,Add_medicine.class));
            }
        });
    }
    private class rem_customAdapter extends ArrayAdapter{
        ArrayList<String> mName;
        ArrayList<String> mInterval;
        Activity mContext;
        public rem_customAdapter(Activity context, int resource, ArrayList<String> name, ArrayList<String> interval) {
            super(context, resource);
            this.mName = name;
            this.mInterval = interval;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mName.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=mContext.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.reminder_item, null,true);
            final TextView medName = (TextView)rowView.findViewById(R.id.reminder_med_name);
            TextView Interval = (TextView)rowView.findViewById(R.id.reminder_interval);
            medName.setText(mName.get(position));
            Interval.setText(mInterval.get(position));
            ImageView remove_img = (ImageView)rowView.findViewById(R.id.remove);
            remove_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mName.remove(position);
                    mInterval.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            return rowView;
        }
    }
}
