package org.nlab.xml.stream.consumer;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jooq.lambda.fi.util.function.CheckedFunction;
import org.nlab.exception.UncheckedExecutionException;
import org.nlab.xml.stream.XmlStream;
import org.nlab.xml.stream.context.StreamContext;
import org.nlab.xml.stream.matcher.EventMatcher;
import org.nlab.xml.stream.matcher.EventMatchers;
import org.nlab.xml.stream.predicate.XmlPredicate;


public class XmlConsumer {

	private final XmlStream stream;

	private final List<CheckedFunction<StreamContext, Boolean>> consumers = new ArrayList<>();

	public XmlConsumer(XmlStream stream) {
		this.stream = Objects.requireNonNull(stream);
	}


	public XmlConsumer matchCss(String query, CheckedConsumeAndContinueConsumer<StreamContext> consumer) {
		addConsumer(EventMatchers.css(query, consumer));
		return this;
	}

	public XmlConsumer matchCss(String query, CheckedFunction<StreamContext, Boolean> consumer) {
		addConsumer(EventMatchers.css(query, consumer));
		return this;
	}


	public XmlConsumer matchElements(String[] elements, CheckedFunction<StreamContext, Boolean> consumer) {
		addConsumer(EventMatchers.elements(elements, consumer));
		return this;
	}

	public XmlConsumer matchElements(String[] elements, CheckedConsumeAndContinueConsumer<StreamContext> consumer) {
		addConsumer(EventMatchers.elements(elements, consumer));
		return this;
	}


	public XmlConsumer matchElement(String element, CheckedFunction<StreamContext, Boolean> consumer) {
		addConsumer(EventMatchers.element(element, consumer));
		return this;
	}

	public XmlConsumer matchElement(String element, CheckedConsumeAndContinueConsumer<StreamContext> consumer) {
		addConsumer(EventMatchers.element(element, consumer));
		return this;
	}


	public XmlConsumer matchAttribute(String attribute, CheckedFunction<StreamContext, Boolean> consumer) {
		addConsumer(EventMatchers.attribute(attribute, consumer));
		return this;
	}

	public XmlConsumer matchAttribute(String attribute, CheckedConsumeAndContinueConsumer<StreamContext> consumer) {
		addConsumer(EventMatchers.attribute(attribute, consumer));
		return this;
	}

	public XmlConsumer matchAttributeValue(String attribute, String value, CheckedFunction<StreamContext, Boolean> consumer) {
		addConsumer(EventMatchers.attributeValue(attribute, value, consumer));
		return this;
	}

	public XmlConsumer matchAttributeValue(String attribute, String value, CheckedConsumeAndContinueConsumer<StreamContext> consumer) {
		addConsumer(EventMatchers.attributeValue(attribute, value, consumer));
		return this;
	}


	public XmlConsumer match(Predicate<StreamContext> predicate, CheckedFunction<StreamContext, Boolean> consumer) {
		addConsumer(new EventMatcher(predicate, consumer));
		return this;
	}

	public XmlConsumer match(Predicate<StreamContext> predicate, CheckedConsumeAndContinueConsumer<StreamContext> consumer) {
		addConsumer(new EventMatcher(predicate, consumer));
		return this;
	}


	public XmlConsumer addConsumer(CheckedFunction<StreamContext, Boolean> consumer) {
		consumers.add(consumer);
		return this;
	}


	/**
	 * Consume the Stream
	 *
	 * @throws XMLStreamException
	 */
	public void consume() {
		for (CheckedFunction<StreamContext, Boolean> consumer : consumers) {
			if (consumer instanceof EventMatcher
					&& ((EventMatcher) consumer).getPredicate() instanceof XmlPredicate
					&& ((XmlPredicate) ((EventMatcher) consumer).getPredicate()).requireSibbling()) {
				stream.getXmlMatcherStreamReader().requireSibbling();
				break;
			}
		}

		try (Stream<StreamContext> stream = this.stream) {
			stream.forEach(c -> {
				for (CheckedFunction<StreamContext, Boolean> consumer : consumers) {
                    try {
                        if (!consumer.apply(c)) {
                            break;
                        }
                    } catch (Throwable throwable) {
                        throw new UncheckedExecutionException(throwable);
                    }
                }
			});
		}
	}
}
