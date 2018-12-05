package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_27191 extends SugarTest {
	FieldSet roleRecordData;
	ContactRecord myContact;
	
	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		sugar().login();
		
		// User is now on the ACL Matrix screen after the Role and Description has been saved
		AdminModule.createRole(roleRecordData);
	}

	/**
	 * Verify custom field set to Read-Only in a Role doesn't effect saving of record
	 * @throws Exception
	 */
	@Test
	public void Roles_27191_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Now on the Access matrix - Click the Contacts module and set the field access
		VoodooUtils.focusFrame("bwc-frame");
		
		// Set Assigned To as Read Only.
		// TODO: VOOD-580
		new VoodooControl("a", "css", ".edit.view tr:nth-of-type(7) td a").click();
		new VoodooControl("div", "id", "assigned_user_namelink").click();
		new VoodooControl("select", "id", "flc_guidassigned_user_name").set("Read Only");

		// Save the Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecordData);
		
		// Log out of Sugar as Admin and log in as QAuser to create a contact record
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Create Contact Record.
		myContact = (ContactRecord)sugar().contacts.create();
		
		// Verify Contact is created without error.
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("fullName"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
