package org.xm.similarity.util;

/**
 * Runtime exception for XML handling.
 */
public class XmlException extends RuntimeException {

    private static final long serialVersionUID = 381260478228427716L;

    public static final String XML_PAYLOAD_EMPTY = "xml.payload.empty";
    public static final String XML_ENCODE_ERROR = "xml.encoding.invalid";
    public static final String FILE_NOT_FOUND = "xml.file.not.found";
    public static final String XML_PARSE_ERROR = "xml.parse.error";
    public static final String XML_READ_ERROR = "xml.read.error";
    public static final String XML_VALIDATE_ERROR = "xml.validate.error";
    public static final String XML_TRANSFORM_ERROR = "xml.transform.error";

    public XmlException() {
        super();
    }

    public XmlException(String key, Throwable cause) {
        super(key, cause);
    }

    public XmlException(String key) {
        super(key);
    }

    public XmlException(Throwable cause) {
        super(cause);
    }

}