package ca.bcit.dmccadden.comp3717_signpost.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import ca.bcit.dmccadden.comp3717_signpost.R;
import ca.bcit.dmccadden.comp3717_signpost.app.AppPreferences;


public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputUserName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputPasswordConfirm;
    private TextView status;

    String email;
    String username;
    String password;
    String confirmPassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputUserName = (EditText) findViewById(R.id.username);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPasswordConfirm = (EditText) findViewById(R.id.confirm_password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        status = (TextView) findViewById(R.id.register_status);
    }

    public void resetInputs() {
        inputUserName.setText("");
        inputEmail.setText("");
        inputPassword.setText("");
        inputPasswordConfirm.setText("");
    }

    public void register(View view) {
        EditText edituser = inputUserName;
        EditText editpass = inputPassword;
        username = edituser.getText().toString();
        password = editpass.getText().toString();
        confirmPassword = inputPasswordConfirm.getText().toString();
        if(!password.equals(confirmPassword)) {
            status.setText("Passwords do not match.");
            resetInputs();
        } else {
            Ion.with(this)
                    .load("http://signpost.nfshost.com/php/create_account.php")
                    .setBodyParameter("username", username)
                    .setBodyParameter("password", password)
                    .asString()
                    .setCallback(new RegisterActivity.Callback());
        }
    }

    class Callback implements FutureCallback<String> {
        private int userId;

        @Override
        public void onCompleted(Exception e, String result) {

            AppPreferences prefs = new AppPreferences(getApplicationContext());
            prefs.storeUserPass(username, password, userId);
            Intent intent = new Intent();
            intent.putExtra("id", Integer.parseInt(result.trim()));
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
    }
}