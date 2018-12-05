package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19968 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * verify display message"Invalid Value: Minimum Length" if set"minimum length"to invalid value.
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19968_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		sugar().navbar.navToAdminTools();

		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("passwordManagement").waitForVisible(30000);
		sugar().admin.adminTools.getControl("passwordManagement").click();
		for(int i=0;i<ds.size();i++) {
			sugar().admin.passwordManagement.getControl("passwordMinLength").set(ds.get(i).get("passwordMinLength"));
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