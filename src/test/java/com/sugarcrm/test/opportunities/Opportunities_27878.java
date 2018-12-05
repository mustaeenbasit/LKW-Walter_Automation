package com.sugarcrm.test.opportunities;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Opportunities_27878 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that no FATAL in log when switch between Opportunity <-->Opportunity + RLI
	 *
	 * @throws Exception
	 */

	@Test
	public void Opportunities_27878_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet data = testData.get(testName).get(0);

		FieldSet oppSettingsData = new FieldSet();
		oppSettingsData.put("desiredView", data.get("desiredView"));
		oppSettingsData.put("rollUp", data.get("rollUp"));

		sugar().admin.switchOpportunityView(oppSettingsData);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		sugar().opportunities.navToListView();
		// Verify that app brought user to Opportunity list view
		sugar().opportunities.listView.assertVisible(true);
		sugar().opportunities.listView.getControl("moduleTitle").assertVisible(true);

		// Check out the log in Admin->System Settings
		sugar().navbar.navToAdminTools();
		new VoodooControl("iframe", "css", "#bwc-frame").waitForVisible();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("systemSettings").click();
		new VoodooControl("a", "css", ".edit.view [target='_blank']").click(); // Click on 'View Log' link
		VoodooUtils.focusWindow(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that no FATAL error like 'Non-distinct result set detected: sqlRows = 21 vs beanSet = 13'
		new VoodooControl("div", "id", "content").assertContains(data.get("error"), false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}