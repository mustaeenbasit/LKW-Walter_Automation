package com.sugarcrm.sugar;

import com.sugarcrm.candybean.automation.webdriver.WebDriverElement;
import com.sugarcrm.candybean.exceptions.CandybeanException;

import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

/**
 * @author David Safar <dsafar@sugarcrm.com>
 *
 */
public class VoodooChildElement extends VoodooControl {
	private VoodooControl parent;

	/**
	 * Child constructor for use by getChildElement to transform a WebDriverElement into a VoodooControl.
	 * @throws Exception
	 */
	protected VoodooChildElement(VoodooControl parentIn, String tagIn, String strategyNameIn, String hookStringIn) throws Exception {
		super(tagIn, strategyNameIn, hookStringIn);
		parent = parentIn;
	}

	/**
	 * Search for this control for up to maxWait ms (with WebDriverSelect support). I
	 * suspect this is a terrible, terrible hack.
	 *
	 * @param  myClass the class of the expected return type.
	 * @param  maxWait the maximum amount of time to search for the control, in ms.
	 * @return the element, if found.
	 * @throws Exception
	 */
	public <T extends WebDriverElement> T waitForElement(Class<?> myClass, long maxWait) throws Exception {
		WebDriverElement parentElement = null;
		final long startTime = currentTimeMillis();
		long currentTimeMs, currentTimeS = 0;
		T toReturn = null;
		Exception exception = null;

		// Set "perf.implicit.wait.seconds" to 0ms for this method temporarily
		int originalImplicitWait = Integer.parseInt(VoodooUtils.voodoo.config.getValue("perf.implicit.wait.seconds"));
		iface.wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

		try {
			parentElement = (WebDriverElement) parent.waitForElement(maxWait);
		} catch (UnfoundElementException e) {
			throw new UnfoundElementException("Could not find parent element " + parent.toString() +
					" while looking for child element: " + toString(), e);
		}

		while((currentTimeMs = currentTimeMillis() - startTime) <= maxWait) {
			try {
				currentTimeS = currentTimeMs / 1000;
				VoodooUtils.voodoo.log.info(currentTimeS + " seconds have passed. Waiting until " + toString()
						+ " is found or " + (maxWait/1000) + " seconds have passed");
				// If the element found is of a VSelect type, return it as a VSelect.
				// Otherwise, a VControl.
				if ("select".equals(tag)) {
					toReturn =  (T) (parentElement.getSelect(hook, 0));
					exception = null;
					break;
				} else {
					toReturn =  (T) (parentElement.getElement(hook, 0));
					exception = null;
					break;
				}
			} catch (CandybeanException | IndexOutOfBoundsException e) {
				exception = e;
			}
			VoodooUtils.pause(250);
		}

		// Reset "perf.implicit.wait.seconds" back to its original
		iface.wd.manage().timeouts().implicitlyWait(originalImplicitWait, TimeUnit.SECONDS);

		if(exception != null) {
			throw new UnfoundElementException((maxWait/1000) + " seconds have passed and the Child element "
					+ toString() + " was not found while searching in Parent " + parent.toString(), exception);
		}

		return toReturn;
	}

	/**
	 * Wait for this child element to become visible for up to the specified number of
	 * ms.
	 *
	 * @param myClass
	 *            the class of the expected return type.
	 * @param maxWait
	 *            the number of ms to wait
	 * @return the element, if found and visible.
	 * @throws Exception
	 *             if the element is not found or not visible
	 */
	public <T extends WebDriverElement> T waitForVisible(Class<?> myClass, long maxWait) throws Exception {
		WebDriverElement parentElement = null;
		final long startTime = currentTimeMillis();
		long elapsedTimeMs;
		T toReturn = null;
		Exception exception = null;

		try {
			parentElement = (WebDriverElement) parent.waitForElement(maxWait);
		} catch (UnfoundElementException e) {
			throw new UnfoundElementException("Could not find parent element " + parent.toString() +
					" while looking for child element: " + toString() + " to be visible.", e);
		}
		try {
			elapsedTimeMs = currentTimeMillis() - startTime;
			toReturn = (T) VoodooUtils.getPause().waitForVisible((T) parentElement.getElement(hook, 0), (maxWait - elapsedTimeMs));
			exception = null;
		} catch (CandybeanException | IndexOutOfBoundsException e) {
			exception = e;
		}

		if(exception != null) {
			throw new UnfoundElementException((maxWait/1000) + " seconds have passed and the Child element "
					+ toString() + " was not found to be visible while searching in Parent " + parent.toString(), exception);
		}

		return toReturn;
	}
}