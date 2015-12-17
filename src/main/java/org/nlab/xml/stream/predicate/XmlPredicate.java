package org.nlab.xml.stream.predicate;

import java.util.function.Predicate;

import org.nlab.xml.stream.context.StreamContext;

/**
 * Created by nlabrot on 14/12/15.
 */
public interface XmlPredicate extends Predicate<StreamContext> {

	default boolean requireSibbling(){
		return false;
	}
}
