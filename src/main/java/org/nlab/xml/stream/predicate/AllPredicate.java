package org.nlab.xml.stream.predicate;

import org.nlab.xml.stream.context.StaxContext;

import java.util.function.Predicate;

/**
 * Created by nlabrot on 15/03/15.
 */
public class AllPredicate implements Predicate<StaxContext> {

    public static final AllPredicate INSTANCE = new AllPredicate();

    protected AllPredicate() {
    }

    @Override
    public boolean test(StaxContext staxContext) {
        return true;
    }

}
