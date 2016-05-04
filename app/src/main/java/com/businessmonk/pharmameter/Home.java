package com.businessmonk.pharmameter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ShareActionProvider;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Home extends Activity {
    ArrayList<Bitmap> ImageBitmap = new ArrayList<Bitmap>();
    ArrayList<String> pharamcyName = new ArrayList<>();
    ArrayList<String> firstPrice = new ArrayList<>();
    ArrayList<String> secondPrice = new ArrayList<>();
    ArrayList<String> medNames = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();

    TinyDB tinyDB;
    ImageButton profile_btn, emergencies_btn, nearby_btn ,invite_btn,order_btn,history_btn, reminder_btn,tip_btn,contact_btn;
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_custom_actionbar);
        TabHost tabHost = (TabHost) findViewById(R.id.mytabhost);
        tabHost.setup();
        tinyDB = new TinyDB(getApplicationContext());
        TextView idPlace = (TextView) findViewById(R.id.id_place);
        idPlace.setText(tinyDB.getString("uid"));
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Promotion");
        tab1.setIndicator("Promotion");
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        //tab 2 etc...
        TabHost.TabSpec tab2 = tabHost.newTabSpec("News");
        tab2.setIndicator("News");
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);
        ImageButton menu_btn = (ImageButton) findViewById(R.id.menu_button);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.this.openOptionsMenu();
            }
        });
        profile_btn = (ImageButton)findViewById(R.id.profile_button);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Profile.class);
                startActivity(intent);
            }
        });
        emergencies_btn =(ImageButton)findViewById(R.id.emergency_button);
        emergencies_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Emergencies.class);
                startActivity(intent);
            }
        });
        nearby_btn =(ImageButton)findViewById(R.id.nearby_button);
        nearby_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Nearby.class);
                startActivity(intent);
            }
        });
        order_btn =(ImageButton)findViewById(R.id.order_button);
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Order.class);
                startActivity(intent);
            }
        });
        reminder_btn =(ImageButton)findViewById(R.id.reminder_button);
        reminder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Reminder.class);
                startActivity(intent);
            }
        });
        tip_btn =(ImageButton)findViewById(R.id.tip_button);
        tip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Tip.class);
                startActivity(intent);
            }
        });
        contact_btn =(ImageButton)findViewById(R.id.contact_button);
        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Contact.class);
                startActivity(intent);
            }
        });
        history_btn =(ImageButton)findViewById(R.id.history_button);
        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,History.class);
                startActivity(intent);
            }
        });
        invite_btn =(ImageButton)findViewById(R.id.invite_button);
        invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "hi, I'm using Pharma Meter. It's amazing app you can also have it from here");
                startActivity(Intent.createChooser(sharingIntent,  "Where to invite"));;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public class ListData {

        //getBitmap

        public void setImage(Bitmap img)
        {
            ImageBitmap.add(img);
        }

        public Bitmap getImage(int postion)
        {
            return ImageBitmap.get(postion);
        }


//pharamcy

        public void setPharamcy(String n)
        {
            pharamcyName.add(n);
        }

        public String getPharamcy(int postion)
        {
            return pharamcyName.get(postion);
        }


// medicine name

        public void setMedicine(String n)
        {
            medNames.add(n);
        }

        public String getMedicine(int postion)
        {
            return medNames.get(postion);
        }


//first price

        public void setPFirst(String n)
        {
            firstPrice.add(n);
        }

        public String getFirst(int postion)
        {
            return firstPrice.get(postion);
        }


        //second price

        public void setsecond(String n)
        {

            secondPrice.add(n);
        }

        public String getsecond(int postion)
        {
            return secondPrice.get(postion);
        }



        public void setDate(String n)
        {
            date.add(n);
        }

        public String getDate(int postion)
        {
            return date.get(postion);
        }
    }


 public class customAdapter extends ArrayAdapter

 {
      Activity mContext;
      int res;
      ArrayList<ListData> mData;

     public customAdapter(Activity context, int resource , ArrayList<ListData> data) {
         super(context, resource);
         mContext = context;
         res = resource;
         mData = data;
     }


     public View getView(int position,View view,ViewGroup parent) {

         LayoutInflater inflater=mContext.getLayoutInflater();
         View rowView=inflater.inflate(R.layout.listview_item, null,true);

         TextView med_name = (TextView) rowView.findViewById(R.id.med_name);
         ImageView imageView = (ImageView) rowView.findViewById(R.id.image_icon);
         TextView pharamcr_name = (TextView) rowView.findViewById(R.id.med_phamacy);
         TextView date = (TextView) rowView.findViewById(R.id.expired);


         ListData listData = mData.get(position);

         med_name.setText(listData.getMedicine(position));
         imageView.setImageBitmap(listData.getImage(position));
         pharamcr_name.setText(listData.getPharamcy(position));
         date.setText(listData.getDate(position));

         return rowView;

     };

 }


    public class hgetPromation extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            URL url = null;
            try {
                url = new URL(tinyDB.getString("host")+"promotions/"+"?token="+tinyDB.getString("token"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20000);
                conn.addRequestProperty("Accept", "application/json");

            }catch (Exception e){
                e.printStackTrace();
            }
            int responseCode =0;
            try {
                responseCode = conn.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {


                String x = conn.getHeaderField("Authorization");
                String[] xx = x.split(" ");

                tinyDB.putString("token",xx[1]);
                String line;
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                response = "";

            }


            Log.e("eee" , response);
            return response;




        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }

    }

}
