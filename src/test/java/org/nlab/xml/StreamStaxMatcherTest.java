package org.nlab.xml;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.nlab.xml.stream.context.StaxContext;
import org.nlab.xml.stream.XmlStreams;
import org.nlab.xml.stream.util.Transformers;

import static org.nlab.xml.stream.predicate.Predicates.css;

/**
 * Created by nlabrot on 12/03/15.
 */
public class StreamStaxMatcherTest {



	@Test
	public void testCss_Dom() throws Exception {

		try (Stream<StaxContext> stream = XmlStreams.stream("src/test/resources/dom/dom.xml")) {
			List<String> nodes = stream
					.filter(css("node"))
					.map(c -> Transformers.toDom(c.getStreamReader()))
					.map(d -> Transformers.toString(d))
					.collect(Collectors.toList());

			Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><node><content>1</content></node>", nodes.get(0));
			Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><node><content>2</content></node>", nodes.get(1));
			Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><node><content>3</content></node>", nodes.get(2));
		}
	}
}
