package com.sugarcrm.test.sweetspot;

import static org.junit.Assert.*;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Sweetspot_29021 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that Email template should not be repeated twice in sweet spot search
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_29021_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.sweetspot.show();
		sugar.sweetspot.search("View Email Templates");

		// Verify that "View Email template" is displayed in results
		sugar.sweetspot.getActionsResult().assertContains("View Email Templates", true);

		// Verify that "View Email template" appears only once
		// TODO: VOOD-1526 - Need a method in VoodooControl.java to return count of the control on the page
		long itemCountFromJS = (long) VoodooUtils.executeJS("return jQuery(\"#sweetspot ul.sweetspot-results > li[data-section='actions'] > ul > li div.result-text\")." + 
															"filter(function() {return jQuery(this).text().trim() == 'View Email Templates'}).length;");
		int itemCount = (int) itemCountFromJS;
		assertTrue("Count of 'View Email template' is more than one", itemCount == 1);
		
		// Verify that Import process email template should be displayed in the search list
		sugar.sweetspot.configure();

		sugar.sweetspot.clickHotkeysAction();
		sugar.sweetspot.showHotkeysDropDownOptions("Import process email template");
		sugar.sweetspot.getControl("hotkeysActionMatch").
				assertContains("Import Process Email Template", true);
		sugar.sweetspot.clickHotkeysDropDownOption();
		sugar.sweetspot.cancelConfiguration();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}