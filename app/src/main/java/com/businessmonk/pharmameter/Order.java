package com.businessmonk.pharmameter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Order extends AppCompatActivity {
    TinyDB tinyDB ;
    ArrayList<String> product_id;
    ArrayList<String> product_Ename;
    ArrayList<String> product_price;
    ArrayList<String> product_indication;
    ArrayList<String> product_pic;
    ArrayList<String> ordered_products;
    ArrayList<String> ordered_id;
    ArrayList<String> ordered_indication;
    ArrayList<String> for_user_id;
    Button order_btn ;
    public static ArrayList<Double> ordered_prices;
    ListView products_list,ordered_list ;
    static TextView totPrice;
    boolean flag= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        products_list = (ListView)findViewById(R.id.products_list);
        ordered_list = (ListView)findViewById(R.id.ordered);
        totPrice = (TextView) findViewById(R.id.tot_price);
        order_btn = (Button)findViewById(R.id.order_btn);
        totPrice.setText("0");
        tinyDB = new TinyDB(getApplicationContext());
        TextView idPlace = (TextView) findViewById(R.id.id_place);
        idPlace.setText(tinyDB.getString("uid"));
        product_id = new ArrayList<>();
        product_Ename = new ArrayList<>();
        product_price = new ArrayList<>();
        product_indication= new ArrayList<>();
        ordered_products= new ArrayList<>();
        ordered_prices= new ArrayList<>();
        ordered_id= new ArrayList<>();
        ordered_indication= new ArrayList<>();
        product_pic = new ArrayList<>();
        for_user_id = new ArrayList<>();
        httpReqGet getpro = new httpReqGet();
        getpro.execute();
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new orderPost().execute();
            }
        });
    }
    ///////////// GET PRoducts ///////////
    public class httpReqGet extends AsyncTask<String, Void, String> {
        ProgressDialog progDailog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(Order.this);
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
                url = new URL(tinyDB.getString("host") + "products" + "?token=" + tinyDB.getString("token"));
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
            progDailog.dismiss();
            Log.e("hiiii", s);
            try {
                parsing(s);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }
    ProductAdapter adapter;
    public static orderedAdapter adapter2;
    ListView pharmacies_listView;
    Context context = this;
    int pos ;
    public void parsing(String res) throws JSONException {
        Log.e("jsonString", res);
        Log.e("finished","parse start");

        JSONArray jsonArray = new JSONArray(res);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Log.e("jsoooon",jsonObject.getString("id"));
            Log.e("jsoooon",jsonObject.getString("english_name"));
            product_id.add(jsonObject.getString("id"));
            product_Ename.add(jsonObject.getString("english_name"));
            product_price.add(jsonObject.getString("price"));
            product_indication.add(jsonObject.getString("indication"));
            product_pic.add(jsonObject.getString("picture"));
        }
        Log.e("finished","parse finish");
        adapter = new ProductAdapter(Order.this,product_id,product_Ename,product_price,product_pic,product_indication);
        products_list.setAdapter(adapter);

        products_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.order_custom_dialog);
                dialog.setTitle("For Whom...");
                pos = i;

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                Button ind_btn = (Button) dialog.findViewById(R.id.indecation);
                final CheckBox notForMe = (CheckBox) dialog.findViewById(R.id.forOthers);
                final EditText otherId = (EditText) dialog.findViewById(R.id.otherId);
                notForMe.setChecked(false);
                notForMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(notForMe.isChecked()==true){
                            flag = true;
                            otherId.setVisibility(View.VISIBLE);
                        }else{
                            flag = false;
                            otherId.setVisibility(View.GONE);

                        }
                    }
                });
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(flag){
                            for_user_id.add(otherId.getText().toString());
                        }else{
                            for_user_id.add(tinyDB.getString("uid"));
                        }
                        Log.e("put into id","ooo");
                        ordered_id.add(product_id.get(pos));
                        ordered_products.add(product_Ename.get(pos));
                        ordered_prices.add(Double.valueOf(product_price.get(pos)));
                        totPrice.setText(String.valueOf(calcPric(ordered_prices))+" L.E");
                        adapter2.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                ind_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.ind_custom_dialog);
                        dialog.setTitle("Indication...");
                        Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                        TextView ind = (TextView)dialog.findViewById(R.id.indecation_text);
                        Log.e("ind",product_indication.get(pos));
                        ind.setText(product_indication.get(pos));
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                dialog.show();
            }
        });
        adapter2 = new orderedAdapter(this,ordered_products);
        ordered_list.setAdapter(adapter2);
        ordered_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(ordered_list.getChildAt(position - ordered_list.getFirstVisiblePosition()))).open(true);
            }
        });


    }
    public static double calcPric(ArrayList<Double> x){
        double sum=0;
        for(int i = 0;i<x.size();i++){
            sum +=x.get(i);
        }
        return sum;
    }


    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////
    ////////////////// POST ORDER ////////////////////////

    public class orderPost extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... strings) {
            String response = "";

            URL url = null;

            try {
                url = new URL(tinyDB.getString("host") + "");
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
                JSONObject json = new JSONObject();
                json.put("pharmacy_id", getIntent().getStringExtra("pharmacy_id"));
                json.put("user_create_id",tinyDB.getString("uid"));
                JSONArray ar = new JSONArray();
                for(int i = 0 ; i < for_user_id.size();i++) {
                    JSONObject product = new JSONObject();
                    product.put("for_user_id",for_user_id.get(i));
                    product.put("product_id", ordered_products.get(i));
                    ar.put(product);
                };
                json.put("products",ar);
                String g = json.toString();
                Log.e("hhhh",g);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", "")
                        .appendQueryParameter("password", "");

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
            Log.e("resp",strings);
            if(strings!=""){
                try {
                    parsing(strings);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Intent intent = new Intent(Login.this,Home.class);
//                startActivity(intent);

            }
            else{
                Toast.makeText(getApplicationContext(),"No Connection",Toast.LENGTH_LONG).show();
            }
        }

    }
}
