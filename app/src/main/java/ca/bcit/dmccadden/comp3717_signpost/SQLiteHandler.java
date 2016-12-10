package ca.bcit.dmccadden.comp3717_signpost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import ca.bcit.dmccadden.comp3717_signpost.helper.Message;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION  = 1;

    // Database Name
    private static final String DATABASE_NAME  = "signpost";

    // Login table name
    private static final String TABLE_MESSAGES = "messages";

    // Login Table Columns names
    private static final String KEY_ID         = "id";
    private static final String KEY_MESSAGE    = "message";
    private static final String KEY_LATITUDE   = "latitude";
    private static final String KEY_LONGITUDE  = "longitude";
    private static final String KEY_VIEWED     = "viewed";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_ID         + " INTEGER PRIMARY KEY,"
                + KEY_MESSAGE    + " TEXT,"
                + KEY_LATITUDE   + " REAL,"
                + KEY_LONGITUDE  + " REAL,"
                + KEY_VIEWED     + " NUMERIC" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);

        // Create tables again
        onCreate(db);
    }



    public void addMessages(ArrayList<Message> messages){


    }




    /**
     * Storing user details in database
     * */
    public void addMessage(String message, double latitude, double longitude, int viewed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE,   message);
        values.put(KEY_LATITUDE,  latitude);
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_VIEWED,    viewed);

        // Inserting Row
        long id = db.insert(TABLE_MESSAGES, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New message inserted into sqlite: " + id);
    }

    /**
     * Getting messages from database
     * */
    public ArrayList<Message> getMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();

        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();

        int count = cursor.getCount();
        for(int i=0;i < count;i++){
            LatLng location = new LatLng(Double.parseDouble(cursor.getString(2)),Double.parseDouble(cursor.getString(3)));
            Message m = new Message(cursor.getString(1), location);
            messages.add(m);

        }

        cursor.close();
        db.close();

        Log.d(TAG, "Fetching user from Sqlite: " + messages.toString());

        return messages;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_MESSAGES, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}