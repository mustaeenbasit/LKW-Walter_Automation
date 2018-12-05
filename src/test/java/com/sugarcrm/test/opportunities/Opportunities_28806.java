package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28806 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that there is cancel/confirm dialog when leave opportunity create
	 * mode with RLI data entered
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28806_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();

		// Entering the RLI name,Expected close Date & Likely fields
		sugar().opportunities.createDrawer.getEditField("rli_name").set(
				sugar().revLineItems.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField(
				"rli_expected_closed_date").set(
						sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(
				sugar().revLineItems.getDefaultData().get("likelyCase"));

		// Navigating to Contacts Module(SideCar Module)
		sugar().navbar.navToModule(sugar().contacts.moduleNamePlural);

		// Verifying that Warning Message is displayed
		sugar().alerts.getWarning().assertVisible(true);

		// Clicking cancel on the Warning Displayed
		sugar().alerts.getWarning().cancelAlert();

		// Verifying Opportunities Create Drawer remains open
		sugar().opportunities.createDrawer.assertVisible(true);

		// Navigating to Accounts Module
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);

		// Verifying that Warning Message is displayed
		sugar().alerts.getWarning().confirmAlert();

		// Verifying Accounts List View is Displayed
		sugar().accounts.listView.assertVisible(true);

		// Verifying opportunities List View is Empty.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}