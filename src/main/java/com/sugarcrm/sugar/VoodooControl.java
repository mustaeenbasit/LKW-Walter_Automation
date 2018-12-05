package com.sugarcrm.sugar;

import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.automation.webdriver.WaitConditions;
import com.sugarcrm.candybean.automation.webdriver.WebDriverElement;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.automation.webdriver.WebDriverSelector;
import com.sugarcrm.candybean.exceptions.CandybeanException;

import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Exposes functionality for interacting directly with controls in SugarCRM. The
 * interface is intended to be as platform-agnostic as is practical (i.e. can
 * wrap web or mobile controls and exposes as consistent an interface to them as
 * possible).
 *
 * @author David Safar <dsafar@sugarcrm.com>
 *
 */
public class VoodooControl {
	Strategy strategy;
	protected static WebDriverInterface iface;
	Hook hook;
	String hookString = "";
	String strategyName = "";
	String tag = "";
	Float  waitScale = 1f;

	/**
	 * Temporary VoodooControl constructor to be called from Views.  This allows us to instantiate
	 * views with no element definition during the transitional period while we are identifying
	 * those element definitions.  Deprecated so as to throw warnings and highlight places that
	 * still need updating.
	 *
	 * @throws	Exception
	 *
	 * @deprecated	Migrate all Views and View subclasses to explicitly define an element.
	 */
	protected VoodooControl() {
		iface = VoodooUtils.iface;
		tag = "html";
		strategyName = "css";
		hookString = "html";
		strategy = Hook.getStrategy(strategyName.toUpperCase());
		hook = new Hook(strategy, hookString);
		waitScale = Float.parseFloat(VoodooUtils.getGrimoireConfig().getValue("wait_scale", "1.0"));
		if (waitScale < 0) waitScale = 1.f;
	}

	/**
	 * Preferred constructor.  Use this to define most elements, and VoodooControl subclasses (such
	 * as View) should invoke this constructor as the first line of their own constructors.
	 *
	 * @param tagIn
	 *            HTML element tag name
	 * @param strategyNameIn
	 *            how to access this element ie... id, css, xpath
	 * @param hookStringIn
	 *            the value of the strategy
	 * @throws Exception
	 */
	public VoodooControl(String tagIn, String strategyNameIn, String hookStringIn) {
		iface = VoodooUtils.iface;
		hookString = hookStringIn;
		strategyName = strategyNameIn;
		tag = tagIn;
		strategy = Hook.getStrategy(strategyNameIn.toUpperCase());
		hook = new Hook(strategy, hookStringIn);
		waitScale = Float.parseFloat(VoodooUtils.getGrimoireConfig().getValue("wait_scale", "1.0"));
		if (waitScale < 0) waitScale = 1.f;
	}

	/**
	 * Search for this element for up to 15s.
	 *
	 * @return the element, if found.
	 * @throws Exception
	 *             if the element is not found
	 */
	public WebDriverElement waitForElement() throws Exception {
		return this.<WebDriverElement> waitForElement(WebDriverElement.class, 15000);
	}

	/**
	 * Search for this control for up to maxWait ms.<br />
	 * <br />
	 *
	 * Note that the duration of a single iteration of the search is capped by
	 * the perf.implicit_wait time, and that if a search takes longer than the
	 * remaining timeout, the last search will be completed even if it runs over
	 * the timeout (which it usually will).
	 *
	 * @return the element, if found.
	 * @throws Exception
	 */
	public WebDriverElement waitForElement(long maxWait) throws Exception {
		return this.<WebDriverElement> waitForElement(WebDriverElement.class, maxWait);
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
		try {
			return (T) iface.getPause().waitUntil(WaitConditions.present(hook), (long)(maxWait*waitScale));
		} catch (CandybeanException e) {
			throw new UnfoundElementException(toString());
		}

	}

	/**
	 * Wait for this element to become visible for up to 15s.
	 *
	 * @return the element, if found and visible.
	 * @throws Exception
	 *             if the element is not found or not visible
	 */
	public WebDriverElement waitForVisible() throws Exception {
		return this.<WebDriverElement> waitForVisible(WebDriverElement.class, 15000);
	}

	/**
	 * Alternate way to access waitForInvisible and waitForVisible
	 *
	 * @return the element, if should be found and visible, null if should not be found and invisible
	 * @throws Exception
	 *             if the element should be visible and is not found or not visible, or if the element should not be visible and is.
	 */
	public WebDriverElement waitForVisible(boolean shouldBeVisible) throws Exception {
		if (shouldBeVisible) {
			return this.<WebDriverElement> waitForVisible(WebDriverElement.class, 15000);
		}
		else {
			waitForInvisible();
			return null;
		}
	}

	/**
	 * Wait for this element to become visible for up to the specified number of
	 * ms.
	 *
	 * @param maxWait
	 *            the number of ms to wait
	 * @return the element, if found and visible.
	 * @throws Exception
	 *             if the element is not found or not visible
	 */
	public WebDriverElement waitForVisible(long maxWait) throws Exception {
			return this.<WebDriverElement> waitForVisible(WebDriverElement.class, maxWait);
	}

