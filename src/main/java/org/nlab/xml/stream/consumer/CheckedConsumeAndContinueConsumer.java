package org.nlab.xml.stream.consumer;

import java.util.function.Consumer;
import java.util.function.Function;

import org.jooq.lambda.fi.util.function.CheckedConsumer;
import org.jooq.lambda.fi.util.function.CheckedFunction;
import org.nlab.exception.UncheckedExecutionException;

/**
 * Created by nlabrot on 19/03/15.
 */
@FunctionalInterface
public interface CheckedConsumeAndContinueConsumer<T> extends CheckedConsumer<T>, CheckedFunction<T, Boolean> {


	void accept(T t) throws Throwable;

	@Override
	default Boolean apply(T t) throws Throwable {
		accept(t);
		return true;
	}
}
