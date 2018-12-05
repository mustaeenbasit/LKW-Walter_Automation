package com.sugarcrm.test.emails;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_20623 extends SugarTest {
	public void setup() throws Exception {		
		sugar().login();
	}

	/**
	 * search_advanced_search_null_search_condition
	 * @throws Exception
	 */
	@Test
	public void Emails_20623_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Emails -> Click on Search icon -> Clear assigned to field -> Search button
		sugar().navbar.navToModule(sugar().emails.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1076, VOOD-1077
		new VoodooControl("a", "css", "#lefttabs .yui-nav #searchTab a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#advancedSearchForm tr.visible-search-option td a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#advancedSearchTable tbody tr:nth-of-type(12) td input:nth-of-type(2)").click();
		new VoodooControl("input", "id", "advancedSearchButton").click();
		VoodooUtils.waitForDialog();

		// Verify that JS Dialog is displayed.
		Assert.assertTrue("Dialog is not displayed", VoodooUtils.isDialogVisible());

		// TODO: VOOD-1045 - Once resolved we should verify dialog message "Please enter some search criteria" 
		VoodooUtils.acceptDialog();


		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}