package ca.bcit.dmccadden.comp3717_signpost.helper;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class ContentUpdateService {

    public static ArrayList<Message> messages;
    public static ArrayList<Message> newMessages;

    private GPSTracker gps;
    private Context context;

    public ContentUpdateService(Context c, GPSTracker gps){
        context = c;
        this.gps = gps;
    }

    public void getMessagesFromServer(){




        if(gps.canGetLocation()) {

            try {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                Area area = new Area(longitude, latitude);
                Log.d(TAG, longitude + " , " + latitude);
                Log.d(TAG, area.topLeftLon + " , " + area.topLeftLat + " , " + area.botRightLon + " , " + area.botRightLat);
                Ion.with(context)
                        .load("http://signpost.nfshost.com/php/get_messages.php")
                        .setBodyParameter("topLeftLon", area.topLeftLon)
                        .setBodyParameter("topLeftLat", area.topLeftLat)
                        .setBodyParameter("botRightLon", area.botRightLon)
                        .setBodyParameter("botRightLat", area.botRightLat)
                        .asString()
                        .setCallback(
                                new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                        Log.d(TAG, result);

                                        try {
                                            JSONObject jsnobject = new JSONObject(result);
                                            JSONArray jsonArray = jsnobject.getJSONArray("messages");

                                            Message.messages.clear();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject explrObject = jsonArray.getJSONObject(i);
                                                int messageID = Integer.parseInt(explrObject.getString("messageid"));
                                                String message = explrObject.getString("message");
                                                double latitude = Double.parseDouble(explrObject.getString("lattitude"));
                                                double longitude = Double.parseDouble(explrObject.getString("longitude"));
                                                Message.messages.add(new Message(message, new LatLng(latitude, longitude)));

                                            }
                                            Log.d(TAG, jsonArray.toString());

                                        }
                                        catch (Exception exception) {
                                            Log.d(TAG, exception.getMessage());
                                        }
                                        //                      buildMessageList();
                                    }
                                }
                        );
            }
            catch(Exception e){
                Log.d(TAG, e.getMessage());
            }
        }
    }
}
