package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.views.ListView;
import com.sugarcrm.sugar.views.RecordView;
import com.sugarcrm.test.SugarTest;

public class Roles_21284 extends SugarTest {
	DataSource accountData = new DataSource();
	ListView accountListView;
	FieldSet chrisUserData = new FieldSet();

	public void setup() throws Exception {
		accountData = testData.get(testName);
		accountListView = sugar().accounts.listView;
		chrisUserData = sugar().users.getDefaultData();

		// Create Chris User
		sugar().users.api.create();

		// Create two account
		sugar().accounts.api.create(accountData);
		FieldSet roleData = testData.get("env_role_setup").get(0);

		// Login as an Admin
		sugar().login();

		// Navigate to Accounts module
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);

		// Sort the accounts in order to assign the records to the users in a sequence
		accountListView.sortBy("headerName", true);

		// Assign the first account record to Chris User
		accountListView.editRecord(1);
		accountListView.getEditField(1, "relAssignedTo").set(chrisUserData.get("userName"));
		accountListView.saveRecord(1);

		// Assign the second account record to qaUser
		accountListView.editRecord(2);
		accountListView.getEditField(2, "relAssignedTo").set(roleData.get("userName"));
		accountListView.saveRecord(2);

		// Create a role : Accounts - field permissions - Billing Street - Owner Read/ Owner Write
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-856 - Lib is needed for Roles Management
		new VoodooControl("a", "css", ".edit tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "billing_addresslink").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "flc_guidbilling_address").click();
		new VoodooControl("option", "css", "#flc_guidbilling_address option[label='" + roleData.get("roleOwnerRead/OwnerWrite") + "']").click();
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign Chris User and qaUser to the role created above
		AdminModule.assignUserToRole(roleData);
		AdminModule.assignUserToRole(chrisUserData);

		// logout from Admin
		sugar().logout();
	}

	/**
	 * Field permissions set to "Owner Read/Owner Write" on Accounts (group field) - Verify listview,detailview and EditView read/write of owned and non-owned records
	 * @throws Exception
	 */
	@Test
	public void Roles_21284_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet qaUserData = sugar().users.getQAUser();

		// Log-In as qaUser
		sugar().login(qaUserData);

		// Navigate to the accounts list view
		sugar().accounts.navToListView();

		FieldSet accountsDefaultData = sugar().accounts.getDefaultData();
		String noAccessLabel = testData.get(testName + "_noAccess").get(0).get("noAccess");
		String defaultBillingAddressCity = accountsDefaultData.get("billingAddressCity");
		String defaultBillingAddressCountry = accountsDefaultData.get("billingAddressCountry");
		RecordView accountRecordView = sugar().accounts.recordView;

		// Verify that you can see City and Billing Country only for Accounts owned by qaUser.  They should be blank for Accounts owned by others.
		accountListView.getDetailField(1, "name").assertEquals(accountData.get(1).get("name"), true);
		accountListView.getDetailField(1, "billingAddressCity").assertEquals(defaultBillingAddressCity, true);
		accountListView.getDetailField(1, "billingAddressCountry").assertEquals(defaultBillingAddressCountry, true);
		accountListView.getDetailField(1, "relAssignedTo").assertEquals(qaUserData.get("userName"), true);

		// No access is displayed for City and Billing Country for Accounts not owned by qaUser.
		accountListView.getDetailField(2, "name").assertEquals(accountData.get(0).get("name"), true);
		// TODO: VOOD-1445 - Need lib support for enhanced disabled check in parent controls of a child
		new VoodooControl("span", "css", ".single:nth-child(2) .noaccess.fld_billing_address_city").assertEquals(noAccessLabel, true);
		new VoodooControl("span", "css", ".single:nth-child(2) .noaccess.fld_billing_address_country").assertEquals(noAccessLabel, true);
		accountListView.getDetailField(2, "relAssignedTo").assertEquals(chrisUserData.get("fullName"), true);

		// Navigate to the Record view of the account owned by qaUser
		accountListView.clickRecord(1);
		accountRecordView.showMore();

		//  Verify that all fields of Billing Address are displayed (Street, City, State, Postal, Country)
		accountRecordView.getDetailField("billingAddressStreet").assertEquals(accountsDefaultData.get("billingAddressStreet"), true);
		accountRecordView.getDetailField("billingAddressCity").assertEquals(defaultBillingAddressCity, true);
		accountRecordView.getDetailField("billingAddressState").assertEquals(accountsDefaultData.get("billingAddressState"), true);
		accountRecordView.getDetailField("billingAddressPostalCode").assertEquals(accountsDefaultData.get("billingAddressPostalCode"), true);
		accountRecordView.getDetailField("billingAddressCountry").assertEquals(defaultBillingAddressCountry, true);

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

		// Close the edit form
		accountRecordView.cancel();

		// Move to the record view of the account record owned by Chris User
		accountRecordView.gotoNextRecord();

		// TODO: VOOD-1445 - Need lib support for enhanced disabled check in parent controls of a child
		VoodooControl nonOwnedRecordBillingAddress = new VoodooControl("span", "css", ".noaccess.fld_billing_address");

		// Verify that the Billing address field does not display any value but No Access
		nonOwnedRecordBillingAddress.assertEquals(noAccessLabel, true);

		// Click the Edit button
		accountRecordView.edit();

		// Verify that the Billing address field is not editable and displays No Access
		nonOwnedRecordBillingAddress.assertEquals(noAccessLabel, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}