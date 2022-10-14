package jonatan.andrei.service;

import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@ApplicationScoped
public class ReadXmlFileService {

    @ConfigProperty(name = "xml.folder")
    String xmlFolder;

    public List<Map<String, String>> readXmlFile(String dumpName, String fileName, Class T) {
        List<Map<String, String>> mapFieldsList = new ArrayList<>();
        try {
            // Reference: https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(xmlFolder + dumpName + "/" + fileName + ".xml"));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("row");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    List<Field> fields = Arrays.asList(T.getDeclaredFields());
                    Map<String, String> fieldsMap = new HashMap<>();
                    for (Field field : fields) {
                        fieldsMap.put(field.getName(), element.getAttribute(field.getName()));
                    }
                    mapFieldsList.add(fieldsMap);
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            Log.error("Erro ao converter xml: ", e);
        }
        return mapFieldsList;
    }
}
