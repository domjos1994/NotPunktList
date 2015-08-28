package dominicjoas.dev.notpunktlist.classes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This is the XML-Wrapper-Class
 * @author Dominic Joas
 * @version 0.1
 */
public class clsXML {
    private Document doc;
    private Element root, tmp;
    private List<String> lsErrors = new ArrayList();
    private Map<String, Element> mpElements = new HashMap<>();

    /**
     * The Constructor of the class which implements the DocumentBuilder
     */
    public clsXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.doc = builder.newDocument();
        } catch(ParserConfigurationException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * The Constructor of the class which implements the DocumentBuilder and sets the root
     * @param root The Root-Element of the file
     */
    public clsXML(String root) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.doc = builder.newDocument();
            this.root = doc.createElement(root);
            doc.appendChild(this.root);
            mpElements.put(root, this.root);
        } catch(ParserConfigurationException | DOMException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * The Constructor of the class which opens a File
     * @param file The File to open
     */
    public clsXML(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.doc = builder.parse(file);
        } catch(ParserConfigurationException | SAXException | IOException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * The Constructor of the class which opens a File
     * @param file The File as Stream
     */
    public clsXML(InputStream file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.doc = builder.parse(file);
        } catch(ParserConfigurationException | SAXException | IOException ex) {
            System.out.println(ex.toString());
        }
    }

    public clsXML(URI file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.doc = builder.parse(file.toURL().openStream());
        } catch(ParserConfigurationException | SAXException | IOException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Sets the Root-Element
     * @param root The name of the Root-Element
     */
    public void setRoot(String root) {
        try {
            this.root = doc.createElement(root);
            doc.appendChild(this.root);
            mpElements.put(root, this.root);
        } catch(DOMException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Return the Root-Element
     * @return The name of the Root-Element
     */
    public String getRoot() {
        String root = "";
        try {
            root = this.doc.getDocumentElement().getNodeName();
        } catch(DOMException ex) {
            System.out.println(ex.toString());
        }
        return root;
    }

    /**
     * Returns the Value of an Element
     * @param elementName The name of the Element
     * @return The Value of an Element
     */
    public String getElementValue(String elementName) {
        String value = "";
        try {
            NodeList list = doc.getElementsByTagName(elementName);
            for(int i = 0; i<=list.getLength()-1; i++) {
                value += list.item(i).getTextContent() + ";";
            }
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        return value;
    }

    public void changeElementValue(String elementName, String value) {
        try {
            NodeList list = doc.getElementsByTagName(elementName);
            list.item(0).setTextContent(value);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Return The Attributes of an Element
     * @param elementName The Name of the Element
     * @return The Attributes
     */
    public Map getAttributes(String elementName) {
        Map<String, String> attributes = new HashMap<>();
        try {
            NodeList list = doc.getElementsByTagName(elementName);
            for(int i = 0; i<=list.item(0).getAttributes().getLength()-1; i++) {
                attributes.put(list.item(0).getAttributes().item(i).getNodeName(), list.item(0).getAttributes().item(i).getTextContent());
            }
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        return attributes;
    }

    public List<Map<String, String>> getItemsWithAttributes(String elementName) {
        List<Map<String, String>> attributes = new ArrayList<>();
        try {
            NodeList list = doc.getElementsByTagName(elementName);
            for(int i = 0; i<=list.getLength()-1; i++) {
                Map<String, String> attribute = new HashMap<>();
                for (int j = 0; j <= list.item(i).getAttributes().getLength() - 1; j++) {
                    attribute.put(list.item(i).getAttributes().item(j).getNodeName(), list.item(i).getAttributes().item(j).getTextContent());
                }
                attributes.add(attribute);
            }
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        return attributes;
    }

    /**
     * Adds an Element to XML-File
     * @param name The Name of the Element
     */
    public void addElement(String name) {
        try {
            this.tmp = this.doc.createElement(name);
            this.root.appendChild(this.tmp);
            mpElements.put(name, this.tmp);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Adds an Element to XML-File
     * @param name The Name of the Element
     * @param value The Value of the Element
     */
    public void addElement(String name, String value) {
        try {
            this.tmp = this.doc.createElement(name);
            this.tmp.appendChild(this.doc.createTextNode(value));
            this.root.appendChild(this.tmp);
            mpElements.put(name, this.tmp);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Adds an Element to XML-File
     * @param name The Name of the Element
     * @param attributes The Attributes of the Element
     */
    public void addElement(String name, Map<String, String> attributes) {
        try {
            this.tmp = this.doc.createElement(name);
            this.root.appendChild(this.tmp);
            for(String key : attributes.keySet()) {
                Attr attribute = doc.createAttribute(key);
                attribute.setValue(attributes.get(key));
                this.tmp.setAttributeNode(attribute);
            }
            mpElements.put(name, this.tmp);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Adds an Element to XML-File
     * @param name The Name of the Element 
     * @param value The Value of the Element
     * @param attributes The Attributes of the Element
     */
    public void addElement(String name, String value, Map<String, String> attributes) {
        try {
            this.tmp = this.doc.createElement(name);
            this.tmp.appendChild(this.doc.createTextNode(value));
            this.root.appendChild(this.tmp);
            for(String key : attributes.keySet()) {
                Attr attribute = doc.createAttribute(key);
                attribute.setValue(attributes.get(key));
                this.tmp.setAttributeNode(attribute);
            }
            mpElements.put(name, this.tmp);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Adds an Subelement to an Element of the XML-File
     * @param name The Name of the Element 
     * @param parentNode The name of the parent-Element
     */
    public void addSubElement(String name, String parentNode) {
        try {
            this.tmp = this.doc.createElement(name);
            this.root.appendChild(this.tmp);
            mpElements.put(name, this.tmp);
            mpElements.get(parentNode).appendChild(this.tmp);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Adds an Subelement to an Element of the XML-File
     * @param name The Name of the Element 
     * @param value The Value of the Element
     * @param parentNode The name of the parent-Element
     */
    public void addSubElement(String name, String value, String parentNode) {
        try {
            this.tmp = this.doc.createElement(name);
            this.tmp.appendChild(this.doc.createTextNode(value));
            this.root.appendChild(this.tmp);
            mpElements.put(name, this.tmp);
            mpElements.get(parentNode).appendChild(this.tmp);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Adds an Subelement to an Element of the XML-File
     * @param name The Name of the Element 
     * @param attributes The Attributes of the Element
     * @param parentNode The name of the parent-Element
     */
    public void addSubElement(String name, Map<String, String> attributes, String parentNode) {
        try {
            this.tmp = this.doc.createElement(name);
            this.root.appendChild(this.tmp);
            for(String key : attributes.keySet()) {
                Attr attribute = doc.createAttribute(key);
                attribute.setValue(attributes.get(key));
                this.tmp.setAttributeNode(attribute);
            }
            mpElements.put(name, this.tmp);
            mpElements.get(parentNode).appendChild(this.tmp);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Adds an Subelement to an Element of the XML-File
     * @param name The Name of the Element 
     * @param value The Value of the Element
     * @param attributes The Attributes of the Element
     * @param parentNode The name of the parent-Element
     */
    public void addSubElement(String name, String value, Map<String, String> attributes, String parentNode) {
        try {
            this.tmp = this.doc.createElement(name);
            this.tmp.appendChild(this.doc.createTextNode(value));
            this.root.appendChild(this.tmp);
            for(String key : attributes.keySet()) {
                Attr attribute = doc.createAttribute(key);
                attribute.setValue(attributes.get(key));
                this.tmp.setAttributeNode(attribute);
            }
            mpElements.put(name, this.tmp);
            mpElements.get(parentNode).appendChild(this.tmp);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Adds the current element to The root Element
     * @deprecated this
     */
    public void addToRoot() {
        try {
            this.root.appendChild(this.tmp);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Saves the XML-File to path
     * @param path Path of the XML-File
     */
    public void save(String path) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        } catch(TransformerException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Returns the Error-List
     * @return List of Errors
     */
    public List getErrorList() {
        return this.lsErrors;
    }
}
