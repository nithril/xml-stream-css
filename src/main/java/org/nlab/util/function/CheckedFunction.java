package org.nlab.util.function;


import org.nlab.exception.UncheckedExecutionException;

import java.util.function.Function;

@FunctionalInterface
public interface CheckedFunction<T, R> extends Function<T, R> {

    R checkedApply(T t) throws Exception;

    @Override
    default R apply(T t) throws UncheckedExecutionException {
        try {
            return checkedApply(t);
        } catch (Exception e) {
            throw new UncheckedExecutionException(e);
        }
    }
}
