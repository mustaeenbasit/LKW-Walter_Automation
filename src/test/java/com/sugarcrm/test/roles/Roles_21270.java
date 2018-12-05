package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21270 extends SugarTest {
	public void setup() throws Exception {
		
		// Initialize Test Data
		sugar().contacts.api.create();
		FieldSet testDataFS = testData.get(testName).get(0);
		FieldSet roleRecord = testData.get("env_role_setup").get(0);

		// Login as a valid user
		sugar().login();

		// Create a new role in Sugar and Select a module and set Access Type = Admin & Mass Update = None
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, set Access Type = Admin & Mass Update = None
		// TODO: VOOD-580
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_admin div select").set(testDataFS.get("admin"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_massupdate div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_massupdate div select").set(roleRecord.get("roleNone"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Select a user for this role
		AdminModule.assignUserToRole(roleRecord);

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
	 * Verify no mass update action is shown while Access Type = Admin & Mass Update = None
	 * @throws Exception
	 */
	@Test
	public void Roles_21270_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Select a record in list view and assert Mass update option is not visible
		sugar().contacts.listView.checkRecord(1);
		sugar().contacts.listView.openActionDropdown();
		sugar().contacts.listView.getControl("massUpdateButton").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}