package org.nlab.xml.matcher;

import jodd.csselly.CSSelly;
import org.apache.commons.lang3.Validate;
import org.nlab.xml.context.StaxContext;
import org.nlab.xml.predicate.AttributePredicate;
import org.nlab.xml.predicate.AttributeValuesPredicate;
import org.nlab.xml.predicate.CssPredicate;
import org.nlab.xml.predicate.ElementPredicate;

import java.util.function.Function;


public final class EventMatchers {

    private EventMatchers() {
    }

    public static EventMatcher css(String query, Function<StaxContext,Boolean> consumer){
        Validate.notBlank(query);
        return new EventMatcher(new CssPredicate(CSSelly.parse(query), true, false) , consumer);
    }


    public static EventMatcher elements(String[] elements, Function<StaxContext,Boolean> consumer){
        Validate.notEmpty(elements);
        return new EventMatcher(new ElementPredicate(true, false, elements), consumer);
    }

    public static EventMatcher element(String element, Function<StaxContext,Boolean> consumer){
        Validate.notBlank(element);
        return new EventMatcher(new ElementPredicate(true, false, element), consumer);
    }

    public static EventMatcher attribute(String[] attributes, Function<StaxContext,Boolean> consumer){
        Validate.notEmpty(attributes);
        return new EventMatcher(new AttributePredicate(true, false, attributes), consumer);
    }

    public static EventMatcher attribute(String attribute, Function<StaxContext,Boolean> consumer){
        Validate.notBlank(attribute);
        return new EventMatcher(new AttributePredicate(true, false, attribute), consumer);
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
