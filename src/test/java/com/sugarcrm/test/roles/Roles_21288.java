package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.views.ListView;
import com.sugarcrm.test.SugarTest;

public class Roles_21288 extends SugarTest {
	DataSource accountData = new DataSource();

	public void setup() throws Exception {
		accountData = testData.get(testName);
		ListView accountListView = sugar().accounts.listView;
		FieldSet chrisUserData = sugar().users.getDefaultData();
		FieldSet roleData = testData.get("env_role_setup").get(0);

		// Create Chris User
		sugar().users.api.create();

		// Create two account
		sugar().accounts.api.create(accountData);

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
	 * Field permissions set to "None" on Accounts (group field) - Verify report of owned and non-owned records
	 * @throws Exception
	 */
	@Test
	public void Roles_21288_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet reportsData = testData.get(testName + "_reportsData").get(0);

		// Log-In as qaUser
		sugar().login(sugar().users.getQAUser());

		// VOOD-643, VOOD-822
		// Create a Rows and Columns Report for Accounts module
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "#report_type_div img[alt='Rows and Columns Report']").click();
		new VoodooControl("table", "id", "Accounts").click();
		VoodooUtils.waitForReady();
		VoodooControl nextButton = new VoodooControl("input", "css", "#filters_div #nextBtn");

		// Click the Next button to skip the filters step
		nextButton.click();
		VoodooUtils.waitForReady();

		VoodooControl searchModuleField = new VoodooControl("input", "id", "dt_input");

		// Choose Display Columns: Name
		// Search for the Name field
		searchModuleField.set(reportsData.get("accountName"));
		VoodooUtils.waitForReady();
		// Select the Name module field to display as column in the report
		new VoodooControl("tr", "id", "Accounts_name").click();
		// Clear the search
		new VoodooControl("input", "id", "clearButton").click();

		// Search for Billing address fields
		searchModuleField.set(reportsData.get("billingAddressFields"));
		VoodooUtils.waitForReady();

		// Assert that you cannot view or select: Billing City, Billing Country, Billing Postal Code, Billing State, and Billing Street
		new VoodooControl("div", "class", "yui-dt-empty").assertEquals(reportsData.get("noRecordsLabel"), true);

		// Click on Assigned to user Option
		new VoodooControl("a", "id", "ygtvlabelel2").click();
		VoodooUtils.waitForReady();
		// Search for the Full Name field
		searchModuleField.set(reportsData.get("assignedUserFullName"));
		VoodooUtils.waitForReady();
		// Select the Full Name field to display as Column in the report
		new VoodooControl("tr", "id", "Users_full_name").click();

		// Click the next button
		nextButton.click();
		VoodooUtils.waitForReady();

		// Enter the Name of the Report and click Save and Run button
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// Sort the Reports by Account Name
		new VoodooControl("img", "css", ".listViewThLinkS1 img").click();
		VoodooUtils.waitForReady();

		VoodooControl accountNameDetailField = sugar().accounts.recordView.getDetailField("name");

		// Click on owned Account records(i.e record owned by qaUser) & Check Billing information field
		new VoodooControl("a", "css", "td.evenListRowS1 a").click();
		// Focus on the new tab where the account got opened
		VoodooUtils.focusWindow(1);
		// Assert that the account opened is the one i.e clicked in the report
		accountNameDetailField.assertEquals(accountData.get(1).get("name"), true);
		// Click showMore link to view the Billing information
		sugar().accounts.recordView.showMore();

		VoodooControl billingAddressDetailField = new VoodooControl("span", "css", ".noaccess.fld_billing_address");

		// Billing information is not visible and the field displays "No access"
		billingAddressDetailField.assertEquals(reportsData.get("noAccessLabel"), true);

		// Close the browser tab
		VoodooUtils.closeWindow();

		// Focus back to the reports page
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		//  Click on non-owned Account records (i.e Account owned by Chris User) & Check Billing information field
		new VoodooControl("a", "css", "td.oddListRowS1 a").click();
		// Focus on the new tab where the account got opened
		VoodooUtils.focusWindow(1);
		// Assert that the account opened is the one i.e clicked in the report
		accountNameDetailField.assertEquals(accountData.get(0).get("name"), true);

		// Billing information is not visible and the field displays "No access"
		billingAddressDetailField.assertEquals(reportsData.get("noAccessLabel"), true);

		// Close the browser tab
		VoodooUtils.closeWindow();
		// Focus back to the reports page
		VoodooUtils.focusWindow(0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}