package com.sugarcrm.sugar;

import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.sugar.views.View;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Exposes functionality for interacting directly with select lists in
 * SugarCRM.  The present implementation is specific to Select2; regular select
 * tags should be handled with VoodooControl, not with this class.
 *
 * @author David Safar <dsafar@sugarcrm.com>
 */
public class VoodooSelect extends VoodooControl {
	public View selectWidget = new View();

	public VoodooSelect(String tagIn, String strategyNameIn, String hookStringIn)
			throws Exception {
		super(tagIn, strategyNameIn, hookStringIn);

		selectWidget.addControl("searchBox", "input", "css", "div#select2-drop div input");
		selectWidget.addControl("hiddenSearchBox", "input", "css", "div#select2-drop div.select2-search-hidden input");
		selectWidget.addControl("textOption", "span", "css", "div#select2-drop ul li div span");
		selectWidget.addControl("searchForMoreLink", "div", "css", ".select2-result-label");
	}

	/**
	 * Set the text of a select replacement widget.
	 * @param toSet	text/state/value to be set
	 * @throws Exception
	 */
	public void set(String toSet) throws Exception {
		VoodooUtils.voodoo.log.info("Setting " + this + " to " + toSet);

		scrollIntoViewIfNeeded(false);
		waitForElement().click();

		// Support dropdowns that use a search box as well as ones that don't.

		VoodooControl searchBox = selectWidget.getControl("searchBox");

		// TODO: VOOD-1220.  Eliminate direct we access here.
		// This is a terrible hack because Select 2 makes the search box visible but small
		// enough that you don't see it on the select lists that "don't" have a search
		// field.  Sometimes, but not always, zero width.  But when the search field is
		// noticeable to the user, it is just a few pixels smaller than the containing list
		if(searchBox.queryVisible()) {
			Hook searchBoxHook = new Hook(Hook.getStrategy(searchBox.getStrategyName().toUpperCase()),
					searchBox.getHookString());
			By searchBoxBy = searchBoxHook.getBy();
			WebElement searchBoxWe = (WebElement) iface.getPause().waitUntil(
					ExpectedConditions.presenceOfElementLocated(searchBoxBy));

			Hook thisHook = new Hook(Hook.getStrategy(this.getStrategyName().toUpperCase()), this.getHookString());
			By thisBy = thisHook.getBy();
			WebElement thisWe = (WebElement) iface.getPause().waitUntil(
					ExpectedConditions.presenceOfElementLocated(thisBy));

			if(searchBoxWe.getSize().getWidth() >= thisWe.getSize().getWidth() * .8) {
				try {
					selectWidget.getControl("searchBox").set(toSet);
					VoodooUtils.waitForReady(30000);
					new VoodooControl("li", "css", "li[role='presentation']").waitForVisible();
				} catch (UnfoundElementException e) {
					throw new UnfoundSelectListOptionException("Could not find option " + toSet + " in select list " + toString(), e);
				}
			}
		}
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + toSet + "')]]").click();
	}

	/**
	 * Click on "Search for more.." link in select box
	 * @throws Exception
	 */
	public void clickSearchForMore() throws Exception
	{
		VoodooControl searchBox = selectWidget.getControl("searchBox");
		if(!searchBox.queryVisible())
			waitForElement().click();
		selectWidget.getControl("searchForMoreLink").click();

		// Wait for "Search and Select Drawer" to open
		new VoodooControl("span", "css", ".fld_title.selection-headerpane").waitForVisible();
	}
}
