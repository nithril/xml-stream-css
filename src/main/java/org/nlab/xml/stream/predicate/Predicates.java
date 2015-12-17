package org.nlab.xml.stream.predicate;

import java.util.function.Predicate;

import org.nlab.xml.stream.context.StreamContext;

import jodd.csselly.CSSelly;

/**
 * Created by nlabrot on 15/03/15.
 */
public final class Predicates {

    private Predicates() {
    }

    public static Predicate<StreamContext> elements(String... elements) {
        return new ElementPredicate(true, false, elements);
    }

    public static Predicate<StreamContext> element(String element) {
        return new ElementPredicate(true, false, element);
    }

    public static Predicate<StreamContext> attributes(String... attributes) {
        return new AttributePredicate(true, false, attributes);
    }

    public static Predicate<StreamContext> attributeValue(String... attributes) {
        return new AttributeValuesPredicate(true, false, attributes);
    }

    public static Predicate<StreamContext> allElements() {
        return AllElementPredicate.INSTANCE_START;
    }

    public static Predicate<StreamContext> css(String query) {
        return new CssPredicate(CSSelly.parse(query), true, false);
    }


    public static Predicate<StreamContext> all() {
        return AllPredicate.INSTANCE;
    }
}
