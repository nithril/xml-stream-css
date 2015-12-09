package org.nlab.xml.stream.reader;

import jodd.lagarto.dom.Document;
import jodd.lagarto.dom.Element;
import jodd.lagarto.dom.Node;
import org.nlab.xml.stream.context.StaxContext;
import org.nlab.xml.stream.context.UserContext;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;


public class XmlMatcherStreamReader extends StreamReaderDelegate {

    private final Document document;
    private final UserContext userContext = new UserContext();
	private Node currentNode;

    private StaxContext currentStaxContext;


    public XmlMatcherStreamReader(XMLStreamReader reader) {
        super(reader);
        document = new Document();
        currentNode = document;
        currentStaxContext = new StaxContext(document, currentNode, this, getEventType(), userContext);
    }

    @Override
    public int next() throws XMLStreamException {
        if (END_ELEMENT == getEventType()) {
            userContext.pop();
            currentNode = currentNode.getParentNode();
        }

        return processEvent(super.next());
    }

    protected int processEvent(int event) {
        if (START_ELEMENT == event) {
            userContext.push();

            Element contextNode = new Element(document, this.getLocalName());
            for (int i = 0; i < this.getAttributeCount(); i++) {
                contextNode.setAttribute(this.getAttributeLocalName(i), this.getAttributeValue(i));
            }
            currentNode.addChild(contextNode);
            currentNode = contextNode;
        }

        currentStaxContext = new StaxContext(document, currentNode, this, event, userContext);

        return event;
    }

    public StaxContext getCurrentContext(){
        return currentStaxContext;
    }


	@Override
	public void setParent(XMLStreamReader reader) {
		throw new UnsupportedOperationException("set parent is not supported");
	}
}
