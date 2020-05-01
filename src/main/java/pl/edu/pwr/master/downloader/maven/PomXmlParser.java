package pl.edu.pwr.master.downloader.maven;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PomXmlParser {

    private static final Logger LOGGER = Logger.getLogger(PomXmlParser.class.getName());
    private static final String MAVEN_DEPENDENCY_PLUGIN_NAME = "maven-dependency-plugin";
    private static final String BACKUP_POM_PATH_SUFFIX = ".javametrics";

    public boolean parsePom(String pomPath, String dependencyPath) throws ParserConfigurationException, IOException, SAXException {
        String newPath = pomPath + BACKUP_POM_PATH_SUFFIX;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new File(pomPath));

        NodeList nodes = document.getElementsByTagName("plugins");
        Element element = (Element) nodes.item(0);

        NodeList plugins = element.getElementsByTagName("plugin");
        for (int i = 0; i < plugins.getLength(); i++) {
            Element e = (Element) plugins.item(i);
            String artifactId = ((Element) e.getElementsByTagName("artifactId").item(0)).getFirstChild().getTextContent();
            if (artifactId.equals(MAVEN_DEPENDENCY_PLUGIN_NAME)) {
                NodeList configuration = e.getElementsByTagName("configuration");
                if (configuration.getLength() > 0) {
                    NodeList outputDirectory = ((Element) configuration.item(0)).getElementsByTagName("outputDirectory");
                    if (outputDirectory.getLength() > 0) {
                        Node outputDirectoryNode = ((Element) outputDirectory.item(0)).getFirstChild();
                        String outputDirectoryPath = outputDirectoryNode.getTextContent();

                        try {
                            Files.copy(Paths.get(pomPath), Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);

                            LOGGER.info("Found " + artifactId + " with outputDirectory node: " + outputDirectoryPath);
                            outputDirectoryNode.setTextContent(dependencyPath);

                            writeXml(pomPath, document);
                            LOGGER.info("Replaced outputDirectory of " + artifactId + " to: " + newPath);
                            return true;
                        }
                        catch (Exception ex) {
                            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                }
            }
        }
        return false;
    }

    public void getPomFromBackup(String pomPath) throws IOException {
        File f = new File(pomPath + BACKUP_POM_PATH_SUFFIX);
        if (f.exists()) {
            Files.copy(Paths.get(pomPath + BACKUP_POM_PATH_SUFFIX), Paths.get(pomPath), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void writeXml(String path, Document xml) throws TransformerConfigurationException, FileNotFoundException, TransformerException {
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.transform(new DOMSource(xml),
                new StreamResult(new FileOutputStream(path)));
    }
}
