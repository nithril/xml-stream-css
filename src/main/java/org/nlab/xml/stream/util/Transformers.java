package org.nlab.xml.stream.util;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;

import org.codehaus.staxmate.dom.DOMConverter;
import org.nlab.exception.UncheckedExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Created by nlabrot on 16/03/15.
 */
public final class Transformers {

	private Transformers() {
	}

	public static Document toDom(XMLStreamReader streamReader) throws UncheckedExecutionException {
		try {
			DOMConverter domConverter = new DOMConverter();
			return domConverter.buildDocument(streamReader);
		} catch (Exception e) {
			throw new UncheckedExecutionException(e);
		}
	}


	public static void toWriter(XMLStreamReader streamReader, Writer writer) throws UncheckedExecutionException {
		try {
			transform(new StAXSource(streamReader), new StreamResult(writer));
		} catch (TransformerException e) {
			throw new UncheckedExecutionException(e);
		}
	}

	public static void toWriter(Node node, Writer writer) throws UncheckedExecutionException {
		try {
			transform(new DOMSource(node), new StreamResult(writer));
		} catch (TransformerException e) {
			throw new UncheckedExecutionException(e);
		}
	}

	public static String toXml(Node node) throws UncheckedExecutionException {
		try {
			StringWriter writer = new StringWriter();
			transform(new DOMSource(node), new StreamResult(writer));
			return writer.toString();
		} catch (TransformerException e) {
			throw new UncheckedExecutionException(e);
		}
	}

	public static String toXml(XMLStreamReader reader) {
		StringWriter writer = new StringWriter();
		toWriter(reader, writer);
		return writer.toString();
	}

	public static void transform(Source source, Result result) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.transform(source, result);
	}


	public static String getElementText(XMLStreamReader reader) {
		try {
			return reader.getElementText();
		} catch (XMLStreamException e) {
			throw new UncheckedExecutionException(e);
		}
	}

}
