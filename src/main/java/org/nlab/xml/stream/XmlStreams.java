package org.nlab.xml.stream;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jooq.lambda.Unchecked;
import org.jooq.lambda.fi.util.function.CheckedConsumer;
import org.jooq.lambda.fi.util.function.CheckedSupplier;
import org.nlab.xml.stream.consumer.XmlConsumer;

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
     *
     * @param path path
     * @return XmlStream
     * @throws XMLStreamException
     * @throws IOException
     */
    public static XmlStream stream(Path path) throws Exception {
        return XmlStreamSpec.with(path).stream();
    }


    /**
     * Create a Stream from a path
     * As the stream parses an InputStream, the stream must be used in a try-with-resource statement
     *
     * @param path path
     * @return XmlStream
     * @throws XMLStreamException
     * @throws IOException
     */
    public static XmlStream stream(String path) throws Exception {
        return XmlStreamSpec.with(path).stream();
    }

    public static CheckedSupplier<XmlStream> sstream(String path) {
        return () -> XmlStreamSpec.with(path).stream();
    }


    /**
     * Create a Stream from an InputStream.
     * The InputStream will be closed if the stream is used in a try-with-resource statement
     *
     * @param is InputStream
     * @return XmlStream
     * @throws XMLStreamException
     */
    public static XmlStream streamAndClose(InputStream is) throws Exception {
        return XmlStreamSpec.with(is).closeOnFinish().stream();
    }

    public static CheckedSupplier<XmlStream> sstreamAndClose(InputStream is) throws IOException, XMLStreamException {
        return XmlStreamSpec.with(is).closeOnFinish().sstream();
    }

    /**
     * Create a Stream from an InputStream.
     * The InputStream will be closed if the stream is used in a try-with-resource statement
     *
     * @param is InputStream
     * @return XmlStream
     * @throws XMLStreamException
     */
    public static XmlStream stream(InputStream is) throws Exception {
        return XmlStreamSpec.with(is).stream();
    }

    public static CheckedSupplier<XmlStream> sstream(InputStream is) throws IOException, XMLStreamException {
        return XmlStreamSpec.with(is).sstream();
    }

    /**
     * Create a Stream from an XMLStreamReader.
     * The XMLStreamReader will be closed if the stream is used in a try-with-resource statement
     *
     * @param reader XMLStreamReader
     * @return XmlStream
     * @throws XMLStreamException
     */
    public static XmlStream streamAndClose(XMLStreamReader reader) throws Exception {
        return XmlStreamSpec.with(reader).closeOnFinish().stream();
    }

    public static CheckedSupplier<XmlStream> sstreamAndClose(XMLStreamReader reader) throws IOException, XMLStreamException {
        return XmlStreamSpec.with(reader).closeOnFinish().sstream();
    }

    /**
     * Create a Stream from an XMLStreamReader.
     *
     * @param reader XMLStreamReader
     * @return XmlStream
     * @throws XMLStreamException
     */
    public static XmlStream stream(XMLStreamReader reader) throws Exception {
        return XmlStreamSpec.with(reader).stream();
    }

    public static CheckedSupplier<XmlStream> sstream(XMLStreamReader reader) throws IOException, XMLStreamException {
        return XmlStreamSpec.with(reader).sstream();
    }




    /**
     * Create an XmlConsumer from a path
     *
     * @param path path
     * @return XmlStream
     * @throws IOException
     * @throws XMLStreamException
     */
    public static XmlConsumer newConsumer(String path) throws Exception {
        return XmlStreamSpec.with(path).consumer();
    }

    /**
     * Create an XmlConsumer from a path
     *
     * @param path path
     * @return XmlStream
     * @throws XMLStreamException
     * @throws IOException
     */
    public static XmlConsumer newConsumer(Path path) throws Exception {
        return XmlStreamSpec.with(path).consumer();
    }

    /**
     * Create an XmlConsumer from an XMLStreamReader
     * The XMLStreamReader is closed once the consumer finished succesfully or with error
     *
     * @param xmlStreamReader
     * @return XmlStream
     * @throws XMLStreamException
     */
    public static XmlConsumer newConsumerAndClose(XMLStreamReader xmlStreamReader) throws Exception {
        return XmlStreamSpec.with(xmlStreamReader).closeOnFinish().consumer();
    }

    /**
     * Create an XmlConsumer from an InputStream
     * The InputStream is closed once the consumer finished succesfully or with error
     *
     * @param inputStream
     * @return XmlStream
     * @throws XMLStreamException
     */
    public static XmlConsumer newConsumerAndClose(InputStream inputStream) throws Exception {
        return XmlStreamSpec.with(inputStream).closeOnFinish().consumer();
    }


}
