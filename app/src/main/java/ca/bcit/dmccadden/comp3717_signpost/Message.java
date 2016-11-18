package ca.bcit.dmccadden.comp3717_signpost;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Deric on 16/10/30.
 */

public class Message implements Parcelable {


    public static ArrayList<Message> messages;





    private String message;
    private LatLng location;

    Message(String m, LatLng l){
        message = m;
        location = l;
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
