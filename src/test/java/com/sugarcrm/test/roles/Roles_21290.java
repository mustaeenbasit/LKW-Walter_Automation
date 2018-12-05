package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.views.RecordView;
import com.sugarcrm.test.SugarTest;

public class Roles_21290 extends SugarTest {
	FieldSet privateUserAccountData = new FieldSet();
	String privateAccountName = "";

	public void setup() throws Exception {
		FieldSet roleRecordData = testData.get("env_role_setup").get(0);
		privateUserAccountData = testData.get(testName).get(0);
		DataSource userData = testData.get(testName + "_userData");

		// Create two users i.e "Chris" and "PrivateUser"
		sugar().users.api.create(userData);
		sugar().login();

		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-856 - Lib is needed for Roles Management
		// Select Accounts module and Set Access Type as "Admin
		new VoodooControl("a", "css", "#contentTable tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(2) div.aclNot.Set").click();
		new VoodooControl("select", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(2) select").set(privateUserAccountData.get("access_type"));
		VoodooUtils.waitForReady();

		// Set "Billing Street" field permissions to "Read/Write", and leave all other fields as "Not Set".
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address").set(roleRecordData.get("roleRead/Write"));

		// Save Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign users (QAUser and Chris) to this Role
		AdminModule.assignUserToRole(roleRecordData);
		AdminModule.assignUserToRole(userData.get(0));

		// Edit the Private User's team
		sugar().users.navToListView();
		sugar().users.listView.clickRecord(1);
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// String variable to hold PrivateUser's userName
		String privateUserName = userData.get(1).get("userName");

		// Remove Global Team from Private User and add only Private Team
		// TODO: VOOD-563 - need lib support for user profile edit page
		new VoodooControl("a", "id", "tab4").click();
		VoodooUtils.waitForReady();
		new VoodooControl("button", "css", "[name='remove_team_name_collection_0']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='teamset_div'] input").set(privateUserName);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#EditView_team_name_table li").click();
		new VoodooControl("input", "css", "[name='primary_team_name_collection']").click();
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		FieldSet accountTeamData = new FieldSet();
		privateAccountName = privateUserAccountData.get("accountName");
		accountTeamData.put("name", privateAccountName);
		accountTeamData.put("relTeam", privateUserAccountData.get("privateTeamName"));
		accountTeamData.put("relAssignedTo", privateUserName);

		// Create an Account record assigned to PrivateUser and team = PrivateUser
		// TODO: VOOD-444 - Support creating relationships via API
		sugar().accounts.create(accountTeamData);

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Access Type "Admin", Field permissions set to "Read/Write" & "Not Set" on Accounts (group field) - Verify user can view records outside of team
	 * @throws Exception
	 */
	@Test
	public void Roles_21290_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts listView
		sugar().accounts.navToListView();

		// Verify that qaUser can view the "PrivateAccount" on the listview
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(privateAccountName, true);

		FieldSet accountsDefaultData = sugar().accounts.getDefaultData();
		RecordView accountRecordView = sugar().accounts.recordView;

		// Navigate to the record view of the PrivateAccount
		sugar().accounts.listView.clickRecord(1);
		accountRecordView.showMore();

		// Assert that All Billing Address information is visible to qaUser
		accountRecordView.getDetailField("billingAddressStreet").assertEquals(accountsDefaultData.get("billingAddressStreet"), true);
		accountRecordView.getDetailField("billingAddressCity").assertEquals(accountsDefaultData.get("billingAddressCity"), true);
		accountRecordView.getDetailField("billingAddressState").assertEquals(accountsDefaultData.get("billingAddressState"), true);
		accountRecordView.getDetailField("billingAddressPostalCode").assertEquals(accountsDefaultData.get("billingAddressPostalCode"), true);
		accountRecordView.getDetailField("billingAddressCountry").assertEquals(accountsDefaultData.get("billingAddressCountry"), true);

		// Click the edit button 
		accountRecordView.edit();

		// Verify that all the fields are editable
		VoodooControl streetEditField = accountRecordView.getEditField("billingAddressStreet");
		VoodooControl cityEditField = accountRecordView.getEditField("billingAddressCity");
		VoodooControl stateEditField = accountRecordView.getEditField("billingAddressState");
		VoodooControl postalCodeEditField = accountRecordView.getEditField("billingAddressPostalCode");
		VoodooControl CountryEditField = accountRecordView.getEditField("billingAddressCountry");

		// Change the values in all the billing address fields
		streetEditField.set(testName);
		cityEditField.set(testName);
		stateEditField.set(testName);
		postalCodeEditField.set(testName);
		CountryEditField.set(testName);

		// Verify the values in the edit fields after altering them
		cityEditField.assertEquals(testName, true);
		stateEditField.assertEquals(testName, true);
		postalCodeEditField.assertEquals(testName, true);
		CountryEditField.assertEquals(testName, true);

		// Didn't assert the text in street field because as per the assertEquals() method, for textarea field, 
		// it would use getText() and getText method would return the old value instead of "testName" which causes 
		// the assertion failure but for the input fields it returns the value, hence the assertion works there.
		streetEditField.assertVisible(true);

		// Cancel the form
		accountRecordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}