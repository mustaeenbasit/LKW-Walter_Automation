package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;

public class Campaigns_19361 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
		sugar.campaigns.api.create();
	}

	/**
	 * ROI View_Verify
	 * */
	@Test
	public void Campaigns_19361_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-692
		sugar.campaigns.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "table.list.view tbody tr:nth-of-type(3) td:nth-of-type(4) a").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1072
		new VoodooControl("input", "id", "viewRoiButtonId").click();

		// Verify ROI report is displayed on the page with default campaign record data
		new VoodooControl("slot", "css", ".detail.view tr:nth-child(1) td:nth-child(2) slot").assertContains(sugar.campaigns.defaultData.get("name"), true);
		new VoodooControl("slot", "css", ".detail.view tr:nth-child(2) td:nth-child(2) slot").assertContains(sugar.campaigns.defaultData.get("status"), true);
		new VoodooControl("slot", "css", ".detail.view tr:nth-child(4) td:nth-child(2) slot").assertContains(sugar.campaigns.defaultData.get("date_end"), true);
		new VoodooControl("slot", "css", ".detail.view tr:nth-child(5) td:nth-child(2) slot").assertContains(sugar.campaigns.defaultData.get("type"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}