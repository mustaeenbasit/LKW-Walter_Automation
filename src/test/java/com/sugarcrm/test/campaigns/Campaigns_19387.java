package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19387 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		sugar.campaigns.api.create();
		customData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify that campaign can be edited by "Edit" icon from list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19387_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Campaigns list view.
		sugar.campaigns.navToListView();
		// Click the "Edit" icon from list view for a campaign
		sugar.campaigns.listView.editRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		// modify campaign information, such as "Name", "Status"
		sugar.campaigns.editView.getEditField("name").set(testName);
		sugar.campaigns.editView.getEditField("status").set(customData.get("status"));
		VoodooUtils.focusDefault();
		// Save
		sugar.campaigns.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		// Verify campaign is updated.
		sugar.campaigns.detailView.getDetailField("name").assertContains(testName, true);
		sugar.campaigns.detailView.getDetailField("status").assertContains(customData.get("status"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}