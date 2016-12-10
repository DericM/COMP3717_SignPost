package ca.bcit.dmccadden.comp3717_signpost.app;

/**
 * Created by knome on 2016-11-24.
 */

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Brody McCrone on 2016-11-24.
 */
public class AppPreferences {
    private static final String PREF_NAME = "signpostprefs";
    private static final String KEY_USER = "user";
    private static final String KEY_PASS = "pass";
    private static final String KEY_ID = "id";
    private static final String KEY_LOGGED_IN = "loggedin";
    private SharedPreferences prefs;
    /**
     * Retrieves stored information (last I checked user and pass).
     * Stored information is SharedPreferences object.
     * @param context
     */
    public AppPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        /*
        SharedPreferences.Editor e = prefs.edit();
        e.clear();
        e.commit();
        */
    }
    /**
     * Stores a username and password. Also overwrites previous
     * username and password.
     *
     * @param user Username to store.
     * @param pass Password to store.
     */
    public void storeUserPass(String user, String pass, int id) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.putString(KEY_USER, user);
        editor.putString(KEY_PASS, pass);
        editor.putInt(KEY_ID, id);
        editor.commit();
    }
    /**
     * Checks if the user has previously logged in
     * to Signpost.
     *
     * @return True if they are logged in, otherwise
     * false.
     */
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }
    /**
     * Retrieves the username. If there isn't one an empty
     * String is returned.
     *
     * @return Username or empty String if there isn't one.
     */
    public String getUser() {
        return prefs.getString(KEY_USER, "");
    }
    /**
     * Retrieves the password. If there isn't one an empty
     * String is returned.
     *
     * @return Password or empty String if there isn't one.
     */
    public String getPass() {
        return prefs.getString(KEY_PASS, "");
    }

    /**
     * Retrieves the userId. If there isn't one -1 is
     * returned.
     *
     * @return Id or -1 if there isn't one.
     */
    public int getId() {
        return prefs.getInt(KEY_ID, -1);
    }

    public void clear() {
        SharedPreferences.Editor e = prefs.edit();
        e.clear();
        e.commit();
    }
}
