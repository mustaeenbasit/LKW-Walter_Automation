package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21280 extends SugarTest {
	DataSource accountDataDS = new DataSource();
	String qauser = "";

	public void setup() throws Exception {
		// Creating 2 Account Records
		accountDataDS = testData.get(testName);
		sugar().accounts.api.create(accountDataDS);
		sugar().login();

		// Create a role
		FieldSet roleRecordData = testData.get("env_role_setup").get(0);
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Set "Billing Street" field permissions to "Read/Owner Write", and leave all other fields as "Not Set".
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#contentTable tbody tr td table:nth-child(9) tbody tr td:nth-child(1) table tbody tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address").set(roleRecordData.get("roleRead/OwnerWrite"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into this Role
		AdminModule.assignUserToRole(roleRecordData);

		// Assign one account record to qauser.
		sugar().accounts.navToListView();
		// Sorting to track assigned record
		sugar().accounts.listView.sortBy("headerName", false);
		sugar().accounts.listView.editRecord(1);
		qauser = sugar().users.getQAUser().get("userName");
		sugar().accounts.listView.getEditField(1, "relAssignedTo").set(qauser);
		sugar().accounts.listView.saveRecord(1);
		sugar().logout();
	}

	/**
	 * Field permissions set to "Read/Owner Write" on Accounts (group field) - Verify report view of owned and non-owned records
	 * @throws Exception
	 */
	@Test
	public void Roles_21280_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log in as qauser
		sugar().login(sugar().users.getQAUser());

		// Navigating to reports module 
		sugar().navbar.selectMenuItem(sugar.reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// Select Row and Column Report Type > Accounts Module
		// TODO: VOOD-822
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Accounts").click();

		// Click "Next"
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();

		// Choose Display Columns
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("tr", "id", "Accounts_billing_address_city").click();
		new VoodooControl("tr", "id", "Accounts_billing_address_country").click();
		new VoodooControl("tr", "id", "Accounts_billing_address_postalcode").click();
		new VoodooControl("tr", "id", "Accounts_billing_address_state").click();
		new VoodooControl("tr", "id", "Accounts_billing_address_street").click();

		// Click "Next"
		nextBtnCtrl.click();

		// Save and Run report
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "css", "#saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// Sorting to verify specific record
		new VoodooControl("a", "css", "#report_results tbody tr th a").click();

		// Verify All Accounts are displayed both owned and non-owned records.
		// Non-Owned Record
		VoodooControl notOwnAccountRecordCtrl = new VoodooControl("a", "css", ".listViewBody .oddListRowS1 a");
		notOwnAccountRecordCtrl.assertEquals(accountDataDS.get(1).get("name"), true);
		// Owned record
		VoodooControl ownAccountRecordCtrl = new VoodooControl("a", "css", ".listViewBody .evenListRowS1 a");
		ownAccountRecordCtrl.assertEquals(accountDataDS.get(0).get("name"), true);

		// Verify that all Billing fields are displayed on report for all Accounts.
		// TODO: VOOD-822
		String billingFieldsNotOwnRecordCtrl = ".oddListRowS1 td:nth-child(%d)";
		FieldSet accountsDefaultData = sugar().accounts.getDefaultData();

		// Billing Fields
		String billingAddressCity = accountsDefaultData.get("billingAddressCity");
		String billingAddressCountry = accountsDefaultData.get("billingAddressCountry");
		String billingAddressPostalCode = accountsDefaultData.get("billingAddressPostalCode");
		String billingAddressState = accountsDefaultData.get("billingAddressState");
		String billingAddressStreet = accountsDefaultData.get("billingAddressStreet");

		// Verifying Non-owned record
		new VoodooControl("td", "css", String.format(billingFieldsNotOwnRecordCtrl, 2)).assertEquals(billingAddressCity, true);
		new VoodooControl("td", "css", String.format(billingFieldsNotOwnRecordCtrl, 3)).assertEquals(billingAddressCountry, true);
		new VoodooControl("td", "css", String.format(billingFieldsNotOwnRecordCtrl, 4)).assertEquals(billingAddressPostalCode, true);
		new VoodooControl("td", "css", String.format(billingFieldsNotOwnRecordCtrl, 5)).assertEquals(billingAddressState, true);
		new VoodooControl("td", "css", String.format(billingFieldsNotOwnRecordCtrl, 6)).assertEquals(billingAddressStreet, true);

		// Verifying Owned record
		String billingFieldsOwnRecordCtrl = ".evenListRowS1 td:nth-child(%d)";
		new VoodooControl("td", "css", String.format(billingFieldsOwnRecordCtrl, 2)).assertEquals(billingAddressCity, true);
		new VoodooControl("td", "css", String.format(billingFieldsOwnRecordCtrl, 3)).assertEquals(billingAddressCountry, true);
		new VoodooControl("td", "css", String.format(billingFieldsOwnRecordCtrl, 4)).assertEquals(billingAddressPostalCode, true);
		new VoodooControl("td", "css", String.format(billingFieldsOwnRecordCtrl, 5)).assertEquals(billingAddressState, true);
		new VoodooControl("td", "css", String.format(billingFieldsOwnRecordCtrl, 6)).assertEquals(billingAddressStreet, true);

		// Click on the non owned account name 
		notOwnAccountRecordCtrl.click();

		VoodooUtils.focusWindow(1);
		// Verifying it is non owned record
		VoodooControl assignedToFieldCtrl = sugar().accounts.recordView.getDetailField("relAssignedTo");
		assignedToFieldCtrl.assertEquals(qauser, false);

		// Show More
		sugar().accounts.recordView.showMore();

		// Verify that all fields of the billing address is not Editable, inline Edit
		VoodooControl billingAddressDetailCtrl = sugar().accounts.recordView.getDetailField("billingAddressStreet");
		billingAddressDetailCtrl.hover();
		VoodooControl pencilIconCtrl = new VoodooControl("i", "css", "span[data-name='billing_address'] .fa.fa-pencil");
		pencilIconCtrl.assertVisible(false);

		// Verify that all fields of the billing address is not Editable, Edit View
		sugar().accounts.recordView.edit();
		VoodooControl billingAddressEditCtrl = sugar().accounts.recordView.getEditField("billingAddressStreet");
		billingAddressEditCtrl.assertVisible(false);

		// cancel
		sugar().accounts.recordView.cancel();

		// Click on the owned account name 
		sugar().accounts.navToListView();
		// Sorting to verify specific record
		sugar().accounts.listView.sortBy("headerName", false);
		sugar().accounts.listView.clickRecord(1);

		// Verifying it is owned record
		assignedToFieldCtrl.assertEquals(qauser, true);

		// Show More
		sugar().accounts.recordView.showMore();

		// Verify fields of the billing address is editable, inline Edit.
		billingAddressDetailCtrl.hover();
		// Click pencil icon
		// TODO: VOOD-854
		pencilIconCtrl.click();
		billingAddressEditCtrl.assertVisible(true);

		// Cancel
		sugar().accounts.recordView.cancel();

		// Verify that all fields of the billing address is editable, Edit view.
		sugar().accounts.recordView.edit();
		billingAddressEditCtrl.assertVisible(true);

		// Cancel Edit view 
		sugar().accounts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}