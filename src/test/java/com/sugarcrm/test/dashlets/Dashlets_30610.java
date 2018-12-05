package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_30610 extends SugarTest {
	VoodooControl configureDashletCtrl, editDashletOption, dashletHeading, selectModule, saveDashletCtrl, dashboardTitle;
	String leadModuleNamePlural = "";
	String myDashboard = "";

	public void setup() throws Exception {
		// TODO: VOOD-963 - Some dashboard controls are needed
		configureDashletCtrl = new VoodooControl("button", "css", ".dashlet-cell.layout_Home .dashlet-header .btn-toolbar .dropdown-toggle");
		editDashletOption = new VoodooControl("a", "css", ".dashlet-cell.layout_Home .dashlet-header .btn-toolbar .btn-group.open [data-dashletaction='editClicked']");
		dashletHeading = new VoodooControl("h4", "css", ".dashlet-cell.layout_Home .dashlet-header h4");
		selectModule = new VoodooSelect("a", "css", ".edit.fld_module a");
		saveDashletCtrl = new VoodooControl("a", "css", ".fld_save_button a:not([class*='hide'])");
		dashboardTitle = sugar().bugs.dashboard.getControl("dashboard");
		leadModuleNamePlural = sugar().leads.moduleNamePlural;
		myDashboard = testData.get(testName).get(0).get("myDashboard");

		// Log-in as Admin
		sugar().login();
	}

	/**
	 * Verify "My scheduled meeting/call” Dashlet should be editable
	 * @throws Exception
	 */
	@Test
	public void Dashlets_30610_Meetings_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Meetings list view
		sugar().meetings.navToListView();

		// Select My Dashboard, if Help dashboard 
		if(!dashboardTitle.queryContains(myDashboard, true)) {
			sugar().dashboard.chooseDashboard(myDashboard);
		}

		// Verify "My Scheduled Meetings" dashlet is present
		dashletHeading.assertContains(sugar().meetings.moduleNamePlural, true);

		// Click Gear icon
		configureDashletCtrl.click();

		// Click Edit
		editDashletOption.click();
		VoodooUtils.waitForReady();

		// Select Leads module
		selectModule.set(leadModuleNamePlural);
		VoodooUtils.waitForReady();

		// Save
		saveDashletCtrl.click();
		VoodooUtils.waitForReady();

		// Verify "Leads" dashlet is present
		dashletHeading.assertContains(leadModuleNamePlural, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	@Test
	public void Dashlets_30610_Calls_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Calls list view
		sugar().calls.navToListView();

		// Select My Dashboard, if Help dashboard 
		if(!dashboardTitle.queryContains(myDashboard, true)) {
			sugar().dashboard.chooseDashboard(myDashboard);
		}

		// Verify "My Scheduled Calls" dashlet is present
		dashletHeading.assertContains(sugar().calls.moduleNamePlural, true);

		// Click Gear icon
		configureDashletCtrl.click();

		// Click Edit
		editDashletOption.click();
		VoodooUtils.waitForReady();

		// Select Leads module
		selectModule.set(leadModuleNamePlural);
		VoodooUtils.waitForReady();

		// Save
		saveDashletCtrl.click();
		VoodooUtils.waitForReady();

		// Verify "Leads" dashlet is present
		dashletHeading.assertContains(leadModuleNamePlural, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}