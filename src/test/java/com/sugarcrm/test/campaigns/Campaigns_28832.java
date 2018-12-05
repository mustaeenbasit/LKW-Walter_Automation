package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_28832 extends SugarTest {
	FieldSet moduleNameData;

	public void setup() throws Exception {
		moduleNameData = testData.get(testName).get(0);

		// Create a Campaign record of type 'Email'
		FieldSet campaignData = new FieldSet();
		campaignData.put("type", moduleNameData.get("type"));
		sugar.campaigns.api.create(campaignData);
		sugar.login();
	}

	/**
	 * Verify that Campaign tracker module should not be displayed in the mega menu bar
	 * 
	 * @throws Exception
	 * */
	@Test
	public void Campaigns_28832_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Campaigns module and go to a campaign's detail view.
		sugar.campaigns.navToListView();
		sugar.campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Click "Create" button in the "Tracker URLs" sub-panel
		// TODO: VOOD-1028
		VoodooControl campaignTrackerCreateBtnCtrl = new VoodooControl("a", "id", "campaign_campaigntrakers_create_button");
		VoodooControl cancelBtnCtrl = new VoodooControl("input", "id", "cancel_button");
		campaignTrackerCreateBtnCtrl.scrollIntoViewIfNeeded(false);
		campaignTrackerCreateBtnCtrl.click();
		VoodooUtils.focusDefault();

		// Verify that Campaign Trackers module should not be displayed in the mega menu bar
		sugar.navbar.assertContains(moduleNameData.get("campaignTrackers"), false);
		sugar.navbar.showAllModules();

		// Also verifying Campaign Trackers on the overflow menu 
		// TODO: VOOD-784
		new VoodooControl("ul", "css", "li.dropdown.more .dropdown-menu ul").assertContains(moduleNameData.get("campaignTrackers"), false);
		new VoodooControl("ul", "css", "li.dropdown.more .dropdown-menu ul").assertContains("Cases", true);

		// Click on the cancel button
		VoodooUtils.focusFrame("bwc-frame");
		cancelBtnCtrl.click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}