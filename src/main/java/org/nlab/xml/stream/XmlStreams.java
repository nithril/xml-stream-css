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
import org.nlab.xml.stream.context.StaxContext;
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
	 * @param file
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public static Stream<StaxContext> stream(Path file) throws XMLStreamException, IOException {
		InputStream inputStream = null;
		XMLStreamReader streamReader = null;
		try {
			inputStream = Files.newInputStream(file);
			streamReader = StaxCachedFactory.getInputFactory().createXMLStreamReader(inputStream);
			return createStreamAndClose(inputStream, streamReader);
		} catch (Exception e) {
			IoCloser.ioCloser().close(streamReader).close(inputStream);
			throw e;
		}
	}

	/**
	 * Create a Stream from a path
	 * As the stream parses an InputStream, the stream must be used in a try-with-resource statement
	 * @param path
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public static Stream<StaxContext> stream(String path) throws XMLStreamException, IOException {
		return stream(Paths.get(path));
	}


	/**
	 * Create a Stream from an InputStream.
	 * The InputStream will be closed if the stream is used in a try-with-resource statement
	 * @param is
	 * @return
	 * @throws XMLStreamException
	 */
	public static Stream<StaxContext> streamAndClose(InputStream is) throws XMLStreamException {
		XMLStreamReader streamReader = null;
		try {
			streamReader = StaxCachedFactory.getInputFactory().createXMLStreamReader(requireNonNull(is));
			return createStreamAndClose(is, streamReader);
		} catch (Exception e) {
			IoCloser.ioCloser().close(streamReader).close(is);
			throw e;
		}
	}

	/**
	 * Create a Stream from an XMLStreamReader.
	 * The XMLStreamReader will be closed if the stream is used in a try-with-resource statement
	 * @param reader
	 * @return
	 * @throws XMLStreamException
	 */
	public static Stream<StaxContext> streamAndClose(XMLStreamReader reader) throws XMLStreamException {
		return createStreamAndClose(requireNonNull(reader));
	}


	/**
	 * Create an XmlConsumer from a path
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public static XmlConsumer newConsumer(String path) throws IOException, XMLStreamException {
		return new XmlConsumer(XmlStreams.stream(path));
	}

	/**
	 * Create an XmlConsumer from a path
	 * @param path
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public static XmlConsumer newConsumer(Path path) throws XMLStreamException, IOException {
		return new XmlConsumer(XmlStreams.stream(path));
	}

	/**
	 * Create an XmlConsumer from an XMLStreamReader
	 * The XMLStreamReader is closed once the consumer finished succesfully or with error
	 * @param xmlStreamReader
	 * @return
	 * @throws XMLStreamException
	 */
	public static XmlConsumer newConsumerAndClose(XMLStreamReader xmlStreamReader) throws XMLStreamException {
		return new XmlConsumer(XmlStreams.streamAndClose(xmlStreamReader));
	}

	/**
	 * Create an XmlConsumer from an InputStream
	 * The InputStream is closed once the consumer finished succesfully or with error
	 * @param inputStream
	 * @return
	 * @throws XMLStreamException
	 */
	public static XmlConsumer newConsumerAndClose(InputStream inputStream) throws XMLStreamException {
		return new XmlConsumer(XmlStreams.streamAndClose(inputStream));
	}



	private static Stream<StaxContext> createStreamAndClose(InputStream is, XMLStreamReader reader) {
		try {
			XmlMatcherStreamReader xmlMatcherStreamReader = new XmlMatcherStreamReader(reader);
			return StreamSupport.stream(new XmlStreamReaderSpliterator(xmlMatcherStreamReader), false)
					.onClose(() -> IoCloser.ioCloser().close(reader).close(is));

		} catch (Exception e) {
			IoCloser.ioCloser().close(reader).close(is);
			throw e;
		}
	}


	private static Stream<StaxContext> createStreamAndClose(XMLStreamReader reader) {
		try {
			XmlMatcherStreamReader xmlMatcherStreamReader = new XmlMatcherStreamReader(reader);
			return StreamSupport.stream(new XmlStreamReaderSpliterator(xmlMatcherStreamReader), false)
					.onClose(() -> IoCloser.ioCloser().close(reader));

		} catch (Exception e) {
			IoCloser.ioCloser().close(reader);
			throw e;
		}
	}
}
