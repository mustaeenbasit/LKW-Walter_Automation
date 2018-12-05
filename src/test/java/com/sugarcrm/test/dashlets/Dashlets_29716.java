package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_29716 extends SugarTest {
	VoodooControl dashboardTitleCtrl;
	FieldSet dashboardData = new FieldSet();

	public void setup() throws Exception {
		dashboardData = testData.get(testName).get(0);
		dashboardTitleCtrl = sugar().leads.dashboard.getControl("dashboardTitle");
		sugar().leads.api.create();

		// Login as an Admin user
		sugar().login();
	}

	private void dashbaordVerification() throws Exception {
		// Verify that user would find 'Help Dashboard' by default in the intelligence pane.(R.H.S)
		dashboardTitleCtrl.assertContains(dashboardData.get("helpDashboard"), true);

		// Click the arrow next to 'Help Dashboard' and choose the 'My Dashboard'
		sugar().dashboard.chooseDashboard(dashboardData.get("myDashboard"));

		// Verify that user would find 'My Dashboard' getting visible now
		dashboardTitleCtrl.assertContains(dashboardData.get("myDashboard"), true);

		// Click the arrow next to 'My Dashboard' and choose the 'Help Dashboard'
		sugar().dashboard.chooseDashboard(dashboardData.get("helpDashboard"));

		// Verify that user would find 'Help Dashboard' getting visible again
		dashboardTitleCtrl.assertContains(dashboardData.get("helpDashboard"), true);
	}

	/**
	 * Verify that 'No saved dashboards' text does not appear in place of 'My Dashboard' or 'Help Dashboard'
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_29716_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		for(int i = 0; i < 2; i++) {
			if(i == 1) {
				// Logout from Admin user and login as a Regular User
				sugar().logout();
				sugar().login(sugar().users.getQAUser());
			}

			// Verification of dash-board on List View
			// Click any of the side-car modules (except 'Contact') in the navigation bar.
			sugar().leads.navToListView();
			dashbaordVerification();

			// Verification of dash-board on Record View
			sugar().leads.listView.clickRecord(1);
			dashbaordVerification();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}