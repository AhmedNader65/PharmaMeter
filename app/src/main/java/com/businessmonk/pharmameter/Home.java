package com.businessmonk.pharmameter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class Home extends Activity {
    public ArrayList<String> id;
    public ArrayList<String> Product_id;
   public ArrayList<Bitmap> ImageBitmap;
    public ArrayList<String> pharamcyId;
    public ArrayList<String> date;
    public ArrayList<String> head;
    public ArrayList<String> body;
    public ArrayList<Bitmap> ImageBitmap2;
    public ArrayList<String> pharamcyId2;
    public ArrayList<String> date2;
    public ArrayList<String> head2;
    public ArrayList<String> id2;
    public ArrayList<String> body2;
    customAdapter custom;
    customAdapter custom2;

    ListView news;
    ListView promo;
    TinyDB tinyDB;
    ImageButton profile_btn, emergencies_btn, nearby_btn ,invite_btn,order_btn,history_btn, reminder_btn,tip_btn,contact_btn;
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_custom_actionbar);
        body = new ArrayList<>();
        date = new ArrayList<>();
        date2 = new ArrayList<>();
        head =  new ArrayList<>();
        head2 =  new ArrayList<>();
        pharamcyId= new ArrayList<>();
        Product_id= new ArrayList<>();
        ImageBitmap= new ArrayList<Bitmap>();
        ImageBitmap2= new ArrayList<Bitmap>();
        pharamcyId2 = new ArrayList<String>();
        id2 = new ArrayList<String>();
        id = new ArrayList<String>();
        body2 = new ArrayList<String>();
        TabHost tabHost = (TabHost) findViewById(R.id.mytabhost);
        //custom = new customAdapter(this,R.id.listViewItem , data );

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

        promo = (ListView)findViewById(R.id.tab1);
         news = (ListView)findViewById(R.id.tab2);

        promo.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        news.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        getPromotion p = new getPromotion();
        p.execute();

        getNews n = new getNews();
        n.execute();
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
                Intent intent = new Intent(Home.this,Pharmacies.class);
                intent.putExtra("flag","order");
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
                Intent intent = new Intent(Home.this,Pharmacies.class);
                intent.putExtra("flag","contact");
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
        /////////////////////////////////////////////
        ////////////////////////////////////////////
        ////////////////////////////////////////////
        //        NOTIFICATION
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        //////////////////////////////////////////
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(Home.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Home.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) Home.this.getSystemService(Home.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

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



    public class customAdapter extends ArrayAdapter

    {
        Activity mContext;
        ArrayList<String> mBody;
        ArrayList<Bitmap> mImg;
        int res;
        public customAdapter(Activity context, int resource , ArrayList<String> body,ArrayList<Bitmap> img) {
            super(context, resource);
            mContext = context;
            res = resource;
            mBody = body;
            mImg = img;
        }

        @Override
        public int getCount() {
            return mBody.size();
        }

        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater=mContext.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.listview_item, null,true);

            TextView med_name = (TextView) rowView.findViewById(R.id.med_name);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image_icon);
//         TextView pharamcr_name = (TextView) rowView.findViewById(R.id.med_phamacy);
//         TextView date = (TextView) rowView.findViewById(R.id.expired);

            med_name.setText(mBody.get(position));
            if(mImg.get(position) != null) {
                imageView.setImageBitmap(mImg.get(position));
            }
//         pharamcr_name.setText(listData.getPharamcy(position));
//         date.setText(listData.getDate(position));

            return rowView;

        };

    }

    public class getNews extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            URL url = null;
            try {
                url = new URL(tinyDB.getString("host")+"news");
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



        public void getDataFromJson(String s) throws JSONException {


            JSONArray jsonArray = new JSONArray(s);
            String img = null;
            Log.e("json2",jsonArray.length()+"");
            for(int i =0 ; i<jsonArray.length() ; i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                pharamcyId2.add(jsonObject.getString("pharmacy_id"));
                head2.add(jsonObject.getString("head"));
                id2.add(jsonObject.getString("id"));
                body2.add(jsonObject.getString("body"));
                if(jsonObject.getString("image") != null) {
                    img = jsonObject.getString("image");
                    ImageBitmap2.add(getImageFromHash(img));
                }
            }
            for(int i = 0 ; i<head2.size();i++){
                Log.e("head2",head2.get(i));
            }
            custom2 = new customAdapter(Home.this,R.id.listViewItem , head2,ImageBitmap2 );

            news.setAdapter(custom2);
            Log.e("hiiii","hiiiii");

        }


        // get image from Hash

        public Bitmap getImageFromHash(String Hash)
        {
            byte[] b = Hash.getBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(b , 0 , b.length);
            return bitmap;
        }


        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            try {
                getDataFromJson(aVoid);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class getPromotion extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            URL url = null;
            try {
                url = new URL(tinyDB.getString("host")+"promotions");
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
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            try {
                getDataFromJson(aVoid);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void getDataFromJson(String s) throws JSONException {


        JSONArray jsonArray = new JSONArray(s);
        String img = null;
        Log.e("json2",jsonArray.length()+"");
        for(int i =0 ; i<jsonArray.length() ; i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            pharamcyId.add(jsonObject.getString("pharmacy_id"));
            Product_id.add(jsonObject.getString("product_id"));
            head.add(jsonObject.getString("head"));
            id.add(jsonObject.getString("id"));
            body.add(jsonObject.getString("body"));
            date.add(jsonObject.getString("end_date"));
            if(jsonObject.getString("image") != null) {
                img = jsonObject.getString("image");
                ImageBitmap.add(getImageFromHash(img));
            }
        }

        custom = new customAdapter(Home.this,R.id.listViewItem , head,ImageBitmap );

        promo.setAdapter(custom);
        Log.e("hiiii","hiiiii");

    }
    // get image from Hash

    public Bitmap getImageFromHash(String Hash)
    {
        byte[] b = Hash.getBytes();
        Bitmap bitmap = BitmapFactory.decodeByteArray(b , 0 , b.length);
        return bitmap;
    }

}
