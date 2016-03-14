package org.nlab.xml.stream.context;

import org.nlab.xml.stream.reader.XmlMatcherStreamReader;

import jodd.lagarto.dom.Document;
import jodd.lagarto.dom.Node;

/**
 * Created by nlabrot on 14/12/15.
 */
public class StreamContext {

	private final XmlMatcherStreamReader streamReader;
	private final UserContext userContext;
	private final PathContext pathContext;
	private int event;

	public StreamContext(XmlMatcherStreamReader streamReader, UserContext userContext, PathContext pathContext) {
		this.streamReader = streamReader;
		this.userContext = userContext;
		this.pathContext = pathContext;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public XmlMatcherStreamReader getStreamReader() {
		return streamReader;
	}

	public UserContext getUserContext() {
		return userContext;
	}

	public PathContext getPathContext() {
		return pathContext;
	}

	public Node getNode() {
		return pathContext.getCurrentNode();
	}

	public Document getDocument() {
		return pathContext.getDocument();
	}
}