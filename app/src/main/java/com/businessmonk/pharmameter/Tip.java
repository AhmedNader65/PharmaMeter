package com.businessmonk.pharmameter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
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

public class Tip extends AppCompatActivity {
    TinyDB tinyDB;
    String image, head, body;
    TextView bodyView, headView;
    ImageView tip_img;
    getTip getTipclass;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_tip_navbar);
        Log.e("hiiiiiiii","hiiii");
        sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        tinyDB = new TinyDB(getApplicationContext());
        TextView idPlace = (TextView) findViewById(R.id.id_place);
        idPlace.setText(tinyDB.getString("uid"));
        bodyView = (TextView) findViewById(R.id.body);
        headView = (TextView) findViewById(R.id.head);
        tip_img = (ImageView) findViewById(R.id.tip_img);
        String body =sharedPref.getString("body","b");
        String head =sharedPref.getString("head","b");
        String img =sharedPref.getString("image","b");
        Log.e("hiiiiiiii",body);
        if(body.length()<2){
            Log.e("hggh","Hghg");
            getTipss g = new getTipss();
            g.execute();
        }else{
            Log.e("hggh","higigi");

            bodyView.setText(body);
            headView.setText(head);
            tip_img.setImageBitmap(decode64(img));
        }
    }


    // get image from Hash

    public Bitmap decode64(String hash){
        hash = hash.substring(22);
        Log.e("has",hash);
        byte[] decodedString = Base64.decode(hash, 0);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
    public class getTipss extends AsyncTask<String, Void, String> {

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
        editor.putString("head",head);
        editor.putString("body",body);
        editor.putString("image",image);
        editor.commit();
        bodyView.setText(body);
        headView.setText(head);
        tip_img.setImageBitmap(decode64(image));
    }

}
