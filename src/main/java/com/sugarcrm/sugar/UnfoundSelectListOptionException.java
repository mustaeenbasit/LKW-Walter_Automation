package com.sugarcrm.sugar;

import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * Indicates that the specified select list option was not found on the current page.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class UnfoundSelectListOptionException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public UnfoundSelectListOptionException() {
		super();
		takeScreenshot();
 	}

	public UnfoundSelectListOptionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		takeScreenshot();
	}

	public UnfoundSelectListOptionException(String message, Throwable cause) {
		super(message, cause);
		takeScreenshot();
	}

	public UnfoundSelectListOptionException(String message) {
		super(message);
		takeScreenshot();
	}

	public UnfoundSelectListOptionException(Throwable cause) {
		super(cause);
		takeScreenshot();
	}

	public void takeScreenshot() {
		try {
			VoodooUtils.takeScreenshot();
		} catch (CandybeanException e) {
			e.printStackTrace();
		}
	}

}
