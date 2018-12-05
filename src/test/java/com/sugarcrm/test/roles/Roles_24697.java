package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_24697 extends SugarTest {
	FieldSet roleRecordData = new FieldSet();

	public void setup() throws Exception {
		FieldSet roleData = testData.get("env_role_setup").get(0);
		roleRecordData = testData.get(testName).get(0);

		// Login
		sugar().login();

		// Create a Role
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// Set Access for Opportunities and Leads module access to 'Disabled'
		// TODO: VOOD-580
		new VoodooControl("a", "css", "#ACLEditView_Access_Opportunities_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Opportunities_access div select").set(roleRecordData.get("disabled"));
		new VoodooControl("a", "css", "#ACLEditView_Access_Leads_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Leads_access div select").set(roleRecordData.get("disabled"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role and logout
		AdminModule.assignUserToRole(roleData);
		sugar().logout();
	}

	/**
	 * Role management_Verify that particular modules can be disabled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_24697_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Enter the URL to navigate to opportunities module
		VoodooUtils.go(new SugarUrl().getBaseUrl()+roleRecordData.get("opportunities"));
		VoodooUtils.waitForReady();

		// Verify that Unauthorized access error message is displayed
		sugar().opportunities.listView.assertContains(roleRecordData.get("verifyText1"), true);
		sugar().opportunities.listView.assertContains(roleRecordData.get("verifyText2"), true);

		// Enter the URL to navigate to leads module
		VoodooUtils.go(new SugarUrl().getBaseUrl()+roleRecordData.get("leads"));
		VoodooUtils.waitForReady();

		// Verify that Unauthorized access error message is displayed
		sugar().opportunities.listView.assertContains(roleRecordData.get("verifyText1"), true);
		sugar().opportunities.listView.assertContains(roleRecordData.get("verifyText2"), true);

		// Navigating to User profile > Access tab
		// TODO: VOOD-563
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "tab3").click();	

		// Verify Opportunities and Leads module not shown in module access table.
		String opportunity = roleRecordData.get("opportunities").substring(1, 13);
		String lead = roleRecordData.get("leads").substring(1, 5);
		VoodooControl moduleAccessTable = new VoodooControl("table", "css", "#user_detailview_tabs div.yui-content div table.detail.view");
		moduleAccessTable.assertContains(opportunity, false);
		moduleAccessTable.assertContains(lead, false);
		VoodooUtils.focusDefault();

		// Logout and Login as Admin
		sugar().logout();
		sugar().login();

		// Verifying Opportunities and Leads module is displayed in navigation bar.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.getControl("moduleTitle").assertEquals(sugar().opportunities.moduleNamePlural, true);
		sugar().leads.navToListView();
		sugar().leads.listView.getControl("moduleTitle").assertEquals(sugar().leads.moduleNamePlural, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}