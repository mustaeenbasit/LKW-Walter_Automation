package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.TeamRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

public class Roles_21291 extends SugarTest {
	FieldSet roleRecordData, customFS;
	UserRecord chrisUser, privateUserRecord;
	TeamRecord myTeamRecord;

	public void setup() throws Exception {
		roleRecordData = testData.get("env_role_setup").get(0);
		customFS = testData.get(testName).get(0);
		FieldSet privateUserDS = testData.get(testName+"_userdata").get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Create user Chris via UI due to API not set user status to Active
		chrisUser = (UserRecord) sugar().users.create();

		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set only five fields to "Read only"
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#contentTable tbody tr td table:nth-child(9) tbody tr td:nth-child(1) table tbody tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();

		new VoodooControl("div", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(2) div.aclNot.Set").click();
		new VoodooControl("select", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(2) select.aclNot.Set").set(customFS.get("access_type"));
		VoodooUtils.waitForReady();

		// Set Type as Read Only
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address").set(customFS.get("read_only"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into this Role
		AdminModule.assignUserToRole(roleRecordData);

		// Assing non Admin user (chris) into this Role
		FieldSet userFs = new FieldSet();
		userFs.put("userName", chrisUser.getRecordIdentifier());
		AdminModule.assignUserToRole(userFs);

		// Create a team
		myTeamRecord = (TeamRecord) sugar().teams.create();

		// Go to Team Management 
		BWCSubpanel userSubpanel = sugar().teams.detailView.subpanels.get(sugar().users.moduleNamePlural);

		sugar().admin.navToAdminPanelLink("teamsManagement");

		// TODO: VOOD-776
		// Assign created team to qauser
		sugar().teams.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		userSubpanel.getControl("teamMembership").click();
		VoodooUtils.focusWindow(1);
		VoodooUtils.focusDefault();
		new VoodooControl("input", "id", "user_name_advanced").set(chrisUser.getRecordIdentifier());
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("input", "css", ".list.view tr:nth-child(3) td:nth-child(1) input").click();
		VoodooControl selectCtrl2= new VoodooControl("input", "css", "#MassUpdate_select_button");
		selectCtrl2.click();
		VoodooUtils.focusWindow(0);

		// Create PrivateUser via UI
		privateUserRecord = (UserRecord) sugar().users.create(privateUserDS);

		// Assign Accounts record to QAUser
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("relTeam").set(customFS.get("privateTeamName"));
		sugar().accounts.recordView.getEditField("name").set(customFS.get("accountName"));
		sugar().accounts.recordView.getEditField("relAssignedTo").set(privateUserRecord.getRecordIdentifier());
		sugar().accounts.recordView.getEditField("billingAddressStreet").set(customFS.get("billingAddressStreet"));
		sugar().accounts.recordView.save();

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Access Type "Admin", Field permissions set to "Read/Owner Write" & "Not Set" on Accounts (group field)
	 * @throws Exception
	 */
	@Test
	public void Roles_21291_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record listView
		sugar().accounts.navToListView();
		
		// Verify that QAuser can view the "PrivateAccount" on the listview.
		sugar().accounts.listView.verifyField(1, "name", customFS.get("accountName"));
		
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		
		// Verifyt that Billing Street information is visible.
		sugar().accounts.recordView.getDetailField("billingAddressStreet").assertContains(customFS.get("billingAddressStreet"), true);
		
		sugar().accounts.recordView.edit();

		// Verify that the Billing Street information is not editable.
		sugar().accounts.recordView.getEditField("billingAddressStreet").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}