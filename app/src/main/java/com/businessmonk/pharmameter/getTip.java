package com.businessmonk.pharmameter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ahmed on 11/05/16.
 */
public class getTip {
        Context c ;
    TinyDB tinyDB;
    public getTips get ;
    public static String head;
    public static String body;
    public static String image;

    public getTip(Context c) {
        this.c = c;
        tinyDB= new TinyDB(c);
        get = new getTips();
    }

    public class getTips extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            URL url = null;
            try {
                url = new URL(tinyDB.getString("host") + "tips");
                Log.e("hi", "hiiii");
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

            Log.e("eee", response);


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                getDataFromJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void getDataFromJson(String s) throws JSONException {


        JSONArray jsonArray = new JSONArray(s);
        String img = null;
        Log.e("hi", s);
        String[] result = null;
        Log.e("json", jsonArray.length() + "");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
         head = jsonObject.getString("head");
         body = jsonObject.getString("body");
         image = jsonObject.getString("image");
    }

}
