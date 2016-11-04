package ca.bcit.dmccadden.comp3717_signpost;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Deric on 16/10/30.
 */

public class Message implements Parcelable {

    private String message;

    private LatLng location;

    Message(String m, LatLng l){
        message = m;
        location = l;
    }



    public static ArrayList<Message> testMessages(){

        ArrayList<Message> messages = new ArrayList<>();

        messages.add(new Message("bcit",             new LatLng(49.249110, -123.002753) ));
        messages.add(new Message("metrotown",        new LatLng(49.227882, -123.002024) ));
        messages.add(new Message("deer lake",        new LatLng(49.236749, -122.973264) ));
        messages.add(new Message("central perk",     new LatLng(49.227341, -123.017770) ));
        messages.add(new Message("burnaby hospital", new LatLng(49.249498, -123.016057) ));

        return messages;
    }

    public String getMessage(){
        return message;
    }

    public LatLng getLocation(){
        return location;
    }



    // Parcelling part
    public Message(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.message = data[0];

        double latitude = Double.parseDouble(data[1]);
        double longitude = Double.parseDouble(data[2]);
        this.location = new LatLng(latitude, longitude);
    }

    //@Оverride
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.message,
                Double.toString(this.location.latitude),
                Double.toString(this.location.longitude)
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };


}
