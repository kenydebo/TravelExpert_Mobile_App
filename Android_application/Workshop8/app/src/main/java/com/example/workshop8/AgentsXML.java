package com.example.workshop8;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** Authour: Filmon Ghezehey
 *  Date: Oct 05-2015
 *  Purpose: Stores Agents Data in to Agent ArrayList From XML Node
 */
public class AgentsXML {

    public ArrayList<Agent> getAllAgents(String url) throws IOException, URISyntaxException, InterruptedException {

        ArrayList<Agent> agents = new ArrayList<Agent>();
        XMLParser parser = new XMLParser();

        Document doc = parser.getXmlFromUrl(url);
        // Document doc = parser.getDomElement(xml); // getting DOM element
        NodeList nl = doc.getElementsByTagName("Agents");
        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) {

            Agent agent = new Agent();

            Element e = (Element) nl.item(i);
            agent.setAgencyId(Integer.parseInt(parser.getValue(e,"AgentId")));
            agent.setAgencyId(Integer.parseInt(parser.getValue(e,"AgencyId")));
            agent.setAgentStatus(parser.getValue(e, "AgentStatus"));
            agent.setAgtBusPhone(parser.getValue(e, "AgtBusPhone"));
            agent.setAgtEmail(parser.getValue(e, "AgtEmail"));
            agent.setAgtFirstName(parser.getValue(e, "AgtFirstName"));
            agent.setAgtLastName(parser.getValue(e, "AgtLastName"));
            agent.setAgtMiddleInitial(parser.getValue(e, "AgtMiddleInitial"));
            agent.setAgtPosition(parser.getValue(e,"AgtPosition"));

            // adding ArrayList
            agents.add(agent);

        }
        return agents;
    }

}
