package org.nlab.xml;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;
import org.nlab.xml.stream.XmlStream;
import org.nlab.xml.stream.XmlStreams;
import org.nlab.xml.stream.consumer.XmlConsumer;
import org.nlab.xml.stream.context.StreamContext;
import org.nlab.xml.stream.predicate.Predicates;

import com.google.common.collect.ImmutableList;
import javaslang.Tuple;
import javaslang.Tuple2;

import static org.nlab.xml.stream.XmlStreams.newConsumer;

/**
 * Created by nlabrot on 16/12/15.
 */
public class PredicateTest extends AbstractTest {

	@Test
	public void testElement() throws Exception {
		List<Tuple2<Predicate<StreamContext>, Integer>> list = ImmutableList.of(
				Tuple.of(Predicates.element("foo"), 0),
				Tuple.of(Predicates.element("child1"), 2),
				Tuple.of(Predicates.element("child2"), 1),
				Tuple.of(Predicates.all(), 49),
				Tuple.of(Predicates.allElements(), 14)
		);
		test(list, "src/test/resources/predicate/children/children.xml");
	}


	@Test
	public void testAttribute() throws Exception {
		List<Tuple2<Predicate<StreamContext>, Integer>> list = ImmutableList.of(
				Tuple.of(Predicates.attributes("testExist"), 1),
				Tuple.of(Predicates.attributeValue("testEquals", "ok"), 1),
				Tuple.of(Predicates.attributeValue("testEquals", "nok"), 0)
		);
		test(list, "src/test/resources/predicate/attribute/attribute.xml");
	}


}
