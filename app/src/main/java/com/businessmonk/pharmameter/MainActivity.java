package com.businessmonk.pharmameter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast tst = Toast.makeText(getApplicationContext(), "Hi Pharma Meter !!", Toast.LENGTH_LONG);
        tst.show();
    }
}
