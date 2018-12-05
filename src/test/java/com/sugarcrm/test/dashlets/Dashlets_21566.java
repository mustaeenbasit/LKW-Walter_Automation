package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21566 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify Opportunity Metrics in Account RHS pane
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21566_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-960 - Dashlet selection
		// Go to account recordView
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Select My Dashboard, if Help dashboard 
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboard");
		FieldSet customFS = testData.get(testName).get(0);
		if(!dashboardTitle.queryContains(customFS.get("dashboardTitle"), true)) {
			sugar().dashboard.chooseDashboard(customFS.get("dashboardTitle"));
		}

		// Creating Opportunity record from Accounts subpanel
		DataSource customData = testData.get(testName+"_data");
		VoodooControl dashletShowsCtrl = new VoodooControl("text", "css", ".label.active");
		VoodooControl activeAmountCtrl = new VoodooControl("div", "css", ".deal-amount-metric.active");
		VoodooControl gearIconCtrl = new VoodooControl("i", "css", ".layout_Home .dashlet-header button i");
		VoodooControl refreshLinkCtrl = new VoodooControl("a", "css", ".dropdown-menu.left li:nth-child(2) a");
		String expectedClosedDate = sugar().opportunities.getDefaultData().get("rli_expected_closed_date");
		StandardSubpanel oppSubpanel = sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		for (int i = 0; i < customData.size(); i++) {
			// Click on Plus("+") icon to add Opportunity with Rli
			oppSubpanel.addRecord();
			sugar().opportunities.createDrawer.getEditField("name").set(customData.get(i).get("opp_name"));
			sugar().opportunities.createDrawer.getEditField("rli_name").set(customData.get(i).get("rli_name"));
			sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(expectedClosedDate);
			sugar().opportunities.createDrawer.getEditField("rli_likely").set(customData.get(i).get("rli_likely"));
			sugar().opportunities.createDrawer.getEditField("rli_stage").set(customData.get(i).get("rli_stage"));
			sugar().opportunities.createDrawer.save();

			// Refresh Opportunity Metrics dashlet
			gearIconCtrl.click();
			refreshLinkCtrl.click();
			VoodooUtils.waitForReady();
			if (i < 2) {
				// Verify "Opportunity Metrics" dashlet  shows "1/2" for Active status and amount is $100/$300.
				dashletShowsCtrl.assertContains(customData.get(i).get("verify_dashlet_shows"), true);
				activeAmountCtrl.assertContains(customData.get(i).get("verifyAmount"), true);
			} else if (i == 2) {
				// Verify "Opportunity Metrics" dashlet  shows "1" for Closed Won status and amount is $300.
				new VoodooControl("text", "css", ".label.won");
				new VoodooControl("div", "css", ".deal-amount-metric.won").assertContains(customData.get(i).get("verifyAmount"), true);
			} else {
				// Verify "Opportunity Metrics" dashlet  shows "1" for Closed Lost status and amount is $0, and verify that information displayed in the chart is the same as we created opportunities
				new VoodooControl("text", "css", ".label.lost");
				new VoodooControl("div", "css", ".deal-amount-metric.lost").assertContains(customData.get(i).get("verifyAmount"), true);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}