package com.stenden.smartguide;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends ActionBarActivity implements OnMapReadyCallback, GuideFragment.OnFragmentInteractionListener {

    MapFragment mapFragment;
    GuideFragment guideFragment;

    boolean isIn3D = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }

        if(guideFragment == null) {
            guideFragment = GuideFragment.newInstance();
        }

        if(savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.contentFragment, mapFragment);
            ft.commit();
        }

        /*
        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "hallo", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.24044, 6.85678), 18));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Switch s = (Switch)menu.findItem(R.id.switchId).getActionView().findViewById(R.id.modeSwitch);
        //Toast.makeText(getApplicationContext(), isIn3D ? "3d" : "2d", Toast.LENGTH_SHORT).show();
        s.setChecked(isIn3D);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                //ft.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);

                isIn3D = isChecked;

                if (isChecked) {
                    //Toast.makeText(getApplicationContext(), "Aan", Toast.LENGTH_SHORT).show();
                    ft.replace(R.id.contentFragment, guideFragment);
                } else {
                    //Toast.makeText(getApplicationContext(), "Uit", Toast.LENGTH_SHORT).show();
                    ft.replace(R.id.contentFragment, mapFragment);
                }

                ft.commit();
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