    /**
	 * Alternate way to access waitForInvisible(int maxWait) and waitForInvisible(maxWait)
	 *
	 * @param maxWait
	 *            the number of ms to wait
	 * @return the element, if should be found and visible, null if should not be found and invisible
	 * @throws Exception
	 *             if the element is not found or not visible, or null if element should not be found and invisible
	 */
	public WebDriverElement waitForVisible(int maxWait, boolean shouldBeVisible) throws Exception {
		if (shouldBeVisible) {
			return this.<WebDriverElement> waitForVisible(WebDriverElement.class, maxWait);
		}
		else {
			waitForInvisible(maxWait);
			return null;
		}
	}

	/**
	 * Wait for this element to become visible for up to the specified number of
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
		try {
			return (T) VoodooUtils.getPause().waitForVisible(hook, (long)(maxWait*waitScale));
		} catch (CandybeanException e) {
			throw new UnfoundElementException(toString());
		}
	}

	/**
	 * Alternate way to access waitForInvisible(int maxWait)
	 * ms.
	 *
	 * @param myClass
	 *            the class of the expected return type.
	 * @param maxWait
	 *            the number of ms to wait
	 * @return the element, if found and visible or null if should not be found and invisible
	 * @throws Exception
	 *             if the element is not found or not visible
	 */
	public <T extends WebDriverElement> T waitForVisible(Class<?> myClass, long maxWait, boolean shouldBeVisible) throws Exception {
		if (shouldBeVisible) {
			try {
				return (T) VoodooUtils.getPause().waitForVisible(hook, (long) (maxWait * waitScale));
			} catch (CandybeanException e) {
				throw new UnfoundElementException(toString());
			}
		}
		else {
			waitForInvisible(maxWait);
			return null;
		}
	}

	/**
	 * Wait for this element to become invisible for up to 15s.
	 *
	 * @throws TimeoutException
	 *             if the element is visible after the time out
	 */
	public void waitForInvisible() throws TimeoutException {
		waitForInvisible(15000);
	}

	/**
	 * Wait for this element to become invisible for up to the specified number of
	 * ms.
	 *
	 * @param maxWait
	 * 				the number of ms to wait
	 * @throws TimeoutException
	 *              if element is visible after maxWait
	 */
	public void waitForInvisible(long maxWait) throws TimeoutException {
		try {
			VoodooUtils.getPause().waitForInvisible(hook, (long) (maxWait * waitScale));
		} catch (CandybeanException e) {
			// CandybeanException means that the element is visible after the time out
			throw new TimeoutException(toString() + " still visible after time out");
		}
	}

	/**
	 * Query whether this element is presently visible.
	 *
	 * @return boolean true if the element is visible, false otherwise.
	 * @throws Exception
	 */
	public boolean queryVisible() throws Exception {
		try {
			return iface.getWebDriverElement(hook).isDisplayed();
		} catch (StaleElementReferenceException e) {
			return false;
		// CandybeanException is caught here because getWebDriverElement only throws CandybeanException when
		// no element is found at all
		} catch (CandybeanException e) {
			return false;
		}
	}

	/**
	 * Query whether this element is presently disabled. For purposes of testing
	 * SugarCRM, an element is "disabled" if either the disabled HTML attribute
	 * is set OR the element has a class named "disabled" on it.
	 *
	 * Note that disabled and readonly are separate attributes. This method only
	 * checks for disabled.
	 *
	 * @return boolean true if the element is disabled, false otherwise.
	 * @throws Exception
	 */
	public boolean isDisabled() throws Exception {
		return (queryAttributeEquals("disabled", "true") || queryAttributeContains("class",
			"disabled"));
	}

	/**
	 *
	 * @return the hook string for this control to be interpreted according to
	 *         the strategy name.
	 */
	public String getHookString() {
		return hookString;
	}

	/**
	 *
	 * @return the strategy name that corresponds to the hook string.
	 */
	public String getStrategyName() {
		return strategyName;
	}

	/**
	 *
	 * @return the name of the tag representing this control in HTML.
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Assert that this element exists on the current page.
	 *
	 * @param shouldExist
	 *            - set to true if you expect the element to exist, false if it
	 *            should be absent.
	 * @throws Exception
	 */
	public void assertExists(boolean shouldExist) throws Exception {
		VoodooUtils.voodoo.log.info("Asserting " + ((shouldExist == false) ? "non-" : "")
			+ "existence of " + this);

		boolean match = (shouldExist == queryExists());

		if (shouldExist)
			assertTrue(this + " does not exist when it should.", match);
		else
			assertTrue(this + " exists when it should not.", match);
	}

	/**
	 * Query whether this element exists on the current page.
	 *
	 * @return boolean true if the element exists; else boolean false.
	 */
	public boolean queryExists() throws Exception {
		VoodooUtils.voodoo.log.info("Querying existence of " + this);

		try {
			return (iface.getWebDriverElement(hook) != null);
		} catch (StaleElementReferenceException e) {
			return false;
		// CandybeanException is caught here because getWebDriverElement only throws CandybeanException when
		// no element is found at all
		} catch (CandybeanException e) {
			return false;
		}
	}

