package ca.bcit.dmccadden.comp3717_signpost.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import ca.bcit.dmccadden.comp3717_signpost.app.AppPreferences;
import ca.bcit.dmccadden.comp3717_signpost.R;


public class LoginActivity extends Activity {
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView status;
    private AppPreferences prefs;
    private String username;
    private String password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        status = (TextView)findViewById(R.id.text_status);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TEST: ", "IN REQUEST CODE");
        switch(requestCode) {

            case (0) : {
                if(resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 0);
    }

    public void login(View view) {
        EditText edituser = inputEmail;
        EditText editpass = inputPassword;
        username = edituser.getText().toString();
        password = editpass.getText().toString();
        Ion.with(this)
                .load("http://signpost.nfshost.com/php/authenticate.php")
                .setBodyParameter("username", username)
                .setBodyParameter("password", password)
                .asString()
                .setCallback(new Callback());
    }

    class Callback implements FutureCallback<String> {
        private int userId;

        @Override
        public void onCompleted(Exception e, String result) {
            if (result.trim().equals("")) {
                status.setText("Invalid log in. Try Again.");
            } else {
                prefs = new AppPreferences(getApplicationContext());
                prefs.storeUserPass(username, password, userId);
                Intent intent = new Intent();
                intent.putExtra("id", Integer.parseInt(result.trim()));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }
}