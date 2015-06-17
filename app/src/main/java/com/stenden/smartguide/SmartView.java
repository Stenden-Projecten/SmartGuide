package com.stenden.smartguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Alex Iakab on 17-6-2015.
 */
public class SmartView  extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartview);
    }
    public void AboutClick(View v)
    {
            Intent t = new Intent(this,About.class);
            startActivity(t);
    }
    public void Click2D(View v)
    {
        MainActivity m = new MainActivity();
        m.setMode(false);
        Intent t = new Intent(this,m.getClass());
        startActivity(t);
    }
    public void Click3D(View v)
    {
        MainActivity m = new MainActivity();
        m.setMode(true);
        Intent t = new Intent(this,m.getClass());
        startActivity(t);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent t = new Intent(this,About.class);
            startActivity(t);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
