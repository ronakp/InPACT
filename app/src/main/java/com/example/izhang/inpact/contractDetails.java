package com.example.izhang.inpact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class contractDetails extends Activity {

    ArrayList<String> arrList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_details2);


        Button completeButton = (Button) findViewById(R.id.completeButton);

        String value = getIntent().getExtras().getString("ContractName");
        TextView contractTitle = (TextView)findViewById(R.id.contractTitle);
        contractTitle.setText(value);

        TextView age = (TextView)findViewById(R.id.ageView);
        age.setText(getIntent().getExtras().getString("ContractAge"));


        ParseQuery query = new ParseQuery("Commitment");
        query.whereEqualTo("cheader", value);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    int cid = 0;
                    for (ParseObject dealsObject : objects) {
                        int arr = dealsObject.getInt("");
                        cid = dealsObject.getInt("cid");
                        String clauses = (String) dealsObject.get("ctext");
                        TextView clausesView = (TextView) findViewById(R.id.clauseView);
                        clausesView.setText(clauses);
                    }
                    ParseQuery cidQuery = new ParseQuery("UserCommit");
                    cidQuery.whereEqualTo("cid", cid);
                    cidQuery.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                for (ParseObject dealsObject : objects) {
                                    arrList.add(dealsObject.get("email").toString());
                                }
                                ListView list = (ListView) findViewById(R.id.partyList);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                        R.layout.listitem,
                                        arrList);
                                list.setAdapter(adapter);

                            } else {
                                Log.d("ERROR IN PARSE HOME", "ERROR: " + e.getMessage());
                            }
                        }
                    });

                } else {
                    Log.d("ERROR IN PARSE HOME", "ERROR: " + e.getMessage());
                }
            }
        });

        if(getIntent().getExtras().getString("Status") == null) completeButton.setVisibility(View.INVISIBLE);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contractDetails.this);
                alertDialogBuilder.setTitle("Approval of Parties");
                alertDialogBuilder
                        .setMessage("Did you approve of the other party memebers here?")
                        .setCancelable(true)
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Todo: Create a positive review for the person
                            }
                        })
                        .setNegativeButton("No, I don't", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Todo: Create a positive negetive review for the person
                            }
                        })
                        .setNeutralButton("Yes, I do", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Todo: Create a positive Cancel review for the person
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
        if (id == R.id.action_forcequit) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contractDetails.this);
            alertDialogBuilder.setTitle("Are you sure?");
            alertDialogBuilder
                    .setMessage("Quitting will negeitvely affect your inPACT score")
                    .setCancelable(true)
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Todo: Create a positive review for the person
                        }
                    })
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Todo: Create a positive Cancel review for the person
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
