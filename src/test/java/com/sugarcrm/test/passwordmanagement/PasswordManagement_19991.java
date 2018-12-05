package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19991 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify display warning message if set minimum length greater than maximum length
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19991_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ds = testData.get(testName);
		sugar().navbar.navToAdminTools();

		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("passwordManagement").click();
		sugar().admin.passwordManagement.getControl("passwordMinLength").set(ds.get(0).get("passwordMinLength"));
		sugar().admin.passwordManagement.getControl("passwordMaxLength").set(ds.get(0).get("passwordMaxLength"));
		sugar().admin.passwordManagement.getControl("save").click();

		// TODO VOOD-948
		new VoodooControl("div", "css", "table#passRequirementId tr:nth-of-type(2) td:nth-of-type(2) div.validation-message").assertEquals(ds.get(0).get("assert"),true);	
		
		// TODO VOOD-949
		new VoodooControl("button", "css", "#btn_cancel").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}