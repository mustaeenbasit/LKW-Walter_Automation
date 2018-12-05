package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28919 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().opportunities.create();
	}

	/**
	 * Verify that Expected Close Date, Likely, Best and Worst fields are
	 * read-only when record opens up on duplicate window.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28919_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();

		// Create duplicate opportunity record
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().opportunities.getDefaultData().get("relAccountName"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(sugar().opportunities.getDefaultData().get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().opportunities.getDefaultData().get("rli_likely"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.save();

		// Verify the duplicate panel appears
		sugar().opportunities.createDrawer.getControl("duplicateCount").assertVisible(true);
		sugar().opportunities.createDrawer.getControl("duplicateHeaderRow").assertVisible(true);

		// Clicking the select link on duplicate window
		sugar().opportunities.createDrawer.selectAndEditDuplicate(1);
		VoodooUtils.waitForReady();

		// Verifying the Expected Close Date, Likely, Best and Worst as read-only fields
		Assert.assertTrue("Expected Close Date field is enabled to Edit", sugar().opportunities.recordView.getEditField("date_closed").isDisabled());
		Assert.assertTrue("Best field is enabled to Edit", sugar().opportunities.recordView.getEditField("bestCase").isDisabled());
		Assert.assertTrue("Worst field is enabled to Edit", sugar().opportunities.recordView.getEditField("worstCase").isDisabled());
		Assert.assertTrue("Likely field is enabled to Edit", sugar().opportunities.recordView.getEditField("likelyCase").isDisabled());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}