package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19425 extends SugarTest {
	FieldSet fs;

	public void setup() throws Exception {
		fs =testData.get(testName).get(0);
		FieldSet campaignData = new FieldSet();
		campaignData.put("type", fs.get("type"));
		sugar.campaigns.api.create(campaignData);
		sugar.login();
	}

	/**
	 * Verify that creating a tracker URL with "Opt"-out Link?" checked works as expected.
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19425_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.campaigns.navToListView();
		sugar.campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Entering the Information in Campaign Tracker.
		// TODO: VOOD-1028
		new VoodooControl("a", "id", "campaign_campaigntrakers_create_button").click();
		new VoodooControl("input", "id", "tracker_name").set(testName);

		// Selecting the Opt-out option in the Tracker.
		new VoodooControl("input", "id", "is_optout").click();
		new VoodooControl("input", "id", "save_button").click();

		// Verifying the information entered in Tracker is same as entered.
		new VoodooControl("span","css", "#list_subpanel_tracked_urls .oddListRowS1 span")
		.assertEquals(testName,true);
		new VoodooControl("span","css", "#list_subpanel_tracked_urls .oddListRowS1 td:nth-child(2) span")
		.assertEquals(fs.get("url"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}