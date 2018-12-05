package com.sugarcrm.sugar;

import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * Indicates that the specified element was not found on the current page.
 * @author David Safar <dsafar@sugarcrm.com>
 *
 */
public class UnfoundElementException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnfoundElementException() {
		super();
		takeScreenshot();
 	}

	public UnfoundElementException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		takeScreenshot();
	}

	public UnfoundElementException(String message, Throwable cause) {
		super(message, cause);
		takeScreenshot();
	}

	public UnfoundElementException(String message) {
		super(message);
		takeScreenshot();
	}

	public UnfoundElementException(Throwable cause) {
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
