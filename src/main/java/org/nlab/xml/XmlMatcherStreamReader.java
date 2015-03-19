package org.nlab.xml;

import jodd.lagarto.dom.Document;
import jodd.lagarto.dom.Element;
import jodd.lagarto.dom.Node;
import org.nlab.xml.context.StaxContext;
import org.nlab.xml.context.UserContext;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.util.*;
import java.util.function.Function;


public class XmlMatcherStreamReader extends StreamReaderDelegate {

    private final Document document;
    private Node currentNode;

    private boolean passthrough = false;

    private final List<Function<StaxContext, Boolean>> consumers = new ArrayList<>();

    private final UserContext userContext = new UserContext();

    public XmlMatcherStreamReader(XMLStreamReader reader) {
        super(reader);
        document = new Document();
        currentNode = document;
    }

    @Override
    public int next() throws XMLStreamException {
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

        StaxContext staxContext = new StaxContext(document, currentNode, this, event, userContext);

        if (!passthrough) {
            for (Function<StaxContext, Boolean> consumer : consumers) {
                if (!consumer.apply(staxContext)){
                    break;
                }
            }
        }

        if (END_ELEMENT == event) {
            userContext.pop();
            currentNode = currentNode.getParentNode();
        }

        return event;
    }


    public XmlMatcherStreamReader addConsumer(Function<StaxContext, Boolean> consumer) {
        consumers.add(consumer);
        return this;
    }



    public boolean isPassthrough() {
        return passthrough;
    }

    public void setPassthrough(boolean passthrough) {
        this.passthrough = passthrough;
    }
}
