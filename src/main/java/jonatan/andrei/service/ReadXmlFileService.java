package jonatan.andrei.service;

import jonatan.andrei.model.User;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;

@ApplicationScoped
@Slf4j
public class ReadXmlFileService {

    @ConfigProperty(name = "xml.folder")
    String xmlFolder;

    public User readXmlFile() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(xmlFolder + "\\users" + ".xml"));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("row");

            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String reputation = element.getAttribute("Reputation");
                    log.info("reputation: " + reputation);

                    return User.builder()
                            .build();
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("Erro ao converter xml ", e);
        }
        return null;
    }
}
