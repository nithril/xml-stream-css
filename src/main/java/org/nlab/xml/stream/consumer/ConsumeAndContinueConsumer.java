package org.nlab.xml.stream.consumer;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by nlabrot on 19/03/15.
 */
@FunctionalInterface
public interface ConsumeAndContinueConsumer<T> extends Consumer<T>, Function<T, Boolean> {


	void accept(T t);

	@Override
	default Boolean apply(T t) {
		accept(t);
		return true;
	}
}
