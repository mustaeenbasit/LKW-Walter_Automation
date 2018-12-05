package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_26520 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify user able to save a record with Read Only field in Role
	 * @throws Exception
	 */
	@Test
	public void Roles_26520_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-856 & VOOD-580
		// Now on the Access matrix - Click on the field level controls for Contacts
		new VoodooControl("a", "css", ".edit.view tr:nth-of-type(6) td a").click();
		VoodooUtils.waitForReady();

		// Click on Assigned to
		new VoodooControl("div", "css", "#assigned_user_namelink").click();
		VoodooUtils.waitForReady();

		// Set Assigned To as Read Only
		new VoodooControl("select", "id", "flc_guidassigned_user_name").set("Read Only");
		VoodooUtils.waitForReady();

		// Save the Role
		new VoodooControl("input", "css", "div#category_data .button").click();
		sugar().admin.studio.waitForAJAX(30000); // Save takes time
		VoodooUtils.focusDefault();

		// Assign a non-admin, QAuser, user to the Role
		AdminModule.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as QAuser to create a contact
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Required to create contacts via UI
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.createDrawer.getEditField("firstName").set(sugar().contacts.getDefaultData().get("firstName"));
		sugar().contacts.createDrawer.getEditField("lastName").set(sugar().contacts.getDefaultData().get("lastName"));
		sugar().contacts.createDrawer.showMore();

		// Verify that "Assigned to" field is not editable
		sugar().contacts.createDrawer.getEditField("relAssignedTo").assertExists(false);
		sugar().contacts.createDrawer.save();

		// Verify that the User is unable to modify the value in the Assigned field, but save the new record.
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("firstName")+" "+sugar().contacts.getDefaultData().get("lastName"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}