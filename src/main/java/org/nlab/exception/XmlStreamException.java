package org.nlab.exception;

/**
 * Created by nlabrot on 14/03/16.
 */
public class XmlStreamException extends Exception {
    public XmlStreamException() {
    }

    public XmlStreamException(String message) {
        super(message);
    }

    public XmlStreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlStreamException(Throwable cause) {
        super(cause);
    }

    public XmlStreamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
