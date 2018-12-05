package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21233 extends SugarTest {
	DataSource roleRecordData;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName);
		FieldSet accountName = new FieldSet();
		accountName.put("name", roleRecordData.get(0).get("name"));
		sugar().accounts.api.create(accountName);
		accountName.clear();
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData.get(0));
		VoodooUtils.focusFrame("bwc-frame");

		// For Accounts module, Set all field permissions to "Owner Read/Owner Write"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();
		for(int i = 0; i < roleRecordData.size(); i++) {
			// Need to click again on fieldCtrl if permissionsCtrl is not visible as some time script fails to click.
			VoodooControl fieldCtrl = new VoodooControl("div", "id", roleRecordData.get(i).get("fields"));
			VoodooControl permissionsCtrl = new VoodooControl("select", "id", roleRecordData.get(i).get("permissions"));
			fieldCtrl.click();
			if(!permissionsCtrl.queryVisible())
				fieldCtrl.click();
			permissionsCtrl.set(roleRecordData.get(0).get("access"));
		}

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData.get(0));

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Field permissions set to Owner Read/Owner Write on Accounts - Inline edit of owned records
	 * @throws Exception
	 */
	@Test
	public void Roles_21233_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Create an Account(Owned record for QAUser)
		sugar().accounts.create();

		// TODO: VOOD-597 - need lib support for date created and date updated fields
		VoodooControl dateModifiedCtrl = new VoodooControl("sapn", "css", ".fld_date_modified");
		VoodooControl dateEnteredCtrl = new VoodooControl("sapn", "css", ".fld_date_entered");

		// Click in-line edit from Action drop down on a record owned by QAUser
		sugar().accounts.listView.editRecord(1);

		// Verify that all fields should be editable accept Date Created
		/**
		 * Update all fields shows that fields are editable.Therefore, edit all the fields
		 * Not going to update Assigned user as Owner Read/Owner Write permission. So first verify the complete modification and after that
		 * update the record only update Assigned user and verify No Access(which shows success modification)
		 */

		sugar().accounts.listView.getEditField(1, "name").set(roleRecordData.get(17).get("fieldValues"));
		sugar().accounts.listView.getEditField(1, "billingAddressCity").set(roleRecordData.get(8).get("fieldValues"));
		sugar().accounts.listView.getEditField(1, "billingAddressCountry").set(roleRecordData.get(0).get("billingAddressCountry"));
		sugar().accounts.listView.getEditField(1, "workPhone").set(roleRecordData.get(18).get("fieldValues"));
		sugar().accounts.listView.getEditField(1, "emailAddress").set(roleRecordData.get(14).get("fieldValues"));
		dateModifiedCtrl.assertAttribute("class", "edit", false);
		dateEnteredCtrl.assertAttribute("class", "edit", false);
		sugar().accounts.listView.saveRecord(1);

		// Verify that modification can be saved
		sugar().accounts.listView.getDetailField(1, "name").assertContains(roleRecordData.get(17).get("fieldValues"), true);
		sugar().accounts.listView.getDetailField(1, "billingAddressCity").assertContains(roleRecordData.get(8).get("fieldValues"), true);
		sugar().accounts.listView.getDetailField(1, "billingAddressCountry").assertContains(roleRecordData.get(0).get("billingAddressCountry"), true);
		sugar().accounts.listView.getDetailField(1, "workPhone").assertContains(roleRecordData.get(18).get("fieldValues"), true);
		sugar().accounts.listView.getDetailField(1, "emailAddress").assertContains(roleRecordData.get(14).get("fieldValues"), true);

		// Update Assigned To user
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.getEditField(1, "relAssignedTo").set(roleRecordData.get(4).get("fieldValues"));
		sugar().accounts.listView.saveRecord(1);

		// Verify that modification can be saved(As "No Access" is shown for all fields)
		String noAccessValue = roleRecordData.get(0).get("noAccess");
		new VoodooControl("sapn", "css", ".noaccess.fld_name").assertContains(noAccessValue, true);
		new VoodooControl("sapn", "css", ".noaccess.fld_billing_address_city").assertContains(noAccessValue, true);
		new VoodooControl("sapn", "css", ".noaccess.fld_billing_address_country").assertContains(noAccessValue, true);
		new VoodooControl("sapn", "css", ".noaccess.fld_phone_office").assertContains(noAccessValue, true);
		new VoodooControl("sapn", "css", ".noaccess.fld_assigned_user_name").assertContains(noAccessValue, true);
		new VoodooControl("sapn", "css", ".noaccess.fld_email.noaccess").assertContains(noAccessValue, true);

		// Click inline edit on a record not owned by QAUser
		sugar().accounts.listView.editRecord(2);

		// Verify that the fields cannot be read. "No Access" is shown for all fields
		// TODO: VOOD-1445
		new VoodooControl("sapn", "css", "tr:nth-child(2) .fld_name.noaccess").assertContains(noAccessValue, true);
		new VoodooControl("sapn", "css", "tr:nth-child(2) .fld_billing_address_city.noaccess").assertContains(noAccessValue, true);
		new VoodooControl("sapn", "css", "tr:nth-child(2) .fld_billing_address_country.noaccess").assertContains(noAccessValue, true);
		new VoodooControl("sapn", "css", "tr:nth-child(2) .fld_phone_office.noaccess").assertContains(noAccessValue, true);
		new VoodooControl("sapn", "css", "tr:nth-child(2) .fld_assigned_user_name.noaccess").assertContains(noAccessValue, true);
		new VoodooControl("sapn", "css", "tr:nth-child(2) .fld_email.noaccess").assertContains(noAccessValue, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}