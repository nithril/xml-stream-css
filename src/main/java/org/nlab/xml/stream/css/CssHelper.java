package org.nlab.xml.stream.css;

import jodd.csselly.Combinator;
import jodd.csselly.CssSelector;
import jodd.csselly.Selector;
import jodd.csselly.selector.PseudoClass;
import jodd.csselly.selector.PseudoClassSelector;
import jodd.csselly.selector.PseudoFunction;
import jodd.csselly.selector.PseudoFunctionSelector;

/**
 * Created by nlabrot on 15/12/15.
 */
public class CssHelper {


	public static boolean containsSibblingCombinator(CssSelector cssSelector) {

		if (cssSelector == null) return true;

		return (cssSelector.getCombinator() == Combinator.ADJACENT_SIBLING ||
				cssSelector.getCombinator() == Combinator.GENERAL_SIBLING);
	}


	public static boolean containsSibbling(CssSelector cssSelector) {

		if (cssSelector == null) return true;

		return containsSibblingCombinator(cssSelector) ||
				containsSibblingSelector(cssSelector);
	}

	public static boolean containsSibblingSelector(CssSelector cssSelector) {

		if (cssSelector == null) return true;

		return cssSelector.getSelectors().stream().anyMatch(selector -> {
			if (selector.getType() == Selector.Type.PSEUDO_FUNCTION) {
				PseudoFunction pseudoFunction = ((PseudoFunctionSelector) selector).getPseudoFunction();
				return pseudoFunction instanceof PseudoFunction.NTH_CHILD ||
						pseudoFunction instanceof PseudoFunction.NTH_LAST_CHILD ||
						pseudoFunction instanceof PseudoFunction.NTH_LAST_OF_TYPE ||
						pseudoFunction instanceof PseudoFunction.NTH_OF_TYPE;
			} else if (selector.getType() == Selector.Type.PSEUDO_CLASS) {
				PseudoClass pseudoFunction = ((PseudoClassSelector) selector).getPseudoClass();
				return pseudoFunction instanceof PseudoClass.FIRST_CHILD ||
						pseudoFunction instanceof PseudoClass.FIRST_OF_TYPE
						;
			}
			return false;
		});
	}
}
