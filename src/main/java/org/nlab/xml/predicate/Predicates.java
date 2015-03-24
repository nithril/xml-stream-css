package org.nlab.xml.predicate;

import jodd.csselly.CSSelly;
import org.nlab.xml.context.StaxContext;

import java.util.function.Predicate;

/**
 * Created by nlabrot on 15/03/15.
 */
public final class Predicates {

    private Predicates() {
    }

    public static Predicate<StaxContext> elements(String... elements) {
        return new ElementPredicate(true, false, elements);
    }

    public static Predicate<StaxContext> element(String element) {
        return new ElementPredicate(true, false, element);
    }

    public static Predicate<StaxContext> attributes(String... attributes) {
        return new AttributeValuesPredicate(true, false, attributes);
    }

    public static Predicate<StaxContext> allElements() {
        return AllElementPredicate.INSTANCE_START;
    }

    public static Predicate<StaxContext> css(String query) {
        return new CssPredicate(CSSelly.parse(query), true, false);
    }


    public static Predicate<StaxContext> all() {
        return AllPredicate.INSTANCE;
    }
}
