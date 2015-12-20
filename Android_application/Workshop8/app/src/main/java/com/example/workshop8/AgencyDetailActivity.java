package com.example.workshop8;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


/*
 * Purpose: Agency Details for TravelExperts Database
 * Author: Mark Poffenroth
 * Date: Sept 2015
 */

public class AgencyDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    //
    private String PHONE_STRING = "1-800-872-8359";
    private final String URL_STRING = "http://10.187.8.242:8080/Workshop7/index.jsp";
    private String EMAIL_TO = "support@TravelExperts.com";
    private final String EMAIL_SUBJECT = "Subject: Customer inquiry";
    private final String EMAIL_CONTENT = "Please send me information regarding...";

    // reference variables
    TextView tvAgncyAddress, tvAgncyCity, tvAgncyProv, tvAgncyPostal, tvAgncyCountry, tvAgncyPhone, tvAgncyFax;
    private Button buttonPhone, buttonWebsite, buttonEmail;

    ArrayList<Agent> agentsList;
    ListView lv_agents;
    AgentDB dbagent = new AgentDB(this);


    private Adapter adapterAgents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Must set contentView before using findViewById to avoid NullPointerException errors
        setContentView(R.layout.activity_agency_detail);

        buttonPhone = (Button) findViewById(R.id.btnPhone);
        buttonWebsite = (Button) findViewById(R.id.btnWebsite);
        buttonEmail = (Button) findViewById(R.id.btnEmail);

        tvAgncyAddress = (TextView)findViewById(R.id.txtAgncyAddress);
        tvAgncyCity = (TextView)findViewById(R.id.txtAgncyCity);
        //tvAgncyProv = (TextView)findViewById(R.id.txtAgncyProv);
        tvAgncyPostal = (TextView)findViewById(R.id.txtAgncyPostal);
        tvAgncyCountry = (TextView)findViewById(R.id.txtAgncyCountry);
        tvAgncyPhone = (TextView)findViewById(R.id.txtAgncyPhone);
        tvAgncyFax = (TextView)findViewById(R.id.txtAgncyFax);

        // get the intent
        Intent intent = getIntent();

        // extract value from intent based on datatype
        tvAgncyAddress.setText(intent.getStringExtra("AgncyAddress"));
        tvAgncyCity.setText(intent.getStringExtra("AgncyCity") + ", " + intent.getStringExtra("AgncyProv"));
        tvAgncyPostal.setText(intent.getStringExtra("AgncyPostal"));
        tvAgncyCountry.setText(intent.getStringExtra("AgncyCountry"));
        tvAgncyPhone.setText(intent.getStringExtra("AgncyPhone"));
        tvAgncyFax.setText(intent.getStringExtra("AgncyFax"));

        // load agents arraylist from db
        agentsList = dbagent.getAgentsInAgency(intent.getStringExtra("AgencyId"));
        Log.e("webserv", "agency agents list " + agentsList.size());

        // list on main screen
        lv_agents = (ListView)findViewById(R.id.lvAgencyAgents);
        //lv_agents = (ListView)findViewById(R.id.lvMainAgents);

        // display agencies and agents lists
        displayAgents(agentsList);





        // Pass ArrayList<Agent> to AgentListViewAdapter Class
        adapterAgents = new AgentListViewAdapter(this, agentsList);

        // Fill Agents ListView by binding Adapter to ListView
        //lv_agents.setAdapter(adapterAgents);



        // generate ontiemclicklistener here in oncreate
        lv_agents.setOnItemClickListener(this);

        PHONE_STRING = intent.getStringExtra("AgncyPhone");

        // Call phone number stored in string
        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Note: Intent.ACTION_DIAL instead of Intent.ACTION_CALL.
                // Shows dialer w/ number entered, but allows user to actually make the call or not
                // ACTION_DIAL Requires Permission in Manifest:
                // <uses-permission android:name="android.permission.CALL_PHONE" />
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PHONE_STRING));
                startActivity(intent);
            }
        });


        // Go to website URL stored in string
        buttonWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_STRING));
                startActivity(intent);;
            }
        });


        // Send email to address stored in string
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                // this sends to email and text clients
                //intent.setType("text/plain");
                // this sends to email clients only
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_TO});
                intent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
                intent.putExtra(Intent.EXTRA_TEXT, EMAIL_CONTENT);
                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                    // finish will close app
                    //finish();
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(null, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Fill agents listview w/ agent names
    private void displayAgents(ArrayList<Agent> agents) {

        ArrayList<HashMap<String,String>> agentData = new ArrayList<HashMap<String,String>>();
        //Log.e("webserv", "agents listsize " + agents.size());

        // loop through agents and add to hashmap
        for (Agent a : agents)
        {
            HashMap<String,String> map = new HashMap<String,String>();

            // add using put keyword
            map.put("AgentKeyword", a.getAgtFirstName() + " " + a.getAgtLastName());

            // add hashmap to arraylist
            agentData.add(map);
        }
        Log.e("webserv", "agency agents list " + agentData.size());

        // get layout we will display to
        int resource = R.layout.activity_agent_list;

        // key to take data from
        String[]from = {"AgentKeyword"};

        // text view field to put data into
        int[]to = {R.id.txtAgtLastName};

        //
        SimpleAdapter adapter = new SimpleAdapter(this, agentData, resource, from, to);

        lv_agents.setAdapter(adapter);
        Log.e("webserv", "agency lv_agents count " + lv_agents.getCount());
    }


    // Find out which list item in which list was clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch(parent.getId()) {

            case R.id.lvAgencyAgents:
                Agent a = agentsList.get(position);

                Intent intent2 = new Intent(this, AgentDetailActivity.class);
                // NOTE: Cast int to string for intent to work properly
                intent2.putExtra("AgentId", String.valueOf(a.getAgentId()));
                intent2.putExtra("AgtFirstName", a.getAgtFirstName());
                intent2.putExtra("AgtLastName", a.getAgtLastName());
                intent2.putExtra("AgtMiddleInitial", a.getAgtMiddleInitial());
                intent2.putExtra("AgtBusPhone", a.getAgtBusPhone());
                intent2.putExtra("AgtEmail", a.getAgtEmail());
                intent2.putExtra("AgtPosition", a.getAgtPosition());
                intent2.putExtra("AgencyId", String.valueOf(a.getAgencyId()));

                Log.e("NOTE ", a.getAgtLastName());

                this.startActivity(intent2);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agency_detail, menu);
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
