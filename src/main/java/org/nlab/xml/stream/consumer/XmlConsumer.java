package org.nlab.xml.stream.consumer;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.nlab.xml.stream.context.StaxContext;
import org.nlab.xml.stream.matcher.EventMatcher;
import org.nlab.xml.stream.matcher.EventMatchers;


public class XmlConsumer {

	private final Stream<StaxContext> stream;

	private final List<Function<StaxContext, Boolean>> consumers = new ArrayList<>();

	public XmlConsumer(Stream<StaxContext> stream) {
		this.stream = Objects.requireNonNull(stream);
	}


	public XmlConsumer matchCss(String query, ConsumeAndContinueConsumer<StaxContext> consumer) {
		addConsumer(EventMatchers.css(query, consumer));
		return this;
	}

	public XmlConsumer matchCss(String query, Function<StaxContext, Boolean> consumer) {
		addConsumer(EventMatchers.css(query, consumer));
		return this;
	}


	public XmlConsumer matchElements(String[] elements, Function<StaxContext, Boolean> consumer) {
		addConsumer(EventMatchers.elements(elements, consumer));
		return this;
	}

	public XmlConsumer matchElements(String[] elements, ConsumeAndContinueConsumer<StaxContext> consumer) {
		addConsumer(EventMatchers.elements(elements, consumer));
		return this;
	}


	public XmlConsumer matchElement(String element, Function<StaxContext, Boolean> consumer) {
		addConsumer(EventMatchers.element(element, consumer));
		return this;
	}

	public XmlConsumer matchElement(String element, ConsumeAndContinueConsumer<StaxContext> consumer) {
		addConsumer(EventMatchers.element(element, consumer));
		return this;
	}


	public XmlConsumer matchAttribute(String attribute, Function<StaxContext, Boolean> consumer) {
		addConsumer(EventMatchers.attribute(attribute, consumer));
		return this;
	}

	public XmlConsumer matchAttribute(String attribute, ConsumeAndContinueConsumer<StaxContext> consumer) {
		addConsumer(EventMatchers.attribute(attribute, consumer));
		return this;
	}

	public XmlConsumer matchAttributeValue(String attribute, String value, Function<StaxContext, Boolean> consumer) {
		addConsumer(EventMatchers.attributeValue(attribute, value, consumer));
		return this;
	}

	public XmlConsumer matchAttributeValue(String attribute, String value, ConsumeAndContinueConsumer<StaxContext> consumer) {
		addConsumer(EventMatchers.attributeValue(attribute, value, consumer));
		return this;
	}


	public XmlConsumer match(Predicate<StaxContext> predicate, Function<StaxContext, Boolean> consumer) {
		addConsumer(new EventMatcher(predicate, consumer));
		return this;
	}

	public XmlConsumer match(Predicate<StaxContext> predicate, ConsumeAndContinueConsumer<StaxContext> consumer) {
		addConsumer(new EventMatcher(predicate, consumer));
		return this;
	}


	public XmlConsumer addConsumer(Function<StaxContext, Boolean> consumer) {
		consumers.add(consumer);
		return this;
	}


	/**
	 * Consume the Stream
	 * @throws XMLStreamException
	 */
	public void consume() throws XMLStreamException {
		try (Stream<StaxContext> stream = this.stream){
			stream.forEach(c -> {
				for (Function<StaxContext, Boolean> consumer : consumers) {
					if (!consumer.apply(c)) {
						break;
					}
				}
			});
		}
	}
}
