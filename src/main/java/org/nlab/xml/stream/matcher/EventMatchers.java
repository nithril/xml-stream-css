package org.nlab.xml.stream.matcher;

import org.apache.commons.lang3.Validate;
import org.nlab.xml.stream.context.StaxContext;
import org.nlab.xml.stream.predicate.*;

import java.util.function.Function;


public final class EventMatchers {

    private EventMatchers() {
    }

    public static EventMatcher css(String query, Function<StaxContext,Boolean> consumer){
        Validate.notBlank(query);
        return new EventMatcher(Predicates.css(query), consumer);
    }


    public static EventMatcher elements(String[] elements, Function<StaxContext,Boolean> consumer){
        Validate.notEmpty(elements);
        return new EventMatcher(Predicates.elements(elements), consumer);
    }

    public static EventMatcher element(String element, Function<StaxContext,Boolean> consumer){
        Validate.notBlank(element);
        return new EventMatcher(Predicates.elements(element), consumer);
    }

    public static EventMatcher attribute(String[] attributes, Function<StaxContext,Boolean> consumer){
        Validate.notEmpty(attributes);
        return new EventMatcher(Predicates.attributes(attributes), consumer);
    }

    public static EventMatcher attribute(String attribute, Function<StaxContext,Boolean> consumer){
        Validate.notBlank(attribute);
        return new EventMatcher(Predicates.attributes(attribute), consumer);
    }



    public static EventMatcher attributeValues(String[] attributeAndValues, Function<StaxContext,Boolean> consumer){
        Validate.notEmpty(attributeAndValues);
        return new EventMatcher(new AttributeValuesPredicate(true, false, attributeAndValues), consumer);
    }

    public static EventMatcher attributeValue(String attribute, String value, Function<StaxContext,Boolean> consumer){
        Validate.notBlank(attribute);
        return new EventMatcher(new AttributeValuesPredicate(true, false, attribute, value), consumer);
    }

}
