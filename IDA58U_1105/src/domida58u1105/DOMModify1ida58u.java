package domida58u1105;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;


public class DOMModify1ida58u 
{
	public static void main(String argv[])
    {
		try {
            File inputFile = new File("IDA58U_orarend.xml");
            File outputFile = new File("IDA58U_orarend_modositott.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            
            
            

            Element ujOra = doc.createElement("ora");
            ujOra.setAttribute("id", "03");
            
            Element targy = doc.createElement("targy");
            targy.appendChild(doc.createTextNode("Numerikus analízis"));
            ujOra.appendChild(targy);
            
            Element idopont = doc.createElement("idopont");
            Element nap = doc.createElement("nap");
            nap.appendChild(doc.createTextNode("Szombat"));
            Element tol = doc.createElement("tol");
            tol.appendChild(doc.createTextNode("16"));
            Element ig = doc.createElement("ig");
            ig.appendChild(doc.createTextNode("18"));
            idopont.appendChild(nap);
            idopont.appendChild(tol);
            idopont.appendChild(ig);
            ujOra.appendChild(idopont);
            
            Element helyszin = doc.createElement("helyszin");
            helyszin.appendChild(doc.createTextNode("A/102"));
            ujOra.appendChild(helyszin);
            
            Element oktato = doc.createElement("oktato");
            oktato.appendChild(doc.createTextNode("Dr. Karácsony Zsolt"));
            ujOra.appendChild(oktato);
            
            Element szak = doc.createElement("szak");
            szak.appendChild(doc.createTextNode("Dr. Karácsony Zsolt"));
            ujOra.appendChild(szak);
            
            root.appendChild(ujOra);
            
            
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            System.out.println("---Módosított fálj---");
            StreamResult result = new StreamResult(System.out);
            StreamResult resultfile = new StreamResult(outputFile);
            transformer.transform(source, result);
            transformer.transform(source, resultfile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
}
