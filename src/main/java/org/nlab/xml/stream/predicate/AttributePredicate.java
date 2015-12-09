package org.nlab.xml.stream.predicate;

import jodd.lagarto.dom.Node;
import org.nlab.xml.stream.context.StaxContext;

import java.util.function.Predicate;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Created by nlabrot on 15/03/15.
 */
public class AttributePredicate implements Predicate<StaxContext> {

    private final String[] attributes;
    private final boolean matchStart;
    private final boolean matchEnd;

    public AttributePredicate(boolean matchStart, boolean matchEnd, String... attributes) {
        this.matchStart = matchStart;
        this.matchEnd = matchEnd;
        this.attributes = attributes;
    }

    @Override
    public boolean test(StaxContext staxContext) {
        if ((!matchStart || START_ELEMENT != staxContext.getEvent()) && (!matchEnd || END_ELEMENT != staxContext.getEvent())) {
            return false;
        }

        Node node = staxContext.getNode();

        for (String attribute : attributes) {
            if (!node.hasAttribute(attribute)){
                return false;
            }
        }
        return true;
    }

}
