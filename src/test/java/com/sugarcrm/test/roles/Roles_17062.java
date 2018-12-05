package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_17062 extends SugarTest {
	FieldSet roleRecord;
	
	public void setup() throws Exception {
		sugar().contacts.api.create();
		roleRecord = testData.get(testName+"_roledata").get(0);
		sugar().login();
	}

	/**
	 * 17062 User shouldn't be able to edit related-to field if he doesn't has access to
	 * @throws Exception
	 */
	@Test
	public void Roles_17062_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-856 & VOOD-580
		// Now on the Access matrix - Click on the field level controls for Contacts
		new VoodooControl("a", "xpath", "//*[contains(@class,'edit')][contains(@class,'view')]//tr[contains(.,'Contacts')]//td/a").click();
		VoodooUtils.waitForReady();

		// Click on Assigned to
		new VoodooControl("div", "id", "assigned_user_namelink").click();
		VoodooUtils.waitForReady();

		// Set Assigned To as Read Only
		new VoodooControl("select", "id", "flc_guidassigned_user_name").set("Read/Owner Write");
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
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.recordView.showMore();

		// Verify that "Assigned to" field is not editable
		sugar().contacts.createDrawer.getEditField("relAssignedTo").assertExists(false);
		sugar().contacts.recordView.cancel(); // Cancel edit mode
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}