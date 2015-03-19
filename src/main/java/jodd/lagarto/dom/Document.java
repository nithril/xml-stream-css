// Copyright (c) 2003-2014, Jodd Team (jodd.org). All Rights Reserved.

package jodd.lagarto.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Document node is always a root node.
 * Holds various DOM-related configuration and information.
 */
public class Document extends Node {

	protected long elapsedTime;
	protected List<String> errors;


	/**
	 * Document constructor with all relevant flags.
	 */
	public Document() {
		super(null, NodeType.DOCUMENT, null);
		this.elapsedTime = System.currentTimeMillis();
	}

	@Override
	public Document clone() {
		Document document = cloneTo(new Document());
		document.elapsedTime = this.elapsedTime;
		return document;
	}

	/**
	 * Notifies document that parsing is done.
	 */
	protected void end() {
		elapsedTime = System.currentTimeMillis() - elapsedTime;
	}

	@Override
	protected void visitNode(NodeVisitor nodeVisitor) {
		nodeVisitor.document(this);
	}

	// ---------------------------------------------------------------- errors


	/**
	 * Returns list of warnings and errors occurred during parsing.
	 * Returns <code>null</code> if parsing was successful; or if
	 * errors are not collected.
	 */
	public List<String> getErrors() {
		return errors;
	}

	// ---------------------------------------------------------------- attr

	/**
	 * Document node does not have attributes.
	 */
	@Override
	public void setAttribute(String name, String value) {
	}

	// ---------------------------------------------------------------- getter

	/**
	 * Returns DOM building elapsed time.
	 */
	public long getElapsedTime() {
		return elapsedTime;
	}



}