	/**
	 * Set the text of an input, the state of a checkbox, or value of a select
	 * element
	 *
	 * @param toSet
	 *            text/state/value to be set
	 * @throws Exception
	 */
	public void set(String toSet) throws Exception {
		VoodooUtils.voodoo.log.info("Setting " + this + " to " + toSet);

		// Check to see if its a checkbox/radio or select element or another input type
		// If its a checkbox or radio button
		// TODO: Figure out how to get this to work with radio buttons
		if ("select".equals(tag)) {
			((WebDriverSelector) waitForVisible(WebDriverSelector.class, 15000)).select(toSet);
		} else if ("input".equals(tag) && waitForVisible() != null
			&& "checkbox".equals(getAttribute("type")) || "radio".equals(getAttribute("type"))) {
			boolean desiredState = Boolean.parseBoolean(toSet);

			Boolean checked = Boolean.parseBoolean(getAttribute("checked"));
			// VoodooUtils.voodoo.log.info("The 'checked' attribute = " +
			// checked + ". This checkbox is " + ((checked == false) ?
			// "not checked" : "checked")); // Keeping this Debug line for
			// future use.
			if (checked != desiredState) {
				waitForVisible().click();
			} else {
				VoodooUtils.voodoo.log.info("Checkbox state for " + this + "is already "
					+ ((checked == false) ? "unchecked" : "checked") + ", no action needed");
			}
		} else { // All other types
			if (getHookString().contains("headerpane")) { // work around SC-3756
			    waitForElement().executeJavascript("$(arguments[0]).val('" + toSet + "').change();");
			} else {
			    waitForVisible().sendString(toSet);
			}
		}
	}

	/**
	 * Click on the control.
	 *
	 * @throws Exception
	 */
	public void click() throws Exception {
		VoodooUtils.voodoo.log.info("Clicking " + this + '.');

		Exception toThrow = null;
		long startTime = System.currentTimeMillis(), currentTime = 0;

		while (currentTime < 15000) {
			currentTime = System.currentTimeMillis() - startTime;
			try {
				waitForVisible();
				// Note that we do not chain off of waitForVisible(); to avoid
				// stale element exceptions.
				iface.getWebDriverElement(hook).click();
				return;
			} catch (StaleElementReferenceException e) {
				VoodooUtils.voodoo.log.warning("Exception caught: StaleElementReferenceException.");
				toThrow = e;
			} catch (TimeoutException e) {
				VoodooUtils.voodoo.log.warning("Exception caught: TimeoutException.");
				toThrow = e;
			} catch (Exception e) {
				if (e.getMessage() != null
					&& e.getMessage().contains("Element is not clickable at point")
					&& e.getMessage().contains("Other element would receive the click:")) {
					VoodooUtils.voodoo.log.warning("Exception caught: overlapping element.");
					toThrow = new OverlappingElementException(e);
				} else {
					throw e;
				}
			}
			long iterationTime = System.currentTimeMillis() - currentTime;
			if (iterationTime < 333) {
				VoodooUtils.pause(333 - iterationTime);
			}
		}

		// If we got here without returning or throwing an exception, the
		// overlap did not clear.
		throw toThrow;
	}

	/**
	 * Hover over the control.
	 *
	 * @throws Exception
	 */
	public void hover() throws Exception {
		VoodooUtils.voodoo.log.info("Hovering over " + this);
		waitForVisible().hover();
	}

	/**
	 * Double click the control.
	 *
	 * @throws Exception
	 */
	public void doubleClick() throws Exception {
		VoodooUtils.voodoo.log.info("Double-clicking " + this);

		waitForVisible().doubleClick();
	}

	/**
	 * Right click the control.
	 *
	 * @throws Exception
	 */
	public void rightClick() throws Exception {
		VoodooUtils.voodoo.log.info("Right-clicking " + this);

		waitForVisible().rightClick();
	}

	/**
	 * Verify that an attribute of the control contains an expected value.
	 * <p>
	 * TODO: If we need an equals match, split this method in two or add an
	 * argument to control contains vs. equals.
	 * <p>
	 *
	 * @param attribute
	 *            the name of the attribute to assert against
	 * @param value
	 *            the value of that attribute which you wish to assert
	 * @throws Exception
	 */
	public void assertAttribute(String attribute, String value) throws Exception {
		assertAttribute(attribute, value, true);
	}

	/**
	 * Verify that an attribute of the control contains or does not contain an
	 * expected value.
	 * <p>
	 * TODO: If we need an equals match, split this method in two or add an
	 * argument to control contains vs. equals.
	 * <p>
	 *
	 * @param attribute
	 *            the name of the attribute to assert against
	 * @param value
	 *            the value of that attribute which you wish to assert
	 * @param expected
	 *            true to assert that the attribute SHOULD contain that value,
	 *            false to assert that it should NOT.
	 * @throws Exception
	 */
	public void assertAttribute(String attribute, String value, boolean expected) throws Exception {
		String foundAttribute = getAttribute(attribute);
		if (expected) {
			VoodooUtils.voodoo.log.info("Verifying " + this + " has attribute " + attribute + "="
				+ value);
			assertTrue("Expected: '" + value + "' Found: '" + foundAttribute + "'",
				foundAttribute.contains(value));
		} else {
			VoodooUtils.voodoo.log.info("Verifying " + this + " does not have attribute "
				+ attribute + "=" + value);
			assertFalse("Not Expected: '" + value + "' Found: '" + foundAttribute + "'",
				foundAttribute.contains(value));
		}
	}

