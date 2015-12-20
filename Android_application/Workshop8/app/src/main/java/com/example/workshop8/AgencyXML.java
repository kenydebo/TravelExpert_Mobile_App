package com.example.workshop8;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
/** Authour: Filmon Ghezehey
 *  Date: Oct 05-2015
 *  Purpose: Stores Agecy Data in to Agency ArrayList From XML Node
 */


public class AgencyXML {

    public ArrayList<Agency> getAllAgency(String url) throws IOException, URISyntaxException, InterruptedException {
        // All static variables
        // String URL = "http://api.androidhive.info/pizza/?format=xml";
        ArrayList<Agency> agencies = new ArrayList<Agency>();

        XMLParser parser = new XMLParser();

        // Document doc = parser.getDomElement(xml); // getting DOM element
        Document doc =parser.getXmlFromUrl(url);
        NodeList nl = doc.getElementsByTagName("Agency");
        // looping through all item nodes <item>
        for(int i = 0; i < nl.getLength(); i++) {

            Agency agency = new Agency();

            Element e = (Element) nl.item(i);
            // adding each child node to arraylist
            agency.setAgencyId(Integer.parseInt(parser.getValue(e, "agencyId")));
            agency.setAgncyAddress(parser.getValue(e, "AgncyAddress"));
            agency.setAgncyCity(parser.getValue(e, "AgncyCity"));
            agency.setAgncyCountry(parser.getValue(e, "AgncyCountry"));
            agency.setAgncyPostal(parser.getValue(e, "AgncyPostal"));
            agency.setAgncyProv(parser.getValue(e, "AgncyProv"));


            // adding ArrayList
            agencies.add(agency);

        }
        return agencies;
    }
}
