package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22978 extends SugarTest {
	public void setup() throws Exception {
		// Create an Account record
		sugar().accounts.api.create();

		// Login as a valid user
		sugar().login();
	}

	/**
	 * Account Detail - Opportunities sub-panel - Create_Verify that an opportunity is created in "Full Form" format from "OPPORTUNITIES" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22978_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to the record view of the created Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click "Create" button on "OPPORTUNITIES" sub-panel, then click "Show More" link
		StandardSubpanel opportunitiesSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opportunitiesSubpanelCtrl.addRecord();
		sugar().opportunities.createDrawer.showMore();

		// Fill mandatory fields and click "Save" button
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("rli_name").set(sugar().opportunities.getDefaultData().get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().opportunities.getDefaultData().get("rli_likely"));
		sugar().opportunities.createDrawer.save();

		// Verify that the related opportunity is displayed on "OPPORTUNITIES" sub-panel
		opportunitiesSubpanelCtrl.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}