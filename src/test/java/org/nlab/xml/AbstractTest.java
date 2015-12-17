package org.nlab.xml;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.junit.Assert;
import org.nlab.xml.stream.XmlStreams;
import org.nlab.xml.stream.consumer.XmlConsumer;
import org.nlab.xml.stream.context.StreamContext;

import javaslang.Tuple2;

import static org.nlab.xml.stream.XmlStreams.newConsumer;

/**
 * Created by nlabrot on 16/12/15.
 */
public class AbstractTest {

	protected void test(List<Tuple2<Predicate<StreamContext>, Integer>> list, String file) throws IOException, XMLStreamException {
		testConsumer(list,file);
		testStream(list,file);
	}

	protected void testConsumer(List<Tuple2<Predicate<StreamContext>, Integer>> list, String file) throws IOException, XMLStreamException {

		Map<Predicate<StreamContext>, AtomicInteger> counters = new HashMap<>();

		XmlConsumer xmlConsumer = newConsumer(file);

		for (Tuple2<Predicate<StreamContext>, Integer> tuple : list) {
			counters.put(tuple._1(), new AtomicInteger());
			xmlConsumer.match(tuple._1(), c -> counters.get(tuple._1()).incrementAndGet());
		}

		xmlConsumer.consume();

		for (Tuple2<Predicate<StreamContext>, Integer> tuple : list) {
			Assert.assertEquals(tuple._1().toString(), tuple._2().intValue(), counters.get(tuple._1()).get());
		}

	}

	protected void testStream(List<Tuple2<Predicate<StreamContext>, Integer>> list, String file) throws IOException, XMLStreamException {

		for (Tuple2<Predicate<StreamContext>, Integer> tuple : list) {

			long count = XmlStreams.stream(file)
					.filter(tuple._1())
					.count();

			Assert.assertEquals(tuple._1().toString(), tuple._2().intValue(), count);
		}

	}
}
