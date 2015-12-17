// Copyright (c) 2003-2014, Jodd Team (jodd.org). All Rights Reserved.

package jodd.lagarto.dom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nlab.xml.stream.css.CssHelper;

import jodd.csselly.CSSelly;
import jodd.csselly.Combinator;
import jodd.csselly.CssSelector;
import jodd.csselly.selector.PseudoClass;
import jodd.csselly.selector.PseudoClassSelector;

/**
 * Node selector selects DOM nodes using {@link CSSelly CSS3 selectors}.
 * Group of queries are supported.
 */
public class NodeSelector {

	protected final Node rootNode;
	protected final Collection<Node> allowedNodes;
	protected final Node searchedNode;

	public NodeSelector(Node rootNode, Collection<Node> allowedNodes, Node searchedNode) {
		this.rootNode = rootNode;
		this.allowedNodes = allowedNodes;
		this.searchedNode = searchedNode;
	}

	public NodeSelector(Node rootNode) {
		this(rootNode, Collections.emptySet(), rootNode);
	}


	// ---------------------------------------------------------------- selector

	/**
	 * Selects nodes using CSS3 selector query.
	 */
	public Set<Node> select(String query) {
		Collection<List<CssSelector>> selectorsCollection = CSSelly.parse(query);
		return select(selectorsCollection);
	}

	/**
	 * Selected nodes using pre-parsed CSS selectors. Take in consideration
	 * collection type for results grouping order.
	 */
	public Set<Node> select(Collection<List<CssSelector>> selectorsCollection) {
		Set<Node> results = new HashSet<>();
		for (List<CssSelector> selectors : selectorsCollection) {
			processSelectors(results, selectors);
		}
		return results;
	}

	/**
	 * Selected nodes using pre-parsed CSS selectors. Take in consideration
	 * collection type for results grouping order.
	 */
	public Set<Node> select(List<CssSelector> selectors) {
		Set<Node> results = new HashSet<>();
		processSelectors(selectors);
		return results;
	}


	/**
	 * Process selectors and keep adding results.
	 */
	protected void processSelectors(Set<Node> results, List<CssSelector> selectors) {
		List<Node> selectedNodes = select(rootNode, selectors);

		for (Node selectedNode : selectedNodes) {
			if (!results.contains(selectedNode)) {
				results.add(selectedNode);
			}
		}
	}

	/**
	 * Process selectors and keep adding results.
	 */
	protected List<Node> processSelectors(List<CssSelector> selectors) {
		return select(rootNode, selectors);
	}


	protected List<Node> select(Node rootNode, List<CssSelector> selectors) {

		// start with the root node
		List<Node> nodes = new ArrayList<>();
		nodes.add(rootNode);

		// iterate all selectors
		for (int i = 0; i < selectors.size(); i++) {
			CssSelector cssSelector = selectors.get(i);

			// create new set of results for current css selector
			List<Node> selectedNodes = new ArrayList<>();
			for (Node node : nodes) {
				walk(node, cssSelector, selectedNodes);
			}

			if (selectedNodes.size() > 0) {
				// post-processing: filter out the results
				List<Node> resultNodes = new ArrayList<>();
				int index = 0;
				for (Node node : selectedNodes) {
					boolean match = filter(selectedNodes, node, cssSelector, index);
					if (match && (i < selectors.size() - 1 || node == searchedNode)) {
						resultNodes.add(node);
					}
					index++;
				}
				// continue with results
				nodes = resultNodes;

			} else {
				nodes = selectedNodes;
				break;
			}
		}
		return nodes;
	}


	/**
	 * Walks over the child notes, maintaining the tree order and not using recursion.
	 */
	protected void walkDescendantsIteratively(Node rootNode, CssSelector cssSelector, List<Node> result) {
		if (CssHelper.containsSibblingCombinator(cssSelector)) {
			//Iterate over descendants only if the css selector combinator is a sibbling one
		/*	for (Node node : rootNode.descendants) {
				//Iterate over descendants only if the css selector combinator is a sibbling one
				selectAndAdd(node, cssSelector, result);
			}*/
			List<Node> nodes = new ArrayList<>(rootNode.getChildNodesAsList());

			while (!nodes.isEmpty()) {
				Node node = nodes.remove(nodes.size() - 1);
				selectAndAdd(node, cssSelector, result);
				// append children in walking order to be processed right after this node
				int childCount = node.getChildNodesCount();
				for (int i = childCount - 1; i >= 0; i--) {
					nodes.add(node.getChild(i));
				}
			}

		} else {
			//Otherwise iterate over the last children
			for (Node ite = rootNode.getLastChild(); ite != null; ite = ite.getLastChild()) {
				selectAndAdd(ite, cssSelector, result);
			}
		}
	}


	/**
	 * Finds nodes in the tree that matches single selector.
	 */
	protected void walk(Node rootNode, CssSelector cssSelector, List<Node> result) {

		// previous combinator determines the behavior
		CssSelector previousCssSelector = cssSelector.getPrevCssSelector();

		Combinator combinator = previousCssSelector != null ?
				previousCssSelector.getCombinator() :
				hasRootSelector(cssSelector) ? Combinator.CHILD : Combinator.DESCENDANT;

		switch (combinator) {
			case DESCENDANT:
				walkDescendantsIteratively(rootNode, cssSelector, result);
				break;
			case CHILD:
				if (CssHelper.containsSibblingCombinator(cssSelector)) {
					//Iterate over children only if the css selector combinator is a sibbling one
					for (Node node : rootNode.getChildNodesAsList()) {
						selectAndAdd(node, cssSelector, result);
					}
				} else if (rootNode.hasChildNodes()) {
					//Otherwise target directly the last child
					selectAndAdd(rootNode.getChild(rootNode.getChildNodesCount() - 1), cssSelector, result);
				}

				break;
			case ADJACENT_SIBLING:
				Node node = rootNode.getNextSiblingElement();
				if (node != null) {
					selectAndAdd(node, cssSelector, result);
				}
				break;
			case GENERAL_SIBLING:
				node = rootNode;
				while (true) {
					node = node.getNextSiblingElement();
					if (node == null) {
						break;
					}
					selectAndAdd(node, cssSelector, result);
				}
				break;
		}
	}

	private boolean hasRootSelector(CssSelector cssSelector) {

		return cssSelector.getSelectors().stream()
				.filter(PseudoClassSelector.class::isInstance).map(PseudoClassSelector.class::cast)
				.map(PseudoClassSelector::getPseudoClass).filter(PseudoClass.ROOT.class::isInstance)
				.findFirst().isPresent();
	}

	/**
	 * Selects single node for single selector and appends it to the results.
	 */
	protected boolean selectAndAdd(Node node, CssSelector cssSelector, List<Node> result) {

		// ignore all nodes that are not elements
		if (node.getNodeType() != Node.NodeType.ELEMENT) {
			return false;
		}
		boolean matched = cssSelector.accept(node);
		if (matched) {
			result.add(node);

		}
		return matched;
	}

	/**
	 * Filter nodes.
	 */
	protected boolean filter(List<Node> currentResults, Node node, CssSelector cssSelector, int index) {
		return cssSelector.accept(currentResults, node, index);
	}

}