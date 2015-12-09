package org.nlab.xml.stream.predicate;

import org.nlab.xml.stream.context.StaxContext;

import java.util.function.Predicate;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Created by nlabrot on 15/03/15.
 */
public class AllElementPredicate implements Predicate<StaxContext> {

    public static final AllElementPredicate INSTANCE_START = new AllElementPredicate(true, false);
    public static final AllElementPredicate INSTANCE_END = new AllElementPredicate(false, true);
    public static final AllElementPredicate INSTANCE_START_END = new AllElementPredicate(true, true);

    private final boolean matchStart;
    private final boolean matchEnd;

    protected AllElementPredicate(boolean matchStart, boolean matchEnd) {
        this.matchStart = matchStart;
        this.matchEnd = matchEnd;
    }

    @Override
    public boolean test(StaxContext staxContext) {
        if ((!matchStart || START_ELEMENT != staxContext.getEvent()) && (!matchEnd || END_ELEMENT != staxContext.getEvent())) {
            return false;
        }
        return true;
    }

}
