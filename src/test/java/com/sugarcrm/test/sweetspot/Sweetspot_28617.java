package com.sugarcrm.test.sweetspot;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Sweetspot_28617 extends SugarTest {
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Verify user can use Sweet Spot to search (and go to) a module, and correct icon appears
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28617_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet modulesIcon = testData.get(testName).get(0);
		
		// Activate Sweetspot
		sugar.sweetspot.show();
		
		// Search Sweetspot for "Accounts" module
		sugar.sweetspot.search(sugar.accounts.moduleNamePlural);
		
		// Verify that Accounts link and it's icon displayed in results
		sugar.sweetspot.getActionsResult().assertContains(sugar.accounts.moduleNamePlural, true);
		sugar.sweetspot.getActionsResult().assertContains(modulesIcon.get("accounts_icon"), true);
		
		// Click "Accounts" module from results
		sugar.sweetspot.clickActionsResult();
		
		// Verify that user directed to the correct module i.e. "Accounts" module
		sugar.accounts.listView.getControl("moduleTitle").assertContains(sugar.accounts.moduleNamePlural, true);

		// Go to the record view and verify module's icon
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.getControl("moduleIDLabel").assertContains(modulesIcon.get("accounts_icon"), true);
		
		// Activate Sweetspot
		sugar.sweetspot.show();
		
		// Search Sweetspot for "Quotes" module
		sugar.sweetspot.search(sugar.quotes.moduleNamePlural);
		
		// Verify that Quotes link and it's icon displayed in results
		sugar.sweetspot.getActionsResult().assertContains(sugar.quotes.moduleNamePlural, true);
		sugar.sweetspot.getActionsResult().assertContains(modulesIcon.get("quotes_icon"), true);
		
		// Click "Quotes" module from results
		sugar.sweetspot.clickActionsResult();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that user directed to the correct module i.e. "Quotes" module
		sugar.quotes.listView.getControl("moduleTitle").assertContains(sugar.quotes.moduleNamePlural, true);
		VoodooUtils.focusDefault();		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}