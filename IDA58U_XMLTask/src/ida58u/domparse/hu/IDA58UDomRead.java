package ida58u.domparse.hu;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class IDA58UDomRead {
	public static void main(String[] args) {
        try {
             //DOM Parser
            File xmlFile = new File("IDA58U_XML.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            //  StringBuilder
            StringBuilder output = new StringBuilder();

            output.append("Repülőtér adatbázis betöltése \n");

            //  Elemek feldolgozása
            processElements(doc, output, "Legitarsasag");
            processElements(doc, output, "Repuloter");
            processElements(doc, output, "Gep");
            processElements(doc, output, "Jarat");
            processElements(doc, output, "Utas");
            processElements(doc, output, "Foglalas");
            

            // Kiírás a konzolra
            System.out.println(output.toString());

            //Kiírás fájlba
            try (PrintWriter writer = new PrintWriter(new FileWriter("IDA58U_read_output.txt"))) {
                writer.print(output.toString());
            }
            System.out.println("\nAdatok fájlba írása sikeres.");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    // Feldolgozo metodus
    private static void processElements(Document doc, StringBuilder sb, String tagName) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            sb.append("\n ||| ").append(tagName.toUpperCase()).append(" |||\n");
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                sb.append("--------------------------------------\n");

                // Speciális formazas
                switch (tagName) {
                    case "Legitarsasag":
                    	sb.append("Légitarsasag ID: ").append(element.getAttribute("LegitarsasagID")).append("\n");
                        sb.append("Név: ").append(getElementText(element, "Nev")).append("\n");
                        sb.append("Alapítás: ").append(getElementText(element, "Alapitas")).append("\n");
                        sb.append("Székhely: ").append(getElementText(element, "Szekhely")).append("\n");
                    	break;
                    case "Repuloter":
                    	sb.append("Repülőtér ID: ").append(element.getAttribute("RepuloterID")).append("\n");
                        sb.append("Név: ").append(getElementText(element, "Nev")).append("\n");
                        sb.append("Cím: ").append(formatCim(element)).append("\n");
                        sb.append("Főkapacitás: ").append(getElementText(element, "Fokapacitas")).append("\n");
                    	break;
                    case "Gep":
                    	sb.append("Gép ID: ").append(element.getAttribute("GepID")).append("\n");
                        sb.append("Típus: ").append(getElementText(element, "Tipus")).append("\n");
                        sb.append("Gyártási év: ").append(getElementText(element, "GyartasiEv")).append("\n");
                        sb.append("üléskapacitás: ").append(getElementText(element, "Uleskapacitas")).append("\n");
                    	break;
                    case "Jarat":
                    	sb.append("Járat ID: ").append(element.getAttribute("JaratID")).append("\n");
                        sb.append("Járatszám: ").append(getElementText(element, "Jaratszam")).append("\n");
                        sb.append("Menetrend: ").append(formatMenetrend(element)).append("\n");
                        sb.append("Helyszin: ").append(formatHelyszin(element)).append("\n");
                    	break;
                    case "Utas":
                    	sb.append("Utas ID: ").append(element.getAttribute("UtasID")).append("\n");
                        sb.append("Név: ").append(getElementText(element, "Nev")).append("\n");
                        sb.append("Születési dátum: ").append(getElementText(element, "SzuletesiDatum")).append("\n");
                        sb.append("Útlevélszám: ").append(getElementText(element, "Utlevelszam")).append("\n");
                        sb.append("Telefonszám: ").append(getElementText(element, "Telefonszam")).append("\n");
                        break;
                    case "Foglalas":
                    	sb.append("Foglalás ID: ").append(element.getAttribute("FoglalasID")).append("\n");
                    	sb.append("Dátum: ").append(getElementText(element, "Datum")).append("\n");
                    	sb.append("Ülésszám: ").append(getElementText(element, "Ulesszam")).append("\n");
                    	break;
                }
            }
        }
    }
    
    //Cim blokk formazasa
    private static String formatCim(Element parent) {
        NodeList cimList = parent.getElementsByTagName("Cim");
        if (cimList.getLength() > 0) {
            Element cimElement = (Element) cimList.item(0);
            String orszag = getElementText(cimElement, "Orszag");
            String varos = getElementText(cimElement, "Varos");
            return orszag + " - " + varos;
        }
        return "N/A";
    }
    
    //Cim blokk formazasa
    private static String formatMenetrend(Element parent) {
    	NodeList menetrendList = parent.getElementsByTagName("Menetrend");
        if (menetrendList.getLength() > 0) {
            Element menetrendElement = (Element) menetrendList.item(0);
            String indulas = getElementText(menetrendElement, "Indulas");
            String erkezes = getElementText(menetrendElement, "Erkezes");
            return indulas + " -> " + erkezes;
        }
        return "";
    }
    
    //Helyszín blokk formazasa
    private static String formatHelyszin(Element parent) {
        NodeList helyszinList = parent.getElementsByTagName("Helyszin");
        if (helyszinList.getLength() > 0) {
            Element helyszinElement = (Element) helyszinList.item(0);
            String honnan = getElementText(helyszinElement, "Honnan");
            String hova = getElementText(helyszinElement, "Hova");
            return honnan + " -> " + hova;
        }
        return "";
    }

    //gyokerelem szoveges tartalmanak visszaadasa
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
}
