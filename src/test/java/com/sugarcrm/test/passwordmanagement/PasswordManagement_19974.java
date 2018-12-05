package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19974 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify can't save password management if set maximum Length is less than requirments.
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19974_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ds = testData.get(testName);
		sugar().navbar.navToAdminTools();

		VoodooUtils.focusFrame("bwc-frame"); 
		
		for(int i=0; i<ds.size()-1;i++) {
			sugar().admin.adminTools.getControl("passwordManagement").click();
			sugar().admin.passwordManagement.getControl("passwordMinLength").set("");
			sugar().admin.passwordManagement.getControl("passwordMaxLength").set(ds.get(i).get("passwordMaxLength"));
			sugar().admin.passwordManagement.getControl("passwordSettingOneUpper").set(ds.get(i).get("passwordSettingOneUpper"));
			sugar().admin.passwordManagement.getControl("passwordSettingOneNumber").set(ds.get(i).get("passwordSettingOneNumber"));
			sugar().admin.passwordManagement.getControl("passwordSettingOneLower").set(ds.get(i).get("passwordSettingOneLower"));
			
			sugar().admin.passwordManagement.getControl("save").click();
			
			// TODO VOOD-948
			new VoodooControl("div", "css", "table#passRequirementId div.validation-message").assertEquals(ds.get(i).get("assert"),true);	
			
			// TODO VOOD-949
			new VoodooControl("button", "css", "#btn_cancel").click();
		}		
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}