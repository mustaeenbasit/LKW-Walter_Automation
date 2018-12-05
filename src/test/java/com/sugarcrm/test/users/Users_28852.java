package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_28852 extends SugarTest{
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that Users having same username should not be imported
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_28852_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to userManagement
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl userManagementCtrl = sugar.admin.adminTools.getControl("userManagement");
		userManagementCtrl.click();
		VoodooUtils.focusDefault();
		
		// VOOD-1647
		// Click on Create New user
		sugar.navbar.clickModuleDropdown(sugar.users);
		new VoodooControl("li", "css", "li[data-module='Users'] ul[role='menu'] li:nth-of-type(5)").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Create Users record by importing the csv file
		VoodooFileField browseToImport = new VoodooFileField("input", "id", "userfile");

		// TODO: VOOD-1396 - Need Controls for the Import Tasks functionality
		VoodooControl nextButton = new VoodooControl("input", "id", "gonext");
		nextButton.click();
		VoodooUtils.waitForReady();
		browseToImport.set("src/test/resources/data/" + testName + "_user.csv");
		nextButton.click();
		VoodooUtils.waitForReady();
		nextButton.click();
		VoodooUtils.waitForReady();
		nextButton.click();
		new VoodooControl("input", "id", "importnow").click();
		VoodooUtils.waitForReady();
		
		// Verify that If in a Excel multiple users having same username exists,then only 1 user should be imported and a message should be displayed for duplicate preceding with no. of duplicates(eg. 1 Duplicates Found) and the same record(s) should not be imported
		FieldSet fs = testData.get(testName).get(0);
		new VoodooControl("span", "css", ".dashletPanelMenu.wizard .screen > span").assertContains(fs.get("errorMsg"), true);
		
		// Exit from import user section
		new VoodooControl("input", "id", "finished").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}