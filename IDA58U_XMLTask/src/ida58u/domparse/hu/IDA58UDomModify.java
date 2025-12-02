package ida58u.domparse.hu;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class IDA58UDomModify {
	public static void main(String[] args) {
        File inputFile = new File("IDA58U_XML.xml");
        File outputFile = new File("IDA58U_XML_modositasok.xml");

        try {
            // XML beolvasása
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();


            StringBuilder report = new StringBuilder();
            report.append("Repülőtér adatbázis módosítások \n");

            modositas1_FokapacitasModositas(doc, report);
            modositas2_GepModositas(doc, report);
            modositas3_UjElemHozzaadasa(doc, report);
            modositas4_ElemTorlese(doc, report);

             // kiírása konzolra
            System.out.println(report.toString());
            //modositas kiirasa
            saveXml(doc, outputFile);

            System.out.println("\nMódosítások sikeresen mentve a IDA58U_XML.xml_modositasok.xml fájlba");

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

     // Módosítja a 3-as repülőtér főkapacitását
    private static void modositas1_FokapacitasModositas(Document doc, StringBuilder report) {
        report.append("\n 1. MÓDOSÍTÁS: A 3-as ID-jú repülőtér kapacitásának módosítása 300-ra\n");
        NodeList repuloterList = doc.getElementsByTagName("Repuloter");
        for (int i = 0; i < repuloterList.getLength(); i++) {
            Element repuloter = (Element) repuloterList.item(i);
            if ("3".equals(repuloter.getAttribute("RepuloterID"))) {
                Node fokapacitasNode = repuloter.getElementsByTagName("Fokapacitas").item(0);
                String regiFokapacitas = fokapacitasNode.getTextContent();
                fokapacitasNode.setTextContent("300");
                report.append("3-as repülőtér módosítva.\n");
                report.append("Régi főkapacitás:").append(regiFokapacitas).append(", Új főkapacitás: 300\n");
                return;
            }
        }
        report.append("HIBA: 3-as ID-jú repülőtér nem található.\n");
    }


    //A SW455-as számú járat gépét módosítjuk egy Airbus gepre
    private static void modositas2_GepModositas(Document doc, StringBuilder report) {
        report.append("\n2. MÓDOSÍTÁS: Járat gép típusának módosítása \n");
        
        NodeList gepList = doc.getElementsByTagName("Gep");
        String airbusGep = "";
        
        for (int i = 0; i < gepList.getLength(); i++) {
        	Element gep = (Element) gepList.item(i);
        	
        	String tipus = getElementText(gep, "Tipus");
        	if (tipus != null && tipus.contains("Airbus")) {
	            //Találjuk meg az első airbus gépet
        		airbusGep = gep.getAttribute("GepID");
	        }
        }
        
        NodeList jaratList = doc.getElementsByTagName("Jarat");
        for (int i = 0; i < jaratList.getLength(); i++) {
            Element jarat = (Element) jaratList.item(i);
            if ("SW455".equals(getElementText(jarat,"Jaratszam"))) {
                String regiGep = jarat.getAttribute("GepID");
                jarat.setAttribute("GepID", airbusGep);
                report.append("SW455 járat gépe módosítva.\n");
                report.append("Régi GepID: ").append(regiGep).append(", Új GepID: ").append(airbusGep).append("\n");
                return;
            }
        }
        report.append("HIBA: SW455 járat nem található.\n");
    }


    //Telefonszám hozzáadása a 3-as utashoz
    private static void modositas3_UjElemHozzaadasa(Document doc, StringBuilder report) {
        report.append("\n3. MÓDOSÍTÁS: Telefonszám hozzáadása 3-as utashoz \n");
        NodeList utasList = doc.getElementsByTagName("Utas");
        for (int i = 0; i < utasList.getLength(); i++) {
            Element utas = (Element) utasList.item(i);
            if ("3".equals(utas.getAttribute("UtasID"))) {
                Element telefonszam = doc.createElement("Telefonszam");
                telefonszam.setTextContent("+52 616 222 9876");
                utas.appendChild(telefonszam);
                report.append("SIKER: +52 616 222 9876 telefonszám hozzáadva a 3-as utashoz.\n");
                return;
            }
        }
        report.append(" HIBA: 3-as utas nem található.\n");
    }


    //2-es utas foglalásának törlése
    private static void modositas4_ElemTorlese(Document doc, StringBuilder report) {
        report.append("\n 5. MÓDOSÍTÁS: 2-es utas foglalásának törlése \n");
        NodeList foglalasList = doc.getElementsByTagName("Foglalas");


        for (int i = foglalasList.getLength() - 1; i >= 0; i--) {
            Element foglalas = (Element) foglalasList.item(i);
            if ("2".equals(foglalas.getAttribute("UtasID"))) {
                String toroltFoglalasID = foglalas.getAttribute("FoglalasID");
                Node szulo = foglalas.getParentNode();
                szulo.removeChild(foglalas);

                report.append("SIKER: 2 utas (FoglalasID: ").append(toroltFoglalasID).append(") foglalása törölve \n");
                return;
            }
        }
        report.append(" HIBA: 2 utashoz tartozó foglalás nem található.\n");
    }
    
    private static String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node.getFirstChild() != null && node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                return node.getFirstChild().getNodeValue().trim();
            }
        }
        return "";
    }

    //Segedfuggveny a menteshez
    private static void saveXml(Document doc, File file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        //kimenet beallitasa
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);
    }

}
