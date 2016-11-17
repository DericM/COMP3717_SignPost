package ca.bcit.dmccadden.comp3717_signpost;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class HistoryActivity extends Activity {


    private ArrayList<Message> messages;
    private ListView listView;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private List<String> messageList;
    List<Map<String, String>> messageMap;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ArrayAdapter<String> adapter;


    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        session = new Session();
        session.setUserId("56");

        messages = new ArrayList<Message>();

        messageMap = new ArrayList<>();


        for(Message m:Message.testMessages())
            messages.add(m);

        /*try{
            for(Message m:session.getAllMessages())
                messages.add(m);
        }
        catch(Exception e){

        }*/


        listView = (ListView) findViewById(R.id.message_history);

        messageList = new ArrayList<>();;
        //= Arrays.asList(testArray);
        for(int i=0; i<messages.size(); i++){
            messageList.add(messages.get(i).getMessage());
        }


        //============

        /*
        messageMap = new ArrayList<>();;
        //= Arrays.asList(testArray);
        for(int i=0; i<messages.size(); i++){
            messageMap.add();
        }*/



        //===============




        adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1,
                messageList);

        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
                //Object o = listView.getItemAtPosition(position);
                Message message = messages.get(position);
                //String itemName = (String) adapter.getItemAtPosition(position);
                Intent i = new Intent(HistoryActivity.this, MapsActivity.class);
                //Bundle args = new Bundle();
                //args.putParcelable("message", message);
                i.putExtra("message", message);
                //i.putExtra("doubleValue_e1", doubleData);
                //i.putExtra("doubleValue_e2", doubleData);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.

                    double latitude  = data.getDoubleExtra("latitude" ,-1);
                    double longitude = data.getDoubleExtra("longitude" ,-1);
                    String s_message = data.getStringExtra("message");

                    addMessage(new Message(s_message, new LatLng(latitude, longitude)));


                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                }
                break;
            }
        }
    }



    public void addMessage(Message message){

        try {
            if(!session.postMessage(message)) {
                System.out.println(session.getError());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        try{
            messages.add(message);
            messageList.add(message.getMessage());
            adapter = new ArrayAdapter<>(getBaseContext(),
                    android.R.layout.simple_list_item_1,
                    messageList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            //adapter.add(message.getMessage());

        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
