package com.businessmonk.pharmameter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
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
public class AlarmReceiver  extends BroadcastReceiver {

    Context mContext;
    NotificationManager notificationManager;
    PendingIntent pendingIntent;
    Uri alarmSound;
    long when;
    TinyDB tinyDB ;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPref = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        when = System.currentTimeMillis();
        tinyDB = new TinyDB(context);
        this.mContext = context;
        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, Order.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        getTips getTips = new getTips();
        getTips.execute();

    }
    public Bitmap decode64(String hash){
        hash = hash.substring(22);
        byte[] decodedString = Base64.decode(hash, 0);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
    public static String head;
    public static String body;
    public static String image;
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

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                mContext).setLargeIcon(decode64(image))
                .setContentTitle(head)
                .setContentText(body).setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(1, mNotifyBuilder.build());
        editor.putString("head",head);
        editor.putString("body",body);
        editor.putString("image",image);
    }
}
