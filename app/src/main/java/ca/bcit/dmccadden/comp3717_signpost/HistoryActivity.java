package ca.bcit.dmccadden.comp3717_signpost;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryActivity extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

/*
        ListView lv = (ListView)findViewById(R.id.message_history);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1,
                termsList)

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position){
                //Object item = adapter.getItemAtPosition(position);
                String itemName = (String)adapter.getItemAtPosition(position);
                Intent intent = new Intent(HistoryActivity.this, MapsActivity.class);

                startActivity(intent);

            }
        });
*/

        
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.create_message);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), GPSTrackerActivity.class);
                //startActivityForResult(intent,1);
            }
        });



    }







}
