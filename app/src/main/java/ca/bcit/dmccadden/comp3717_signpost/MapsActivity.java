package ca.bcit.dmccadden.comp3717_signpost;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.ContentValues.TAG;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //Message message = (Message)getIntent().getSerializableExtra("message");


        for(Message m:Message.messages){
            double latitude = m.getLocation().latitude;
            double longitude = m.getLocation().longitude;
            LatLng location = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(location).title(m.getMessage()));
        }


        try {
            Bundle data = getIntent().getExtras();
            Message message = (Message) data.getParcelable("message");

            double latitude  = message.getLocation().latitude;
            double longitude = message.getLocation().longitude;
            LatLng location = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(location).title(message.getMessage()).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
        }
        catch(Exception e){
            Log.d(TAG, e.getMessage());
        }

    }


}