	/**
	 * Return the requested attribute of the element or null if the attribute
	 * does not exist.
	 * <p>
	 * Note: This method does not wait for visibility, so it may be invoked on
	 * invisible elements. The underlying code (CandyBean, Selenium) may not
	 * agree, however.
	 * <p>
	 *
	 * @param attribute
	 *            the name of the attribute to return
	 * @return the value of the specified attribute or null
	 * @throws Exception
	 *             if element is not present
	 */
	public String getAttribute(String attribute) throws Exception {
		VoodooUtils.voodoo.log.info("Querying attribute '" + attribute + "' of " + this);
		try {
			// if this element is not found, waitForElement will throw a UnfoundElementException
			// if this element is found and the attribute is not or the attribute does not have a value
			// WebDriverElement getAttribute will throw CandybeanException
			return waitForElement().getAttribute(attribute);
		} catch (CandybeanException e) {
			return null;
		}
	}

	/**
	 * Query whether an attribute exists.
	 *
	 * @param attributeName
	 *            the attribute to query
	 *
	 * @return boolean true if specified attribute exists, false otherwise.
	 *
	 * @throws Exception
	 */
	public boolean hasAttribute(String attributeName) throws Exception {
		String attributeValue = getAttribute(attributeName);
		return attributeValue != null;
	}

	/**
	 * Waits up to 15000 ms for the specified attribute to contain the specified string.
	 *
	 * @param attributeName
	 *            the attribute to query
	 *
	 * @param attributeValue
	 *            the value of the attribute to query
	 *
	 * @throws Exception
	 */
	public void waitForAttributeToContain(String attributeName, String attributeValue) throws Exception {
		waitForAttributeToContain(attributeName, attributeValue, true, 15000);
	}

	/**
	 * Waits up to the 15000 ms for the specified attribute not to contain the specified string.
	 *
	 * @param attributeName
	 *            the attribute to query
	 *
	 * @param attributeValue
	 *            the value of the attribute to query
	 *
	 * @throws Exception
	 */
	public void waitForAttributeNotToContain(String attributeName, String attributeValue) throws Exception {
		waitForAttributeToContain(attributeName, attributeValue, false, 15000);
	}

	/**
	 * Waits up to the specified number of milliseconds for the specified attribute to contain (or not to contain) the specified string.
	 *
	 * @param attributeName
	 *            the attribute to query
	 *
	 * @param attributeValue
	 *            the value of the attribute to query
	 *
	 * @param expectValue
	 *            whether the attribute value is expected or not
	 *
	 * @param maxWait
	 *            timeout period in ms
	 *
	 * @throws Exception
	 */
	public void waitForAttributeToContain(String attributeName, String attributeValue, boolean expectValue, int maxWait) throws Exception {
		try {
			VoodooUtils.getPause().waitForAttribute(hook, attributeName, attributeValue, expectValue, (long) (maxWait * waitScale));
		} catch (CandybeanException e) {
			// CandybeanException means that the attribute does not appear/disappear after the time out
			throw new TimeoutException(toString() + attributeName + (expectValue ? "does not" : "does") + " contain value of " + attributeValue);
		}
	}

	/**
	 * Return the control's text.
	 *
	 * @return the text of this element
	 * @throws Exception
	 */
	public String getText() throws Exception {
		if (tag.equals("input")) {
			return getAttribute("value");
		} else {
			// Pass the getText() request to WebDriverElement
			return StringEscapeUtils.unescapeHtml(waitForElement().getText());
		}
	}

	/**
	 * Get an element which is a child of this element.
	 * @param  childTag    a String containing the name of the HTML tag the child element should have.
	 * @param childStrategy a String containing the name of the strategy  (e.g. "css", "xpath", etc.) for how to locate the child element.
	 * @param  childHook   a String containing the hook locating the element (syntax depends on the Strategy used)
	 * @return a VoodooControl object representing the child element.
	 * @throws Exception
	 */
	public VoodooControl getChildElement(String childTag, String childStrategy, String childHook) throws Exception {
	    return new VoodooChildElement(this, childTag, childStrategy, childHook);
	}

	// TODO: Core team to implement triggerEvent(String event) for a future
	// sprint.
	// e.g. onclick, onmouseover, onmouseout, onfocus, onblur, etc...
	public void triggerEvent(String event) {
		VoodooUtils.voodoo.log.info("(UNIMPLEMENTED) Triggering event " + event + " on " + this);
	}

	// Not sure what this is or why we'd want it or if it should use
	// waitForVisible()
	public void scroll() throws Exception {
		waitForElement().scroll();
	}

	/**
	 * Drag one element to another
	 *
	 * @param dropHere
	 *            location to drop element
	 * @throws Exception
	 */
	public void dragNDrop(VoodooControl dropHere) throws Exception {
		waitForVisible().dragNDrop(dropHere.waitForVisible());
	}

