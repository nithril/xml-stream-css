package org.nlab.xml.stream.matcher;

import java.util.function.Function;
import java.util.function.Predicate;

import org.nlab.xml.stream.context.StreamContext;


public class EventMatcher implements Function<StreamContext, Boolean> {

	private final Predicate<StreamContext> predicate;
	private final Function<StreamContext, Boolean> consumer;

	public EventMatcher(Predicate<StreamContext> predicate, Function<StreamContext, Boolean> consumer) {
		this.predicate = predicate;
		this.consumer = consumer;
	}

	@Override
	public Boolean apply(StreamContext staxContext) {
		if (predicate.test(staxContext)) {
			return consumer.apply(staxContext);
		}
		return true;
	}

	public Predicate<StreamContext> getPredicate() {
		return predicate;
	}

	public Function<StreamContext, Boolean> getConsumer() {
		return consumer;
	}
}
