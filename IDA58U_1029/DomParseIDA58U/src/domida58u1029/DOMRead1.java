package domida58u1029;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DOMRead1
{
    public static void main(String argv[]) throws SAXException,
    IOException, ParserConfigurationException
    {
        File xmlFile = new File("IDA58U_orarend.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        Document neptunkod = dBuilder.parse(xmlFile);

        neptunkod.getDocumentElement().normalize();

        System.out.println("Gyökér elem: " + neptunkod.getDocumentElement().getNodeName());

        NodeList nList = neptunkod.getElementsByTagName("ora");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            System.out.println("\nAktuális elem: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String hid = elem.getAttribute("id");

                Node node1 = elem.getElementsByTagName("targy").item(0);
                String tname = node1.getTextContent();

                Node node2 = elem.getElementsByTagName("idopont").item(0);

                Element idopontElem = (Element) elem.getElementsByTagName("idopont").item(0);
                String napname = idopontElem.getElementsByTagName("nap").item(0).getTextContent();
                String tolname = idopontElem.getElementsByTagName("tol").item(0).getTextContent();
                String igname  = idopontElem.getElementsByTagName("ig").item(0).getTextContent();

                Node node3 = elem.getElementsByTagName("helyszin").item(0);
                String hname = node3.getTextContent();

                Node node4 = elem.getElementsByTagName("oktato").item(0);
                String oname = node4.getTextContent();

                Node node5 = elem.getElementsByTagName("szak").item(0);
                String szname = node5.getTextContent();

                System.out.println("Óra ID: " + hid);
                System.out.println("Tárgy: " + tname);
                System.out.println("Nap " + napname);
                System.out.println("Tól " + tolname);
                System.out.println("Ig " + igname);
                System.out.println("Helyszin: " + hname);
                System.out.println("Oktató: " + oname);
                System.out.println("Szak: " + szname);
            }
        }
    }
}