package com.businessmonk.pharmameter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Activity {
    String userName;
    String password;
    Button Login;
    TinyDB tinyDB;
    EditText userNameEditable;
    EditText passwardEditable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        tinyDB = new TinyDB(getApplicationContext());
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
//
        userNameEditable = (EditText) findViewById(R.id.username_login_screen);
        passwardEditable = (EditText) findViewById(R.id.password_login_screen);
        Login = (Button) findViewById(R.id.loginButton);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = passwardEditable.getText().toString();
                userName = userNameEditable.getText().toString();

                Log.e("respon", userName);
                Log.e("respon2", password);
                httpReqPost req = new httpReqPost();
                req.execute("login");
            }
        });

    }


    public void signup(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }



    public String parsing(String res) throws JSONException {

        JSONObject jsonObject = new JSONObject(res);
        return jsonObject.getString("token");
    }

    public class httpReqPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String response = "";
            String what = strings[0];

            URL url = null;

            try {
                url = new URL("http://192.168.1.5:8080/api/" + what);
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
                        .appendQueryParameter("username", userName)
                        .appendQueryParameter("password", password);

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
            if(strings!=""){
            try {
                tinyDB.putString(parsing(strings),"token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Login.this,Home.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"Wrong username or password",Toast.LENGTH_LONG).show();
            }
            try {
                Log.e("hi",parsing(strings));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
