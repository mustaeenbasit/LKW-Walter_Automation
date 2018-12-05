package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19408 extends SugarTest {
	CampaignRecord myCampaign;
	TargetListRecord myTargetList;
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		myCampaign = (CampaignRecord) sugar.campaigns.api.create(ds.get(0));
		myTargetList = (TargetListRecord)sugar.targetlists.api.create();

		sugar.login();
	}

	/**
	 * Verify that "Select" target list function in the campaign details view works correctly
	 *
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19408_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Goto to Campaign record
		myCampaign.navToRecord();

		// Assign Target list to Campaign
		// TODO: VOOD-1028
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", ".pagination > td > table > tbody > tr > td:nth-child(1) span").click();
		new VoodooControl("a", "id","prospect_list_campaigns_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0); // move back the focus to default window
		VoodooUtils.focusDefault();

		// Already on Campaign record
		// Verify target list records selected are displayed in the "Target List" sub-panel correctly.
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("tr", "css", "#list_subpanel_prospectlists tr.oddListRowS1").assertContains(myTargetList.getRecordIdentifier(), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
