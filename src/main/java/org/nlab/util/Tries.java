package org.nlab.util;

import java.util.concurrent.Callable;

import org.jooq.lambda.Unchecked;
import org.jooq.lambda.fi.lang.CheckedRunnable;
import org.jooq.lambda.fi.util.function.CheckedConsumer;
import org.jooq.lambda.fi.util.function.CheckedFunction;
import org.jooq.lambda.fi.util.function.CheckedSupplier;


/**
 * Created by nlabrot on 11/03/16.
 */
public final class Tries {

    public static <T extends AutoCloseable, R> R tryWithResult(CheckedSupplier<T> supplier, CheckedFunction<T, R> f) {
        try (T closeable = supplier.get()) {
            return Unchecked.function(f).apply(closeable);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static <R> R tryWithResult(Callable<R> f) {
        try {
            return f.call();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }


    public static <T extends AutoCloseable> void tryWithoutResult(CheckedSupplier<T> supplier, CheckedConsumer<T> f) {
        try (T closeable = supplier.get()) {
            Unchecked.consumer(f).accept(closeable);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T extends AutoCloseable> void tryWithoutResult(CheckedRunnable f) {
        try {
            f.run();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

}
