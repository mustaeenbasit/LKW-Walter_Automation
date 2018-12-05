package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27106 extends SugarTest {

	FieldSet fs;
	public void setup() throws Exception {
		VoodooUtils.clearLocalStorage();
		fs = testData.get(testName).get(0);
		sugar().login();
		sugar().meetings.api.create();
	}

	/**
	 * Verify that Help Dashlet appears at the first time in listview and recordview in Meetings module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27106_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();

		// This test is not fit for automation and is best executed manually as this bevaiour is valid for a fresh instance only.
		// For all occasions, the first dashboard that appears by default is - My Dashboard
		if (sugar().home.dashboard.getControl("dashboardTitle").getText().equals(fs.get("my_dashboard_title"))) {
			sugar().home.dashboard.chooseDashboard(fs.get("dashboard_title"));
		}
			
		// TODO: VOOD-1256
		VoodooControl helpDashBoardCtrl = new VoodooControl("span", "css", "div[data-voodoo-name='help-dashboard-headerpane'] span[data-voodoo-name='name']");
		VoodooControl helpDashletCtrl = new VoodooControl("h4", "css", "ul.dashlet-cell.rows.row-fluid.layout_Home > li:nth-child(1) h4");

		// Verify that Help Dashlet appears at the first time on listview 
		helpDashBoardCtrl.assertEquals(fs.get("dashboard_title"), true);
		helpDashletCtrl.assertContains(fs.get("listview_dashlet_title"), true);

		sugar().meetings.listView.clickRecord(1);

		// This test is not fit for automation and is best executed manually as this bevaiour is valid for a fresh instance only.
		// For all occasions, the first dashboard that appears by default is - My Dashboard
		if (sugar().home.dashboard.getControl("dashboardTitle").getText().equals(fs.get("my_dashboard_title"))) {
			sugar().home.dashboard.chooseDashboard(fs.get("dashboard_title"));
		}

		// Verify that Help Dashlet appears at the first time on recordview 
		helpDashBoardCtrl.assertEquals(fs.get("dashboard_title"), true);
		helpDashletCtrl.assertContains(fs.get("recordview_dashlet_title"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}