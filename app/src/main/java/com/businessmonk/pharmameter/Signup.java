package com.businessmonk.pharmameter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Signup extends Activity {

    TinyDB tinyDB;
    public String response = "";
    public URL url = null;
    public String tok;
    EditText userNameView, firstNameView,lastNameView,passwordView,passwordConfirmView,emailView,phoneView;
    String username;
    String firstName;
    String lastName;
    String password;
    String email;
    String phone;
    Button continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        tinyDB = new TinyDB(getApplicationContext());
        userNameView = (EditText) findViewById(R.id.username_edit);
        firstNameView = (EditText) findViewById(R.id.first_name_edit);
        lastNameView = (EditText) findViewById(R.id.last_name_edit);
        passwordView = (EditText) findViewById(R.id.password_edit);
        passwordConfirmView = (EditText) findViewById(R.id.passwordConfirm_edit);
        emailView = (EditText)findViewById(R.id.Email);
        phoneView = (EditText)findViewById(R.id.PhoneNumber);
        continue_btn = (Button)findViewById(R.id.con_btn);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = userNameView.getText().toString();
                firstName = firstNameView.getText().toString();
                lastName = lastNameView.getText().toString();
                if (passwordConfirmView.getText().toString().equals(passwordView.getText().toString())) {
                    password = passwordView.getText().toString();
                } else {
                    Toast.makeText(getApplicationContext(), "password is not match", Toast.LENGTH_SHORT).show();
                }
                email = emailView.getText().toString();
                phone = phoneView.getText().toString();
                Log.e("hi",username);
                Log.e("hi",firstName);
                Log.e("hi",lastName);
                Log.e("hi",password);
                Log.e("hi",email);
                Log.e("hi",phone);
                httpReqPost req = new httpReqPost();
                req.execute("signup");

            }
        });
    }
    public String parsing(String res) throws JSONException {

        JSONObject jsonObject = new JSONObject(res);
        return jsonObject.getString("msg");


    }

    public class httpReqPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String response = "";
            String what = strings[0];

            URL url = null;

            try {
                url = new URL(tinyDB.getString("host") + what);
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
                        .appendQueryParameter("username", username)
                        .appendQueryParameter("password", password)
                        .appendQueryParameter("first_name", firstName)
                        .appendQueryParameter("last_name", lastName)
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("mobile_number", phone);
                Log.e("first_name_test",firstName);
                Log.e("first_name_test",lastName);

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
            if (strings != "") {
                try {
                    tinyDB.putString(parsing(strings), "token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Signup.this,Login.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "wrong", Toast.LENGTH_LONG).show();
            }

        }

    }
}
