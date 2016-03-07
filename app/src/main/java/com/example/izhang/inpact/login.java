package com.example.izhang.inpact;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

public class login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

        setContentView(R.layout.activity_login);

        // Get android_id
        final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // Shared preferences

        ParseQuery query = new ParseQuery("Users");
        query.whereContains("androidid",android_id);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject dealsObject : objects) {
                        // Save email of owner
                        SharedPreferences.Editor edit = getSharedPreferences("owneremail", MODE_PRIVATE).edit();
                        edit.putString("owneremail", dealsObject.get("email").toString());
                        edit.commit();
                        Intent homePage = new Intent(getApplicationContext(), main.class);
                        //startActivity(homePage);
                        //finish();
                    }
                } else {
                    Log.d("ERROR IN PARSE HOME", "ERROR: " + e.getMessage());
                }
            }
        });

        final Button login = (Button) findViewById(R.id.loginButton);
        final Button register = (Button) findViewById(R.id.registerButton);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText passbox = (EditText) findViewById(R.id.passBox);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homePage = new Intent(getApplicationContext(), main.class);
                startActivity(homePage);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(getApplicationContext(), register.class);
                startActivity(regIntent);
            }
        });

    }


}
