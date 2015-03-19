// Copyright (c) 2003-2014, Jodd Team (jodd.org). All Rights Reserved.

package jodd.csselly;


/**
 * CSSelly exception.
 */
public class CSSellyException extends RuntimeException {

	public CSSellyException(Throwable t) {
		super(t);
	}

	public CSSellyException(String message) {
		super(message);
	}

	public CSSellyException(String message, int state, int line, int column) {
		super(message + " (state: " + state + (line != -1 ? " error at: " + line + ':' + column : "" + ')'));
	}
}