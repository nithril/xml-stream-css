package org.nlab.xml;

import java.util.List;
import java.util.function.Predicate;

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.Test;
import org.nlab.xml.stream.context.StreamContext;
import org.nlab.xml.stream.predicate.Predicates;

import com.google.common.collect.ImmutableList;

/**
 * Created by nlabrot on 16/12/15.
 */
public class PredicateTest extends AbstractTest {

	@Test
	public void testElement() throws Exception {
		List<Tuple2<Predicate<StreamContext>, Integer>> list = ImmutableList.of(
				Tuple.tuple(Predicates.element("foo"), 0),
				Tuple.tuple(Predicates.element("child1"), 2),
				Tuple.tuple(Predicates.element("child2"), 1),
				Tuple.tuple(Predicates.all(), 49),
				Tuple.tuple(Predicates.allElements(), 14)
		);
		test(list, "src/test/resources/predicate/children/children.xml");
	}


	@Test
	public void testAttribute() throws Exception {
		List<Tuple2<Predicate<StreamContext>, Integer>> list = ImmutableList.of(
				Tuple.tuple(Predicates.attributes("testExist"), 1),
				Tuple.tuple(Predicates.attributeValue("testEquals", "ok"), 1),
				Tuple.tuple(Predicates.attributeValue("testEquals", "nok"), 0)
		);
		test(list, "src/test/resources/predicate/attribute/attribute.xml");
	}


}
