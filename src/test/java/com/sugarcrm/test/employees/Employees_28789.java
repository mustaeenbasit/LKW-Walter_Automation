package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_28789 extends SugarTest {
	public void setup() throws Exception {
		// login as a qauser
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * Verify Unauthorized access message is displayed if a user click Save and Continue after edit his own employee record
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_28789_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.toggleUserActionsMenu();
		// TODO: VOOD-1041
		// Select the user menu > Employees 
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		// Select its own user name 
		new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr/td[contains(.,'qauser')]/span/b/a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		// Click Edit
		new VoodooControl("a", "id", "edit_button").click();
		// Click Save and Continue
		new VoodooControl("button", "id", "save_and_continue").click();
		VoodooUtils.waitForReady();
		
		FieldSet fs = testData.get(testName).get(0);
		// Assert that "Unauthorized access to employees." message displayed
		new VoodooControl("body", "css", "body").assertContains(fs.get("message_string"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}