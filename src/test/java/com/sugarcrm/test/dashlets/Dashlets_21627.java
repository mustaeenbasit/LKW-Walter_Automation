package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21627 extends SugarTest {
	public void setup() throws Exception {
		// Add a Quote record
		sugar().quotes.api.create();

		// Login as admin
		sugar().login();
	}

	/**
	 * Add "My quotes" dashlet to dashlet options for Home Module
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21627_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		FieldSet customData = testData.get(testName).get(0);

		// Add a 'List View' Dashlet on My Dashboard
		sugar().home.dashboard.edit();

		// Clicking 'Add row' on Second column
		// TODO: VOOD-1004 - Library support need to create dashlet
		new VoodooControl("a", "css", ".dashlets li:nth-child(2) [data-value='1']").click();
		sugar().home.dashboard.addDashlet(3,1);

		// TODO: VOOD-960 - Dashlet selection 
		// Search and clicking 'List View' link
		new VoodooControl("input", "css", ".layout_Home input[placeholder='Search by Title, Description...']").set(customData.get("listView"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();

		// Adding Quotes module
		new VoodooSelect("div", "css", ".edit.fld_module div").set(sugar().quotes.moduleNamePlural);
		VoodooUtils.waitForReady();

		// Clicking on Save button
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save 'My Dashboard' with the new dashlet
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1004 - Library support need to create dashlet
		// Verify the dashlet 'My Quotes' is added to the dashboard
		new VoodooControl("h4", "css", "li:nth-child(2) .row-fluid:nth-child(3) .dashlet-title").assertEquals(customData.get("dashletName"), true);

		// Verify Quote Name is displayed as the the input data in the dashlet
		new VoodooControl("a", "css", "li:nth-child(2) .row-fluid:nth-child(3) .fld_name a").assertEquals(sugar().quotes.getDefaultData().get("name"), true);

		// Verify Valid Until date is displayed as the the input data in the dashlet
		new VoodooControl("div", "css", "li:nth-child(2) .row-fluid:nth-child(3) .fld_date_quote_expected_closed").assertEquals(sugar().quotes.getDefaultData().get("date_quote_expected_closed"), true);

		// Verify that only one Quote record is displayed
		new VoodooControl("tr", "css", "li:nth-child(2) .row-fluid:nth-child(3) tr:nth-child(2)").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}