	/**
	 * Drag one element to another (preferably the natural container) via JS
	 *
	 * Care must be taken to move an element to the natural container for that element otherwise results may be
	 * unpredictable. e.g. "ul" is natural container for a "li", "tr" is natural container for a "td", "tbody" is
	 * natural container for "tr", "table" is natural container for a "tr" and so on.
	 * "div" and "span" are universal containers.
	 *
	 * @param dropHere
	 *            location to drop element
	 * @throws Exception
	 */
	public void dragNDropViaJS(VoodooControl dropHere) throws Exception {
		String dragJS = "var moveElement=arguments[0];var toElement=arguments[1];jQuery(moveElement).appendTo(jQuery(toElement));";

		/*
		 "waitForVisible().executeJavascript(dragJS, dropHere.waitForElement())" does not work here. as
		 dropHere.waitForElement() returns a WebElement which is not acceptable to executeJavascript.

		 "waitForVisible().executeJavascript(dragJS, dropHere.getHookString());" does work here but this is
		 also not acceptable as explained below:

		 The jQuery() constructor can accept a CSS selector, a JavaScript Element or jQuery object, an Array,
		 an Object, nothing, an HTML string, or a callback function. A Candybean hook string can identify an
		 element in one of eight ways: CSS, XPATH, ID, name, link, partial link, class, or tag.
		 This code - "waitForVisible().executeJavascript(dragJS, dropHere.getHookString());" breaks SIX of
		 these eight. The only overlapping type is CSS, and tag may work by accident depending on the expected
		 format because a plain HTML tag name happens to also be a valid CSS selector. Therefore, this code will
		 not work with fully 75% of the ways we can identify an element in VoodooGrimoire, especially
		 since the preferred way to do it when possible is by ID, and that is one of the ones that's broken.

		 Hence the choice for a Javascript/jQuery Object.
		 */
		waitForVisible().executeJavascript(dragJS, dropHere.waitForElement().executeJavascript("return arguments[0];"));
		VoodooUtils.waitForReady();
	}

	/**
	 * Query whether an attribute contains a specified string.
	 *
	 * @param attribute
	 *            the attribute to query
	 * @param value
	 *            the string to search for within the attribute
	 *
	 * @return boolean true if value is found in the specified attribute, false
	 *         otherwise.
	 * @throws Exception
	 */
	public boolean queryAttributeContains(String attribute, String value) throws Exception {
		String attributeValue = getAttribute(attribute);
		return attributeValue != null && attributeValue.contains(value);
	}

	/**
	 * Query whether an attribute exactly equals a specified string.
	 *
	 * @param attribute
	 *            the attribute to query
	 * @param value
	 *            the string to search for within the attribute
	 * @return boolean true if value is an exact match for the specified
	 *         attribute, false otherwise.
	 * @throws Exception
	 */
	public boolean queryAttributeEquals(String attribute, String value) throws Exception {
		String attributeValue = getAttribute(attribute);
		return attributeValue != null && attributeValue.equals(value);
	}

	/**
	 * Query whether string appears within this control.
	 *
	 * @param string
	 *            the string to search for
	 * @param caseSensitive
	 *            boolean true for case-sensitive search, boolean false for
	 *            case-insensitive
	 * @return boolean true if string is found, false otherwise.
	 * @throws Exception
	 */
	public boolean queryContains(String string, boolean caseSensitive) throws Exception {
		return waitForVisible().contains(string, caseSensitive);
	}

	/**
	 * Assert the string does or does not appear within this control.
	 *
	 * @param string
	 *            the string to search for
	 * @param shouldContain
	 *            boolean true for a positive assert, boolean false for a
	 *            negative "assertnot" assert.
	 * @throws Exception
	 */
	public void assertContains(String string, boolean shouldContain) throws Exception {
		VoodooUtils.voodoo.log.info("Asserting " + ((shouldContain == false) ? "non-" : "")
			+ "existence of text '" + string + "' within " + this);

		// Omitted waitForElement() and replaced with getText() because
		// getText() performs a waitForElement()
		String actualString = getText();
		boolean match = (shouldContain == actualString.contains(string));

		if (shouldContain)
			assertTrue(this + " does not contain string '" + string + "' when it should. Actual UI string is '" + actualString + "'", match);
		else
			assertTrue(this + " contains string '" + string + "' when it should not. Actual UI string is '" + actualString + "'", match);
	}

	/**
	 * Assert the string does or does not equal the value of this control.
	 *
	 * @param string
	 *            the string to search for
	 * @param shouldEqual
	 *            boolean true for a positive assert, boolean false for a
	 *            negative "assertnot" assert.
	 * @throws Exception
	 */
	public void assertEquals(String string, boolean shouldEqual) throws Exception {
		VoodooUtils.voodoo.log.info("Asserting " + this + "' equals the string '" + string + "'");

		// Omitted waitForElement() and replaced with getText() because
		// getText() performs a waitForElement()
		String actualString = getText();
		boolean match = (shouldEqual == actualString.equals(string));

		if (shouldEqual)
			assertTrue(this + " does not equal '" + string + "' when it should. Actual UI string is '" + actualString + "'", match);
		else
			assertTrue(this + " equals '" + string + "' when it should not. Actual UI string is '" + actualString + "'", match);
	}

