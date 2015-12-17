package org.nlab.xml.stream.context;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jodd.lagarto.dom.Document;
import jodd.lagarto.dom.Element;
import jodd.lagarto.dom.Node;

/**
 * Created by nlabrot on 13/12/15.
 */
public class PathContext {

	private List<Node> ancestorsAndSiblings = new ArrayList<>(1000);
	private Set<Node> ancestorsAndSiblingsAsSet = new HashSet<>(1000);

	private List<Node> ancestors = new ArrayList<>(100);
	private Set<Node> ancestorsAsSet = new HashSet<>(100);

	private Node currentNode;
	private final Document document;

	private boolean recordSibbling = false;

	public PathContext(Document document) {
		this.document = document;
		this.currentNode = document;

		recordPathStartElem(document);
	}

	public void startElem(XMLStreamReader reader) {

		Element contextNode = new Element(document, reader.getLocalName());
		for (int i = 0; i < reader.getAttributeCount(); i++) {
			contextNode.setAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
		}

		currentNode.addChild(contextNode);
		currentNode = contextNode;

		recordPathStartElem(contextNode);
	}

	public void endElem() {
		recordPathEndElem(currentNode);

		if (recordSibbling) {
			currentNode.removeAllChilds();
			currentNode = currentNode.getParentNode();
		} else {
			currentNode.removeAllChilds();
			Node parentNode = currentNode.getParentNode();
			parentNode.removeChild(currentNode);
			currentNode = parentNode;
		}
	}


	public String findAncestorOrSelfAttribute(String name) {
		Node currentNode = getCurrentNode();
		while (currentNode != null) {
			if (currentNode.hasAttribute(name)) {
				return currentNode.getAttribute(name);
			}
			currentNode = currentNode.getParentNode();
		}
		return null;
	}

	public Set<Node> findAncestorsAndSiblings() {
		if (!isRecordSibbling()){
			throw new UnsupportedOperationException("Sibbling are not recorded");
		}

		Set<Node> nodes = new HashSet<>();

		Node currentNode = getCurrentNode();

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

		Node currentNode = getCurrentNode();

		while (currentNode != null) {
			nodes.add(currentNode);
			currentNode = currentNode.getParentNode();
		}
		return nodes;
	}

	private void recordPathStartElem(Node elem) {
		if (recordSibbling) {
			ancestorsAndSiblings.add(elem);
			ancestorsAndSiblingsAsSet.add(elem);
		}

		ancestors.add(elem);
		ancestorsAsSet.add(elem);
	}

	private void recordPathEndElem(Node elem) {
		if (recordSibbling) {
			int ite = ancestorsAndSiblings.size() - 1;
			while (ancestorsAndSiblings.get(ite).getParentNode() == elem) {
				ancestorsAndSiblingsAsSet.remove(ancestorsAndSiblings.remove(ite--));
			}
		}

		ancestors.remove(ancestors.size() - 1);
		ancestorsAsSet.remove(elem);
	}


	public List<Node> getAncestorsAndSiblings() {
		return ancestorsAndSiblings;
	}

	public Set<Node> getAncestorsAndSiblingsAsSet() {
		return ancestorsAndSiblingsAsSet;
	}

	public List<Node> getAncestors() {
		return ancestors;
	}

	public Set<Node> getAncestorsAsSet() {
		return ancestorsAsSet;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public Document getDocument() {
		return document;
	}

	public boolean isRecordSibbling() {
		return recordSibbling;
	}

	public void requireSibbling() {
		this.recordSibbling = true;
	}
}
