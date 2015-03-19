package org.nlab.xml.context;

import jodd.lagarto.dom.Document;
import jodd.lagarto.dom.Node;
import org.nlab.util.function.CheckedConsumer;
import org.nlab.util.function.CheckedFunction;
import org.nlab.xml.XmlMatcherStreamReader;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nlabrot on 15/03/15.
 */
public class StaxContext {

    private final Document document;
    private final Node node;
    private final XmlMatcherStreamReader streamReader;
    private final int event;
    private final UserContext userContext;


    public StaxContext(Document document, Node node, XmlMatcherStreamReader streamReader, int event, UserContext userContext) {
        this.document = document;
        this.node = node;
        this.streamReader = streamReader;
        this.event = event;
        this.userContext = userContext;
    }

    public void doInPassthrough(CheckedConsumer<XMLStreamReader> c) {
        try {
            streamReader.setPassthrough(true);
            c.accept(streamReader);
        } finally {
            streamReader.setPassthrough(false);
        }
    }

    public <R> R doInPassthrough(CheckedFunction<XMLStreamReader, R> c) {
        try {
            streamReader.setPassthrough(true);
            return c.apply(streamReader);
        } finally {
            streamReader.setPassthrough(false);
        }
    }

    public String findAncestorOrSelfAttribute(String name) {
        Node currentNode = this.node;
        while (currentNode != null) {
            if (currentNode.hasAttribute(name)){
                return currentNode.getAttribute(name);
            }
            currentNode = currentNode.getParentNode();
        }
        return null;
    }

    public Document getDocument() {
        return document;
    }

    public Node getNode() {
        return node;
    }

    public XmlMatcherStreamReader getStreamReader() {
        return streamReader;
    }

    public int getEvent() {
        return event;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public List<Node> findAncestorsAndSiblings() {
        List<Node> nodes = new ArrayList<>();

        Node currentNode = node;

        while (currentNode != null) {
            nodes.add(currentNode);

            while (currentNode.getPreviousSibling() != null) {
                nodes.add(currentNode.getPreviousSibling());
                currentNode = currentNode.getPreviousSibling();
            }
            currentNode = currentNode.getParentNode();
        }
        return nodes;
    }

    public List<Node> findAncestors() {
        List<Node> nodes = new ArrayList<>();

        Node currentNode = node;

        while (currentNode != null) {
            nodes.add(currentNode);
            currentNode = currentNode.getParentNode();
        }
        return nodes;
    }

}
