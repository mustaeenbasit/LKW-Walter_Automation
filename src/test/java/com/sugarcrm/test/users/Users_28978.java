package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_28978 extends SugarTest {
	FieldSet roleRecordData;
	VoodooControl leadRoleCtrl, leadAccessCtrl, saveBtnCtrl; 
	UserRecord qaUser;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		qaUser = new UserRecord(sugar.users.getQAUser());
		sugar.login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Set access 'Disabled' for Leads
		// TODO: VOOD-580, VOOD-856
		leadRoleCtrl = new VoodooControl("td", "css", "#ACLEditView_Access_Leads_access div:nth-of-type(2)");
		leadAccessCtrl = new VoodooControl("select", "css", "td#ACLEditView_Access_Leads_access div select");
		saveBtnCtrl = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		leadRoleCtrl.click();
		leadAccessCtrl.set(roleRecordData.get("disabledAccess"));

		// Save role
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Logout from the Admin user
		sugar.logout();
	}

	/**
	 * Module should appear in navigation bar when access field set to "Not Set" from "Disabled"
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_28978_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		qaUser.login();

		// TODO: VOOD-784
		VoodooControl leadsCtrl = new VoodooControl("a", "css", ".module-list li[data-module='Leads'] a");
		VoodooControl opportunitiesCtrl = new VoodooControl("a", "css", ".module-list li[data-module='Opportunities'] a");

		// Verify that the Lead module is not appearing in navigation bar
		leadsCtrl.assertExists(false);

		// Logout & login as admin.
		sugar.logout();
		sugar.login();

		// Go to Admin -> Role Management -> myRole
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("rolesManagement").click();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580
		new VoodooControl("input", "id", "name_basic").set(roleRecordData.get("roleName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(3) a").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Change the access of Lead to "Not Set"
		leadRoleCtrl.click();
		leadAccessCtrl.set(roleRecordData.get("notSetAccess"));

		// Set the access of Opportunities to "Disabled"
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Opportunities_access div:nth-of-type(2)").doubleClick();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Opportunities_access div select").set(roleRecordData.get("disabledAccess"));

		// Save role
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Logout & Login as QAUser
		sugar.logout();
		qaUser.login();

		// Verify that the Leads module should appear in navigation bar
		leadsCtrl.waitForVisible();
		leadsCtrl.assertExists(true);

		// Verify that the Opportunities should not appear in navigation bar
		opportunitiesCtrl.assertExists(false);
		
		// Required to logout as QAuser and login as Admin
		sugar.logout();
		sugar.login();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}