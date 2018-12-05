package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;

public class Campaigns_19351 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();
		sugar.campaigns.api.create();
	}

	/**
	 * Verify that campaign can be edited from detail view.
	 * */
	@Test
	public void Campaigns_19351_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-692
		sugar.campaigns.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "table.list.view tbody tr:nth-of-type(3) td:nth-of-type(4) a").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		VoodooControl nameCtrl = new VoodooControl("span", "id", "name");
		VoodooControl statusCtrl = new VoodooControl("span", "id", "status");
		VoodooControl typeCtrl = new VoodooControl("span", "id", "campaign_type");

		// Verifying default values
		nameCtrl.assertEquals(sugar.campaigns.getDefaultData().get("name"), true);
		statusCtrl.assertAttribute("value", sugar.campaigns.getDefaultData().get("status"), true);
		typeCtrl.assertAttribute("value", sugar.campaigns.getDefaultData().get("type"), true);

		// Edit and save campaign
		new VoodooControl("a", "id", "edit_button").click();
		new VoodooControl("input", "id", "name").set(customData.get("name"));
		new VoodooControl("select", "id", "status").set(customData.get("status"));
		new VoodooControl("select", "id", "campaign_type").set(customData.get("type"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForAlertExpiration(); // add more wait after save

		// Verifying updated values
		nameCtrl.assertEquals(customData.get("name"), true);
		statusCtrl.assertAttribute("value", customData.get("status"), true);
		typeCtrl.assertAttribute("value", customData.get("type"), true);

		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}