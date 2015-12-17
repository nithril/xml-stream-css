package org.nlab.xml.stream.reader;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.nlab.xml.stream.context.PathContext;
import org.nlab.xml.stream.context.StreamContext;
import org.nlab.xml.stream.context.UserContext;

import jodd.lagarto.dom.Document;


public class XmlMatcherStreamReader extends StreamReaderDelegate {

	private final StreamContext streamContext;


	public XmlMatcherStreamReader(XMLStreamReader reader) {
		super(reader);
		Document document = new Document();

		streamContext = new StreamContext(this , new UserContext() , new PathContext(document));
		streamContext.setEvent(getEventType());
	}

	@Override
	public int next() throws XMLStreamException {
		if (END_ELEMENT == getEventType()) {
			streamContext.getPathContext().endElem();
			streamContext.getUserContext().pop();
		}
		return processEvent(super.next());
	}

	protected int processEvent(int event) {
		if (START_ELEMENT == event) {
			streamContext.getUserContext().push();
			streamContext.getPathContext().startElem(this);
		}
		streamContext.setEvent(event);
		return event;
	}


	@Override
	public void setParent(XMLStreamReader reader) {
		throw new UnsupportedOperationException("set parent is not supported");
	}

	public void requireSibbling() {
		streamContext.getPathContext().requireSibbling();
	}

	public StreamContext getStreamContext() {
		return streamContext;
	}
}
