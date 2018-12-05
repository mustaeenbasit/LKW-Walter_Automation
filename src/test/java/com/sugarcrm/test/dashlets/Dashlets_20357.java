package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Dashlets_20357 extends SugarTest {
	public void setup() throws Exception {
		sugar.cases.api.create();
		sugar.login();
	}

	/**
	 * Verify that call record is displayed in "Planned Activities" Dashlet of "Cases" detail view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20357_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar.cases.navToListView();
		sugar.cases.listView.clickRecord(1);

		// TODO: VOOD-963
		VoodooControl dashboardTitle = sugar.accounts.dashboard.getControl("dashboard");			

		// Select My Dashboard, if Help dashboard 
		if(!dashboardTitle.queryContains(customData.get("my_dashboard"), true))
			sugar.dashboard.chooseDashboard(customData.get("my_dashboard"));

		// Log call from Planned activity
		// TODO: VOOD-1305
		new VoodooControl("a", "css", ".dashlets li.row-fluid:nth-of-type(1) .dashlet-header .actions a").click();
		new VoodooControl("a", "css", ".dashlets li.row-fluid:nth-of-type(1) .dashlet-header .actions .dropdown-menu li:nth-of-type(2) a[data-dashletaction='createRecord']").click();
		sugar.calls.createDrawer.getEditField("name").set(customData.get("name"));
		sugar.calls.createDrawer.save();

		// TODO: TR-8565 
		VoodooUtils.refresh();

		// Verify call is in Planned Activity dashlet
		new VoodooControl("div", "css", ".dashlet-tabs.tab2 [class=dashlet-tab]").click();
		new VoodooControl("a", "css", ".dashlets li.row-fluid:nth-of-type(1) .tab-pane.active a:nth-of-type(2)").assertEquals(customData.get("name"), true);

		// Verify the call record in calls subpanel
		StandardSubpanel callSubpanel = sugar.cases.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		callSubpanel.toggleSubpanel();
		VoodooUtils.waitForReady();
		callSubpanel.getDetailField(1, "name").assertEquals(customData.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}