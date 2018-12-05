package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_29141 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that "acl_role_set_id" field is NOT displayed in Field name for User’s module
	 *
	 * @throws Exception
	 */
	@Test
	public void Reports_29141_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-822
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "img[name='summationImg']");

		// Go to Reports -> Create reports -> SummationReport -> Users
		sugar().reports.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", "Users").click();
		VoodooUtils.waitForReady();

		// Verify that Field name "acc_role_set_id" should not be displayed
		new VoodooControl("tr", "xpath", "//*[@id='module_fields']/div[3]/table/tbody[2]/tr[contains(.,'acl_role_set_id')]").assertExists(false);

		// Cancel creating Report
		new VoodooControl("input", "id", "cancelBtn").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}