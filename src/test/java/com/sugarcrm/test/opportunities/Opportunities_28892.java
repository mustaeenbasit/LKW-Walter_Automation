package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;

public class Opportunities_28892 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that after edit Opportunity Name should be shown correctly
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28892_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Opportunity List View
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Editing the name of the Opportunity
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("name").set(testName);
		sugar().opportunities.recordView.getEditField("relAccountName").set(
				sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.recordView.save();

		// Clicking the edit Button again, After record is saved.
		sugar().opportunities.recordView.edit();

		// Verify that the Opportunity name is shown correctly as entered.
		sugar().opportunities.recordView.getEditField("name").assertEquals(
				testName, true);
		sugar().opportunities.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
