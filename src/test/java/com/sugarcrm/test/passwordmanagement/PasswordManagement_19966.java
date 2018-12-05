package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19966 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * verify display a warning message "Minimum Length is less than 0" in red when set"minimum length"to negative number.
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19966_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("passwordManagement").click();
		sugar().admin.passwordManagement.getControl("passwordMinLength").set(ds.get(0).get("passwordMinLength"));
		sugar().admin.passwordManagement.getControl("save").click();

		// Verify error message appears, when minimum length is of -ve value
		// TODO: VOOD-948
		new VoodooControl("div", "css", "table#passRequirementId div.validation-message").assertEquals(ds.get(0).get("assert"),true);

		// TODO: VOOD-949
		VoodooControl cancelCtrl = new VoodooControl("button", "css", "#btn_cancel");
		cancelCtrl.click();

		VoodooUtils.waitForReady();
		
		// Verify default minimum length for password
		sugar().admin.adminTools.getControl("passwordManagement").click();
		sugar().admin.passwordManagement.getControl("passwordMinLength").assertEquals(ds.get(1).get("passwordMinLength"),true);
		cancelCtrl.click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}