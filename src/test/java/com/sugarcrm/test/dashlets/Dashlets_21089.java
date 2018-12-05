package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21089 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that call record is displayed in "Planned Activities" dashlet of "Leads" detail view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21089_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Leads record view
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		FieldSet customData = testData.get(testName).get(0);

		// Choose "My Dashboard"
		VoodooControl dashboardTitle = sugar().opportunities.dashboard.getControl("dashboard");;
		if(!dashboardTitle.queryContains(customData.get("my_dashboard"), true)){
			sugar().opportunities.dashboard.chooseDashboard(customData.get("my_dashboard"));
		}

		// In the "Planned Activities" dashlet, click "Log Call" button
		// TODO: VOOD-960
		new VoodooControl("span", "css", ".dashboard ul li ul li:nth-child(1) .well-invisible .dashlet-cell .fa.fa-plus").click();
		new VoodooControl("a", "css", ".layout_Home div:nth-child(1) span ul li:nth-child(2) a").click();

		// Enter the required fields and click "Save" button
		sugar().calls.createDrawer.getEditField("name").set(sugar().calls.getDefaultData().get("name"));
		sugar().calls.createDrawer.save();

		// Verify the scheduled call record is displayed in "Planned Activities" dashlet
		// TODO: VOOD-960
		new VoodooControl("div", "css", ".layout_Home .tab2 div div:nth-child(2)").click();
		new VoodooControl("a", "css", ".layout_Home .dashlet-unordered-list .tab-content a:nth-child(2)").assertContains(sugar().calls.getDefaultData().get("name"), true);

		// Verify call is displayed on Calendar
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "id", "cal-scrollable").assertContains(sugar().calls.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}