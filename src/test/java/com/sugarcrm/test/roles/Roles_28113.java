package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28113 extends SugarTest {
	DataSource accountData;
	FieldSet roleRecordData; 

	public void setup() throws Exception {
		accountData = testData.get(testName + "_account");
		roleRecordData = testData.get(testName).get(0);
		sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 *  Verify that Setting Account fields to "Read Only" does not breaks sorting on list view
	 * @throws Exception
	 */
	@Test
	public void Roles_28113_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a role myRole
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// For Account module, Set "Name" to "Read Only"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "namelink").click();
		new VoodooControl("select", "id", "flc_guidname").set(roleRecordData.get("access"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Logout from the Admin user
		sugar().logout();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Access the Account module
		sugar().accounts.navToListView();

		// Click on the "Name" column to sort
		sugar().accounts.listView.sortBy("headerName", true);

		// Verify that the list view sorts by Name
		for(int i = 0; i < accountData.size(); i++) {
			sugar().accounts.listView.getDetailField(i+1, "name").assertEquals(accountData.get(i).get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}