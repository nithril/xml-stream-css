package org.nlab.xml.stream.reader;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.nlab.exception.UncheckedExecutionException;
import org.nlab.xml.stream.context.StreamContext;

import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Created by nlabrot on 08/12/15.
 */
public class PartialXmlStreamReaderSpliterator implements Spliterator<StreamContext> {

	private final XmlMatcherStreamReader xmlMatcherStreamReader;
	private final int depth;

	public PartialXmlStreamReaderSpliterator(XmlMatcherStreamReader xmlMatcherStreamReader) {
		Validate.isTrue(START_ELEMENT == xmlMatcherStreamReader.getEventType() ||
				START_DOCUMENT == xmlMatcherStreamReader.getEventType());
		this.xmlMatcherStreamReader = xmlMatcherStreamReader;
		this.depth = xmlMatcherStreamReader.getStreamContext().getPathContext().getAncestors().size();
	}

	@Override
	public boolean tryAdvance(Consumer<? super StreamContext> action) {
		try {
			int event = xmlMatcherStreamReader.getEventType();

			StreamContext currentContext = xmlMatcherStreamReader.getStreamContext();

			action.accept(currentContext);

			if (XMLStreamConstants.END_DOCUMENT == event) {
				return false;
			}

			if (XMLStreamConstants.END_ELEMENT == event) {
				if (depth == xmlMatcherStreamReader.getStreamContext().getPathContext().getAncestors().size()) {
					return false;
				}

			}

			xmlMatcherStreamReader.next();
			return true;

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
