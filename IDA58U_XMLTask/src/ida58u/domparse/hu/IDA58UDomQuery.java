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
import java.util.HashSet;
import java.util.Set;

public class IDA58UDomQuery {
	public static void main(String[] args) {
        try {
            //DOM Parser
            File xmlFile = new File("IDA58U_XML.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // StringBuilder
            StringBuilder output = new StringBuilder();
            output.append("Repülőtér adatbázis lekérdezések \n");

            //Lekérdezések
            lekerdezes1_MindenRepuloterNeve(doc, output);
            lekerdezes2_RepterekSzazFoFelett(doc, output);
            lekerdezes3_MarciusTizenotUtaniFoglalasok(doc, output);
            lekerdezes4_AirbusJaratok(doc, output);
            lekerdezes5_KiUtazikLondonba(doc, output);


            System.out.println(output.toString());

            // Kiírás fájlba
            try (PrintWriter writer = new PrintWriter(new FileWriter("IDA58U_query_output.xml"))) {
                writer.print(output.toString());
            }
            System.out.println("\nLekérdezések fájlba írása sikeres");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    //LEKÉRDEZÉSEK
	
	//Listázza ki az összes repülőtér nevét.
	private static void lekerdezes1_MindenRepuloterNeve(Document doc, StringBuilder sb) {
	    sb.append("\n1. LEKÉRDEZÉS: Minden repülőtér neve \n");

	    NodeList repterNodeList = doc.getElementsByTagName("Repuloter");
	    for (int i = 0; i < repterNodeList.getLength(); i++) {
	        Node node = repterNodeList.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	            Element repter = (Element) node;
	            String nev = getElementText(repter, "Nev");
	            sb.append(" - ").append(nev).append("\n");
	        }
	    }
	}

	//Listázza ki azokat a repülőtereket, amelyeknek a főkapacitása 100 fölött van.
	private static void lekerdezes2_RepterekSzazFoFelett(Document doc, StringBuilder sb) {
		sb.append("\n2. LEKÉRDEZÉS: Repülőterek 100 fő felett\n");

	    NodeList repterNodeList = doc.getElementsByTagName("Repuloter");
	    for (int i = 0; i < repterNodeList.getLength(); i++) {
	        Node node = repterNodeList.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	            Element repter = (Element) node;
	            String kapacitasStr = getElementText(repter, "Fokapacitas");
	            try {
	                int kapacitas = Integer.parseInt(kapacitasStr);
	                if (kapacitas > 100) {
	                    sb.append(" - ").append(getElementText(repter, "Nev"))
	                      .append(" (Főkapacitás: ").append(kapacitas).append(")\n");
	                }
	            } catch (NumberFormatException e) {
	                // Hibás adat, kihagyjuk
	            }
	        }
	    }
	}
	
	//Listázza ki azoknak az utasoknak a nevét, akik március 15. után foglaltak.
	private static void lekerdezes3_MarciusTizenotUtaniFoglalasok(Document doc, StringBuilder sb) {
		sb.append("\n3. LEKÉRDEZÉS: Március 15. utáni foglalások\n");

	    Set<String> utasIDk = new HashSet<>();
	    NodeList foglalasList = doc.getElementsByTagName("Foglalas");
	    for (int i = 0; i < foglalasList.getLength(); i++) {
	        Element foglalas = (Element) foglalasList.item(i);
	        String datumStr = getElementText(foglalas, "Datum");
	        if (datumStr != null && !datumStr.isEmpty()) {
	            String[] parts = datumStr.split("-");
	            if (parts.length == 3) {
	                int honap = Integer.parseInt(parts[1]);
	                int nap = Integer.parseInt(parts[2]);
	                if (honap > 3 || (honap == 3 && nap > 15)) {
	                    utasIDk.add(foglalas.getAttribute("UtasID"));
	                }
	            }
	        }
	    }

	    if (utasIDk.isEmpty()) {
	        sb.append(" (Nincs március 15. utáni foglalás.)\n");
	        return;
	    }

	    NodeList utasList = doc.getElementsByTagName("Utas");
	    for (int i = 0; i < utasList.getLength(); i++) {
	        Element utas = (Element) utasList.item(i);
	        if (utasIDk.contains(utas.getAttribute("UtasID"))) {
	            sb.append(" - ").append(getElementText(utas, "Nev")).append("\n");
	        }
	    }
	}

	//Listázza ki az összes Airbus típusú géppel közlekedő járatot.
	private static void lekerdezes4_AirbusJaratok(Document doc, StringBuilder sb) {
	    sb.append("\n4. LEKÉRDEZÉS: Airbus típusú géppel közlekedő járatok \n");

	    NodeList jaratList = doc.getElementsByTagName("Jarat");
	    NodeList gepList = doc.getElementsByTagName("Gep");

	    for (int i = 0; i < jaratList.getLength(); i++) {
	        Element jarat = (Element) jaratList.item(i);

	        // A Jarat elemnek van GepID attribútuma (pl. GepID="1")
	        String gepID = jarat.getAttribute("GepID");
	        if (gepID == null || gepID.isEmpty()) continue;

	        // Megkeressük a Gep elemet a GepID alapján
	        Element gepTalalt = null;
	        for (int j = 0; j < gepList.getLength(); j++) {
	            Element gep = (Element) gepList.item(j);
	            String thisGepID = gep.getAttribute("GepID");
	            if (gepID.equals(thisGepID)) {
	                gepTalalt = gep;
	                break;
	            }
	        }

	        if (gepTalalt == null) {
	            // nincs ilyen gép bejegyezve — kihagyjuk
	            continue;
	        }

	        String tipus = getElementText(gepTalalt, "Tipus");
	        // Ellenőrizzük, hogy a típus tartalmazza-e az "Airbus" szót (pl. "Airbus A320")
	        if (tipus != null && tipus.contains("Airbus")) {
	            // Járatszám (a séma szerint <Jaratszam> a tag)
	            String jaratszam = getElementText(jarat, "Jaratszam");
	            sb.append(" - ").append(jaratszam).append(" (Gép: ").append(tipus).append(", GepID: ").append(gepID).append(")\n");
	        }
	    }
	}

	//Listázza ki a Londonba utazó utasokat.
	private static void lekerdezes5_KiUtazikLondonba(Document doc, StringBuilder sb) {
	    sb.append("\n5. LEKÉRDEZÉS: Ki utazik Londonba?\n");

	    Set<String> utasIDk = new HashSet<>();

	    // Megkeressük a londoni járatok ID-ját
	    NodeList jaratList = doc.getElementsByTagName("Jarat");
	    Set<String> londonJaratok = new HashSet<>();

	    for (int i = 0; i < jaratList.getLength(); i++) {
	        Element jarat = (Element) jaratList.item(i);
	        
	        NodeList helyszinList = jarat.getElementsByTagName("Helyszin");
	        if (helyszinList.getLength() > 0) {
	            Element helyszinElement = (Element) helyszinList.item(0);
	            String honnan = getElementText(helyszinElement, "Honnan");
	            String hova = getElementText(helyszinElement, "Hova");
		        if ("London".equals(hova)) {
		            londonJaratok.add(jarat.getAttribute("JaratID"));
		        }   
	        }
	    }

	    if (londonJaratok.isEmpty()) {
	        sb.append(" (Nincs Londonba tartó járat.)\n");
	        return;
	    }

	    // Utasok a londoni járatokon
	    NodeList foglalasList = doc.getElementsByTagName("Foglalas");
	    for (int i = 0; i < foglalasList.getLength(); i++) {
	        Element foglalas = (Element) foglalasList.item(i);

	        if (londonJaratok.contains(foglalas.getAttribute("JaratID"))) {
	            utasIDk.add(foglalas.getAttribute("UtasID"));
	        }
	    }

	    if (utasIDk.isEmpty()) {
	        sb.append(" (Senki nem foglalt Londonba.)\n");
	        return;
	    }

	    // Utasok nevei
	    NodeList utasList = doc.getElementsByTagName("Utas");
	    for (int i = 0; i < utasList.getLength(); i++) {
	        Element utas = (Element) utasList.item(i);
	        if (utasIDk.contains(utas.getAttribute("UtasID"))) {
	            sb.append(" - ").append(getElementText(utas, "Nev")).append("\n");
	        }
	    }
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

}
