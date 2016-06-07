package com.businessmonk.pharmameter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Contact extends AppCompatActivity {
    TinyDB tinyDB ;
    EditText title,body;
    String msgTitle, msgBody ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        body = (EditText)findViewById(R.id.contact_body);
        title = (EditText)findViewById(R.id.contact_title);
        tinyDB= new TinyDB(getApplicationContext());
        TextView idPlace = (TextView) findViewById(R.id.id_place);
        idPlace.setText(tinyDB.getString("uid"));
    }

    public void send(View view) {
        msgTitle = title.getText().toString();
        msgBody = body.getText().toString();
        httpReqPost post = new httpReqPost();
        post.execute();
        }

    public class httpReqPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String response = "";

            URL url = null;

            try {
                url = new URL(tinyDB.getString("host") + "send_message"+"?token="+tinyDB.getString("token"));
                Log.e("hi", url.toString());
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }


            HttpURLConnection conn = null;

            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(10000);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("title", msgTitle)
                        .appendQueryParameter("body", msgBody)
                        .appendQueryParameter("pharmacy_id",getIntent().getStringExtra("pharmacy_id"));

                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = null;
                writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = 0;
                responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = null;
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    while ((line = br.readLine()) != null) {
                        response += line;
                    }

                } else {
                    response = "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            Log.e("sent",strings);
            if(strings.equals("\"Message sent.!\"")){
                Toast.makeText(getApplicationContext(),"Sent Successfully ",Toast.LENGTH_SHORT).show();
                Contact.this.finish();
            }else{
                Toast.makeText(getApplicationContext(),"Check your connection ",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
