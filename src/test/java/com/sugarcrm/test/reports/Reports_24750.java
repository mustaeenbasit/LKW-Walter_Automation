package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Reports_24750 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		
		// Login as non-admin user (i.e qauser)
		UserRecord qauser = new UserRecord(sugar().users.getQAUser());
		qauser.login();
	}

	/**
	 * Verify a non-admin user clicking on user name on Report is navigated to Assigned User's Employee Detail View page
	 * @throws Exception
	 */
	@Test
	public void Reports_24750_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		FieldSet customData = testData.get(testName).get(0);
		
		// Navigating to Reports and creating a report
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		// Clicking 'Rows and Columns Report'
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		VoodooUtils.waitForReady();
		
		// Clicking Accounts Module
		new VoodooControl("td", "css", "#buttons_table tr #buttons_td td").click();
		VoodooUtils.waitForReady();

		// Clicking on next button
		VoodooControl nextButtonCtrl = new VoodooControl("input", "id", "nextBtn");
		nextButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Selecting display columns
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("a", "css", "#module_tree .ygtvchildren tr td:nth-child(3) a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("tr", "id", "Users_user_name").click();
		
		// Clicking on next button
		nextButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Entering "Report Name"
		new VoodooControl("input", "id", "save_report_as").set(testName);
		VoodooUtils.waitForReady();

		// Save and run report
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// Verify that report displays Accounts Name as per the account record created
		new VoodooControl("a", "css", ".listViewBody tr:nth-child(3) td a").assertEquals(sugar().accounts.defaultData.get("name"), true);
		
		// Click the assigned user name link
		new VoodooControl("a", "css", ".listViewBody tr:nth-child(3) td:nth-of-type(2) a").click();
		VoodooUtils.waitForReady();
		
		// Verify user is successfully navigated to the 'Assigned to' user's employee detail view page.
		// TODO VOOD-1041 : Need lib support of employees module
		VoodooUtils.focusWindow(customData.get("newTabPageTitle"));
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("h2", "css", ".moduleTitle h2").assertContains(customData.get("adminFullName"), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}