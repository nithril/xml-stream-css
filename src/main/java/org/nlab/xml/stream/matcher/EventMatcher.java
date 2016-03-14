package org.nlab.xml.stream.matcher;

import java.util.function.Function;
import java.util.function.Predicate;

import org.jooq.lambda.fi.util.function.CheckedFunction;
import org.nlab.xml.stream.context.StreamContext;


public class EventMatcher implements CheckedFunction<StreamContext, Boolean> {

	private final Predicate<StreamContext> predicate;
	private final CheckedFunction<StreamContext, Boolean> consumer;

	public EventMatcher(Predicate<StreamContext> predicate, CheckedFunction<StreamContext, Boolean> consumer) {
		this.predicate = predicate;
		this.consumer = consumer;
	}

	@Override
	public Boolean apply(StreamContext staxContext) throws Throwable {
		if (predicate.test(staxContext)) {
			return consumer.apply(staxContext);
		}
		return true;
	}

	public Predicate<StreamContext> getPredicate() {
		return predicate;
	}

	public CheckedFunction<StreamContext, Boolean> getConsumer() {
		return consumer;
	}
}
