package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Mohd. Shariq <mshariq@sugarcrm.com>
 */
public class Accounts_17252 extends SugarTest {
	FieldSet roleRecord; 

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();
		// Create a role 
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "#contentTable table tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "phone_officelink").click();
		new VoodooControl("select", "id", "flc_guidphone_office").click();
		new VoodooControl("option", "css", "#flc_guidphone_office [label='Read Only']").click();
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * check field's read only ACL control in listview inline edit
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17252_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		// TODO: VOOD-1430 
		// Verify that the field should be displayed as read-only
		new VoodooControl("div", "css", ".fld_phone_office").assertAttribute("class", "detail");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}