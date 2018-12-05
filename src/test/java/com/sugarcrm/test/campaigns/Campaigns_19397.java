package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19397 extends SugarTest {
	DataSource campaignsData = new DataSource();

	public void setup() throws Exception {
		campaignsData = testData.get(testName);
		sugar().campaigns.api.create(campaignsData);
		sugar().login();
	}

	/**
	 * List campaigns_Verify that sort as ASC/DESC function in the Campaign listview works correctly
	 * @throws Exception
	 * */


	@Test
	public void Campaigns_19397_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Campaigns module.
		sugar().navbar.navToModule(sugar().campaigns.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1534: Need lib support to sort columns in BWC module
		//  Click "Campaign" name
		VoodooControl nameCtrl = new VoodooControl("th", "xpath", "//*[@id='MassUpdate']/table/tbody/tr/th[contains(.,'Campaign')]/div/a");
		nameCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		// Verify that The campaign list is sorted by name in ascending order
		sugar().campaigns.listView.verifyField(1, "name", campaignsData.get(0).get("name"));
		sugar().campaigns.listView.verifyField(2, "name", campaignsData.get(1).get("name"));
		sugar().campaigns.listView.verifyField(3, "name", campaignsData.get(2).get("name"));
		VoodooUtils.focusFrame("bwc-frame");
		// Click "Campaign" name
		nameCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Verify that The campaign list is sorted by name in descending order
		sugar().campaigns.listView.verifyField(1, "name", campaignsData.get(2).get("name"));
		sugar().campaigns.listView.verifyField(2, "name", campaignsData.get(1).get("name"));
		sugar().campaigns.listView.verifyField(3, "name", campaignsData.get(0).get("name"));

		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1534: Need lib support to sort columns in BWC module
		VoodooControl statusCtrl = new VoodooControl("th", "xpath", "//*[@id='MassUpdate']/table/tbody/tr/th[contains(.,'Status')]/div/a");
		// Click "Campaign" status
		statusCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		// Verify that The campaign list is sorted by status in ascending order
		sugar().campaigns.listView.verifyField(1, "status", campaignsData.get(1).get("status"));
		sugar().campaigns.listView.verifyField(2, "status", campaignsData.get(2).get("status"));
		sugar().campaigns.listView.verifyField(3, "status", campaignsData.get(0).get("status"));
		VoodooUtils.focusFrame("bwc-frame");
		// Click "Campaign" status
		statusCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		// Verify that The campaign list is sorted by status in descending order
		sugar().campaigns.listView.verifyField(1, "status", campaignsData.get(0).get("status"));
		sugar().campaigns.listView.verifyField(2, "status", campaignsData.get(2).get("status"));
		sugar().campaigns.listView.verifyField(3, "status", campaignsData.get(1).get("status"));
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1534: Need lib support to sort columns in BWC module
		VoodooControl typeCtrl = new VoodooControl("th", "xpath", "//*[@id='MassUpdate']/table/tbody/tr/th[contains(.,'Type')]/div/a");
		// Click "Campaign" type
		typeCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		// Verify that The campaign list is sorted by type in ascending order
		sugar().campaigns.listView.verifyField(1, "type", campaignsData.get(2).get("type"));
		sugar().campaigns.listView.verifyField(2, "type", campaignsData.get(1).get("type"));
		sugar().campaigns.listView.verifyField(3, "type", campaignsData.get(0).get("type"));
		VoodooUtils.focusFrame("bwc-frame");
		// // Click "Campaign" type
		typeCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		// Verify that The campaign list is sorted by type in descending order
		sugar().campaigns.listView.verifyField(1, "type", campaignsData.get(0).get("type"));
		sugar().campaigns.listView.verifyField(2, "type", campaignsData.get(1).get("type"));
		sugar().campaigns.listView.verifyField(3, "type", campaignsData.get(2).get("type"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
