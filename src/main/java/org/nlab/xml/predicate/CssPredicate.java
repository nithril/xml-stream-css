package org.nlab.xml.predicate;

import jodd.csselly.CssSelector;
import jodd.lagarto.dom.NodeSelector;
import org.nlab.xml.context.StaxContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Created by nlabrot on 15/03/15.
 */
public class CssPredicate implements Predicate<StaxContext> {

    private final Collection<List<CssSelector>> selectors;
    private final boolean matchStart;
    private final boolean matchEnd;

    public CssPredicate(Collection<List<CssSelector>> selectors, boolean matchStart, boolean matchEnd) {
        this.matchStart = matchStart;
        this.matchEnd = matchEnd;
        this.selectors = new ArrayList<>(selectors);
    }

    @Override
    public boolean test(StaxContext staxContext) {
        if ((!matchStart || START_ELEMENT != staxContext.getEvent()) && (!matchEnd || END_ELEMENT != staxContext.getEvent())) {
            return false;
        }

        NodeSelector nodeSelector = new NodeSelector(staxContext.getDocument(), staxContext.findAncestorsAndSiblings());
        return !nodeSelector.select(selectors).isEmpty();
    }

}
