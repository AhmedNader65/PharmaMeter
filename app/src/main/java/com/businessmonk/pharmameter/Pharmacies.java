package com.businessmonk.pharmameter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Pharmacies extends AppCompatActivity {
    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_pharmacies);
        tinyDB = new TinyDB(getApplicationContext());
        pharmacies = new ArrayList();
        pharmacies_id = new ArrayList();
        pharmacies_logo = new ArrayList();
        pharmacies_email = new ArrayList();
        pharmacies_Num = new ArrayList();
        pharmacies_24 = new ArrayList();
        pharmacies_listView = (ListView)findViewById(R.id.pharmacies_list);
        httpReqGet getPharmacies = new httpReqGet();
        getPharmacies.execute();
        pharmacies_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = getIntent();
                String flag =intent.getStringExtra("flag");
                Intent intent2 ;
                Log.e("flag",flag);
                if(flag.contains("order")){
                    intent2 = new Intent(Pharmacies.this,Order.class);
                    intent2.putExtra("pharmacy_id",pharmacies_id.get(i));
                }else{
                    intent2 = new Intent(Pharmacies.this,Contact.class);
                    intent2.putExtra("pharmacy_id",pharmacies_id.get(i));
                }
                startActivity(intent2);
            }
        });
    }
    public ArrayList<String> pharmacies ;
    public ArrayList<String> pharmacies_id ;
    public ArrayList<String> pharmacies_logo ;
    public ArrayList<String> pharmacies_email ;
    public ArrayList<String> pharmacies_Num ;
    public ArrayList<String> pharmacies_24 ;
    ///////////// GET PHARMACIES ///////////
    public class httpReqGet extends AsyncTask<String, Void, String> {
        private ProgressDialog progDailog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(Pharmacies.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        //String className;
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            //className = strings[1];
            URL url = null;
            try {
                url = new URL(tinyDB.getString("host") + "pharmacies" + "?token=" + tinyDB.getString("token"));
                Log.e("tokkkk",url+"");
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
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter wr = new OutputStreamWriter(os);
                wr.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int responseCode = 0;
            try {
                responseCode = conn.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = null;
                String x = conn.getHeaderField("Authorization");
                String[] xx = x.split(" ");

                tinyDB.putString("token", xx[1]);
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
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progDailog.dismiss();
            Log.e("hiiii", s);
            try {
                if(s!=""){
                parsing(s);}
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }
    ArrayAdapter adapter;
    ListView pharmacies_listView;
    public void parsing(String res) throws JSONException {
        Log.e("jsonString", res);

        JSONArray jsonArray = new JSONArray(res);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            pharmacies_id.add(jsonObject.getString("id"));
            pharmacies.add(jsonObject.getString("name"));
            pharmacies_email.add(jsonObject.getString("email"));
            pharmacies_Num.add(jsonObject.getString("phone_number"));
            pharmacies_logo.add(jsonObject.getString("logo"));
            pharmacies_24.add(jsonObject.getString("is_24_7"));
        }
        adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,pharmacies);
        pharmacies_listView.setAdapter(adapter);

    }
}
