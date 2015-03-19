package org.nlab.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.Writer;

/**
 * Created by nlabrot on 16/03/15.
 */
public class Transformers {

    private XmlMatcherStreamReader streamReader;

    public org.w3c.dom.Document toDom() throws TransformerException, ParserConfigurationException {
        streamReader.setPassthrough(true);
        org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        transform(new StAXSource(streamReader), new DOMResult(document));
        return document;
    }

    public void toWriter(Writer writer) throws TransformerException {
        streamReader.setPassthrough(true);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        transform(new StAXSource(streamReader),new StreamResult(writer));
    }

    private void transform(Source source , Result result) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.transform(source, result);
    }
}
