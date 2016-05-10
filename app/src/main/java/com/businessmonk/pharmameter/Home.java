package com.businessmonk.pharmameter;

import android.app.Activity;
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

public class Home extends Activity {
   public ArrayList<Bitmap> ImageBitmap;
    public ArrayList<String> pharamcyName;
    public ArrayList<String> firstPrice ;
    public ArrayList<String> secondPrice;
    public ArrayList<String> medNames;
    public ArrayList<String> date;
    public ArrayList<ListData> data;
    public ArrayList<String> head;
    customAdapter custom;
   ;


    TinyDB tinyDB;
    ImageButton profile_btn, emergencies_btn, nearby_btn ,invite_btn,order_btn,history_btn, reminder_btn,tip_btn,contact_btn;
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_custom_actionbar);
        data = new ArrayList<>();
        date = new ArrayList<>();
        head =  new ArrayList<>();
        medNames = new ArrayList<>();
        secondPrice = new ArrayList<>();
        firstPrice = new ArrayList<>();
        pharamcyName = new ArrayList<>();
        ImageBitmap= new ArrayList<Bitmap>();
        TabHost tabHost = (TabHost) findViewById(R.id.mytabhost);
        custom = new customAdapter(this,R.id.listViewItem , data );
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

        ListView promo = (ListView)findViewById(R.id.tab1);

                promo.setAdapter(custom);
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
        promo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent in = new Intent();
                ListData ll = new ListData();
                in.putExtra("med_pharamcy" , ll.getPharamcy(i));
                in.putExtra("med_name", ll.getMedicine(i));
                in.putExtra("expierd" , ll.getDate(i));

                startActivity(in);

            }
        });



//        getPromation p = new getPromation();
//        p.execute();
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


        public void setHead(String h)
        {
            head.add(h);
        }

        public String getHead(int postion)
        {
            return head.get(postion);
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
//         TextView pharamcr_name = (TextView) rowView.findViewById(R.id.med_phamacy);
//         TextView date = (TextView) rowView.findViewById(R.id.expired);


         ListData listData = mData.get(position);

         med_name.setText(listData.getHead(position));
         if(listData.getImage(position) != null) {
             imageView.setImageBitmap(listData.getImage(position));
         }
//         pharamcr_name.setText(listData.getPharamcy(position));
//         date.setText(listData.getDate(position));

         return rowView;

     };

 }


    public class getPromation extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... strings) {
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
            try {
                getDataFromJson(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }



        public void getDataFromJson(String s) throws JSONException {


            JSONArray jsonArray = new JSONArray(s);
              String img = null;
              String[] result = null;
            Log.e("json",jsonArray.length()+"");
             for(int i =0 ; i<jsonArray.length() ; i++)
             {
                 JSONObject jsonObject = jsonArray.getJSONObject(i);
                 ListData listData = new ListData();
                 listData.setMedicine(jsonObject.getString("id"));
                 listData.setPharamcy(jsonObject.getString("pharmacy_id"));
                 listData.setDate(jsonObject.getString("end_date"));
                 listData.setHead(jsonObject.getString("head"));
                 if(jsonObject.getString("image") != null) {
                     img = jsonObject.getString("image");
                     listData.setImage(getImageFromHash(img));
                 }

                 data.add(listData);
             }

        }


        // get image from Hash

        public Bitmap getImageFromHash(String Hash)
        {
            byte[] b = Hash.getBytes();
             Bitmap bitmap = BitmapFactory.decodeByteArray(b , 0 , b.length);

            return bitmap;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for(int i = 0 ; i< data.size() ; i++)
            {
                ListData item = new ListData();
                custom.add(data.get(i));
                Log.e("bitmapPOST", "bitmap8");
            }
        }
    }

}
