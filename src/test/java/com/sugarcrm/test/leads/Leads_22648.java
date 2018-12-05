package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22648 extends SugarTest {

	public void setup() throws Exception {
		// Create test Lead record
		sugar().leads.api.create();
		
		// Login as admin
		sugar().login();
	}

	/**
	 * Schedule Meeting By Full Form_Verify that the meeting is not scheduled for lead when using "Cancel" function.
	 * @throws Exception
	 */
	@Test
	public void Leads_22648_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		FieldSet dashletData = testData.get(testName).get(0);
		
		// Navigate to the Leads module and click the lead record to open its record view
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);

		// In the RHS pane switch to My Dashboard
		VoodooControl dashboardTitle = sugar().leads.dashboard.getControl("dashboard");
		if(!dashboardTitle.queryContains(dashletData.get("myDashboard"), true))
			sugar().dashboard.chooseDashboard(dashletData.get("myDashboard"));

		// TODO: VOOD-670 - More Dashlet Support
		// Click the Schedule meeting option in the Planned Activities Dashlet
		new VoodooControl("a", "css", ".dashboard ul.dashlet-row li .actions").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".actions.dashlet-toolbar .dropdown-menu a").click();
		VoodooUtils.waitForReady();

		// Click the cancel link in the Meetings create drawer
		sugar().meetings.createDrawer.cancel();
		String dashletCtrl = "[data-voodoo-name='planned-activities'] ";
		
		// Assert that the tab is for module Meeting
		new VoodooControl("div", "css", dashletCtrl + ".dashlet-tab.active").assertContains(sugar().meetings.moduleNamePlural, true);
		
		// Assert that there is no new meeting created in "Planned Activities" dashlet
		new VoodooControl("span", "css", dashletCtrl + ".dashlet-tab.active .count").assertEquals(dashletData.get("meetingCount"), true);
		
		// Assert that there is no data available in this meeting tab
		new VoodooControl("div", "css", dashletCtrl + ".tab-pane.active").assertEquals(dashletData.get("noData"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}