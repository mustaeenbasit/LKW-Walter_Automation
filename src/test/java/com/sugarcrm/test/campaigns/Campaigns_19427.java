package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19427 extends SugarTest {
	DataSource campaignSettings;

	public void setup() throws Exception {
		campaignSettings =testData.get(testName);

		// Setting Campaign of Email Type
		FieldSet campaignData = new FieldSet();
		campaignData.put("type", campaignSettings.get(0).get("type"));
		sugar.campaigns.api.create(campaignData);
		sugar.login();

		// Navigating to Campaign List View
		sugar.campaigns.navToListView();

		// Navigating to Campaign Record View
		sugar.campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Entering the Information in Campaign Tracker.
		// TODO: VOOD-1028
		VoodooControl campaignTrackerCreateBtn = new VoodooControl("a", "id", "campaign_campaigntrakers_create_button");
		VoodooControl trackerName = new VoodooControl("input", "id", "tracker_name");
		VoodooControl saveButton = new VoodooControl("input", "id", "save_button");
		VoodooControl trackerUrl = new VoodooControl("input", "id","tracker_url");
		for(int i = 0; i < campaignSettings.size(); i++) {
			campaignTrackerCreateBtn.click();
			trackerName.set(campaignSettings.get(i).get("name"));
			trackerUrl.set(campaignSettings.get(i).get("url"));
			saveButton.click();
		}
	}

	/**
	 * Tacker URLs management_Verify that tracker URLs list can be sort
	 * by column header in "Tracker URLs" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19427_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1534
		VoodooControl nameHeaderCtrl = new VoodooControl("a", "css", "#list_subpanel_tracked_urls tr:nth-child(2) th:nth-child(1) a");
		VoodooControl urlHeaderCtrl = new VoodooControl("a", "css", "#list_subpanel_tracked_urls tr:nth-child(2) th:nth-child(2) a");
		VoodooControl keyHeaderCtrl = new VoodooControl("a", "css", "#list_subpanel_tracked_urls tr:nth-child(2) th:nth-child(3) a");

		// TODO: VOOD-1028
		VoodooControl nameRow1 = new VoodooControl("td","css", "#list_subpanel_tracked_urls tr.oddListRowS1 > td:nth-child(1)");
		VoodooControl nameRow2 = new VoodooControl("td","css", "#list_subpanel_tracked_urls tr.evenListRowS1 > td:nth-child(1)");
		VoodooControl urlRow1 = new VoodooControl("td","css", "#list_subpanel_tracked_urls tr.oddListRowS1 > td:nth-child(2)");
		VoodooControl urlRow2 = new VoodooControl("td","css", "#list_subpanel_tracked_urls tr.evenListRowS1 > td:nth-child(2)");
		VoodooControl keyRow1 = new VoodooControl("td","css", "#list_subpanel_tracked_urls tr.oddListRowS1 > td:nth-child(3)");
		VoodooControl keyRow2 = new VoodooControl("td","css", "#list_subpanel_tracked_urls tr.evenListRowS1 > td:nth-child(3)");

		// Clicking the Name Header in Tracker Url subpanel
		nameHeaderCtrl.click();

		// Verify Name is in Descending Order
		nameRow1.assertEquals(campaignSettings.get(1).get("name"), true);
		nameRow2.assertEquals(campaignSettings.get(0).get("name"), true);
		nameHeaderCtrl.click();

		// Verify Name is in Ascending Order
		nameRow1.assertEquals(campaignSettings.get(0).get("name"), true);
		nameRow2.assertEquals(campaignSettings.get(1).get("name"), true);

		// Clicking the Url Header in Tracker Url subpanel
		urlHeaderCtrl.click();

		// Verify Url is in Descending Order
		urlRow1.assertEquals(campaignSettings.get(1).get("url"), true);
		urlRow2.assertEquals(campaignSettings.get(0).get("url"), true);
		urlHeaderCtrl.click();

		// Verify Url is in Ascending Order
		urlRow1.assertEquals(campaignSettings.get(0).get("url"), true);
		urlRow2.assertEquals(campaignSettings.get(1).get("url"), true);

		// Clicking the Key Header in Tracker Url subpanel
		keyHeaderCtrl.click();

		// Verify key is in Descending Order
		keyRow1.assertEquals(campaignSettings.get(1).get("key"), true);
		keyRow2.assertEquals(campaignSettings.get(0).get("key"), true);
		keyHeaderCtrl.click();

		// Verify key is in Ascending Order
		keyRow1.assertEquals(campaignSettings.get(0).get("key"), true);
		keyRow2.assertEquals(campaignSettings.get(1).get("key"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");

	}

	public void cleanup() throws Exception {}
}