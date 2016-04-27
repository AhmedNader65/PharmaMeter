package com.businessmonk.pharmameter;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_custom_actionbar);
        TabHost tabHost = (TabHost) findViewById(R.id.mytabhost);
        tabHost.setup();

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
