package com.businessmonk.pharmameter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class Nearby extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TinyDB tinyDB;
    //    String pharamcyName,branchName,branchAddress,branchNum,branchEmail,branchLogo,branch_24;
//    double lat,lng;
    ArrayList<String> branches = new ArrayList<>();
    ArrayList<String> branchesAddress = new ArrayList<>();
    ArrayList<String> branchesNum = new ArrayList<>();
    ArrayList<String> branchesEmail = new ArrayList<>();
    ArrayList<String> branchLogo = new ArrayList<>();
    ArrayList<String> branch_24 = new ArrayList<>();
    ArrayList<String> branches_id = new ArrayList<>();
    ArrayList<Double> branchesLat = new ArrayList<>();
    ArrayList<Double> branchesLng = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tinyDB = new TinyDB(getApplicationContext());

        httpReqGet getNearBy = new httpReqGet();
        getNearBy.execute();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

    }

    ///////////// GET BRANCHES ///////////
    public class httpReqGet extends AsyncTask<String, Void, String> {

        //String className;
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            //className = strings[1];
            URL url = null;
            Log.e("responso2", tinyDB.getString("host") + "nearby" + "?token=" + tinyDB.getString("token"));
            try {
                url = new URL(tinyDB.getString("host") + "nearby" + "?token=" + tinyDB.getString("token"));

                Log.e("responso", url.toString());
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


                String x = conn.getHeaderField("Authorization");
                String[] xx = x.split(" ");

                tinyDB.putString("token", xx[1]);
                response = "";

            }
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("hiiii", s);
            try {
                parsing(s);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }

    public void parsing(String res) throws JSONException {
        Log.e("jsonString", res);

        JSONArray jsonArray = new JSONArray(res);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            branches_id.add(jsonObject.getString("id"));
            branchesEmail.add(jsonObject.getString("email"));
            branchesAddress.add(jsonObject.getString("address"));
            branchesNum.add(jsonObject.getString("phone_number"));
            branchesLat.add(jsonObject.getDouble("lat"));
            branchesLng.add(jsonObject.getDouble("lng"));
            String brName = jsonObject.getString("branch_name");
            JSONObject pharmacy = jsonObject.getJSONObject("pharmacy");
            String pharmacy_name = pharmacy.getString("name");
            branchLogo.add(pharmacy.getString("logo"));
            branch_24.add(pharmacy.getString("is_24_7"));
            branches.add(pharmacy_name + " " + brName);
        }
        for (int i = 0; i < branches.size(); i++) {

            LatLng branch = new LatLng(branchesLat.get(i), branchesLng.get(i));
            mMap.addMarker(new MarkerOptions().position(branch).title(branches.get(i)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(branchesLat.get(0), branchesLng.get(0))));
        }

    }
    Location location;
    LatLng myLocation;


}
