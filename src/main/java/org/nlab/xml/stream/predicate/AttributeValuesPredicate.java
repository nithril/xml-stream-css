package org.nlab.xml.stream.predicate;

import java.util.Arrays;

import org.apache.commons.lang3.Validate;
import org.nlab.xml.stream.context.StreamContext;

import jodd.lagarto.dom.Node;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Created by nlabrot on 15/03/15.
 */
public class AttributeValuesPredicate implements XmlPredicate {

	private final String[] attributeAndValues;
	private final boolean matchStart;
	private final boolean matchEnd;

	public AttributeValuesPredicate(boolean matchStart, boolean matchEnd, String... attributeAndValues) {
		this.matchStart = matchStart;
		this.matchEnd = matchEnd;
		Validate.isTrue((attributeAndValues.length & 1) == 0);
		this.attributeAndValues = attributeAndValues;
	}

	@Override
	public boolean test(StreamContext staxContext) {
		if ((!matchStart || START_ELEMENT != staxContext.getEvent()) && (!matchEnd || END_ELEMENT != staxContext.getEvent())) {
			return false;
		}

		Node node = staxContext.getNode();

		for (int i = 0; i < attributeAndValues.length; i += 2) {
			if (!node.hasAttribute(attributeAndValues[i])) {
				return false;
			}
			if (!node.getAttribute(attributeAndValues[i]).equals(attributeAndValues[i + 1])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "AttributeValuesPredicate{" +
				"attributeAndValues=" + Arrays.toString(attributeAndValues) +
				", matchStart=" + matchStart +
				", matchEnd=" + matchEnd +
				'}';
	}
}
