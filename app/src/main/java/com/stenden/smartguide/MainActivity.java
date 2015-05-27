package com.stenden.smartguide;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends ActionBarActivity implements OnMapReadyCallback, GuideFragment.OnFragmentInteractionListener {
    MapFragment mapFragment;
    GuideFragment guideFragment;

    final String GUIDE_TAG = "guideFragment";
    final String MAP_TAG = "mapFragment";

    boolean isIn3D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            isIn3D = savedInstanceState.getBoolean("isIn3D");
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        mapFragment = (MapFragment)getFragmentManager().findFragmentByTag(MAP_TAG);
        if(mapFragment == null) {
            Log.i("SmartGuide", "Creating new MapFragment");

            mapFragment = MapFragment.newInstance();
            mapFragment.getMapAsync(this);

            ft.add(R.id.contentFragment, mapFragment, MAP_TAG);
        }

        guideFragment = (GuideFragment)getFragmentManager().findFragmentByTag(GUIDE_TAG);
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
        getFragmentManager().executePendingTransactions();

        /*
        if(savedInstanceState == null) {
            Log.i("SmartGuide", "new state");
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            guideFragment = GuideFragment.newInstance();
            guideFragment.setRetainInstance(true);
            ft.add(R.id.contentFragment, guideFragment, GUIDE_TAG);

            mapFragment = MapFragment.newInstance();
            mapFragment.setRetainInstance(true);
            mapFragment.getMapAsync(this);
            ft.add(R.id.contentFragment, mapFragment, MAP_TAG);

            ft.commit();
        } else {
            Log.i("SmartGuide", "resuming state");

            isIn3D = savedInstanceState.getBoolean("isIn3D");

            mapFragment = (MapFragment)getFragmentManager().findFragmentByTag(MAP_TAG);
            guideFragment = (GuideFragment)getFragmentManager().findFragmentByTag(GUIDE_TAG);

            Log.i("SmartGuide", mapFragment == null ? "mapFragment: fucked" : "mapFragment: ok");
            Log.i("SmartGuide", guideFragment == null ? "guideFragment: fucked" : "guideFragment: ok");
        }
        */
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
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                //ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);

                isIn3D = isChecked;

                if(isIn3D) {
                    //Toast.makeText(getApplicationContext(), "Aan", Toast.LENGTH_SHORT).show();
                    //ft.replace(R.id.contentFragment, guideFragment);
                    ft.hide(mapFragment);
                    ft.show(guideFragment);
                } else {
                    //Toast.makeText(getApplicationContext(), "Uit", Toast.LENGTH_SHORT).show();
                    //ft.replace(R.id.contentFragment, mapFragment);
                    ft.hide(guideFragment);
                    ft.show(mapFragment);
                }

                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
                getFragmentManager().executePendingTransactions();
            }
        });

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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
