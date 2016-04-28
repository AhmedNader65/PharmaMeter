package com.businessmonk.pharmameter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Signup extends Activity {

    TinyDB tinyDB;
    public String response = "";
    public URL url = null;
    public String tok;
    EditText userNameView;
    EditText fullNameView;
    String UserName;
    String fullName;
    EditText PasswordView;
    String Password;
    EditText PasswordConfirmView;
    EditText BirthDateView;
    java.sql.Date birth;
    CheckBox isPregnantView;
    String isPregnant;
    EditText PregnantDateView;
    java.sql.Date PregnantDate;
    CheckBox haveChildView;
    String haveChild;
    EditText ChildrenNumView;
    int ChildrenNum;
    Calendar myCalendar;
    EditText Selected;
    String gender;
    private RadioButton maleRadio;
    private RadioButton femaleRadio;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        tinyDB = new TinyDB(getApplicationContext());
        myCalendar = Calendar.getInstance();
        BirthDateView = (EditText) findViewById(R.id.birthDate_edit);
        PregnantDateView = (EditText) findViewById(R.id.pregnant_Date);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(Selected);
            }
        };
        PregnantDateView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Signup.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                Selected = PregnantDateView;
            }
        });
        BirthDateView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Signup.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                Selected = BirthDateView;

            }
        });
    }

    private void updateLabel(EditText Select) {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Select.setText(sdf.format(myCalendar.getTime()));
    }

    public void Continue(View view) throws ParseException {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        userNameView = (EditText) findViewById(R.id.username_edit);
        fullNameView = (EditText) findViewById(R.id.fullName_edit);
        UserName = userNameView.getText().toString();
        fullName = fullNameView.getText().toString();
        PasswordView = (EditText) findViewById(R.id.password_edit);

        PasswordConfirmView = (EditText) findViewById(R.id.passwordConfirm_edit);
        if (PasswordConfirmView.getText().toString().equals(PasswordView.getText().toString())) {
            Password = PasswordView.getText().toString();
        } else {
            Toast.makeText(getApplicationContext(), "Password is not match", Toast.LENGTH_SHORT).show();
        }
        birth = java.sql.Date.valueOf(BirthDateView.getText().toString());
        isPregnantView = (CheckBox) findViewById(R.id.pregnant);
        if (isPregnantView.isChecked()) {
            isPregnant = "yes";
        } else {
            isPregnant = "no";
        }
        PregnantDate = java.sql.Date.valueOf(PregnantDateView.getText().toString());
        haveChildView = (CheckBox) findViewById(R.id.don);
        if (haveChildView.isChecked()) {
            haveChild = "no";
        } else {
            haveChild = "yes";
        }
        ChildrenNumView = (EditText) findViewById(R.id.children_num);
        ChildrenNum = Integer.valueOf(ChildrenNumView.getText().toString());

        maleRadio = (RadioButton) findViewById(R.id.male_radio);
        femaleRadio = (RadioButton) findViewById(R.id.female_radio);
        if (maleRadio.isChecked()) {
            gender = "male";
        }
        if (femaleRadio.isChecked()) {
            gender = "female";
        }
        Log.e("inf", UserName);
        Log.e("inf", Password);
        Log.e("inf", birth.toString());
        Log.e("inf", isPregnant.toString());
        Log.e("inf", PregnantDate.toString());
        Log.e("inf", haveChild.toString());
        Log.e("inf", String.valueOf(ChildrenNum));
        httpReqPost req = new httpReqPost();
        req.execute("signup");
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
                        .appendQueryParameter("username", UserName)
                        .appendQueryParameter("password", Password)
                        .appendQueryParameter("full_name", fullName)
                        .appendQueryParameter("birthdate", birth.toString())
                        .appendQueryParameter("email", "aaaa@gmail.com")
                        .appendQueryParameter("mobile_number", "010342423")
                        .appendQueryParameter("phone_number", "057321412")
                        .appendQueryParameter("gender", gender)
                        .appendQueryParameter("facebook_id", "432423")
                        .appendQueryParameter("address", "Mansoura")
                        .appendQueryParameter("area_id", "1")
                        .appendQueryParameter("lat", "123,4567890")
                        .appendQueryParameter("lng", "123,4567890")
                        .appendQueryParameter("is_pregnant", isPregnant.toString())
                        .appendQueryParameter("pregnancy_date", PregnantDate.toString())
                        .appendQueryParameter("currently_lactating", "no");

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
            if (strings != "") {
                try {
                    tinyDB.putString(parsing(strings), "token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Signup.this, Home.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "wrong", Toast.LENGTH_LONG).show();
            }
            try {
                Log.e("hi", parsing(strings));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
