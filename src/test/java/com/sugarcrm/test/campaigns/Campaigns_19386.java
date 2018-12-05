package com.sugarcrm.test.campaigns;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19386 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Create Campaign_Verify that creating campaign by &8220;Create Campaign (Classic)&8221; function can be canceled.
	 *
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19386_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Campaigns module and click "Create Campaign (Classic)" link in shortcuts navigation
		sugar.navbar.navToModule(sugar.campaigns.moduleNamePlural);
		sugar.navbar.clickModuleDropdown(sugar.campaigns);
		sugar.campaigns.menu.getControl("createCampaignClassic").click();
		VoodooUtils.focusFrame("bwc-frame");
		// Enter required fields
		FieldSet fs = sugar.campaigns.getDefaultData();
		sugar.campaigns.editView.getEditField("name").set(fs.get("name"));
		sugar.campaigns.editView.getEditField("status").set(fs.get("status"));
		sugar.campaigns.editView.getEditField("type").set(fs.get("type"));
		sugar.campaigns.editView.getEditField("date_end").set(fs.get("date_end"));
		VoodooUtils.focusDefault();
		// Click Cancel
		sugar.campaigns.editView.cancel();

		// Verify campaign is not saved and campaign list view is displayed as current page.
		VoodooUtils.focusFrame("bwc-frame");
		sugar.campaigns.listView.assertVisible(true);
		VoodooUtils.focusDefault();
		Assert.assertTrue("Record is not saved", sugar.campaigns.listView.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}