package com.example.izhang.inpact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.Parse;

import java.util.ArrayList;

public class addCommitment extends Activity {
    ArrayList<String> Names = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_commitment);

        final Button addButton = (Button) findViewById(R.id.addButton);

        ListView list = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this,
                R.layout.listitem,
                Names);
        list.setAdapter(adapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText userName = (EditText) findViewById(R.id.userName);
                String currentName = userName.getText().toString();
                //parse stuff here to find user
                for (String x : Names) {
                    if (x.equals(currentName)) {
                        userName.setText("");
                        return;
                    }
                }
                Names.add(currentName);
                userName.setText("");
                adapter.notifyDataSetChanged();
            }
        });
        final Button nextButton = (Button) findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(), addContractDetails.class );
                nextIntent.putStringArrayListExtra("Names", Names);

                startActivity(nextIntent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_commitment, menu);
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
