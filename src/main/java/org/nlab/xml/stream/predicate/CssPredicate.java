package org.nlab.xml.stream.predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nlab.xml.stream.context.StreamContext;
import org.nlab.xml.stream.css.CssHelper;

import jodd.csselly.Combinator;
import jodd.csselly.CssSelector;
import jodd.csselly.Selector;
import jodd.csselly.selector.PseudoFunction;
import jodd.csselly.selector.PseudoFunctionSelector;
import jodd.lagarto.dom.NodeSelector;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Created by nlabrot on 15/03/15.
 */
public class CssPredicate implements XmlPredicate {

	private final List<List<CssSelector>> selectors;
	private final boolean matchStart;
	private final boolean matchEnd;
	private final boolean requireSibbling;

	public CssPredicate(Collection<List<CssSelector>> selectors, boolean matchStart, boolean matchEnd) {
		this.matchStart = matchStart;
		this.matchEnd = matchEnd;
		this.selectors = new ArrayList<>(selectors);

		requireSibbling = this.selectors.get(0).stream().anyMatch(s -> s.getCombinator() == Combinator.ADJACENT_SIBLING ||
				s.getCombinator() == Combinator.GENERAL_SIBLING) |
				this.selectors.get(0).stream().anyMatch(CssHelper::containsSibblingSelector);

	}

	public boolean requireSibbling() {
		return requireSibbling;
	}


	@Override
	public boolean test(StreamContext streamContext) {
		if ((!matchStart || START_ELEMENT != streamContext.getEvent()) && (!matchEnd || END_ELEMENT != streamContext.getEvent())) {
			return false;
		}
		NodeSelector nodeSelector = new NodeSelector(streamContext.getDocument(), streamContext.getPathContext().getAncestors(), streamContext.getNode());
		return nodeSelector.select(selectors).contains(streamContext.getPathContext().getCurrentNode());
	}

	@Override
	public String toString() {
		return "CssPredicate{" +
				"selectors=" + selectors +
				", matchStart=" + matchStart +
				", matchEnd=" + matchEnd +
				", requireSibbling=" + requireSibbling +
				'}';
	}
}
