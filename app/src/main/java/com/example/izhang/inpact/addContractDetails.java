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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class addContractDetails extends Activity {
    private ArrayList<String> clauses;
    private ArrayAdapter<String> adapter;
    int rowCount;
    String ownerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contract_details);
        final ArrayList<String> nameArr = getIntent().getExtras().getStringArrayList("Names");

        final EditText contractTitle = (EditText) findViewById(R.id.title);

        final Calendar cal = Calendar.getInstance();
        cal.getTime();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        clauses = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.listitem, clauses);
        ListView listView = (ListView) findViewById(R.id.clauses);
        listView.setAdapter(adapter);
        final Button addClauseButton = (Button) findViewById(R.id.addClause);
        addClauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText clause = (EditText) findViewById(R.id.clause);
                String currentClause = clause.getText().toString();
                for (String x : clauses) {
                    if (x.equals(currentClause)) {
                        clause.setText("");
                        return;
                    }
                }
                clauses.add(currentClause);
                clause.setText("");
                adapter.notifyDataSetChanged();
            }
        });
        final Button finishButton = (Button) findViewById(R.id.Finish);

        // Android ID
        // Get android_id
        final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        rowCount = 0;
        ParseQuery query = new ParseQuery("Commitment");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject dealsObject : objects) {
                        rowCount++;
                    }
                    finishButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            SharedPreferences prefs = getSharedPreferences("owneremail", MODE_PRIVATE);
                            ownerEmail = prefs.getString("owneremail", null);

                            //HERE
                            ParseObject parseObject = new ParseObject("Commitment");
                            //Adding content to a commitment
                            parseObject.put("cid", rowCount + 1);
                            parseObject.put("cheader", contractTitle.getText().toString());
                            parseObject.put("ctext", clauses.toString());
                            parseObject.put("email", ownerEmail);
                            // Which user is owner

                            ParseObject userObj = new ParseObject("UserCommit");
                            userObj.put("email", ownerEmail);
                            userObj.put("cid", rowCount + 1);
                            userObj.put("type", "owner");
                            userObj.put("rate", 1);
                            userObj.put("status", "ongoing");
                            userObj.saveInBackground();

                            // Query for user & save information
                            String password;
                            for (String email : nameArr) {
                                ParseObject userObj1 = new ParseObject("UserCommit");
                                userObj1.put("email", email);
                                userObj1.put("cid", rowCount + 1);
                                userObj1.put("type", "participant");
                                userObj1.put("rate", 1);
                                userObj1.put("status", "invited");
                                userObj1.saveInBackground();
                            }


                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    //Log.d("In Parse Class", e.toString());
                                }
                            });

                            //saving parse object
                            Intent intent = new Intent(getApplicationContext(), main.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Toast.makeText(getApplicationContext(), "Contract Created", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }
                    });
                } else {
                    Log.d("ERROR IN PARSE HOME", "ERROR: " + e.getMessage());
                }
            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contract_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
