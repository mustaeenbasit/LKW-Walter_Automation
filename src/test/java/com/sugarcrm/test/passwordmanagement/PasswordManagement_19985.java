package com.sugarcrm.test.passwordmanagement;

import com.sugarcrm.sugar.SugarUrl;
import org.junit.Test;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19985 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the URL of Password Management can be accessed by users given permission
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19985_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// admin user can access Password Management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("passwordManagement").click();
		
		// sugar().admin.passwordManagement.getControl("save").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().logout();
		
		// normal user can not access through the valid URL
		sugar().login(sugar().users.getQAUser());
		FieldSet customData = testData.get(testName).get(0);
		Configuration config = VoodooUtils.getGrimoireConfig();
		String url = new SugarUrl().getBaseUrl();
		url = url+customData.get("url");
		VoodooUtils.voodoo.log.info("url " + url + "...");
		VoodooUtils.go(url);	
		sugar().alerts.getWarning().waitForVisible();
		
		// Verify that only admin can access Password management.
		String message = sugar().alerts.getWarning().getText();
		VoodooUtils.voodoo.log.info("message " + message + "...");
		sugar().alerts.getWarning().assertElementContains(customData.get("assert"), true);
		sugar().alerts.closeAllWarning();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
