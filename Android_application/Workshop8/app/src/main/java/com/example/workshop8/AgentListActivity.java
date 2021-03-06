package com.example.workshop8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/*
 * Purpose: Agent List for TravelExperts Database
 * Author: Mark Poffenroth
 * Date: Sept 2015
 */

public class AgentListActivity extends AppCompatActivity {

    // reference variables
    TextView txtAgtLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_list);

        txtAgtLastName = (TextView)findViewById(R.id.txtAgtLastName);

        Intent intent = new Intent();

        // extract value from intent based on datatype
        txtAgtLastName.setText(intent.getStringExtra("AgtLastName"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agent_list, menu);
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
