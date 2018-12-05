package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28043 extends SugarTest {
	public void setup() throws Exception {
		FieldSet roleRecord = testData.get("env_role_setup").get(0);

		// Login as Admin User
		sugar().login();

		// Admin -> Role management -> Creating Role
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
	}

	/**
	 * Verify that a valid message is displayed in the pop-up, that appears on saving a role in Role Management.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_28043_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click any Module name and observe the pop-up message that appears on the top. (for e.g Accounts)
		// xPath needed here because there is neither proper id (or class) available for 'Accounts' or any module nor it has stable position.
		// TODO: VOOD-580
		new VoodooControl("a", "xpath", "//*[@class='edit view']/tbody/tr/td[contains(.,'" + sugar().accounts.moduleNamePlural + "')]/a").click();

		FieldSet alertMessageData = testData.get(testName).get(0);

		// Verify that It should show pop-up message as "Done"
		// TODO: VOOD-2029
		VoodooControl doneAlertCtrl = new VoodooControl("div", "id", "ajaxStatusDiv");
		Assert.assertTrue("Done pop-up message is not visible", doneAlertCtrl.queryVisible());
		doneAlertCtrl.assertContains(alertMessageData.get("done"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}