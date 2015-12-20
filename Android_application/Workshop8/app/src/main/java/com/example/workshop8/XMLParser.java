package com.example.workshop8;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** Authour: Filmon Ghezehey
 *  Date: Oct 05-2015
 *  Purpose: Parses an xml from webservices in to XML Nodes
 */


public class XMLParser{



    public Document getXmlFromUrl(String urlstring) throws IOException, InterruptedException, URISyntaxException {


        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setValidating(false);
        builderFactory.setIgnoringComments(false);
        builderFactory.setIgnoringElementContentWhitespace(true);
        builderFactory.setNamespaceAware(true);
        URL url = new URL(urlstring);
        // InputStream inputStream = url.openStream();
        URLConnection urlConnection = url.openConnection();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

        //Get Document Builder
        DocumentBuilder builder = null;

        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document dom = null;
        try {
            dom = builder.parse(in);
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return dom;
    }
    // Getting node value param elem element

    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

//  Getting node value param Element node param key string

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }
}