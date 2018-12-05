package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Basant Chandak <bchandak@sugarcrm.com>
 */
public class Campaigns_19394 extends SugarTest {
	DataSource campaignData = new DataSource();

	public void setup() throws Exception {
		campaignData = testData.get(testName);
		sugar.campaigns.api.create(campaignData);
		sugar.login();
	}

	/**
	 * Search campaign_Verify that basic search function in the "Campaign Search" 
	 * works correctly.(Only my items)
	 */
	@Test
	public void Campaigns_19394_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Assigning a campaign Record to qaUser
		sugar.campaigns.navToListView();
		sugar.campaigns.listView.clickRecord(1);
		sugar.campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.campaigns.editView.getEditField("assignedTo").set(sugar.users.getQAUser().get("userName"));
		VoodooUtils.focusDefault();
		sugar.campaigns.editView.save();

		// Verifying currently both campaign records are shown
		sugar.campaigns.navToListView();
		sugar.campaigns.listView.verifyField(1, "name", campaignData.get(1).get("name"));
		sugar.campaigns.listView.verifyField(2, "name", campaignData.get(0).get("name"));

		// Clicking My Items checkbox only
		VoodooUtils.focusFrame("bwc-frame");
		sugar.campaigns.listView.getControl("myItemsCheckbox").click();
		VoodooUtils.focusDefault();
		sugar.campaigns.listView.submitSearchForm();

		// Verifying campaign assigned to Administrator is only visible
		sugar.campaigns.listView.verifyField(1, "name", campaignData.get(0).get("name"));
		VoodooUtils.focusFrame("bwc-frame");
		sugar.campaigns.listView.getControl("checkbox03").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}