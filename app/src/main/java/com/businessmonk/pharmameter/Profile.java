package com.businessmonk.pharmameter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Profile extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageView profileconficon , profileEditicon,emailEditicon,phoneEditicon,birthdateicon,pregnant_Dateicon,emailEditiconConfirm,phoneEditiconConfirm,child_edit_icon,child_confirm_icon ;
    LinearLayout v , e ;
    EditText emailEdit,phoneEdit,birthdate,pregnant_DateEdit,howMenyChild ,firstName_edit,lastName_edit,mobile_edit,address_edit,area_edit;
    RadioButton male , female;
    RadioGroup genderGroup;
    CheckBox ispregView ,isLactView,haveChild;
    TextView starFromView,name_view,address_view,mobile_view,area_view;
    TinyDB tinyDB;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
//    boolean isMale,isFemale,isPreg,isLact
    boolean birthNotPer = true;
    com.businessmonk.pharmameter.CircularImageView profile_pic ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /////////////////////////////////////////////
        ////////////// DEFINES /////////////////////
        ////////////////////////////////////////////
        tinyDB = new TinyDB(getApplicationContext());
        profileconficon = (ImageView) findViewById(R.id.profile_edit_confirm);
        profileEditicon = (ImageView) findViewById(R.id.profile_edit);
        pregnant_Dateicon = (ImageView) findViewById(R.id.pregnantdate_edit_icon);
        firstName_edit = (EditText)findViewById(R.id.profile_first_name_edit);
        name_view = (TextView) findViewById(R.id.profile_name_text);
        lastName_edit = (EditText)findViewById(R.id.profile_last_name_edit);
        address_edit = (EditText)findViewById(R.id.profile_address_edit);
        address_view=(TextView)findViewById(R.id.profile_address_text);
        mobile_edit = (EditText)findViewById(R.id.profile_mobile_num_edit);
        mobile_view = (TextView)findViewById(R.id.profile_mobile_num_text);
        area_edit = (EditText)findViewById(R.id.area_edit);
        area_view = (TextView)findViewById(R.id.area_text);
        emailEditicon = (ImageView) findViewById(R.id.email_edit_icon);
        phoneEditicon = (ImageView) findViewById(R.id.phone_edit_icon);
        birthdateicon = (ImageView) findViewById(R.id.birth_edit_icon);
        emailEditiconConfirm = (ImageView) findViewById(R.id.email_confirm_icon);
        phoneEditiconConfirm = (ImageView) findViewById(R.id.phone_confirm_icon);
        emailEdit = (EditText) findViewById(R.id.email_profile_edit);
        phoneEdit = (EditText) findViewById(R.id.phone_profile_edit);
        birthdate = (EditText) findViewById(R.id.birth_date_profile_edit);
        pregnant_DateEdit = (EditText) findViewById(R.id.pregnant_Date);
        v = (LinearLayout) findViewById(R.id.view_sec);
        e = (LinearLayout) findViewById(R.id.edit_sec);
        howMenyChild = (EditText) findViewById(R.id.children_num);
        child_edit_icon = (ImageView)findViewById(R.id.child_edit_icon);
        child_confirm_icon = (ImageView)findViewById(R.id.child_confirm_icon);
        male = (RadioButton)findViewById(R.id.male_radio);
        female = (RadioButton)findViewById(R.id.female_radio);
        isLactView = (CheckBox)findViewById(R.id.lactating);
        starFromView = (TextView)findViewById(R.id.start) ;
        ispregView = (CheckBox)findViewById(R.id.pregnant);
        profile_pic = (com.businessmonk.pharmameter.CircularImageView)findViewById(R.id.profile_pic_circular);
        /////////////////////////////////////////////
        ////////////// Hide Edit /////////////////////
        ////////////////////////////////////////////
        e.setVisibility(View.INVISIBLE);
        emailEdit.setEnabled(false);
        phoneEdit.setEnabled(false);
        birthdate.setEnabled(false);
        birthdate.setEnabled(false);
        howMenyChild.setEnabled(false);
        /////////////////////////////////////////////
        ////////////// Date Picker //////////////////
        ////////////////////////////////////////////
         myCalendar = Calendar.getInstance();

         date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        /////////////////////////////////////////////
        ////////////// Fill Data////////////////////
        ////////////////////////////////////////////
        profile_pic.setImageResource(R.drawable.profile_pic);
        if(tinyDB.getString("image")=="no"){
            profile_pic.setImageResource(R.drawable.profile_pic);
        }else {
            profile_pic.setImageURI(Uri.parse(tinyDB.getString("image")));
        }
        /////////////////////////////////////////////
        ////////////// Click listener///////////////
        ////////////////////////////////////////////

        profileEditicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.setVisibility(View.INVISIBLE);
                e.setVisibility(View.VISIBLE);

            }
        });
        profileconficon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tinyDB.putString("realFirstName",firstName_edit.getText().toString());
                tinyDB.putString("realLastname",lastName_edit.getText().toString());
                tinyDB.putString("realAddress",address_edit.getText().toString());
                tinyDB.putString("realMobile",mobile_edit.getText().toString());
                tinyDB.putString("realArea",area_edit.getText().toString());
                httpReqPosttoProfile httpPost = new httpReqPosttoProfile();
                httpPost.execute();
                e.setVisibility(View.INVISIBLE);
                v.setVisibility(View.VISIBLE);

            }
        });
        emailEditicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEdit.setEnabled(true);
                emailEdit.setFocusable(true);
                emailEditiconConfirm.setVisibility(View.VISIBLE);
                emailEditicon.setVisibility(View.INVISIBLE);
            }
        });
        emailEditiconConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailEdit.getText().toString().contains("@")&&emailEdit.getText().toString().endsWith(".com")){
                emailEdit.setEnabled(false);
                emailEditiconConfirm.setVisibility(View.INVISIBLE);
                emailEditicon.setVisibility(View.VISIBLE);
                    httpReqPost httpPost = new httpReqPost();
                    httpPost.execute("email",emailEdit.getText().toString());
            }else {
                    Toast.makeText(getApplicationContext(),"Wrong Email address",Toast.LENGTH_SHORT).show();
                }
                }
        });
        phoneEditicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    phoneEdit.setEnabled(true);
                    phoneEditiconConfirm.setVisibility(View.VISIBLE);
                    phoneEditicon.setVisibility(View.INVISIBLE);

            }
        });
        phoneEditiconConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneEdit.getText().toString().matches("[0-9]+") && phoneEdit.getText().toString().length() > 7) {
                    phoneEdit.setEnabled(false);

                phoneEditiconConfirm.setVisibility(View.INVISIBLE);
                phoneEditicon.setVisibility(View.VISIBLE);
                    httpReqPost httpPost = new httpReqPost();
                    httpPost.execute("phone_number",phoneEdit.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),"Wrong phone number",Toast.LENGTH_SHORT).show();

                }
            }
        });
        birthdateicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthNotPer = true;
                new DatePickerDialog(Profile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        pregnant_Dateicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthNotPer = false;

                new DatePickerDialog(Profile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        starFromView.setVisibility(View.GONE);
        isLactView.setVisibility(View.GONE);
        ispregView.setVisibility(View.GONE);
        pregnant_DateEdit.setVisibility(View.GONE);
        if(female.isChecked()){

            starFromView.setVisibility(View.VISIBLE);
            isLactView.setVisibility(View.VISIBLE);
            ispregView.setVisibility(View.VISIBLE);
            pregnant_DateEdit.setVisibility(View.VISIBLE);
        }
        genderGroup = (RadioGroup)findViewById(R.id.Gender_group);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(male.isChecked()){
                    if(tinyDB.getString("realGender").equals("male")){

                    }else {
                        httpReqPosttoProfile httpPost = new httpReqPosttoProfile();
                        httpPost.execute();
                        man = true;
                    }

                    isLactView.setVisibility(View.GONE);
                    ispregView.setVisibility(View.GONE);
                    pregnant_DateEdit.setVisibility(View.GONE);
                    starFromView.setVisibility(View.GONE);
                }else{
                    if(tinyDB.getString("realGender").equals("female")){

                    }else {
                        httpReqPost httpPost = new httpReqPost();
                        httpPost.execute("gender", "female");
                    }
                    isLactView.setVisibility(View.VISIBLE);
                    ispregView.setVisibility(View.VISIBLE);
                    pregnant_DateEdit.setVisibility(View.VISIBLE);
                    starFromView.setVisibility(View.VISIBLE);

                }
            }
        });
        pregnant_Dateicon.setVisibility(View.GONE);
        starFromView.setVisibility(View.GONE);
        pregnant_DateEdit.setVisibility(View.GONE);
        if(ispregView.isChecked()){

            starFromView.setVisibility(View.VISIBLE);
        }
        ispregView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(ispregView.isChecked()){
                    if(tinyDB.getString("realIsPregnant").equals("yes")){

                    }else {
                        httpReqPost httpPost = new httpReqPost();
                        httpPost.execute("is_pregnant", "yes");
                    }
                    starFromView.setVisibility(View.VISIBLE);
                    pregnant_Dateicon.setVisibility(View.VISIBLE);
                    pregnant_DateEdit.setVisibility(View.VISIBLE);

                }else {

                    if(tinyDB.getString("realIsPregnant").equals("no")){

                    }else {
                        httpReqPost httpPost = new httpReqPost();
                        httpPost.execute("is_pregnant", "no");
                    }
                    starFromView.setVisibility(View.GONE);
                    pregnant_Dateicon.setVisibility(View.GONE);
                    pregnant_DateEdit.setVisibility(View.GONE);

                }
            }
        });
        haveChild = (CheckBox)findViewById(R.id.don);
        haveChild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(haveChild.isChecked()){
                    howMenyChild.setVisibility(View.GONE);
                }else{
                    howMenyChild.setVisibility(View.VISIBLE);
                }

            }
        });
        child_edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                howMenyChild.setEnabled(true);
                child_confirm_icon.setVisibility(View.VISIBLE);
                child_edit_icon.setVisibility(View.INVISIBLE);
            }
        });
        child_confirm_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                howMenyChild.setEnabled(false);
                child_confirm_icon.setVisibility(View.INVISIBLE);
                child_edit_icon.setVisibility(View.VISIBLE);
            }
        });
        httpReqGet req = new httpReqGet();
        req.execute();

    }

    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    ////////////////////// Update_Date_View /////////////////////////
    ////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(birthNotPer) {

            httpReqPost httpPost = new httpReqPost();
            birthdate.setText(sdf.format(myCalendar.getTime()));
            httpPost.execute("birthdate",birthdate.getText().toString());
        }else{
            pregnant_DateEdit.setText(sdf.format(myCalendar.getTime()));
            httpReqPost httpPost = new httpReqPost();
            httpPost.execute("pregnancy_date",pregnant_DateEdit.getText().toString());
        }
    }

    public void profile_pic(View view) {
        PopupMenu popupMenu = new PopupMenu(Profile.this, view);
        popupMenu.setOnMenuItemClickListener(Profile.this);
        popupMenu.inflate(R.menu.photo_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_remove:
                tinyDB.putString("image","no");
                profile_pic.setImageResource(R.drawable.profile_pic);
                return true;
            case R.id.action_change:
                pickImage();
        }
        return true;
    }
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getApplicationContext(). getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            Uri tempUri = getImageUri(getApplicationContext(), yourSelectedImage);
            tinyDB.putString("image",tempUri.toString());
            profile_pic.setImageURI(tempUri);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////// GET_PROFILE ////////////////////////////
    ////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    public class httpReqGet extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            URL url = null;
            try {
                url = new URL(tinyDB.getString("host")+"profile/"+tinyDB.getString("uid")+"?token="+tinyDB.getString("token"));
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


                String x = conn.getHeaderField("Authorization");
                String[] xx = x.split(" ");

                tinyDB.putString("token",xx[1]);
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
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                parsing(s);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }

    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    ////////////////////// parsing_profile ///////////////////////////
    ////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    public void parsing(String res) throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        tinyDB.putString("realFirstName",jsonObject.getString("first_name"));
        tinyDB.putString("realLastname",jsonObject.getString("last_name"));
        tinyDB.putString("realAddress",jsonObject.getString("address"));
        tinyDB.putString("realBirthDate",jsonObject.getString("birthdate"));
        tinyDB.putString("realEmail",jsonObject.getString("email"));
        tinyDB.putString("realMobile",jsonObject.getString("mobile_number"));
        tinyDB.putString("realPhone",jsonObject.getString("phone_number"));
        tinyDB.putString("realGender",jsonObject.getString("gender"));
        tinyDB.putString("realArea",jsonObject.getString("area_id"));
        tinyDB.putString("realIsPregnant",jsonObject.getString("is_pregnant"));
        tinyDB.putString("realPregnantDate",jsonObject.getString("pregnancy_date"));
        tinyDB.putString("realIsLact",jsonObject.getString("currently_lactating"));

        emailEdit.setText(tinyDB.getString("realEmail"));
        phoneEdit.setText(tinyDB.getString("realPhone"));
        birthdate.setText(tinyDB.getString("realBirthDate"));
        if(tinyDB.getString("realGender").equals("male")){
            male.setChecked(true);
        }else if(tinyDB.getString("realGender").equals("female")){
            female.setChecked(true);
        }if(tinyDB.getString("realIsPregnant").equals("yes")){
            ispregView.setChecked(true);
            pregnant_Dateicon.setVisibility(View.VISIBLE);
        }
        firstName_edit.setText(tinyDB.getString("realFirstName"));
        lastName_edit.setText(tinyDB.getString("realLastname"));
        name_view.setText(tinyDB.getString("realFirstName")+ " "+tinyDB.getString("realLastname"));
        address_edit.setText(tinyDB.getString("realAddress"));
        address_view.setText(tinyDB.getString("realAddress"));
        area_edit.setText(tinyDB.getString("realArea"));
        area_view.setText(tinyDB.getString("realArea"));
        mobile_edit.setText(tinyDB.getString("realMobile"));
        mobile_view.setText(tinyDB.getString("realMobile"));

    }
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    ////////////////////// UPDATE_PROFILE ///////////////////////////
    ////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    public class httpReqPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            String property = strings[0];
            String value = strings[1];
            if(property=="gender"){
                tinyDB.putString("realGender",value);
            }
            if(property=="is_pregnant"){
                tinyDB.putString("realIsPregnant",value);
            }
            URL url = null;

            try {
                url = new URL(tinyDB.getString("host") + "update_property"+"?token="+tinyDB.getString("token"));
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
                        .appendQueryParameter("id", tinyDB.getString("uid"))

                .appendQueryParameter("property", property)
                        .appendQueryParameter("value", value);

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

                    String x = conn.getHeaderField("Authorization");
                    String[] xx = x.split(" ");

                    tinyDB.putString("token",xx[1]);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    ////////////////////// UPDATE_PROFILE ///////////////////////////
    ////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    boolean man ;
    public class httpReqPosttoProfile extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String response = "";
            URL url = null;

            try {
                url = new URL(tinyDB.getString("host") + "submit_profile"+"?token="+tinyDB.getString("token"));
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

                Uri.Builder builder = new Uri.Builder();
                if(man){
                    builder.appendQueryParameter("id", tinyDB.getString("uid"))

                            .appendQueryParameter("gender", "male")
                            .appendQueryParameter("is_pregnant", "no")
                            .appendQueryParameter("currently_lactating","no");
                    man = false;
                }else {
                    builder.appendQueryParameter("id", tinyDB.getString("uid"))

                            .appendQueryParameter("first_name", tinyDB.getString("realFirstName"))
                            .appendQueryParameter("last_name", tinyDB.getString("realLastname"))
                            .appendQueryParameter("address", tinyDB.getString("realAddress"))
                            .appendQueryParameter("mobile_number", tinyDB.getString("realMobile"))
                            .appendQueryParameter("area", tinyDB.getString("realArea"));
                }
                String query = builder.build().getEncodedQuery();
                Log.e("query",query);
                Log.e("url",url+"");
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
                    String x = conn.getHeaderField("Authorization");
                    String[] xx = x.split(" ");
                    tinyDB.putString("token",xx[1]);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            firstName_edit.setText(tinyDB.getString("realFirstName"));
            lastName_edit.setText(tinyDB.getString("realLastname"));
            name_view.setText(tinyDB.getString("realFirstName")+ " "+tinyDB.getString("realLastname"));
            address_edit.setText(tinyDB.getString("realAddress"));
            address_view.setText(tinyDB.getString("realAddress"));
            area_edit.setText(tinyDB.getString("realArea"));
            area_view.setText(tinyDB.getString("realArea"));
            mobile_edit.setText(tinyDB.getString("realMobile"));
            mobile_view.setText(tinyDB.getString("realMobile"));
        }

    }

}
