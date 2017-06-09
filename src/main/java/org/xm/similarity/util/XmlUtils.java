package org.xm.similarity.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Encapsulating XML common operations.
 */
public final class XmlUtils {

    private static final String XMLNS_XSI = "xmlns:xsi";
    private static final String XSI_SCHEMA_LOCATION = "xsi:schemaLocation";
    private static final String LOGIC_YES = "yes";
    private static final String DEFAULT_ENCODE = "UTF-8";
    private static final String REG_INVALID_CHARS = "&#\\d+;";

    /**
     * Creates a new document instance.
     *
     * @return a new document instance
     * @throws XmlException problem creating a new document
     */
    public static Document newDocument() throws XmlException {
        Document doc = null;

        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new XmlException(e);
        }

        return doc;
    }

    /**
     * Parses the content of the given XML file as an XML document.
     *
     * @param file the XML file instance
     * @return the document instance representing the entire XML document
     * @throws XmlException problem parsing the XML file
     */
    public static Document getDocument(File file) throws XmlException {
        InputStream in = getInputStream(file);
        return getDocument(in);
    }

    /**
     * Parses the content of the given stream as an XML document.
     *
     * @param in the XML file input stream
     * @return the document instance representing the entire XML document
     * @throws XmlException problem parsing the XML input stream
     */
    public static Document getDocument(InputStream in) throws XmlException {
        Document doc = null;

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(in);
        } catch (ParserConfigurationException e) {
            throw new XmlException(e);
        } catch (SAXException e) {
            throw new XmlException(XmlException.XML_PARSE_ERROR, e);
        } catch (IOException e) {
            throw new XmlException(XmlException.XML_READ_ERROR, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO
                }
            }
        }

        return doc;
    }

    /**
     * Creates a root element as well as a new document with specific tag name.
     *
     * @param tagName the name of the root element
     * @return a new element instance
     * @throws XmlException problem generating a new document
     */
    public static Element createRootElement(String tagName) throws XmlException {
        Document doc = newDocument();
        Element root = doc.createElement(tagName);
        doc.appendChild(root);
        return root;
    }

    /**
     * Gets the root element from input stream.
     *
     * @param in the XML file input stream
     * @return the root element of parsed document
     * @throws XmlException problem parsing the XML file input stream
     */
    public static Element getRootElementFromStream(InputStream in) throws XmlException {
        return getDocument(in).getDocumentElement();
    }

    /**
     * Gets the root element from given XML file.
     *
     * @param fileName the name of the XML file
     * @return the root element of parsed document
     * @throws XmlException problem parsing the XML file
     */
    public static Element getRootElementFromFile(File file) throws XmlException {
        return getDocument(file).getDocumentElement();
    }

    /**
     * Gets the root element from the given XML payload.
     *
     * @param payload the XML payload representing the XML file.
     * @return the root element of parsed document
     * @throws XmlException problem parsing the XML payload
     */
    public static Element getRootElementFromString(String payload) throws XmlException {
        if (payload == null || payload.trim().length() < 1) {
            throw new XmlException(XmlException.XML_PAYLOAD_EMPTY);
        }

        byte[] bytes = null;

        try {
            bytes = payload.getBytes(DEFAULT_ENCODE);
        } catch (UnsupportedEncodingException e) {
            throw new XmlException(XmlException.XML_ENCODE_ERROR, e);
        }

        InputStream in = new ByteArrayInputStream(bytes);
        return getDocument(in).getDocumentElement();
    }

    /**
     * Gets the descendant elements list from the parent element.
     *
     * @param parent  the parent element in the element tree
     * @param tagName the specified tag name
     * @return the NOT NULL descendant elements list
     */
    public static List<Element> getElements(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        List<Element> elements = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                elements.add((Element) node);
            }
        }

        return elements;
    }

    /**
     * Gets the immediately descendant element from the parent element.
     *
     * @param parent  the parent element in the element tree
     * @param tagName the specified tag name.
     * @return immediately descendant element of parent element, NULL otherwise.
     */
    public static Element getElement(Element parent, String tagName) {
        List<Element> children = getElements(parent, tagName);

        if (children.isEmpty()) {
            return null;
        } else {
            return children.get(0);
        }
    }

    /**
     * Gets the immediately child elements list from the parent element.
     *
     * @param parent  the parent element in the element tree
     * @param tagName the specified tag name
     * @return the NOT NULL immediately child elements list
     */
    public static List<Element> getChildElements(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        List<Element> elements = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element && node.getParentNode() == parent) {
                elements.add((Element) node);
            }
        }

        return elements;
    }

    /**
     * Gets the immediately child element from the parent element.
     *
     * @param parent  the parent element in the element tree
     * @param tagName the specified tag name
     * @return immediately child element of parent element, NULL otherwise
     */
    public static Element getChildElement(Element parent, String tagName) {
        List<Element> children = getChildElements(parent, tagName);

        if (children.isEmpty()) {
            return null;
        } else {
            return children.get(0);
        }
    }

    /**
     * Gets the value of the child element by tag name under the given parent
     * element. If there is more than one child element, return the value of the
     * first one.
     *
     * @param parent  the parent element
     * @param tagName the tag name of the child element
     * @return value of the first child element, NULL if tag not exists
     */
    public static String getElementValue(Element parent, String tagName) {
        String value = null;

        Element element = getElement(parent, tagName);
        if (element != null) {
            value = element.getTextContent();
        }

        return value;
    }

    /**
     * Appends the child element to the parent element.
     *
     * @param parent  the parent element
     * @param tagName the child element name
     * @return the child element added to the parent element
     */
    public static Element appendElement(Element parent, String tagName) {
        Element child = parent.getOwnerDocument().createElement(tagName);
        parent.appendChild(child);
        return child;
    }

    /**
     * Appends the child element as well as value to the parent element.
     *
     * @param parent  the parent element
     * @param tagName the child element name
     * @param value   the child element value
     * @return the child element added to the parent element
     */
    public static Element appendElement(Element parent, String tagName, String value) {
        Element child = appendElement(parent, tagName);
        child.setTextContent(value);
        return child;
    }

    /**
     * Appends another element as a child element.
     *
     * @param parent the parent element
     * @param child  the child element to append
     */
    public static void appendElement(Element parent, Element child) {
        Node tmp = parent.getOwnerDocument().importNode(child, true);
        parent.appendChild(tmp);
    }

    /**
     * Appends the CDATA element to the parent element.
     *
     * @param parent  the parent element
     * @param tagName the CDATA element name
     * @param value   the CDATA element value
     * @return the CDATA element added to the parent element
     */
    public static Element appendCDATAElement(Element parent, String tagName, String value) {
        Element child = appendElement(parent, tagName);
        if (value == null) { // avoid "null" word in the XML payload
            value = "";
        }

        Node cdata = child.getOwnerDocument().createCDATASection(value);
        child.appendChild(cdata);
        return child;
    }

    /**
     * Converts the Node/Element instance to XML payload.
     *
     * @param node the node/element instance to convert
     * @return the XML payload representing the node/element
     * @throws XmlException problem converting XML to string
     */
    public static String childNodeToString(Node node) throws XmlException {
        String payload = null;

        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();

            Properties props = tf.getOutputProperties();
            props.setProperty(OutputKeys.OMIT_XML_DECLARATION, LOGIC_YES);
            tf.setOutputProperties(props);

            StringWriter writer = new StringWriter();
            tf.transform(new DOMSource(node), new StreamResult(writer));
            payload = writer.toString();
            payload = payload.replaceAll(REG_INVALID_CHARS, " ");
        } catch (TransformerException e) {
            throw new XmlException(XmlException.XML_TRANSFORM_ERROR, e);
        }

        return payload;
    }

    /**
     * Converts the Node/Document/Element instance to XML payload.
     *
     * @param node the node/document/element instance to convert
     * @return the XML payload representing the node/document/element
     * @throws XmlException problem converting XML to string
     */
    public static String nodeToString(Node node) throws XmlException {
        String payload = null;

        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();

            Properties props = tf.getOutputProperties();
            props.setProperty(OutputKeys.INDENT, LOGIC_YES);
            props.setProperty(OutputKeys.ENCODING, DEFAULT_ENCODE);
            tf.setOutputProperties(props);

            StringWriter writer = new StringWriter();
            tf.transform(new DOMSource(node), new StreamResult(writer));
            payload = writer.toString();
            payload = payload.replaceAll(REG_INVALID_CHARS, " ");
        } catch (TransformerException e) {
            throw new XmlException(XmlException.XML_TRANSFORM_ERROR, e);
        }

        return payload;
    }

    /**
     * Converts the an XML file to XML payload.
     *
     * @param file the XML file instance
     * @return the XML payload representing the XML file
     * @throws XmlException problem transforming XML to string
     */
    public static String xmlToString(File file) throws XmlException {
        Element root = getRootElementFromFile(file);
        return nodeToString(root);
    }

    /**
     * Converts the an XML file input stream to XML payload.
     *
     * @param in the XML file input stream
     * @return the payload represents the XML file
     * @throws XmlException problem transforming XML to string
     */
    public static String xmlToString(InputStream in) throws XmlException {
        Element root = getRootElementFromStream(in);
        return nodeToString(root);
    }

    /**
     * Saves the node/document/element as XML file.
     *
     * @param doc  the XML node/document/element to save
     * @param file the XML file to save
     * @throws XmlException problem persisting XML file
     */
    public static void saveToXml(Node doc, File file) throws XmlException {
        OutputStream out = null;

        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();

            Properties props = tf.getOutputProperties();
            props.setProperty(OutputKeys.METHOD, XMLConstants.XML_NS_PREFIX);
            props.setProperty(OutputKeys.INDENT, LOGIC_YES);
            tf.setOutputProperties(props);

            DOMSource dom = new DOMSource(doc);
            out = getOutputStream(file);
            Result result = new StreamResult(out);
            tf.transform(dom, result);
        } catch (TransformerException e) {
            throw new XmlException(XmlException.XML_TRANSFORM_ERROR, e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }
    }

    /**
     * Validates the element tree context via given XML schema file.
     *
     * @param doc        the XML document to validate
     * @param schemaFile the XML schema file instance
     * @throws XmlException error occurs if the schema file not exists
     */
    public static void validateXml(Node doc, File schemaFile) throws XmlException {
        validateXml(doc, getInputStream(schemaFile));
    }

    /**
     * Validates the element tree context via given XML schema file.
     *
     * @param doc          the XML document to validate
     * @param schemaStream the XML schema file input stream
     * @throws XmlException error occurs if validation fail
     */
    public static void validateXml(Node doc, InputStream schemaStream) throws XmlException {
        try {
            Source source = new StreamSource(schemaStream);
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(source);

            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(doc));
        } catch (SAXException e) {
            throw new XmlException(XmlException.XML_VALIDATE_ERROR, e);
        } catch (IOException e) {
            throw new XmlException(XmlException.XML_READ_ERROR, e);
        } finally {
            if (schemaStream != null) {
                try {
                    schemaStream.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }
    }

    /**
     * Transforms the XML content to XHTML/HTML format string with the XSL.
     *
     * @param payload  the XML payload to convert
     * @param xsltFile the XML stylesheet file
     * @return the transformed XHTML/HTML format string
     * @throws XmlException problem converting XML to HTML
     */
    public static String xmlToHtml(String payload, File xsltFile) throws XmlException {
        String result = null;

        try {
            Source template = new StreamSource(xsltFile);
            Transformer transformer = TransformerFactory.newInstance().newTransformer(template);

            Properties props = transformer.getOutputProperties();
            props.setProperty(OutputKeys.OMIT_XML_DECLARATION, LOGIC_YES);
            transformer.setOutputProperties(props);

            StreamSource source = new StreamSource(new StringReader(payload));
            StreamResult sr = new StreamResult(new StringWriter());
            transformer.transform(source, sr);

            result = sr.getWriter().toString();
        } catch (TransformerException e) {
            throw new XmlException(XmlException.XML_TRANSFORM_ERROR, e);
        }

        return result;
    }

    /**
     * Sets the namespace to specific element.
     *
     * @param element        the element to set
     * @param namespace      the namespace to set
     * @param schemaLocation the XML schema file location URI
     */
    public static void setNamespace(Element element, String namespace, String schemaLocation) {
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE, namespace);
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLNS_XSI,
                XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        element.setAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, XSI_SCHEMA_LOCATION, schemaLocation);
    }

    /**
     * Encode the XML payload to legality character.
     *
     * @param payload the XML payload to encode
     * @return the encoded XML payload
     * @throws XmlException problem encoding the XML payload
     */
    public static String encodeXml(String payload) throws XmlException {
        Element root = createRootElement(XMLConstants.XML_NS_PREFIX);
        root.setTextContent(payload);
        return childNodeToString(root.getFirstChild());
    }

    private static InputStream getInputStream(File file) throws XmlException {
        InputStream in = null;

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new XmlException(XmlException.FILE_NOT_FOUND, e);
        }

        return in;
    }

    private static OutputStream getOutputStream(File file) throws XmlException {
        OutputStream in = null;

        try {
            in = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new XmlException(XmlException.FILE_NOT_FOUND, e);
        }

        return in;
    }

}
