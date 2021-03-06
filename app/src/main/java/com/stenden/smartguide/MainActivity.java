package com.stenden.smartguide;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebViewFragment;
import android.widget.CompoundButton;
import android.widget.Switch;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends ActionBarActivity implements OnMapReadyCallback {
    MapFragment mapFragment;
    GuideFragment guideFragment;

    final String GUIDE_TAG = "guideFragment";
    final String MAP_TAG = "mapFragment";

    boolean isIn3D = false;
    public void setMode(boolean toggle)
    {
        if(toggle == true) {
            isIn3D=true;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mapFragment);
        }
        else
        {
            isIn3D = false;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(guideFragment);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            Log.i("SmartGuide", "savedInstanceState");
            isIn3D = savedInstanceState.getBoolean("isIn3D");
        } else {
            Log.i("SmartGuide", "intent");
            isIn3D = getIntent().getExtras().getBoolean("isIn3D");
        }

        Log.i("SmartGuide", "isIn3D: " + isIn3D);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        mapFragment = (MapFragment)getSupportFragmentManager().findFragmentByTag(MAP_TAG);
        if(mapFragment == null) {
            Log.i("SmartGuide", "Creating new MapFragment");

            mapFragment = MapFragment.newInstance();

            ft.add(R.id.contentFragment, mapFragment, MAP_TAG);
        }

        guideFragment = (GuideFragment)getSupportFragmentManager().findFragmentByTag(GUIDE_TAG);
        if(guideFragment == null) {
            Log.i("SmartGuide", "Creating new GuideFragment");

            guideFragment = GuideFragment.newInstance();
            guideFragment.setRetainInstance(true);

            ft.add(R.id.contentFragment, guideFragment, GUIDE_TAG);
        }

        if(isIn3D) {
            ft.hide(mapFragment);
        } else {
            ft.hide(guideFragment);
        }

        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isIn3D", isIn3D);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.24044, 6.85678), 18));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Switch s = (Switch)menu.findItem(R.id.switchId).getActionView().findViewById(R.id.modeSwitch);
        s.setChecked(isIn3D);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);

                isIn3D = isChecked;

                if(isIn3D) {
                    ft.hide(mapFragment);
                    ft.show(guideFragment);

                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    ft.hide(guideFragment);
                    ft.show(mapFragment);

                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
                getSupportFragmentManager().executePendingTransactions();
            }
        });

        return true;
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
}