	/**
	 * Assert that this element does or does not contain the string. Searches
	 * all elements contained from this element.
	 *
	 * @param string
	 *            the string to search for
	 * @param shouldContain
	 *            boolean true for a positive assert, boolean false for a
	 *            negative "assertnot" assert.
	 * @throws Exception
	 */
	public void assertElementContains(String string, boolean shouldContain) throws Exception {
		VoodooUtils.voodoo.log.info("Asserting " + this + "' contains the string '" + string + "'");

		// Omitted waitForElement() and replaced with getText() because
		// getText() performs a waitForElement()
		String actualString = getText();
		boolean match = (shouldContain == actualString.contains(string));

		if (shouldContain)
			assertTrue(this + " does not contain the string '" + string + "' when it should. Actual UI string is '" + actualString + "'", match);
		else
			assertTrue(this + " contains the string '" + string + "' when it should not. Actual UI string is '" + actualString + "'", match);
	}

	/**
	 * Assert that this element is or is not visible.
	 *
	 * @param shouldBeVisible
	 *            true if the element should be visible, false if it should not.
	 * @throws Exception
	 */
	public void assertVisible(boolean shouldBeVisible) throws Exception {
		VoodooUtils.voodoo.log.info("Asserting " + ((shouldBeVisible == false) ? "in" : "")
			+ "visibility of " + this);

		VoodooUtils.pause(1000);
		boolean match = (shouldBeVisible == queryVisible());

		if (shouldBeVisible) {
			assertTrue(this + " is not visible when it should be.", match);
		} else {
			assertTrue(this + " is visible when it should not be.", match);
		}
	}

	public String toString() {
		return "<" + tag + " " + strategyName + '=' + "\"" + hookString + "\"" + " />";
	}

	/**
	 * Append a string to this control.
	 * <p>
	 * Must be used with input type fields.<br>
	 *
	 * @param toAppend
	 *            Text to append to this control.
	 * @throws Exception
	 */
	public void append(String toAppend) throws Exception {
		VoodooUtils.voodoo.log.info("Appending '" + this + "' with '" + toAppend + "'");
		// TODO: Update this method once CB-148 is delivered
		waitForVisible().sendString(getText() + toAppend);
	}

	/**
	 *
	 * @param hookStringIn
	 *            String to set this controls hookString to
	 * @throws Exception
	 */
	public void setHookString(String hookStringIn) throws Exception {
		hookString = hookStringIn;
	}

	/**
	 * Get a CSS attribute of the element
	 * @param propertyName attribte name
	 *            String to set this controls hookString to
	 * @throws Exception
	 */
	public String getCssAttribute(String propertyName) throws Exception {
		VoodooUtils.voodoo.log.info("Getting CSS attribute " + propertyName + " for " + this);

		return waitForElement().getCssValue(propertyName);
	}

	/**
	 * Verify that a CSS attribute of the control contains an expected value.
	 * <p>
	 * TODO: If we need an equals match, split this method in two or add an
	 * argument to control contains vs. equals.
	 * <p>
	 *
	 * @param attribute CSS attribute
	 *            the name of the CSS attribute to assert against
	 * @param value
	 *            the value of that attribute which you wish to assert
	 * @throws Exception
	 */
	public void assertCssAttribute(String attribute, String value) throws Exception {
		assertCssAttribute(attribute, value, true);
	}

	/**
	 * Verify that a CSS attribute of the control contains or does not contain an
	 * expected value.
	 * <p>
	 * TODO: If we need an equals match, split this method in two or add an
	 * argument to control contains vs. equals.
	 * <p>
	 *
	 * @param attribute CSS attribute
	 *            the name of the CSS attribute to assert against
	 * @param value
	 *            the value of that attribute which you wish to assert
	 * @param expected
	 *            true to assert that the attribute SHOULD contain that value,
	 *            false to assert that it should NOT.
	 * @throws Exception
	 */
	public void assertCssAttribute(String attribute, String value, boolean expected) throws Exception {
		String foundAttribute = getCssAttribute(attribute);
		if (expected) {
			VoodooUtils.voodoo.log.info("Verifying " + this + " has CSS attribute " + attribute + "="
				+ value);
			assertTrue("Expected: '" + value + "' Found: '" + foundAttribute + "'",
				foundAttribute.contains(value));
		} else {
			VoodooUtils.voodoo.log.info("Verifying " + this + " does not have attribute "
				+ attribute + "=" + value);
			assertFalse("Not Expected: '" + value + "' Found: '" + foundAttribute + "'",
				foundAttribute.contains(value));
		}
	}

	/**
	 * Scroll the given element vertically
	 *
	 * @param scrollBy, integer (+ve for down scroll and -ve for up scroll), default 10
	 *
	 * @throws Exception
	 */
	public void scrollVertically(int scrollBy) throws Exception {
		waitForElement().executeJavascript("arguments[0].scrollTop += " + scrollBy + ";");
		// TODO: VOOD-1290 - Find out a way to remove hard coded wait at scroll methods in VoodooControl.java
		VoodooUtils.pause(2000); // Wait for action to complete
	}

	/**
	 * Move ScrollBar the given element vertically to its topmost position
	 * @throws Exception
	 */
	public void scrollVerticallyHome() throws Exception {
		waitForElement().executeJavascript("arguments[0].scrollTop = 0;");
		// TODO: VOOD-1290 - Find out a way to remove hard coded wait at scroll methods in VoodooControl.java
		VoodooUtils.pause(2000); // Wait for action to complete
	}

