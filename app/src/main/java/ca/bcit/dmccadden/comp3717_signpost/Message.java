package ca.bcit.dmccadden.comp3717_signpost;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Deric on 16/10/30.
 */

public class Message extends Activity {

    private String message;

    private LatLng location;

    Message(String m, LatLng l){
        message = m;
        location = l;
    }




}
