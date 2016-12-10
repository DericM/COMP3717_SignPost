package ca.bcit.dmccadden.comp3717_signpost.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ca.bcit.dmccadden.comp3717_signpost.R;

public class PostActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        FloatingActionButton submit = (FloatingActionButton)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText post_text = (EditText)findViewById(R.id.post_text);
                Intent i = getIntent();
                i.putExtra("message", post_text.getText().toString());
                setResult(PostActivity.RESULT_OK, i);
                finish();
            }
        });
    }
}
