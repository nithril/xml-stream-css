package org.nlab.xml.predicate;

import org.nlab.xml.context.StaxContext;

import java.util.function.Predicate;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Created by nlabrot on 15/03/15.
 */
public class ElementPredicate implements Predicate<StaxContext> {

    private final String[] elements;
    private final boolean matchStart;
    private final boolean matchEnd;

    public ElementPredicate(boolean matchStart, boolean matchEnd, String... elements) {
        this.matchStart = matchStart;
        this.matchEnd = matchEnd;
        this.elements = elements;
    }

    @Override
    public boolean test(StaxContext staxContext) {
        if ((!matchStart || START_ELEMENT != staxContext.getEvent()) && (!matchEnd || END_ELEMENT != staxContext.getEvent())) {
            return false;
        }

        for (String element : elements) {
            if (element.equals(staxContext.getNode().getNodeName())) {
                return true;
            }
        }
        return false;
    }

}
