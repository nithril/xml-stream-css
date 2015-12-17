package org.nlab.xml;

import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.nlab.util.IoCloser;
import org.nlab.xml.stream.XmlStreams;
import org.nlab.xml.stream.context.StreamContext;
import org.nlab.xml.stream.factory.StaxCachedFactory;
import org.nlab.xml.stream.util.Transformers;

import static org.nlab.xml.stream.XmlStreams.newConsumerAndClose;
import static org.nlab.xml.stream.predicate.Predicates.css;

/**
 * Created by nlabrot on 12/03/15.
 */
public class StreamStaxMatcherTest {



	@Test
	public void testCss_Dom() throws Exception {

		try (Stream<StreamContext> stream = XmlStreams.stream("src/test/resources/dom/dom.xml")) {
			List<String> nodes = stream
					.filter(css("node"))
					.map(c -> Transformers.toDom(c.getStreamReader()))
					.map(d -> Transformers.toText(d))
					.collect(Collectors.toList());

			Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><node><content>1</content></node>", nodes.get(0));
			Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><node><content>2</content></node>", nodes.get(1));
			Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><node><content>3</content></node>", nodes.get(2));
		}
	}
	@Test
	public void testWikiConsume() throws Exception {
		AtomicInteger integer = new AtomicInteger();

		newConsumerAndClose(new FileInputStream("src/test/resources/enwiki-latest-pages-articles2.xml"))
				.matchCss("mediawiki:root > page > title", c -> {
					//integer.incrementAndGet();
					Transformers.toWriter(c.getStreamReader(), new StringWriter());
				})

				.consume();

		//Assert.assertEquals(3, integer.get());
	}

	@Test
	public void testWiki() throws Exception {

		//XmlStreamSpec.with("").closeOnFinish().consume().;

		try (Stream<StreamContext> stream = XmlStreams.stream("src/test/resources/enwiki-latest-pages-articles2.xml")) {
			stream
					.filter(css("mediawiki:root page"))
					//.map(c -> Transformers.toText(c.getStreamReader()))
					.forEach(t -> {});

		}
	}
	@Test
	public void testWiki2() throws Exception {
		InputStream inputStream = null;
		XMLStreamReader streamReader = null;
		try {
			inputStream = Files.newInputStream(Paths.get("src/test/resources/enwiki-latest-pages-articles2.xml"));
			streamReader = StaxCachedFactory.getInputFactory().createXMLStreamReader(inputStream);

			while(streamReader.hasNext()){
				streamReader.next();
			}


		} catch (Exception e) {
			IoCloser.ioCloser().close(streamReader).close(inputStream);
			throw e;
		}

	}
}
