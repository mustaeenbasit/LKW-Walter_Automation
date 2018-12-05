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

public class Roles_21289 extends SugarTest {
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

		// TODO:VOOD-1507 [Support Studio Module ListView Layouts View]
		VoodooControl phoneOfficeDefault = new VoodooControl("li", "css", "#Default li[data-name='phone_office']");

		// Navigate to Studio >> Accounts >> Layouts >> List View   
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Add Billing Street field from hidden to default
		new VoodooControl("li", "css", "#Hidden li[data-name='billing_address_street']").dragNDropViaJS(phoneOfficeDefault);

		// Add Billing State field from hidden to default
		new VoodooControl("li", "css", "#Hidden li[data-name='billing_address_state']").dragNDropViaJS(phoneOfficeDefault);

		// Add Billing postal code field from hidden to default
		new VoodooControl("li", "css", "#Hidden li[data-name='billing_address_postalcode']").dragNDropViaJS(phoneOfficeDefault);

		// Save and Deploy the layout settings
		new VoodooControl("input", "css", ".list-editor #savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

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
		new VoodooControl("option", "css", "#flc_guidbilling_address option[label='" + roleData.get("roleNone") + "']").click();
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
	 * Field permissions set to "None" on Accounts (group field) - Verify listview, detailview, editview of owned and non-owned records
	 * @throws Exception
	 */
	@Test
	public void Roles_21289_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet qaUserData = sugar().users.getQAUser();

		// Log-In as qaUser
		sugar().login(qaUserData);

		// Navigate to the accounts list view
		sugar().accounts.navToListView();

		String noAccessLabel = testData.get(testName + "_noAccess").get(0).get("noAccess");
		RecordView accountRecordView = sugar().accounts.recordView;

		// Verify that qaUser cannot view any billing address information for any records on the listview, No access is displayed
		accountListView.getDetailField(1, "name").assertEquals(accountData.get(1).get("name"), true);
		// TODO: VOOD-1445 - Need lib support for enhanced disabled check in parent controls of a child
		new VoodooControl("span", "css", ".single .noaccess.fld_billing_address_city").assertEquals(noAccessLabel, true);
		new VoodooControl("span", "css", ".single .noaccess.fld_billing_address_street").assertEquals(noAccessLabel, true);
		new VoodooControl("span", "css", ".single .noaccess.fld_billing_address_state").assertEquals(noAccessLabel, true);
		new VoodooControl("span", "css", ".single .noaccess.fld_billing_address_postalcode").assertEquals(noAccessLabel, true);
		new VoodooControl("span", "css", ".single .noaccess.fld_billing_address_country").assertEquals(noAccessLabel, true);
		accountListView.getDetailField(1, "relAssignedTo").assertEquals(qaUserData.get("userName"), true);

		// No access is displayed for City and Billing Country for Accounts not owned by qaUser.
		accountListView.getDetailField(2, "name").assertEquals(accountData.get(0).get("name"), true);
		// TODO: VOOD-1445 - Need lib support for enhanced disabled check in parent controls of a child
		new VoodooControl("span", "css", ".single:nth-child(2) .noaccess.fld_billing_address_city").assertEquals(noAccessLabel, true);
		new VoodooControl("span", "css", ".single:nth-child(2) .noaccess.fld_billing_address_street").assertEquals(noAccessLabel, true);
		new VoodooControl("span", "css", ".single:nth-child(2) .noaccess.fld_billing_address_state").assertEquals(noAccessLabel, true);
		new VoodooControl("span", "css", ".single:nth-child(2) .noaccess.fld_billing_address_postalcode").assertEquals(noAccessLabel, true);
		new VoodooControl("span", "css", ".single:nth-child(2) .noaccess.fld_billing_address_country").assertEquals(noAccessLabel, true);
		accountListView.getDetailField(2, "relAssignedTo").assertEquals(chrisUserData.get("fullName"), true);

		// TODO: VOOD-1445 - Need lib support for enhanced disabled check in parent controls of a child
		VoodooControl recordViewBillingAddress = new VoodooControl("span", "css", ".noaccess.fld_billing_address");

		// Navigate to the Record view of the account owned by qaUser
		accountListView.clickRecord(1);
		accountRecordView.showMore();

		// Verify that all fields of Billing Address are not displayed (Street, City, State, Postal, Country)
		recordViewBillingAddress.assertEquals(noAccessLabel, true);

		// Click the edit button 
		accountRecordView.edit();

		//  Verify that all fields of Billing Address are not displayed or editable (Street, City, State, Postal, Country).
		recordViewBillingAddress.assertEquals(noAccessLabel, true);

		// Close the edit form
		accountRecordView.cancel();

		// Move to the record view of the account record owned by Chris User
		accountRecordView.gotoNextRecord();

		// Verify that the Billing address field does not display any value but No Access
		recordViewBillingAddress.assertEquals(noAccessLabel, true);

		// Click the Edit button
		accountRecordView.edit();

		// Verify that the Billing address field is not editable and displays No Access
		recordViewBillingAddress.assertEquals(noAccessLabel, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}