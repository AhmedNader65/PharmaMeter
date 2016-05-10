package com.businessmonk.pharmameter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ahmed on 27/04/16.
 */
public class httpReqGet extends AsyncTask<String,Void,String> {

//String className;
@Override
protected String doInBackground(String... strings) {
    String response = "";
    //className = strings[1];
    URL url = null;
    String tok = strings[0];
        try {
        url = new URL("http://192.168.1.10:8080/api/test?token="+tok);
        Log.e("respon",url.toString());
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
        String x = conn.getHeaderField("Authorization");
        String[] xx= x.split(" ");
        Log.e("respone2 ",xx[1]);
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

    }

}
