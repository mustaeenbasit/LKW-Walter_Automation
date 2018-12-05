package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_24707 extends SugarTest {
	VoodooControl employeeCtrl, advanceSearchCtrl;

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Employee - Search_Verify employees can be searched by "Last Name" field by Advanced Search.
	 * @throws Exception
	 */
	@Test
	public void Employees_24707_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// TODO VOOD-1041 -need lib support of employees module
		sugar.navbar.toggleUserActionsMenu();
		employeeCtrl = new VoodooControl("a", "css", "li.profileactions-employees a");
		employeeCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Search qauser
		String qauserName = sugar.users.getQAUser().get("userName");
		new VoodooControl("a", "id", "advanced_search_link").click();
		new VoodooControl("input", "id", "last_name_advanced").set(qauserName);
		advanceSearchCtrl = new VoodooControl("a", "id", "search_form_submit_advanced");
		advanceSearchCtrl.click();

		// Verify that Employee name which is searched by "Last Name" field.
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr.oddListRowS1 > td:nth-child(3) > b > a").assertContains(qauserName, true);

		// Search administrator
		new VoodooControl("input", "id", "last_name_advanced").set("administrator");
		advanceSearchCtrl.click();

		// Verify that Employee name which is searched by "Last Name" field.
		new VoodooControl("a", "css", "#MassUpdate > table > tbody > tr.oddListRowS1 > td:nth-child(3) > b > a").assertContains("Administrator", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}