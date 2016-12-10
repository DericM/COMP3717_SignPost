package ca.bcit.dmccadden.comp3717_signpost.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ca.bcit.dmccadden.comp3717_signpost.app.AppPreferences;
import ca.bcit.dmccadden.comp3717_signpost.helper.Area;
import ca.bcit.dmccadden.comp3717_signpost.helper.ContentUpdateService;
import ca.bcit.dmccadden.comp3717_signpost.helper.GPSTracker;
import ca.bcit.dmccadden.comp3717_signpost.helper.Message;
import ca.bcit.dmccadden.comp3717_signpost.R;
import ca.bcit.dmccadden.comp3717_signpost.SQLiteHandler;
import ca.bcit.dmccadden.comp3717_signpost.SessionManager;

import static android.content.ContentValues.TAG;

public class  MainActivity extends Activity {
    private int userId = -1;

    private ListView             listView;

    private GPSTracker           gps;
    private SQLiteHandler        db;
    private SessionManager       session;
    private ContentUpdateService updateService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        gps = new GPSTracker(MainActivity.this);
        //updateService = new ContentUpdateService(gps);

        listView = (ListView) findViewById(R.id.message_history);

        Message.messages = new ArrayList<Message>();

        getMessagesFromServer();

        final long period = 5000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getMessagesFromServer();

            }
        }, 0, period);


        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
                Message message = Message.messages.get(position);
                //String itemName = (String) adapter.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                //Bundle args = new Bundle();
                //args.putParcelable("message", message);
                i.putExtra("message", message);
                startActivity(i);
            }
        });


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.create_message);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                startActivityForResult(intent,1);
            }
        });

        AppPreferences prefs = new AppPreferences(this);
        prefs.clear(); //<--- FOr demonstration purposes. Makes log in pop up on every run
        if(prefs.isLoggedIn() == false) {
            //starts login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 2);
        } else {
            userId = prefs.getId();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    if(gps.canGetLocation()) {

                        double latitude = 0;
                        double longitude = 0;

                        try {
                            latitude = gps.getLocation().getLatitude();
                            longitude = gps.getLocation().getLongitude();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                        String s_message = data.getStringExtra("message");

                        addMessage(new Message(s_message, new LatLng(latitude, longitude)));
                    }
                }
                break;
            }
            case 2:
                if(resultCode == Activity.RESULT_OK) {
                    userId = data.getIntExtra("id", -2);
                    Log.v("SJIT", "" + userId);
                }
        }
    }




    public void getMessagesFromServer(){

        double latitude = 0;
        double longitude = 0;

        if(gps.canGetLocation()) {


            try {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }
            catch(Exception exception){
                exception.printStackTrace();
            }

            Area area = new Area(longitude, latitude);
            Log.d(TAG, longitude + " , " + latitude);
            Log.d(TAG, area.topLeftLon + " , " + area.topLeftLat + " , " + area.botRightLon + " , "  + area.botRightLat);
            Ion.with(this)
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

                                    try{


                                        JSONObject jsnobject = new JSONObject(result);

                                        JSONArray jsonArray = jsnobject.getJSONArray("messages");

                                        Message.messages.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject explrObject = jsonArray.getJSONObject(i);
                                            //int messageID    = Integer.parseInt(explrObject.getString("messageid"));
                                            String message   = explrObject.getString("message");
                                            double latitude  = Double.parseDouble(explrObject.getString("lattitude"));
                                            double longitude = Double.parseDouble(explrObject.getString("longitude"));
                                            Message.messages.add(new Message(message , new LatLng(latitude, longitude)));

                                        }
                                        Log.d(TAG, jsonArray.toString());

                                    }
                                    catch(Exception exception){
                                        Log.d(TAG, exception.getMessage());
                                    }
                                    buildMessageList();
                                }
                            }
                    );
        }

    }




    public void buildMessageList(){
        List<String> messageList;
        ArrayAdapter<String> adapter;

        messageList = new ArrayList<>();

        try{
            printCoords(gps.getLocation().getLatitude(), gps.getLocation().getLongitude());
        }
        catch(Exception e) {
            Log.d(TAG, e.getMessage());
        }


        for(int i=0; i<Message.messages.size(); i++){
            messageList.add(Message.messages.get(Message.messages.size()-1-i).getMessage());
        }

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        //for (Message item : Message.messages) {
        for(int i=0; i<Message.messages.size(); i++){
            Message item = Message.messages.get(Message.messages.size()-1-i);
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("title", item.getMessage());
            datum.put("date", " " + item.getLocation().latitude + " : " + item.getLocation().longitude);
            data.add(datum);
        }
        SimpleAdapter adapter1 = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"title", "date"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        listView.setAdapter(adapter1);

    }



    public void addMessage(Message message){
        Ion.with(this)
            .load("http://signpost.nfshost.com/php/post_message.php")
            .setBodyParameter("id", String.valueOf(userId))
            .setBodyParameter("message", message.getMessage())
            .setBodyParameter("lattitude", ""+message.getLocation().latitude)
            .setBodyParameter("longitude", ""+message.getLocation().longitude)
            .asString()
            .setCallback(
                new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.d(TAG, result);

                    }
                }
        );
    }

    public void printCoords(double latitude, double longitude) {
        //TextView tv = new TextView(getApplicationContext());
        //RelativeLayout l = (RelativeLayout)findViewById(R.id.layout);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        TextView coords = (TextView)findViewById(R.id.latitude);

        coords.setText(latitude + " " + longitude + " " + formattedDate);
        //l.addView(tv);
    }




    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();

        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark) // notification icon
                .setContentTitle("Notification!") // title for notification
                .setContentText("Hello word") // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(this, MainActivity.class);
        //PendingIntent pi = PendingIntent.getActivity(this,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
        //mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }
}