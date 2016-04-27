package com.businessmonk.pharmameter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Signup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void Continue(View view) {
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
    }
}
