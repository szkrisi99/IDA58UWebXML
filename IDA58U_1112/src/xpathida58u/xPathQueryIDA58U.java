package xpathida58u;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xPathQueryIDA58U {
	public static void main(String[] args)
	{
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			
			Document document = documentBuilder.parse("studentIDA58U.xml");
		
			document.getDocumentElement().normalize();
			
			XPath xPath = XPathFactory.newInstance().newXPath();
			
			// String neptunkod = "class";
			//String neptunkod = "class/student";
			//String neptunkod = "class/student[@id='2']";
			//String neptunkod = "//student";
			//String neptunkod = "class/student[2]";
			//String neptunkod = "class/student[last()]";
			//String neptunkod = "class/student[last()-1]";
			//String neptunkod = "class/student[position() <=2]";
			//String neptunkod = "class/*";
			String neptunkod = "//student[@*]";
			
			NodeList neptunKod = (NodeList) xPath.compile(neptunkod).evaluate(document, XPathConstants.NODESET);
			
			for (int i = 0; i <neptunKod.getLength(); i++)
			{
				Node node = neptunKod.item(i);
				
				System.out.print("\nAktuális elem " + node.getNodeName());
				
				if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("student")) {
				
					Element element = (Element) node;
					
					System.out.println("Hallgato ID: " + element.getAttribute("id"));
					System.out.println("Keresztnev: " + element.getElementsByTagName("keresztnev").item(0).getTextContent());
					System.out.println("Vezeteknev: " + element.getElementsByTagName("vezeteknev").item(0).getTextContent());
					System.out.println("Becenev: " + element.getElementsByTagName("becenev").item(0).getTextContent());
					System.out.println("Kor: " + element.getElementsByTagName("kor").item(0).getTextContent());
				}
			}
		} catch(ParserConfigurationException e) {
			e.printStackTrace();
		} catch(SAXException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(XPathExpressionException e) {
			e.printStackTrace();
		}
	}
}
