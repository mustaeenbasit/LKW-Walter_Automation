package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22977 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that a new opportunity is created by clicking on Create button in "OPPORTUNITIES" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22977_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to an account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click "Create" button on "Opportunities" sub-panel. 
		StandardSubpanel oppSubPanel = sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		oppSubPanel.scrollIntoViewIfNeeded(false);
		oppSubPanel.addRecord();
		sugar().opportunities.createDrawer.showMore();

		// Fill mandatory fields and click "Save" button
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("rli_name").set(sugar().opportunities.getDefaultData().get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().opportunities.getDefaultData().get("rli_likely"));
		sugar().opportunities.createDrawer.save();

		// Verify that the related opportunity is displayed on "Opportunities" sub-panel.
		oppSubPanel.scrollIntoViewIfNeeded(false);
		oppSubPanel.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}