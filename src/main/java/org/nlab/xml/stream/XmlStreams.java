package org.nlab.xml.stream;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.nlab.util.IoCloser;
import org.nlab.xml.stream.consumer.XmlConsumer;
import org.nlab.xml.stream.context.StreamContext;
import org.nlab.xml.stream.factory.StaxCachedFactory;
import org.nlab.xml.stream.reader.XmlMatcherStreamReader;
import org.nlab.xml.stream.reader.XmlStreamReaderSpliterator;

import static java.util.Objects.requireNonNull;

/**
 * Created by nlabrot on 08/12/15.
 */
public final class XmlStreams {

	private XmlStreams() {
	}

	/**
	 * Create a Stream from a path
	 * As the stream parses an InputStream, the stream must be embed in a try-with-resource statement
	 * @param path
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public static XmlStream stream(Path path) throws XMLStreamException, IOException {
		return XmlStreamSpec.with(path).stream();
	}

	/**
	 * Create a Stream from a path
	 * As the stream parses an InputStream, the stream must be used in a try-with-resource statement
	 * @param path
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public static XmlStream stream(String path) throws XMLStreamException, IOException {
		return XmlStreamSpec.with(path).stream();
	}


	/**
	 * Create a Stream from an InputStream.
	 * The InputStream will be closed if the stream is used in a try-with-resource statement
	 * @param is
	 * @return
	 * @throws XMLStreamException
	 */
	public static XmlStream streamAndClose(InputStream is) throws XMLStreamException, IOException {
		return XmlStreamSpec.with(is).closeOnFinish().stream();
	}

	/**
	 * Create a Stream from an XMLStreamReader.
	 * The XMLStreamReader will be closed if the stream is used in a try-with-resource statement
	 * @param reader
	 * @return
	 * @throws XMLStreamException
	 */
	public static XmlStream streamAndClose(XMLStreamReader reader) throws XMLStreamException, IOException {
		return XmlStreamSpec.with(reader).closeOnFinish().stream();
	}


	/**
	 * Create an XmlConsumer from a path
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public static XmlConsumer newConsumer(String path) throws IOException, XMLStreamException {
		return XmlStreamSpec.with(path).consume();
	}

	/**
	 * Create an XmlConsumer from a path
	 * @param path
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public static XmlConsumer newConsumer(Path path) throws XMLStreamException, IOException {
		return XmlStreamSpec.with(path).consume();
	}

	/**
	 * Create an XmlConsumer from an XMLStreamReader
	 * The XMLStreamReader is closed once the consumer finished succesfully or with error
	 * @param xmlStreamReader
	 * @return
	 * @throws XMLStreamException
	 */
	public static XmlConsumer newConsumerAndClose(XMLStreamReader xmlStreamReader) throws XMLStreamException, IOException {
		return XmlStreamSpec.with(xmlStreamReader).closeOnFinish().consume();
	}

	/**
	 * Create an XmlConsumer from an InputStream
	 * The InputStream is closed once the consumer finished succesfully or with error
	 * @param inputStream
	 * @return
	 * @throws XMLStreamException
	 */
	public static XmlConsumer newConsumerAndClose(InputStream inputStream) throws XMLStreamException, IOException {
		return XmlStreamSpec.with(inputStream).closeOnFinish().consume();
	}


}
