package org.nlab.xml.stream.predicate;

import java.util.Arrays;

import org.nlab.xml.stream.context.StreamContext;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Created by nlabrot on 15/03/15.
 */
public class ElementPredicate implements XmlPredicate {

    private final String[] elements;
    private final boolean matchStart;
    private final boolean matchEnd;

    public ElementPredicate(boolean matchStart, boolean matchEnd, String... elements) {
        this.matchStart = matchStart;
        this.matchEnd = matchEnd;
        this.elements = elements;
    }

    @Override
    public boolean test(StreamContext staxContext) {
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

    @Override
    public String toString() {
        return "ElementPredicate{" +
                "elements=" + Arrays.toString(elements) +
                ", matchStart=" + matchStart +
                ", matchEnd=" + matchEnd +
                '}';
    }
}
