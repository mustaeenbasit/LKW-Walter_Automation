package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21246 extends SugarTest {
	FieldSet customFS = new FieldSet(), roleRecordData = new FieldSet();
	VoodooControl accessCtrl, accessSelectCtrl, reportSaveBtnCtrl;

	public void setup() throws Exception {
		roleRecordData = testData.get("env_role_setup").get(0);
		customFS = testData.get(testName).get(0);
		sugar().cases.api.create();
		sugar().login();

		// Admin -> Role management -> Select a module(cases) -> Set Access Role to be All
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		accessCtrl = new VoodooControl("td", "css", "td#ACLEditView_Access_Cases_list");
		accessCtrl.click();
		accessSelectCtrl = new VoodooControl("select", "css", "td#ACLEditView_Access_Cases_list div select");
		accessSelectCtrl.set(customFS.get("access_type"));
		reportSaveBtnCtrl = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		reportSaveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecordData);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify user should see all the records in the list view while the List role is set to All/Not Set
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_21246_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
		
		// Go to the select module 
		sugar().cases.navToListView();
		
		// Verify that All the records should be shown in the list view. 
		sugar().cases.listView.verifyField(1, "name", sugar().cases.getDefaultData().get("name"));
		
		// Logout as QAuser and Login as Admin
		sugar().logout();
		sugar().login();
		
		// Go to the setup and change the Access role to be Not Set. 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "roles_management").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "name_basic").set(roleRecordData.get("roleName"));
		new VoodooControl("a", "id", "search_form_submit").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#MassUpdate table tbody tr.oddListRowS1 td:nth-child(3) b a").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Updated Access role to be Not Set.
		accessCtrl.click();
		accessSelectCtrl.set(customFS.get("default_access"));
		reportSaveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to the select module 
		sugar().cases.navToListView();
				
		// Verify that All the records should be shown in the list view after the change.
		sugar().cases.listView.verifyField(1, "name", sugar().cases.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}