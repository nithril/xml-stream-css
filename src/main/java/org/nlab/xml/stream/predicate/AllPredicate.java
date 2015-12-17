package org.nlab.xml.stream.predicate;

import org.nlab.xml.stream.context.StreamContext;

/**
 * Created by nlabrot on 15/03/15.
 */
public class AllPredicate implements XmlPredicate {

    public static final AllPredicate INSTANCE = new AllPredicate();

    protected AllPredicate() {
    }

    @Override
    public boolean test(StreamContext staxContext) {
        return true;
    }

    @Override
    public String toString() {
        return "AllPredicate{}";
    }
}
