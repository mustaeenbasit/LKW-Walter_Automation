package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26496 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * TC 26496: Verify that the error add-on exclamation icon for Select2 dropdown fields appears in case user tries 
	 * to save record with required field left blank
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26496_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// RLI Module
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.save();
		
		// Verify for error tooltip
		sugar().revLineItems.createDrawer.waitForVisible();
		sugar().revLineItems.createDrawer.getEditField("name").assertAttribute("class", "inherit-width required");
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").assertAttribute("class", "select2-choice select2-default");
		sugar().revLineItems.createDrawer.getEditField("likelyCase").assertAttribute("class", "required");
		sugar().revLineItems.createDrawer.getEditField("date_closed").assertAttribute("class", "required");
		sugar().alerts.closeAllError();
		sugar().revLineItems.createDrawer.cancel();
		
		// Opportunities Module
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.save();
		
		// Verify for error tooltip
		sugar().opportunities.createDrawer.waitForVisible();
		sugar().opportunities.createDrawer.getEditField("name").assertAttribute("class", "inherit-width required");
		sugar().opportunities.createDrawer.getEditField("relAccountName").assertAttribute("class", "select2-choice select2-default");
		sugar().alerts.closeAllError();
		sugar().opportunities.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}