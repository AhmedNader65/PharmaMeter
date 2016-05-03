package com.businessmonk.pharmameter;

import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Profile extends AppCompatActivity {
    ImageView profileconficon , profileEditicon,emailEditicon,phoneEditicon,birthdateicon,pregnant_Dateicon,emailEditiconConfirm,phoneEditiconConfirm,birthdateiconConfirm,pregnant_DateiconConfirm ;
    LinearLayout v , e ;
    EditText emailEdit,phoneEdit,birthdate,pregnant_DateEdit,howMenyChild ,firstName_edit,lastName_edit,mobile_edit,address_edit,area_edit;
    RadioButton male , female;
    RadioGroup genderGroup;
    CheckBox ispregView ,isLactView,haveChild;
    TextView starFromView,name_view,address_view,mobile_view,area_view;
    TinyDB tinyDB;
//    boolean isMale,isFemale,isPreg,isLact
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        birthdateiconConfirm = (ImageView) findViewById(R.id.birth_confirm_icon);
        pregnant_DateiconConfirm = (ImageView) findViewById(R.id.pregnantdate_confirm_icon);
        v = (LinearLayout) findViewById(R.id.view_sec);
        e = (LinearLayout) findViewById(R.id.edit_sec);
        e.setVisibility(View.INVISIBLE);
        emailEdit = (EditText) findViewById(R.id.email_profile_edit);
        phoneEdit = (EditText) findViewById(R.id.phone_profile_edit);
        birthdate = (EditText) findViewById(R.id.birth_date_profile_edit);
        pregnant_DateEdit = (EditText) findViewById(R.id.pregnant_Date);
        emailEdit.setEnabled(false);
        phoneEdit.setEnabled(false);
        birthdate.setEnabled(false);
        birthdate.setEnabled(false);
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

                e.setVisibility(View.INVISIBLE);
                v.setVisibility(View.VISIBLE);
            }
        });
        emailEditicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEdit.setEnabled(true);
                emailEditiconConfirm.setVisibility(View.VISIBLE);
                emailEditicon.setVisibility(View.INVISIBLE);
            }
        });
        emailEditiconConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEdit.setEnabled(false);
                emailEditiconConfirm.setVisibility(View.INVISIBLE);
                emailEditicon.setVisibility(View.VISIBLE);

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
                phoneEdit.setEnabled(false);

                phoneEditiconConfirm.setVisibility(View.INVISIBLE);
                phoneEditicon.setVisibility(View.VISIBLE);

            }
        });
        birthdateicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthdate.setEnabled(true);

                birthdateiconConfirm.setVisibility(View.VISIBLE);
                birthdateicon.setVisibility(View.INVISIBLE);
            }
        });
        birthdateiconConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthdate.setEnabled(false);

                birthdateiconConfirm.setVisibility(View.INVISIBLE);
                birthdateicon.setVisibility(View.VISIBLE);

            }
        });
        pregnant_Dateicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregnant_DateEdit.setEnabled(true);

                pregnant_DateiconConfirm.setVisibility(View.VISIBLE);
                pregnant_Dateicon.setVisibility(View.INVISIBLE);
            }
        });
        pregnant_DateiconConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pregnant_DateEdit.setEnabled(false);

                pregnant_DateiconConfirm.setVisibility(View.INVISIBLE);
                pregnant_Dateicon.setVisibility(View.VISIBLE);
            }
        });

        male = (RadioButton)findViewById(R.id.male_radio);
        female = (RadioButton)findViewById(R.id.female_radio);
        isLactView = (CheckBox)findViewById(R.id.lactating);
        starFromView = (TextView)findViewById(R.id.start) ;
        ispregView = (CheckBox)findViewById(R.id.pregnant);

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
                    isLactView.setVisibility(View.GONE);
                    ispregView.setVisibility(View.GONE);
                    pregnant_DateEdit.setVisibility(View.GONE);
                    starFromView.setVisibility(View.GONE);

                }else{

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

                    starFromView.setVisibility(View.VISIBLE);
                    pregnant_Dateicon.setVisibility(View.VISIBLE);
                    pregnant_DateEdit.setVisibility(View.VISIBLE);

                }else {

                    starFromView.setVisibility(View.GONE);
                    pregnant_Dateicon.setVisibility(View.GONE);
                    pregnant_DateEdit.setVisibility(View.GONE);

                }
            }
        });
        howMenyChild = (EditText) findViewById(R.id.children_num);
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
        httpReqGet req = new httpReqGet();
        req.execute();

    }
    public class httpReqGet extends AsyncTask<String,Void,String> {

        //String className;
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            //className = strings[1];
            URL url = null;
            Log.e("responso2",tinyDB.getString("host")+"profile/"+tinyDB.getString("uid")+"?token="+tinyDB.getString("token"));
            try {
                url = new URL(tinyDB.getString("host")+"profile/"+tinyDB.getString("uid")+"?token="+tinyDB.getString("token"));
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
                String[] xx= x.split(" ");
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
    String realFirstName , realLastname,realAddres,realArea,realMobile,realEmail,
    realPhone,realBirthDate,realGender , realIsPregnant,realPregnantDate,realIsLact,
    realDontHaveChild,realChildNum;
    public void parsing(String res) throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        realFirstName=jsonObject.getString("first_name");
        realLastname=jsonObject.getString("last_name");
        Log.e("firstname",realFirstName);
        Log.e("firstname",realLastname);
        realAddres=jsonObject.getString("address");
        realBirthDate=jsonObject.getString("birthdate");
        Log.e("firstname",realAddres);

        realEmail=jsonObject.getString("email");
        realMobile=jsonObject.getString("mobile_number");
        realPhone=jsonObject.getString("phone_number");
        Log.e("firstname",realMobile);

        realGender=jsonObject.getString("gender");
        realArea=jsonObject.getString("area_id");
        realIsPregnant=jsonObject.getString("is_pregnant");
        realIsPregnant=jsonObject.getString("is_pregnant");
        realPregnantDate=jsonObject.getString("pregnancy_date");
        realIsLact=jsonObject.getString("currently_lactating");
        emailEdit.setText(realEmail);
        phoneEdit.setText(realPhone);
        birthdate.setText(realBirthDate);
        if(realGender=="male"){
            male.setChecked(true);
        }else if(realGender=="female"){
            female.setChecked(true);
        }
        firstName_edit.setText(realFirstName);
        lastName_edit.setText(realLastname);
        name_view.setText(realFirstName+ " "+realLastname);
        address_edit.setText(realAddres);
        address_view.setText(realAddres);
        area_edit.setText(realArea);
        area_view.setText(realArea);
        mobile_edit.setText(realMobile);
        mobile_view.setText(realMobile);

    }
}
