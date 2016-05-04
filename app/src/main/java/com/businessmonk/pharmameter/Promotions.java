package com.businessmonk.pharmameter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Promotions extends AppCompatActivity {


    TextView medicine = (TextView)findViewById(R.id.medicine_name);
    TextView pharamcy = (TextView)findViewById(R.id.pharmacy_name);
    TextView first_price = (TextView)findViewById(R.id.first_price);
    TextView sale_price = (TextView)findViewById(R.id.sale_price);
    TextView date= (TextView)findViewById(R.id.date);
    ImageView img = (ImageView)findViewById(R.id.medicine_img);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotions_activity);

        TextView med_namet = (TextView)findViewById(R.id.med_name);
//        TextView med_pharamcyt = (TextView)findViewById(R.id.med_phamacy);
//        TextView med_before = (TextView)findViewById(R.id.first_price);
//        TextView med_after = (TextView)findViewById(R.id.sale_price);
        TextView med_date = (TextView)findViewById(R.id.date);

        Intent in = getIntent();

        
        String med_name =  in.getSerializableExtra("med_name").toString();
        String med_pharamcy =  in.getSerializableExtra("med_pharamcy").toString();
//        String before = in.getSerializableExtra("before").toString();
//        String after = in.getSerializableExtra("after").toString();
        String expired = in.getSerializableExtra("expierd").toString();

        med_namet.setText(med_name);
//        med_pharamcyt.setText(med_pharamcy);
//        med_before.setText(before);
//        med_after.setText(after);
        med_date.setText(expired);


    }
}
