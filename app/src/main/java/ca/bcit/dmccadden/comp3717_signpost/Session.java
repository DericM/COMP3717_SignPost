package ca.bcit.dmccadden.comp3717_signpost;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import com.google.android.gms.maps.model.LatLng;

public class Session {
    public static final String CREATE_ACCOUNT
            = "http://signpost.nfshost.com/php/create_account.php";
    public static final String POST_MESSAGE
            = "http://signpost.nfshost.com/php/post_message.php";
    public static final String GET_MESSAGES
            = "http://signpost.nfshost.com/php/get_messages.php";

    public static final String ERROR_NONE
            = "No error";
    public static final String ERROR_NOT_LOGGED_IN
            = "Not logged in";
    public static final String ERROR_INVALID_MESSAGE
            = "Invalid message";
    public static final String ERROR_INVALID_PASSWORD
            = "Invalid password";
    public static final String ERROR_QUERY_FAILED
            = "database query failed.";

    private String userId;
    private String error = "none";

	/*
	public static void main(String[] args) throws Exception {

		Session session = new Session();

		session.userId = "56";

		session.postMessage("WOOO I DEEED IT", 3.230984023, 5.2098343);

		String username = "Cats23logan";
		String password = "Gorillas";
		String confirmPassword = password;
		if(session.createAccount(username, password, confirmPassword)) {
			System.out.println(session.userId);
		}

		session.getMessages(0, 0);
		System.out.println("ERROR LOG: " + session.error);

		session.setUserId("56");
		System.out.println(session.postMessage("desdaf", 533.3234234, 32.234234));
		System.out.println(session.error);

	}
*/

    public ArrayList<Message> parseMessages(String messages) {
        ArrayList<Message> messageArray = new ArrayList<Message>();
        for(String msgPackage : messages.split("\n")) {
            if(msgPackage.equals("[]") == false
                    && msgPackage.trim().equals("") == false) {
                String[] s = msgPackage.split(",");
                messageArray.add(new Message(
                        s[0],
                        new LatLng(Double.parseDouble(s[1]),
                        Double.parseDouble(s[2]))));

            }
        }
        return messageArray;
    }

    public ArrayList<Message> getMessages(double lat, double lon) throws Exception {
        if(validateCoords(lat, lon) == false) {
            return null;
        }

        Encoder encoder = new Encoder();
        try {
            encoder.encodeParameter("lattitude", String.valueOf(lat));
            encoder.encodeParameter("longitude", String.valueOf(lon));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return parseMessages(post(encoder.getData(), GET_MESSAGES));

    }

    public boolean postMessage(Message message) throws Exception {
        String msg = message.getMessage();
        Double lat = message.getLocation().latitude;
        Double lon = message.getLocation().longitude;
        if(userId == null) {
            error = ERROR_NOT_LOGGED_IN;
            return false;
        }

        if(validateMessage(msg) == false) {
            error = ERROR_INVALID_MESSAGE;
            return false;
        }

        Encoder encoder = new Encoder();
        try {
            encoder.encodeParameter("id", userId);
            encoder.encodeParameter("message", msg);
            encoder.encodeParameter("lattitude", String.valueOf(lat));
            encoder.encodeParameter("longitude", String.valueOf(lon));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        post(encoder.getData(), POST_MESSAGE);
        return true;
    }

    /*
    public boolean postMessage(String msg, double lat, double lon) throws Exception {
        if(userId == null) {
            error = ERROR_NOT_LOGGED_IN;
            return false;
        }

        if(validateMessage(msg) == false) {
            error = ERROR_INVALID_MESSAGE;
            return false;
        }

        Encoder encoder = new Encoder();
        try {
            encoder.encodeParameter("id", userId);
            encoder.encodeParameter("message", msg);
            encoder.encodeParameter("lattitude", String.valueOf(lat));
            encoder.encodeParameter("longitude", String.valueOf(lon));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        post(encoder.getData(), POST_MESSAGE);
        return true;
    }
    */

    public boolean createAccount(String username, String password, String confirmPassword) throws Exception {
        if(validatePassword(password, confirmPassword) == false) {
            error = ERROR_INVALID_PASSWORD;
            return false;
        }

        Encoder encoder = new Encoder();
        try {
            encoder.encodeParameter("username", username);
            encoder.encodeParameter("password", password);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String postReturnData = post(encoder.getData(), CREATE_ACCOUNT);
        if(postReturnData.equals(ERROR_QUERY_FAILED)) {
            error = ERROR_QUERY_FAILED;
            return false;
        }
        //POTENTIAL FUCK UP postReturnDat could be like "    43   " or worse
        userId = postReturnData;
        return true;
    }

    public boolean validateCoords(double lat, double lon) {
        //UNIMPLEMENTED
        return true;
    }

    public boolean validatePassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public boolean validateMessage(String msg) {
        return msg != "";
    }
    /*
     * "http://signpost.nfshost.com/php/create_account.php"
     */
    public String post(String data, String address) throws Exception {
        System.out.println("posting");
        URL url = new URL(address);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

        wr.write(data);
        wr.flush();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        String returnData = "";
        while((line = reader.readLine()) != null) {
            returnData += line + "\n";
        }

        return returnData;
    }

    class Encoder {
        String data;

        void encodeParameter(String key, String value) throws UnsupportedEncodingException {
            if(data == null) {
                data = URLEncoder.encode(key, "UTF-8")
                        + "=" + URLEncoder.encode(value, "UTF-8");
            } else {
                data += "&" + URLEncoder.encode(key, "UTF-8")
                        + "=" + URLEncoder.encode(value, "UTF-8");
            }
        }

        String getData() {
            return data;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}