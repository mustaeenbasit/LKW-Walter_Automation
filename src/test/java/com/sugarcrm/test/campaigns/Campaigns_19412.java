package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19412 extends SugarTest {
	DataSource targetListData = new DataSource();

	public void setup() throws Exception {
		targetListData = testData.get(testName);
		CampaignRecord myCampaign;

		// Create Campaign Record
		myCampaign = (CampaignRecord) sugar.campaigns.api.create();
		sugar.login();

		// Change campaign type to "Newsletter" (3 target lists are linked to Newsletter campaign by default)
		myCampaign.navToRecord();
		sugar.campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.campaigns.editView.getEditField("type").set("Newsletter");
		VoodooUtils.focusDefault();
		sugar.campaigns.editView.save();
	}

	/**
	 * Target List management_Verify that "Unlink" function in "Target List" sub-panel works correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19412_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Need focus to bwc-frame
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1072
		VoodooControl targetListSubpanel = new VoodooControl("div", "css", "#list_subpanel_prospectlists");

		// Verifying target list exist before deletion in target list subpanel
		targetListSubpanel.assertContains(targetListData.get(0).get("targetListName"), true);

		// TODO: VOOD-1072
		// Unlink first target list from Campaign subpanel
		new VoodooControl("span", "css", "#list_subpanel_prospectlists tr:nth-child(3) .ab").click();
		new VoodooControl("ul", "css", "#list_subpanel_prospectlists tr:nth-child(3) .subnav.ddopen").click();
		VoodooUtils.acceptDialog();

		// Verifying target list is deleted from target list subpanel
		targetListSubpanel.assertContains(targetListData.get(0).get("targetListName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}