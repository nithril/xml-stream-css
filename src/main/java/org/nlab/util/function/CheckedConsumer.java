package org.nlab.util.function;

import org.nlab.exception.UncheckedExecutionException;

import java.util.function.Consumer;


@FunctionalInterface
public interface CheckedConsumer<T> extends Consumer<T> {

    void checkedAccept(T t) throws Exception;

    @Override
    default void accept(T t) throws UncheckedExecutionException {
        try {
            checkedAccept(t);
        } catch (Exception e) {
            throw new UncheckedExecutionException(e);
        }
    }
}
