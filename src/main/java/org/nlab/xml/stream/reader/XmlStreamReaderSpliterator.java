package org.nlab.xml.stream.reader;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.nlab.exception.UncheckedExecutionException;
import org.nlab.xml.stream.reader.XmlMatcherStreamReader;
import org.nlab.xml.stream.context.StaxContext;

/**
 * Created by nlabrot on 08/12/15.
 */
public class XmlStreamReaderSpliterator implements Spliterator<StaxContext> {

	private final XmlMatcherStreamReader xmlMatcherStreamReader;

	public XmlStreamReaderSpliterator(XmlMatcherStreamReader xmlMatcherStreamReader) {
		this.xmlMatcherStreamReader = xmlMatcherStreamReader;
	}

	@Override
	public boolean tryAdvance(Consumer<? super StaxContext> action) {
		try {
			int event = xmlMatcherStreamReader.getEventType();

			StaxContext currentContext = xmlMatcherStreamReader.getCurrentContext();

			action.accept(currentContext);

			if (event == XMLStreamConstants.END_DOCUMENT) {
				return false;
			} else {
				xmlMatcherStreamReader.next();
				return true;
			}
		} catch (XMLStreamException e) {
			throw new UncheckedExecutionException(e);
		}
	}

	@Override
	public Spliterator trySplit() {
		return null;
	}

	@Override
	public long estimateSize() {
		return 0;
	}

	@Override
	public int characteristics() {
		return Spliterator.NONNULL | Spliterator.DISTINCT | Spliterator.IMMUTABLE;
	}
}
