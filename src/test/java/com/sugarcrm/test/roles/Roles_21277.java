package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21277 extends SugarTest {
	DataSource accountData = new DataSource();
	String qauser = "";

	public void setup() throws Exception {
		// Creating 2 Account Records
		accountData = testData.get(testName);
		sugar().accounts.api.create(accountData);
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
	 * Field permissions set to "Read/Owner Write" on Accounts (group field) - Verify listview and detailview read
	 * @throws Exception
	 */
	@Test
	public void Roles_21277_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log in as qauser
		sugar().login(sugar().users.getQAUser());

		// Go to Accounts record listView
		sugar().accounts.navToListView();

		// Verify that all Accounts are displayed on the listview (both owned and non-owned records).
		Assert.assertTrue("Accounts list view doesn't have two records", sugar().accounts.listView.countRows() == 2);

		// Verifying account names on the list view
		// Need sorting to access specific record
		sugar().accounts.listView.sortBy("headerName", false);
		for (int i = 1 ; i <= accountData.size() ; i++) {
			sugar().accounts.listView.getDetailField(i, "name").assertEquals(accountData.get(i-1).get("name"), true);
		}

		// Click on owned record
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();

		// Verifying it is owned record
		VoodooControl assignedToFieldCtrl = sugar().accounts.recordView.getDetailField("relAssignedTo");
		assignedToFieldCtrl.assertEquals(qauser, true);

		// Verify that all fields of the billing address is visible.
		VoodooControl billingAddressFieldCtrl = sugar().accounts.recordView.getDetailField("billingAddressStreet");
		billingAddressFieldCtrl.assertVisible(true);

		// Click on a non-owned record.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(2);

		// Verifying it is non owned record
		assignedToFieldCtrl.assertEquals(qauser, false);

		// Verify that all fields of the billing address is visible.
		billingAddressFieldCtrl.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}