	/**
	 * Scroll the given element horizontally
	 *
	 * @param scrollBy, integer (+ve for right scroll and -ve for left scroll), default 10
	 *
	 * @throws Exception
	*/
	public void scrollHorizontally(int scrollBy) throws Exception {
		waitForElement().executeJavascript("arguments[0].scrollLeft += " + scrollBy + ";");
		// TODO: VOOD-1290 - Find out a way to remove hard coded wait at scroll methods in VoodooControl.java
		VoodooUtils.pause(2000); // Wait for action to complete
	}

	/**
	 * Move ScrollBar the given element horizontally to its leftmost position
	 * @throws Exception
	 */
	public void scrollHorizontallyHome() throws Exception {
		waitForElement().executeJavascript("arguments[0].scrollLeft = 0;");
		// TODO: VOOD-1290 - Find out a way to remove hard coded wait at scroll methods in VoodooControl.java
		VoodooUtils.pause(2000); // Wait for action to complete
	}

	/**
	 * Bring the given element into view
	 *
	 * @throws Exception
	 */
	public void scrollIntoView() throws Exception {
		waitForElement().executeJavascript("arguments[0].scrollIntoView(true);");
		// TODO: VOOD-1290 - Find out a way to remove hard coded wait at scroll methods in VoodooControl.java
		VoodooUtils.pause(2000); // Wait for action to complete
	}

	/**
	 * Bring the given element into view if needed
	 * @param checkWithClick boolean if we need to check visibility with a click
	 * @throws Exception
	 */
	public void scrollIntoViewIfNeeded(boolean checkWithClick) throws Exception {
		// TODO: Work to eliminate need for $RightAdjustment and $BottomAdjustment
		String JScode = composeJavascriptCodeForScroll(
				"// parent is the closest ancestor div with scrollbars\n" +
				"var parent = elem.scrollParent();\n" +
				"\n"
				);

		waitForElement().executeJavascript(JScode);

		// TODO: remove this hardcoded pause
		VoodooUtils.pause(500); // Allow for JS code to execute

		if (checkWithClick) {
			click();
		}
	}

	/**
	 * Bring the given element into view if needed
	 * @param parentScrollDiv VoodooControl closest parent scrolling div
	 * @param checkWithClick boolean if we need to check visibility with a click
	 * @throws Exception
	 */
	public void scrollIntoViewIfNeeded(VoodooControl parentScrollDiv, boolean checkWithClick) throws Exception {
		// TODO: Work to eliminate need for $RightAdjustment and $BottomAdjustment
		String JScode = composeJavascriptCodeForScroll(
				"// parent is the closest ancestor div with scrollbars\n" +
				"var parent = jQuery(arguments[1]);\n" +
				"\n"
				);

		// TODO: CB-243 - executeJavascript methods should auto-unbox WebElements from WebDriverElements
		// executeJavascript accepts WebElement object, not a VoodooControl object, hence parentWe object
		Hook thisHook = new Hook(Hook.getStrategy(parentScrollDiv.getStrategyName().toUpperCase()), parentScrollDiv.getHookString());
		By thisBy = thisHook.getBy();
		WebElement parentWe = (WebElement) iface.getPause().waitUntil(
				ExpectedConditions.presenceOfElementLocated(thisBy));

		waitForElement().executeJavascript(JScode, parentWe);

		// TODO: remove this hardcoded pause
		VoodooUtils.pause(500); // Allow for JS code to execute

		if (checkWithClick) {
			click();
		}
	}

	/**
	 * Compose Javascript code for scrolling
	 * @param findScrollDivParent VoodooControl closest parent scrolling div
	 * @throws Exception
	 */
	private String composeJavascriptCodeForScroll(String findScrollDivParent) throws Exception {
		// TODO: Work to eliminate need for $RightAdjustment and $BottomAdjustment
		String JScode = "var elem = jQuery(arguments[0]);\n" +
						"\n" +
						"// If BWC frame is present, check whether this element resides within BWC iframe, \n" +
						"// if yes then recalculate elem \n" +
						"if (jQuery(\"iframe#bwc-frame\").length == 1) {\n" +
						"	if (jQuery(\"iframe#bwc-frame\").contents().find(elem.selector).length != 0) {\n" +
						"	   elem = jQuery(\"iframe#bwc-frame\").contents().find(elem.selector);\n" +
						"	}\n" +
						"}\n" +
						"\n" +
						"// If element has a span parent then select that span instead\n" +
						"// Also, datepicker controls have actual html elsewhere, hence picking parent span is the only way out here\n" +
						"if (elem.parent().prop('tagName') == 'SPAN') {\n" +
						"	elem = elem.parent();	\n" +
						"}\n" +
						findScrollDivParent +
						"// In listview, some part at RHS is occupied by preview button etc, hence $RightAdjustment needed \n" +
						"// Similarly, some bottom adjustment is required to make subpanels fully viewable in recordview \n" +
						"var rightAdjustment = 100; \n" +
						"var bottomAdjustment = 200;  \n" +
						"\n" +
						"// If no actual scroll Div is present, then document is selected as scroll div and this requires \n" +
						"// different method to calculate coordinates\n" +
						"if ($(document).is(parent)) {\n" +
						"	var parentTop = document.body.clientTop; \n" +
						"	var parentBottom = document.body.clientTop + document.body.clientHeight - bottomAdjustment; \n" +
						"	var parentLeft = document.body.clientLeft; \n" +
						"	var parentRight = document.body.clientTop + document.body.clientWidth - rightAdjustment; \n" +
						"}\n" +
						"else {\n" +
						"	var parentTop = parent.offset().top; \n" +
						"	var parentBottom = parent.offset().top + parent.height() - bottomAdjustment; \n" +
						"	var parentLeft = parent.offset().left; \n" +
						"	var parentRight = parent.offset().left + parent.width() - rightAdjustment; 	\n" +
						"}\n" +
						" \n" +
						"var elemTop = elem.offset().top; \n" +
						"var elemBottom = elem.offset().top + elem.height(); \n" +
						"var elemLeft = elem.offset().left; \n" +
						"var elemRight = elem.offset().left + elem.width(); \n" +
						" \n" +
						"var visibleAtTop = elemTop  >= parentTop; \n" +
						"var visibleAtLeft = elemLeft >= parentLeft; \n" +
						"var visibleAtBottom = elemBottom <= parentBottom; \n" +
						"var visibleAtRight = elemRight <= parentRight; \n" +
						"\n" +
						"if (!visibleAtTop || !visibleAtBottom) { \n" +
						"	parent.scrollTop(elemTop - parentTop - ((parent.height() - bottomAdjustment) / 2) + (elem.height() / 2)); \n" +
						"}\n" +
						"if (!visibleAtLeft || !visibleAtRight) { \n" +
						"	parent.scrollLeft(elemLeft - parentLeft - ((parent.width() - rightAdjustment) / 2) + (elem.width() / 2));  \n" +
						"}";
		return JScode;
	}

