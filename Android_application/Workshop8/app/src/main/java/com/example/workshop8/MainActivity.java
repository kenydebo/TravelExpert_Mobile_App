package com.example.workshop8;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpression;

/*
 * Purpose: Main methods for CMPP 264 Lab 13
 * Author: Mark Poffenroth
 * Date: Sept 2015
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // constants
    private final String PHONE_STRING = "1-800-872-8359";
    private final String URL_STRING = "http://10.187.8.242:8080/Workshop7/index.jsp";
    //"http://10.187.7.102:8080/Workshop7/index.jsp";    //"http://www.sait.ca";
    private final String EMAIL_TO = "support@TravelExperts.com";
    private final String EMAIL_SUBJECT = "Subject: Customer inquiry";
    private final String EMAIL_CONTENT = "Please send me information regarding...";

    // reference variables
    private Button buttonPhone, buttonWebsite, buttonEmail;
    private ArrayList<Agency> agenciesList = new ArrayList<>();
    private ArrayList<Agent> agentsList = new ArrayList<>();
    private ListView lv_agencies;
    private ListView lv_agents;
    private EditText et_searchAgent;
    private AgentDB dbagent = new AgentDB(this);
    private AgencyDB dbagency = new AgencyDB(this);
    private AgentListViewAdapter adapterAgents;
    private boolean flagWebRan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate objects
        buttonPhone = (Button) findViewById(R.id.btnPhone);
        buttonWebsite = (Button) findViewById(R.id.btnWebsite);
        buttonEmail = (Button) findViewById(R.id.btnEmail);

        // Run once to fill db tables w/ initial data in local db
        //dbagency.fillAgenciesTableDummy();
        //dbagent.fillAgentsTableDummy();

        // load agencies and agents arraylist from db
        agenciesList = dbagency.getAgencies();
        agentsList = dbagent.getAgents("0");

        // load agencies and agents arraylist fro XML Web service
        new DownloadXML().execute();


        // initialize ListView
        lv_agencies = (ListView)findViewById(R.id.lvMainAgencies);
        lv_agents = (ListView)findViewById(R.id.lvMainAgents);


        // Pass ArrayList<Agent> to AgentListViewAdapter Class
        adapterAgents = new AgentListViewAdapter(this, agentsList);

        // Fill Agents ListView by binding Adapter to ListView
        lv_agents.setAdapter(adapterAgents);

        // display agencies lists
        //displayAgencies();

        // generate ontiemclicklistener here in oncreate
        lv_agencies.setOnItemClickListener(this);



        // initialize EditText search filter field
        et_searchAgent = (EditText) findViewById(R.id.etSearchAgent);

        // Capture Text in EditText
        et_searchAgent.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // Filter adapter based on EditText chars
                String text = et_searchAgent.getText().toString().toLowerCase(Locale.getDefault());
                adapterAgents.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });


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
                // intent.setType("text/plain");    // sends to email and text clients
                intent.setType("message/rfc822");   // sends to email clients only
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_TO});
                intent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
                intent.putExtra(Intent.EXTRA_TEXT, EMAIL_CONTENT);
                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(null, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    // Fill agencies listview w/ agency addresses
    private void displayAgencies() {

        ArrayList<HashMap<String,String>> agencyData = new ArrayList<HashMap<String,String>>();

        // loop through agencies and add to hashmap
        for (Agency ag : agenciesList)
        {
            HashMap<String,String> map = new HashMap<String,String>();

            // add using put keyword
            map.put("AgencyKeyword", ag.getAgncyAddress() + " \n" + ag.getAgncyCity() + ", " +
                    ag.getAgncyProv() + " " + ag.getAgncyCountry());

            // add hashmap to arraylist
            agencyData.add(map);
        }

        // get layout we will display to
        int resource = R.layout.activity_agency_list;

        // key to take data from
        String[]from = {"AgencyKeyword"};

        // text view field to put data into
        int[]to = {R.id.txtAgncyAddress};

        //
        SimpleAdapter adapter = new SimpleAdapter(this, agencyData, resource, from, to);

        lv_agencies.setAdapter(adapter);
    }


    // Find out which list item in which list was clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch(parent.getId()) {

            case R.id.lvMainAgencies:
                Agency agency = agenciesList.get(position);

                Intent intent = new Intent(this, AgencyDetailActivity.class);
                // NOTE: Cast int to string for intent to work properly
                intent.putExtra("AgencyId", String.valueOf(agency.getAgencyId()));
                intent.putExtra("AgncyAddress", agency.getAgncyAddress());
                intent.putExtra("AgncyCity", agency.getAgncyCity());
                intent.putExtra("AgncyProv", agency.getAgncyProv());
                intent.putExtra("AgncyPostal", agency.getAgncyPostal());
                intent.putExtra("AgncyCountry", agency.getAgncyCountry());
                intent.putExtra("AgncyPhone", agency.getAgncyPhone());
                intent.putExtra("AgncyFax", agency.getAgncyFax());
                this.startActivity(intent);
                break;

            case R.id.lvMainAgents:
                Agent agent = agentsList.get(position);

                Intent intent2 = new Intent(this, AgentDetailActivity.class);
                // NOTE: Cast int to string for intent to work properly
                intent2.putExtra("AgentId", String.valueOf(agent.getAgentId()));
                intent2.putExtra("AgtFirstName", agent.getAgtFirstName());
                intent2.putExtra("AgtMiddleInitial", agent.getAgtMiddleInitial());
                intent2.putExtra("AgtLastName", agent.getAgtLastName());
                intent2.putExtra("AgtBusPhone", agent.getAgtBusPhone());
                intent2.putExtra("AgtEmail", agent.getAgtEmail());
                intent2.putExtra("AgtPosition", agent.getAgtPosition());
                // NOTE: Cast int to string for intent to work properly
                intent2.putExtra("AgencyId", String.valueOf(agent.getAgencyId()));
                intent2.putExtra("AgentStatus", agent.getAgentStatus());
                this.startActivity(intent2);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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



    // Web Services Start

    // DownloadXML AsyncTask
    // NOTE:
    // 1st param is for onPreExecute ; Void means no params received into this function
    // 2nd param is for ; Void means it is not monitoring the progress of this task being performed
    // 3rd param is for ; Void means doInBackground is not going to pass anything when it finishes to onPostExecute

    //private class DownloadXML extends AsyncTask<String, Void, Void> {
    private class DownloadXML extends AsyncTask<Void, Void, Void> {

        NodeList nodeList1;
        NodeList nodeList2;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressbar
            pDialog = new ProgressDialog(MainActivity.this);
            // Set progressbar title
            pDialog.setTitle("Getting Data");
            // Set progressbar message
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // Show progressbar
            pDialog.show();
        }

        // NOTE:
        // doInBackground cannot output anything to display
        // output to display is done in onPostExecute
        @Override
        protected Void doInBackground(Void... voids ) {

            //String url1x = "http://10.187.31.217:8080/Webserv/GetAgenciesXML";
            //String url2x = "http://10.187.31.217:8080/Webserv/GetAgentsXML";

            //String url1x = "http://192.168.1.8:8080/Webserv/GetAgenciesXML";
            //String url2x = "http://192.168.1.8:8080/Webserv/GetAgentsXML";

            String url1x = "http://10.187.8.242:8080/Webserv/GetAgenciesXML";
            String url2x = "http://10.187.8.242:8080/Webserv/GetAgentsXML";

            int AgencyId = 0;
            String AgncyAddress = "";
            String AgncyCity = "";
            String AgncyProv = "";
            String AgncyPostal = "";
            String AgncyCountry = "";
            String AgncyPhone = "";
            String AgncyFax = "";
            int AgentId = 0;
            String AgtFirstName = "";
            String AgtLastName = "";
            String AgtMiddleInitial = "";
            String AgtBusPhone = "";
            String AgtEmail = "";
            String AgtPosition = "";
            String AgentStatus = "";

            try {

                // Get Agency data from XML
                URL url1 = new URL(url1x);

                // standard for reading an XML file
                DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder1 = factory1.newDocumentBuilder();
                // Download the XML file
                Document document1 = builder1.parse(url1.openStream());

                // Root node
                Element root1 = document1.getDocumentElement();
                Log.e("webserv", root1.getNodeName());

                NodeList nlist1 = root1.getChildNodes();

                if (nlist1 != null) {
                    // Clear list
                    agenciesList.clear();

                    for (int i = 0; i < nlist1.getLength(); i++) {
                        if (nlist1.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            Element el = (Element) nlist1.item(i);
                            if (el.getNodeName().contains("Agency")) {
                                AgencyId = Integer.parseInt(el.getElementsByTagName("AgencyId").item(0).getTextContent());
                                AgncyAddress = el.getElementsByTagName("AgncyAddress").item(0).getTextContent();
                                AgncyCity = el.getElementsByTagName("AgncyCity").item(0).getTextContent();
                                AgncyProv = el.getElementsByTagName("AgncyProv").item(0).getTextContent();
                                AgncyPostal = el.getElementsByTagName("AgncyPostal").item(0).getTextContent();
                                AgncyCountry = el.getElementsByTagName("AgncyCountry").item(0).getTextContent();
                                AgncyPhone = el.getElementsByTagName("AgncyPhone").item(0).getTextContent();
                                AgncyFax = el.getElementsByTagName("AgncyFax").item(0).getTextContent();
                                //AgncyMap = el.getElementsByTagName("AgncyMap").item(0).getTextContent();

                                //Log.e("webserv", el.getElementsByTagName("AgencyId").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgncyAddress").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgncyCity").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgncyProv").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgncyPostal").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgncyCountry").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgncyPhone").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgncyFax").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgncyMap").item(0).getTextContent());
                            }
                            agenciesList.add(new Agency(AgencyId, AgncyAddress, AgncyCity, AgncyProv, AgncyPostal,
                                    AgncyCountry, AgncyPhone, AgncyFax ));
                        }
                    }
                }

                // Get Agent data from XML
                URL url2 = new URL(url2x);

                // standard for reading an XML file
                DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder2 = factory2.newDocumentBuilder();
                // Download the XML file
                Document document2 = builder2.parse(url2.openStream());

                // Root node
                Element root2 = document2.getDocumentElement();
                Log.e("webserv", root2.getNodeName());

                NodeList nlist2 = root2.getChildNodes();

                if (nlist2 != null) {
                    // Clear list
                    agentsList.clear();
                    for (int i = 0; i < nlist2.getLength(); i++) {
                        if (nlist2.item(i).getNodeType() == Node.ELEMENT_NODE) {

                            Element el = (Element) nlist2.item(i);
                            if (el.getNodeName().contains("Agent")) {
                                AgentId = Integer.parseInt(el.getElementsByTagName("AgentId").item(0).getTextContent());
                                AgtFirstName = el.getElementsByTagName("AgtFirstName").item(0).getTextContent();
                                if (el.getElementsByTagName("AgtMiddleInitial").item(0).getTextContent().equals("null"))
                                    AgtMiddleInitial = "";
                                else
                                    AgtMiddleInitial = el.getElementsByTagName("AgtMiddleInitial").item(0).getTextContent();
                                AgtLastName = el.getElementsByTagName("AgtLastName").item(0).getTextContent();
                                AgtBusPhone = el.getElementsByTagName("AgtBusPhone").item(0).getTextContent();
                                AgtEmail = el.getElementsByTagName("AgtEmail").item(0).getTextContent();
                                AgtPosition = el.getElementsByTagName("AgtPosition").item(0).getTextContent();
                                AgencyId = Integer.parseInt(el.getElementsByTagName("AgencyId").item(0).getTextContent());
                                AgentStatus = el.getElementsByTagName("AgentStatus").item(0).getTextContent();

                                //Log.e("webserv", el.getElementsByTagName("AgentId").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgtFirstName").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgtMiddleInitial").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgtLastName").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgtBusPhone").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgtEmail").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgtPosition").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgencyId").item(0).getTextContent());
                                //Log.e("webserv", el.getElementsByTagName("AgentStatus").item(0).getTextContent());
                            }
                            agentsList.add(new Agent(AgentId, AgtFirstName, AgtMiddleInitial, AgtLastName, AgtBusPhone,
                                    AgtEmail, AgtPosition, AgencyId, AgentStatus ));
                        }
                    }
                }
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            if (agenciesList.size() > 0) {

                // Clear local SQLite db tables
                dbagency.clearAgenciesTable();

                // Save updated web data to local db
                dbagency.fillAgenciesTable(agenciesList);
            }

            if (agentsList.size() > 0) {

                // Clear local SQLite db tables
                dbagent.clearAgentsTable();

                // Save updated web data to local db
                dbagent.fillAgentsTable(agentsList);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            agenciesList = dbagency.getAgencies();
            agentsList = dbagent.getAgents("0");
            Log.e("webserv postex list ", String.valueOf(agenciesList.size()));
            Log.e("webserv postex db ", String.valueOf(dbagency.getAgencyCount()));

            Log.e("webserv postex list ", String.valueOf(agentsList.size()));
            Log.e("webserv postex db ", String.valueOf(dbagent.getAgentCount()));

            // Display agencies lists
            displayAgencies();

            // Close progressbar
            pDialog.dismiss();
        }
    }

    // getNode function
    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }

}
