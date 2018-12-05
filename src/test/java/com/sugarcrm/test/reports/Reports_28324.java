package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Reports_28324 extends SugarTest {
	public void setup() throws Exception {
		// Login as non-admin user (i.e qauser)
		UserRecord qauser = new UserRecord(sugar().users.getQAUser());
		qauser.login();
	}

	/**
	 * Verify non-admin user should only have view access in the advanced reports from mega menu
	 * @throws Exception
	 */
	@Test
	public void Reports_28324_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.selectMenuItem(sugar().reports, "manageAdvancedReports");

		// TODO: VOOD-1057
		VoodooControl manageReportCaretDropdown = new VoodooControl("button", "css", "[data-module='ReportMaker'] button[data-toggle='dropdown']");
		manageReportCaretDropdown.click();

		// Verify non-admin user should only have the view access of Advanced reports from Mega menu
		new VoodooControl("a", "css", "li.active [data-navbar-menu-item='LNK_NEW_REPORTMAKER']").assertVisible(false);
		new VoodooControl("a", "css", "li.active [data-navbar-menu-item='LNK_LIST_REPORTMAKER']").assertVisible(true);
		new VoodooControl("a", "css", "li.active [data-navbar-menu-item='LNK_NEW_CUSTOMQUERY']").assertVisible(false);
		new VoodooControl("a", "css", "li.active [data-navbar-menu-item='LNK_CUSTOMQUERIES']").assertVisible(true);
		new VoodooControl("a", "css", "li.active [data-navbar-menu-item='LNK_NEW_DATASET']").assertVisible(false);
		new VoodooControl("a", "css", "li.active [data-navbar-menu-item='LNK_LIST_DATASET']").assertVisible(true);
		new VoodooControl("a", "css", "li.active [data-navbar-menu-item='LBL_ALL_REPORTS']").assertVisible(true);

		manageReportCaretDropdown.click(); // to close caret-icon dropdown

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}