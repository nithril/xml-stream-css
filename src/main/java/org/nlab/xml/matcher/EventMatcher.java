package org.nlab.xml.matcher;

import org.nlab.xml.context.StaxContext;

import java.util.function.Function;
import java.util.function.Predicate;


public class EventMatcher implements Function<StaxContext, Boolean> {

    private final Predicate<StaxContext> predicate;
    private final Function<StaxContext,Boolean> consumer;

    public EventMatcher(Predicate<StaxContext> predicate, Function<StaxContext,Boolean> consumer) {
        this.predicate = predicate;
        this.consumer = consumer;
    }

    @Override
    public Boolean apply(StaxContext staxContext) {
        if (predicate.test(staxContext)) {
            return consumer.apply(staxContext);
        }
        return true;
    }

    public Predicate<StaxContext> getPredicate() {
        return predicate;
    }

    public Function<StaxContext,Boolean> getConsumer() {
        return consumer;
    }
}
