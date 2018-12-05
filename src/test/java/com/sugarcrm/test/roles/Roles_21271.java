package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21271 extends SugarTest {
	public void setup() throws Exception {

		// Initialize Test Data
		sugar().contacts.api.create();
		FieldSet testDataFS = testData.get(testName).get(0);
		FieldSet roleRecordFS = testData.get("env_role_setup").get(0);

		// Login as a valid user
		sugar().login();

		// Create a new role in Sugar and Select a module and set Access Type = Admin & View = Owner
		AdminModule.createRole(roleRecordFS);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, set Access Type = Admin & View = Owner
		// TODO: VOOD-580
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_admin div select").set(testDataFS.get("admin"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_view div select").set(roleRecordFS.get("roleOwner"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Select a user for this role
		AdminModule.assignUserToRole(roleRecordFS);

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Create a contact record assigned to QAUser
		// TODO: VOOD-444
		FieldSet contactName = new FieldSet();
		contactName.put("lastName", testName);
		sugar().contacts.create(contactName);
	}

	/**
	 * Verify user can see own records in the list view while Access Type = Admin & View = Owner
	 * @throws Exception
	 */
	@Test
	public void Roles_21271_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify count of records on the list view 
		Assert.assertTrue("The number of record listed for user qauser are not matched", sugar().contacts.listView.countRows() == 2);

		// Verify owned record is clickable and it should go to the record detail view page.
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.getDetailField("lastName").assertContains(testName, true);

		// Verify non-owned record is not clickable
		sugar().contacts.navToListView();

		// TODO: VOOD-2119 ListView row "Link" css Matches assigned user when Link is disabled
		new VoodooControl("a", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(2) span[class*='full_name'] a").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}