package org.nlab.xml;

import org.nlab.xml.context.StaxContext;
import org.nlab.xml.factory.StaxCachedFactory;
import org.nlab.xml.matcher.EventMatcher;
import org.nlab.xml.matcher.EventMatchers;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.nlab.util.IoCloser.ioCloser;


public class XmlMatcherConsumer {

    private final boolean closeStream;
    private final InputStream inputStream;
    private final XmlMatcherStreamReader streamReader;


    public XmlMatcherConsumer(XMLStreamReader streamReader) {
        this.closeStream = false;
        this.inputStream = null;
        this.streamReader = new XmlMatcherStreamReader(streamReader);
    }


    public XmlMatcherConsumer(InputStream inputStream, boolean closeStream) {
        this.closeStream = closeStream;
        this.inputStream = inputStream;
        try {
            this.streamReader = new XmlMatcherStreamReader(StaxCachedFactory.getInputFactory().createXMLStreamReader(inputStream));
        } catch (XMLStreamException e) {
            throw new RuntimeException();
        }
    }


    public XmlMatcherConsumer matchCss(String query, ConsumeAndContinueConsumer<StaxContext> consumer) {
        streamReader.addConsumer(EventMatchers.css(query, consumer));
        return this;
    }

    public XmlMatcherConsumer matchCss(String query, Function<StaxContext, Boolean> consumer) {
        streamReader.addConsumer(EventMatchers.css(query, consumer));
        return this;
    }


    public XmlMatcherConsumer matchElements(String[] elements, Function<StaxContext, Boolean> consumer) {
        streamReader.addConsumer(EventMatchers.elements(elements, consumer));
        return this;
    }

    public XmlMatcherConsumer matchElements(String[] elements, ConsumeAndContinueConsumer<StaxContext> consumer) {
        streamReader.addConsumer(EventMatchers.elements(elements, consumer));
        return this;
    }


    public XmlMatcherConsumer matchElement(String element, Function<StaxContext, Boolean> consumer) {
        streamReader.addConsumer(EventMatchers.element(element, consumer));
        return this;
    }

    public XmlMatcherConsumer matchElement(String element, ConsumeAndContinueConsumer<StaxContext> consumer) {
        streamReader.addConsumer(EventMatchers.element(element, consumer));
        return this;
    }


    public XmlMatcherConsumer matchAttribute(String attribute, Function<StaxContext, Boolean> consumer) {
        streamReader.addConsumer(EventMatchers.attribute(attribute, consumer));
        return this;
    }

    public XmlMatcherConsumer matchAttribute(String attribute, ConsumeAndContinueConsumer<StaxContext> consumer) {
        streamReader.addConsumer(EventMatchers.attribute(attribute, consumer));
        return this;
    }

    public XmlMatcherConsumer matchAttributeValue(String attribute, String value, Function<StaxContext, Boolean> consumer) {
        streamReader.addConsumer(EventMatchers.attributeValue(attribute, value, consumer));
        return this;
    }

    public XmlMatcherConsumer matchAttributeValue(String attribute, String value, ConsumeAndContinueConsumer<StaxContext> consumer) {
        streamReader.addConsumer(EventMatchers.attributeValue(attribute, value, consumer));
        return this;
    }


    public XmlMatcherConsumer match(Predicate<StaxContext> predicate, Function<StaxContext, Boolean> consumer) {
        streamReader.addConsumer(new EventMatcher(predicate, consumer));
        return this;
    }

    public XmlMatcherConsumer match(Predicate<StaxContext> predicate, ConsumeAndContinueConsumer<StaxContext> consumer) {
        streamReader.addConsumer(new EventMatcher(predicate, consumer));
        return this;
    }


    public void consume() {
        try {
            streamReader.processEvent(streamReader.getEventType());
            while (streamReader.hasNext()) {
                streamReader.next();
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } finally {
            if (closeStream) {
                ioCloser().close(streamReader).close(inputStream);
            }
        }
    }


    public static XmlMatcherConsumer newConsume(XMLStreamReader xmlStreamReader) {
        return new XmlMatcherConsumer(xmlStreamReader);
    }

    public static XmlMatcherConsumer newConsume(InputStream inputStream) {
        return new XmlMatcherConsumer(inputStream, false);
    }

    public static XmlMatcherConsumer newConsumeAndClose(InputStream inputStream) {
        return new XmlMatcherConsumer(inputStream, true);
    }

}
