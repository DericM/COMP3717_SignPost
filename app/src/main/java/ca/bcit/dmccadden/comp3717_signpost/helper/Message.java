package ca.bcit.dmccadden.comp3717_signpost.helper;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class Message implements Parcelable {


    public static ArrayList<Message> messages;


    private int messageID;
    private String message;
    private LatLng location;
    private int viewed;


    public Message(String m, LatLng l){
        message = m;
        location = l;
    }

    public Message(int ID, String m, LatLng l, int v){
        messageID = ID;
        message = m;
        location = l;
        viewed = v;
    }


    public int getID(){
        return messageID;
    }

    public String getMessage(){
        return message;
    }

    public LatLng getLocation(){
        return location;
    }

    public int is_viewed(){
        return viewed;
    }

    public void view(){
        viewed = 1;
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
