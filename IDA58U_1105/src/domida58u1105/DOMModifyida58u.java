package domida58u1105;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;


public class DOMModifyida58u 
{
	public static void main(String argv[])
    {
		try {
            File inputFile = new File("IDA58U_hallgato.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList hallgatok = doc.getElementsByTagName("hallgato");

            for (int i = 0; i < hallgatok.getLength(); i++) {
                Node node = hallgatok.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element hallgato = (Element) node;

                    if ("01".equals(hallgato.getAttribute("id"))) {
                        hallgato.getElementsByTagName("keresztnev").item(0).setTextContent("Bálint");
                        hallgato.getElementsByTagName("vezeteknev").item(0).setTextContent("Tóth");
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            System.out.println("---Módosított fálj---");
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
}
