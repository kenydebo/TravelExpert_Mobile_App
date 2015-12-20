package com.example.workshop8;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Purpose: Agent Details for TravelExperts Database
 * Author: Mark Poffenroth
 * Date: Sept 2015
 */

public class AgentDetailActivity extends AppCompatActivity {

    //
    private String PHONE_STRING = "1-800-872-8359";
    private final String URL_STRING = "http://10.187.8.242:8080/Workshop7/index.jsp";
    private String EMAIL_TO = "support@TravelExperts.com";
    private final String EMAIL_SUBJECT = "Subject: Customer inquiry";
    private final String EMAIL_CONTENT = "Please send me information regarding...";

    // reference variables
    TextView tvAgentId, tvAgtFirstName, tvAgtMiddleInitial, tvAgtLastName, tvAgtBusPhone, tvAgtEmail, tvAgtPosition, tvAgencyId;
    private Button buttonPhone;
    private Button buttonWebsite;
    private Button buttonEmail;

    private Agency agency;
    //ArrayList<Agency> agencies;
    //ListView lv_agencies;
    AgencyDB dbagency = new AgencyDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Must set contentView before using findViewById to avoid NullPointerException errors
        setContentView(R.layout.activity_agent_detail);

        buttonPhone = (Button) findViewById(R.id.btnPhone);
        buttonWebsite = (Button) findViewById(R.id.btnWebsite);
        buttonEmail = (Button) findViewById(R.id.btnEmail);

        //tvAgentId = (TextView)findViewById(R.id.txtAgentId);
        tvAgtFirstName = (TextView)findViewById(R.id.txtAgtFirstName);
        tvAgtMiddleInitial = (TextView)findViewById(R.id.txtAgtMiddleInitial);
        tvAgtLastName = (TextView)findViewById(R.id.txtAgtLastName);
        tvAgtBusPhone = (TextView)findViewById(R.id.txtAgtBusPhone);
        tvAgtEmail = (TextView)findViewById(R.id.txtAgtEmail);
        tvAgtPosition = (TextView)findViewById(R.id.txtAgtPosition);
        tvAgencyId = (TextView)findViewById(R.id.txtAgencyId);

        // get the intent
        Intent intent = getIntent();

        // extract value from intent based on datatype
        //tvAgentId.setText(intent.getStringExtra("AgentId"));
        if (intent.getStringExtra("AgtMiddleInitial") == null)
            tvAgtFirstName.setText(intent.getStringExtra("AgtFirstName") + " " + intent.getStringExtra("AgtLastName"));
        else
            tvAgtFirstName.setText(intent.getStringExtra("AgtFirstName") + " " +
                intent.getStringExtra("AgtMiddleInitial") + " " + intent.getStringExtra("AgtLastName"));
        //tvAgtLastName.setText(intent.getStringExtra("AgtLastName"));
        //tvAgtMiddleInitial.setText(intent.getStringExtra("AgtMiddleInitial"));
        tvAgtBusPhone.setText(intent.getStringExtra("AgtBusPhone"));
        tvAgtEmail.setText(intent.getStringExtra("AgtEmail"));
        tvAgtPosition.setText(intent.getStringExtra("AgtPosition"));

        // load agencies and agents arraylist from db
        //agency = dbagency.getAgency(Integer.valueOf(intent.getStringExtra("AgencyId")));
        agency = dbagency.getAgency(intent.getStringExtra("AgencyId"));
        Log.e("webserv", "agents agencyid " + agency.getAgencyId());

        // list on main screen
        //lv_agencies = (ListView)findViewById(R.id.lvMainAgencies);

        // display agencies and agents lists
        //displayAgencies();

        // generate ontiemclicklistener here in oncreate
        //lv_agencies.setOnItemClickListener(this);

        // display agency address
        tvAgencyId.setText(agency.getAgncyAddress() + " \n" + agency.getAgncyCity() + ", " +
                agency.getAgncyProv() + " " + agency.getAgncyCountry());


        PHONE_STRING = intent.getStringExtra("AgtBusPhone");
        EMAIL_TO = intent.getStringExtra("AgtEmail");


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



//    // Fill agencies listview w/ agency addresses
//    private void displayAgencies() {
//
//        ArrayList<HashMap<String,String>> agencyData = new ArrayList<HashMap<String,String>>();
//
//        // loop through agencies and add to hashmap
//        for (Agency ag : agencies)
//        {
//            HashMap<String,String> map = new HashMap<String,String>();
//
//            // add using put keyword
//            map.put("AgencyKeyword", ag.getAgncyAddress() + " \n" + ag.getAgncyCity() + ", " +
//                    ag.getAgncyProv() + " " + ag.getAgncyCountry());
//
//            // add hashmap to arraylist
//            agencyData.add(map);
//        }
//
//        // get layout we will display to
//        int resource = R.layout.activity_agency_list;
//
//        // key to take data from
//        String[]from = {"AgencyKeyword"};
//
//        // text view field to put data into
//        int[]to = {R.id.txtAgncyAddress};
//
//        //
//        SimpleAdapter adapter = new SimpleAdapter(this, agencyData, resource, from, to);
//
//        lv_agencies.setAdapter(adapter);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agent_detail, menu);
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
