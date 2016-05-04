package com.businessmonk.pharmameter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class Emergencies extends AppCompatActivity {
    TinyDB tinyDB ;
    ListView emergencies_listview ;
    String hospital_name,hospital_phone,hospital_address,hospital_startTime,hospital_endTime;
    double lat, lng;
    ArrayList<String> hospitalsName, hospitalsId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergencies_custom_actionbar);
        dialog = false;
        tinyDB = new TinyDB(getApplicationContext());
        emergencies_listview = (ListView)findViewById(R.id.hospitals_list_view);
        TextView idPlace = (TextView) findViewById(R.id.id_place);
        idPlace.setText(tinyDB.getString("uid"));
        httpReqGet getReq = new httpReqGet();
        getReq.execute();
    }
    public class httpReqGet extends AsyncTask<String,Void,String> {

        //String className;
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            //className = strings[1];
            URL url = null;
            Log.e("responso2",tinyDB.getString("host")+"hospitals/"+"?token="+tinyDB.getString("token"));
            try {
                if(!dialog) {
                    url = new URL(tinyDB.getString("host") + "hospitals/" + "?token=" + tinyDB.getString("token"));
                }else{
                    url = new URL(tinyDB.getString("host") + "emergencyDetails/" +tinyDB.getString("hos_id")+ "?token=" + tinyDB.getString("token"));
                }
                Log.e("responso",url.toString());
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
                String[] xx = x.split(" ");

                tinyDB.putString("token",xx[1]);
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


                String x = conn.getHeaderField("Authorization");
                String[] xx = x.split(" ");

                tinyDB.putString("token",xx[1]);
                response = "";

            }
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("hiiii",s);
            try {
                parsing(s);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }

    boolean dialog = false;
    public void parsing(String res) throws JSONException {
        Log.e("jsonString",res);
        if(!dialog){
        hospitalsName = new ArrayList<>();
            hospitalsId = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(res);
        for(int i = 0 ; i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
           String hospitalId = jsonObject.getString("id");
            String hospitalName = jsonObject.getString("hospital_name");
            hospitalsName.add(hospitalName);
              hospitalsId.add(hospitalId);
        }
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,hospitalsName);
        emergencies_listview.setAdapter(adapter);
        emergencies_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog = true;
                tinyDB.putString("hos_id",hospitalsId.get(i));
                httpReqGet getReq = new httpReqGet();
                getReq.execute();
            }
        });

    }else{
            Log.e("loooooong",res);
            JSONObject jsonObject = new JSONObject(res);
            hospital_name= jsonObject.getString("hospital_name");
            hospital_address = jsonObject.getString("address");
            hospital_phone = jsonObject.getString("phone_number");
            hospital_startTime = jsonObject.getString("start_time").substring(11,16);
            hospital_endTime = jsonObject.getString("end_time").substring(11,16);
             lat  = jsonObject.getDouble("lat");
             lng  = jsonObject.getDouble("lng");
            Log.e("laaaaaaaat", lat + "");
            Log.e("loooooong",lng+"");

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Emergencies.this);
            alertDialog.setTitle(hospital_name);
            alertDialog.setMessage("Address : "+hospital_address+"\n"+
                    "Phone : "+hospital_phone+"\n"+
                    "Start time : "+hospital_startTime+"\n"+
                    "End time : "+hospital_endTime);
            alertDialog.setPositiveButton("LOCATION", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent  = new Intent(Emergencies.this,MapsActivity.class);
                    intent.putExtra("lat",lat);
                    intent.putExtra("lng",lng);
                    intent.putExtra("name",hospital_name);
                    intent.putExtra("address",hospital_address);
                    intent.putExtra("phone",hospital_phone);
                    intent.putExtra("startTime",hospital_startTime);
                    intent.putExtra("endTime",hospital_endTime);
                    startActivity(intent);
                }
            });
                    alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }

    }
}