	/**
	 * Assert if this control is checked or not.
	 * <p>
	 *
	 * @param shouldBeChecked boolean state of checkbox/radio
	 * @throws Exception
	 */
	public void assertChecked(boolean shouldBeChecked) throws Exception {
		VoodooUtils.voodoo.log.info("Verifying " + this + " is checked or not checked.");
		if (shouldBeChecked) {
			assertTrue("Expected '" + this + "' to be checked, but it wasn't!", isChecked());
		} else {
			assertFalse("Expected '" + this + "' to NOT be checked, but it was!", isChecked());
		}
	}

	/**
	 * Determine if this control is checked or not.
	 * <p>
	 * Only works on Checkbox and Radio controls.<br>
	 *
	 * @return True if checked, False otherwise
	 * @throws Exception
	 */
	public boolean isChecked() throws Exception {
		String type = getAttribute("type");
		boolean isType = "checkbox".equals(type) || "radio".equals(type);
		if(!isType) {
			VoodooUtils.voodoo.log.info(this + "  is not a checkbox or a radio button!");
			throw new Exception(this + " is not a checkbox or a radio button!");
		} else {
			String checked = getAttribute("checked");
			VoodooUtils.voodoo.log.info("Checked ?: " + checked);
			if (checked != null && ("checked".equalsIgnoreCase(checked) || "true".equalsIgnoreCase(checked) || checked.equalsIgnoreCase("1"))) {
				VoodooUtils.voodoo.log.info(this + "  is checked!");
				return true;
			}
		}
		VoodooUtils.voodoo.log.info(this + "  is unchecked!");
		return false;
	}

	/**
	 * returns count of the element and its siblings
	 *
	 * Sibling is defined as elements with same parent and same tag
	 *
	 * @return count
	 * @throws Exception
	 */
	public int count() throws Exception {
		long elementCount = (long) waitForElement().executeJavascript(
				"return jQuery(arguments[0].parentNode).children().filter(arguments[0].tagName).length;");
		return (int) elementCount;
	}

	/**
	 * returns count of the element and its exact siblings
	 * Exact Sibling is defined as elements with same parent, same tag and with exactly same class(es)
	 *
	 * For more accurate result, use xpath to locate the from element
	 *
	 * @return count
	 * @throws Exception
	 */
	public int countWithClass() throws Exception {
		long elementCount = (long) waitForElement().executeJavascript(
				"var elem = arguments[0]; return jQuery(elem.parentNode).children().filter(elem.tagName)." +
				"filter(function() { return (this.className ? this.className === elem.className : false); }).length;");
		return (int) elementCount;
	}

	/**
	 * returns count of the element (same tag and containing one or more classes of the from element) in the whole page/iframe
	 *
	 * @return count
	 * @throws Exception
	 */
	public int countAll() throws Exception {
		// We need a filter function to match the class. We need to match that ANY one of the classes
		// of an element of the set-to-be-filtered matches ANY one of the classes of the given element
		long elementCount = (long) waitForElement().executeJavascript(
				"var elem = arguments[0]; return jQuery(elem.tagName).filter(" +
				"		function() { " +
				"			s1 = this.className; " +
				"			s2 = elem.className; " +
				"			s3 = s2.split(' '); " +
				"			matchCount = 0; " +
				"			for(i = 0; i < s3.length; i++) { " +
				"				matchCount = matchCount + (s1.indexOf(s3[i]) >= 0 ? 1 : 0); " +
				"			} " +
				"			return matchCount ? true : false; " +
				"		}).length;");
		return (int) elementCount;
	}
}
