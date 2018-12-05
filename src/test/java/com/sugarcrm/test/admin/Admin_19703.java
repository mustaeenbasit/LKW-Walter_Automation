package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Admin_19703 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify a non Admin  user can be assigned a Developer Role
	 * @throws Exception
	 */
	@Test
	public void Admin_19703_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		AdminModule.createRole(customData);

		// Access =>Enabled, Access Type => Developer
		// 1. Accounts
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-856
		new VoodooControl("div", "css", "#ACLEditView_Access_Accounts_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_access div select").set(customData.get("access"));
		new VoodooControl("div", "css", "#ACLEditView_Access_Accounts_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_admin div select").set(customData.get("access_type"));
		VoodooUtils.waitForReady();

		// 2. Contacts
		new VoodooControl("div", "css", "#ACLEditView_Access_Contacts_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_access div select").set(customData.get("access"));
		new VoodooControl("div", "css", "#ACLEditView_Access_Contacts_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_admin div select").set(customData.get("access_type"));
		VoodooUtils.waitForReady();

		// 3. Leads
		new VoodooControl("div", "css", "#ACLEditView_Access_Leads_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Leads_access div select").set(customData.get("access"));
		new VoodooControl("div", "css", "#ACLEditView_Access_Leads_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Leads_admin div select").set(customData.get("access_type"));
		VoodooUtils.waitForReady();

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(customData);
		VoodooUtils.focusDefault();
		sugar().logout();

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		// Verify user access to the the 3 modules assigned to the developer Role (Accounts, Contacts and Leads)
		new VoodooControl("a", "id", "studiolink_Accounts").assertExists(true);
		new VoodooControl("a", "id", "studiolink_Contacts").assertExists(true);
		new VoodooControl("a", "id", "studiolink_Leads").assertExists(true);

		new VoodooControl("a", "id", "studiolink_Bugs").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Calls").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Campaigns").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Cases").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Contracts").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Documents").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Employees").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Notes").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Meetings").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Opportunities").assertExists(false);
		new VoodooControl("a", "id", "studiolink_RevenueLineItems").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Quotes").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Tasks").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Products").assertExists(false);
		new VoodooControl("a", "id", "studiolink_ProductTemplates").assertExists(false);
		new VoodooControl("a", "id", "studiolink_ProjectTask").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Project").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Prospects").assertExists(false);
		new VoodooControl("a", "id", "studiolink_Users").assertExists(false);
		VoodooUtils.focusDefault();

		// TODO: No need of below lines, cleanup does it automatically. But right now it is not working
		sugar().logout();
		sugar().login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}