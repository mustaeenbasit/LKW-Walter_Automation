package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21238 extends SugarTest {

	public void setup() throws Exception {
		FieldSet rolesData = testData.get("env_role_setup").get(0);
		DataSource customDS = testData.get(testName);
		sugar().contacts.api.create();
		sugar().login();

		// Create Chris user Via UI
		UserRecord chrisUser = (UserRecord) sugar().users.create();

		// Create a role
		AdminModule.createRole(rolesData);
		VoodooUtils.focusFrame("bwc-frame");

		// For Contacts module, Set all field permissions to "Owner Read/Owner Write"
		// TODO: VOOD-580 - Create a Roles (ACL) Module LIB, VOOD-856 - Lib is needed for Roles Management
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().contacts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();
		for(int i = 0; i < customDS.size(); i++) {
			// Need to click again on fieldCtrl if permissionsCtrl is not visible as some time script fails to click.
			VoodooControl fieldCtrl = new VoodooControl("div", "id", customDS.get(i).get("fields"));
			VoodooControl permissionsCtrl = new VoodooControl("select", "id", customDS.get(i).get("permissions"));
			fieldCtrl.click();
			if(!permissionsCtrl.queryVisible()) {
				fieldCtrl.click();
			}
			VoodooUtils.waitForReady();
			permissionsCtrl.set(rolesData.get("roleOwnerRead/OwnerWrite"));
		}

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(rolesData);

		// Assign a user (Chris) into the Role
		AdminModule.assignUserToRole(chrisUser);

		// Assign a contact record to Chris
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("relAssignedTo").set(chrisUser.get("userName"));
		sugar().contacts.recordView.save();

		// Logout from Admin and Login with Chris
		sugar().logout();
		sugar().login(chrisUser);
	}

	/**
	 * Field permissions set to Owner Read/Owner Write on Accounts - Assign to another user.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21238_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that records owned by Chris should be displayed
		sugar().contacts.navToListView();
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("fullName"));
		
		// Assign contact record to QAuser
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("relAssignedTo").set(sugar().users.qaUser.get("userName"));
		sugar().contacts.recordView.save();
		
		// TODO: VOOD-1349 - Need Lib support to check if a given element is ancestor/descendant of another given element
		// Verify that records not owned by Chirs should not be displayed
		sugar().contacts.navToListView();
		new VoodooControl("span", "css", ".fld_full_name").assertAttribute("class", "noaccess", true);
		new VoodooControl("span", "css", ".fld_phone_work").assertAttribute("class", "noaccess", true);
		new VoodooControl("span", "css", ".fld_assigned_user_name").assertAttribute("class", "noaccess", true);
		new VoodooControl("span", "css", ".fld_date_modified").assertAttribute("class", "noaccess", true);
		new VoodooControl("span", "css", ".fld_date_entered").assertAttribute("class", "noaccess", true);

		// Logout as Chris and login as QAuser User
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		
		// TODO: VOOD-1349 - Need Lib support to check if a given element is ancestor/descendant of another given element
		// Verify that records owned by Chirs should be displayed
		sugar().contacts.navToListView();
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("fullName"));
		
		// Edit the record
		sugar().contacts.listView.editRecord(1);
		sugar().contacts.listView.getEditField(1, "lastName").set(testName);
		sugar().contacts.listView.saveRecord(1);
		
		// Verify that the QAuser can Edit the newly assigned record.
		sugar().contacts.listView.verifyField(1, "fullName", testName);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}