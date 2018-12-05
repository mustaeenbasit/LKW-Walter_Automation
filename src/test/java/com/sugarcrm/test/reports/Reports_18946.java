package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_18946 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that "Reports to ID" item is available in "Related" drop down list under "Assigned to User" module when creating a lead report.
	 * @throws Exception
	 */
	@Test
	public void Reports_18946_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Reports module
		sugar().navbar.navToModule(testData.get(testName).get(0).get("module_plural_name"));

		// Create Rows and Columns report against Leads module
		// TODO: VOOD-822
		new VoodooControl("li", "css", ".dropdown.active .fa-caret-down").click();
		new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li:nth-of-type(1)").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(1) table tbody tr:nth-child(1) td:nth-of-type(1)").click();
		new VoodooControl("table", "id", "Leads").click();

		// Click on "Assigned to User" available in the "Related Modules" panel.
		new VoodooControl("a", "css", "#module_tree div:nth-of-type(2) td.ygtvcell.ygtvcontent a").click();
		new VoodooControl("input", "id", "dt_input").set(testData.get(testName).get(0).get("module_plural_name"));
		VoodooUtils.waitForAlertExpiration(); // Need wait for search completion, field to be visible

		// Verify Reports To ID field under assign to user
		new VoodooControl("div", "css", "#Users_reports_to_id div").assertEquals(testData.get(testName).get(0).get("reports_id"), true);

		// clear search
		new VoodooControl("input", "id", "clearButton").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}