package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19971 extends SugarTest {
	DataSource ds;
	VoodooControl passwordMgt, passwordMaxLength, saveButton;

	public void setup() throws Exception {
		ds = testData.get(testName);
		passwordMgt = sugar().admin.adminTools.getControl("passwordManagement");
		passwordMaxLength = sugar().admin.passwordManagement.getControl("passwordMaxLength");
		saveButton = sugar().admin.passwordManagement.getControl("save");
		sugar().login();
	}

	/**
	 * [Password Rules]-administrator set"maximum length"to 0, verify can not save
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19971_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Password management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		passwordMgt.click();
		passwordMaxLength.set(ds.get(1).get("max_length"));
		saveButton.click();

		// TODO: VOOD-948, VOOD-949
		// Verify error messages on max and min input length 
		new VoodooControl("div", "css", "#passRequirementId tr:nth-of-type(2) td:nth-of-type(2) div.validation-message").assertEquals(ds.get(0).get("error_message"), true);
		new VoodooControl("div", "css", "#passRequirementId tr:nth-of-type(2) td:nth-of-type(4) div.validation-message").assertEquals(ds.get(1).get("error_message"), true);
		new VoodooControl("button", "id", "btn_cancel").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}