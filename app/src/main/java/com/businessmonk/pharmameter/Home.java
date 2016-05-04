package com.businessmonk.pharmameter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity {
    TinyDB tinyDB;
    ImageButton profile_btn, emergencies_btn, nearby_btn ,invite_btn,order_btn,history_btn, reminder_btn,tip_btn,contact_btn;
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_custom_actionbar);
        TabHost tabHost = (TabHost) findViewById(R.id.mytabhost);
        tabHost.setup();
        tinyDB = new TinyDB(getApplicationContext());
        TextView idPlace = (TextView) findViewById(R.id.id_place);
        idPlace.setText(tinyDB.getString("uid"));
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Promotion");
        tab1.setIndicator("Promotion");
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        //tab 2 etc...
        TabHost.TabSpec tab2 = tabHost.newTabSpec("News");
        tab2.setIndicator("News");
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);
        ImageButton menu_btn = (ImageButton) findViewById(R.id.menu_button);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.this.openOptionsMenu();
            }
        });
        profile_btn = (ImageButton)findViewById(R.id.profile_button);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Profile.class);
                startActivity(intent);
            }
        });
        emergencies_btn =(ImageButton)findViewById(R.id.emergency_button);
        emergencies_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Emergencies.class);
                startActivity(intent);
            }
        });
        nearby_btn =(ImageButton)findViewById(R.id.nearby_button);
        nearby_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Nearby.class);
                startActivity(intent);
            }
        });
        order_btn =(ImageButton)findViewById(R.id.order_button);
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Order.class);
                startActivity(intent);
            }
        });
        reminder_btn =(ImageButton)findViewById(R.id.reminder_button);
        reminder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Reminder.class);
                startActivity(intent);
            }
        });
        tip_btn =(ImageButton)findViewById(R.id.tip_button);
        tip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Tip.class);
                startActivity(intent);
            }
        });
        contact_btn =(ImageButton)findViewById(R.id.contact_button);
        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Contact.class);
                startActivity(intent);
            }
        });
        history_btn =(ImageButton)findViewById(R.id.history_button);
        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,History.class);
                startActivity(intent);
            }
        });
        invite_btn =(ImageButton)findViewById(R.id.invite_button);
        invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "hi, I'm using Pharma Meter. It's amazing app you can also have it from here");
                startActivity(Intent.createChooser(sharingIntent,  "Where to invite"));;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
