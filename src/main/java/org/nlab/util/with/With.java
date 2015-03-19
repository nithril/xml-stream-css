package org.nlab.util.with;

import org.nlab.exception.UncheckedExecutionException;
import org.nlab.util.function.CheckedConsumer;
import org.nlab.util.function.CheckedFunction;

/**
 * Created by nlabrot on 10/03/15.
 */
public class With<T> {

    private final T with;

    protected With(T with) {
        this.with = with;
    }

    public <R> R uncheckedApply(CheckedFunction<T, R> f) throws UncheckedExecutionException {
        return f.apply(with);
    }

    public static <T> With<T> with(T t) {
        return new With<T>(t);
    }

    public static <T,R> R with(T t, CheckedFunction<T, R> f) {
        return f.apply(t);
    }

    public static <T> T with(T t, CheckedConsumer<T> f) {
        f.accept(t);
        return t;
    }
}
