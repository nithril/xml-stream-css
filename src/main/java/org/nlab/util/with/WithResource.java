package org.nlab.util.with;

import org.nlab.exception.UncheckedExecutionException;
import org.nlab.util.function.CheckedConsumer;
import org.nlab.util.function.CheckedFunction;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nlabrot on 10/03/15.
 */
public class WithResource {

    private final InputStream with;

    protected WithResource(InputStream with) {
        this.with = with;
    }

    public <R> R uncheckedApply(CheckedFunction<InputStream, R> f) throws UncheckedExecutionException {
        try (InputStream is = with) {
            return f.apply(is);
        } catch (Exception e) {
            throw new UncheckedExecutionException(e);
        }
    }


    public static WithResource withResource(InputStream with) {
        return new WithResource(with);
    }

    public static <R> R withResource(InputStream is, CheckedFunction<InputStream, R> f) throws UncheckedExecutionException {
        try(InputStream with = is) {
            return f.apply(with);
        } catch (Exception e) {
            throw new UncheckedExecutionException(e);
        }
    }

    public static void withResource(InputStream is, CheckedConsumer<InputStream> f) {
        try(InputStream with = is) {
            f.accept(with);
        } catch (Exception e) {
            throw new UncheckedExecutionException(e);
        }
    }
}
