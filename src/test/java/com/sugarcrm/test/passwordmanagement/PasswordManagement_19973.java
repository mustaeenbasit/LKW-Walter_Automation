package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19973 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * verify display message"Invalid Value: Maximum Length" if set"Maximum length"to invalid value.
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19973_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		sugar().navbar.navToAdminTools();

		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("passwordManagement").click();
		for(int i=0;i<ds.size();i++) {
			// TODO VOOD-951
			new VoodooControl("input", "css", "#passwordsetting_maxpwdlength").set(ds.get(i).get("passwordMaxLength"));
			sugar().admin.passwordManagement.getControl("save").click();
			// TODO VOOD-948
			new VoodooControl("div", "css", "table#passRequirementId div.validation-message").assertEquals(ds.get(0).get("assert"),true);
		}
		
		// TODO VOOD-949
		new VoodooControl("button", "css", "#btn_cancel").click();

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}