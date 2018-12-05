package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_29133 extends SugarTest {
	VoodooControl activeDropdown, advancedReportingCtrl, activeDropdownValue;

	public void setup() throws Exception {
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify the access rights on Advanced Reports through Non admin user
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_29133_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1559
		// Define controls
		VoodooControl advancedReportingCtrl = sugar().reports.menu.getControl("manageAdvancedReports");
		VoodooControl activeDropdown = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");

		// Go to Reports Module	
		sugar().reports.navToListView();

		// Click on Reports -> Manage Advanced Reports
		activeDropdown.click();
		advancedReportingCtrl.click();

		// Go to Reports Module	
		sugar().reports.navToListView();

		// Click on Reports -> Manage Advanced Reports
		activeDropdown.click();
		advancedReportingCtrl.click();

		// Click on active drop-down arrow
		activeDropdown.click();

		// Verify that Non-Admin user CANNOT create, edit, delete, schedule, run, view results, export and Custom Queries and the Create option is NOT available in Advanced Reports mega menu
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_REPORTMAKER']").assertExists(false);
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_CUSTOMQUERY']").assertExists(false);
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_DATASET']").assertExists(false);

		// Verify that Non-Admin user CAN "View Custom Queries, View Advanced Reports, View Data Formats and View Reports".
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_LIST_REPORTMAKER']").assertExists(true);
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_CUSTOMQUERIES']").assertExists(true);
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_LIST_DATASET']").assertExists(true);
		new VoodooControl("a", "css", "[data-navbar-menu-item='LBL_ALL_REPORTS']").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}