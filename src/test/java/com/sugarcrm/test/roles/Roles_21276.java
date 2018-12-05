package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21276 extends SugarTest {
	
	AccountRecord adminAccount, userAccount;
	public void setup() throws Exception {
		FieldSet roleRecordData = testData.get("env_role_setup").get(0);
		
		// Create Account record assigned to admin
		adminAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();

		// Create a new role, select qauser in this role, 
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580, VOOD-856
		// Select "Account" module
		new VoodooControl("div", "css", "#contentTable table tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();
		
		// Set Billing Address Group Field as Read/Write
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address").set(roleRecordData.get("roleRead/Write"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a QAUser to this Role
		AdminModule.assignUserToRole(roleRecordData);
	
		// Logout as Admin and Login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		
		// Create a unique Account record assigned to QAuser via UI to populate email field
		// TODO: VOOD-444
		FieldSet accountNameFS = new FieldSet();
		accountNameFS.put("name", testName);
		userAccount = (AccountRecord) sugar().accounts.create(accountNameFS);
	}

	/**
	 * Field permissions set to Read/Write & Not Set on Accounts (Group Field) - Verify "inline edit" and "edit view" write
	 * @throws Exception
	 */
	@Test
	public void Roles_21273_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that the edit button is displayed for non-owned records.
		sugar().accounts.listView.openRowActionDropdown(2);
		sugar().accounts.listView.getControl("edit02").assertVisible(true);
		
		// Click on the inline edit icon of an owned record > make edits to each of the fields > Save.
		// TODO: VOOD-597 - need lib support for date created and date updated fields
		VoodooControl dateModifiedCtrl = new VoodooControl("sapn", "css", ".fld_date_modified");
		VoodooControl dateEnteredCtrl = new VoodooControl("sapn", "css", ".fld_date_entered");

		// Verify that all fields should be editable accept Date Created/Modifeld
		dateModifiedCtrl.assertAttribute("class", "edit", false);
		dateEnteredCtrl.assertAttribute("class", "edit", false);
		
		// Create data to update list view with NEW set of values w.r.t default data
		FieldSet updateFS1 = new FieldSet();
		updateFS1.put("billingAddressCity", userAccount.get("billingAddressCity") + testName);
		updateFS1.put("billingAddressCountry", userAccount.get("billingAddressCountry") + testName);
		
		// Inline Edit the record and save
		sugar().accounts.listView.updateRecord(1, updateFS1);

		// Verify that modifications are saved
		// TODO: VOOD-2120 Need support for Module.listView.verify(<row>, FieldSet). OK in subpanel
		sugar().accounts.listView.verifyField(1, "billingAddressCity", updateFS1.get("billingAddressCity"));
		sugar().accounts.listView.verifyField(1, "billingAddressCountry", updateFS1.get("billingAddressCountry"));
		
		// Click on owned Account record > click Edit button > make edits to each of the fields > click Save
		// Create data to update record view with NEW set of values w.r.t updateFS1
		FieldSet updateFS2 = new FieldSet();
		updateFS2.put("name", userAccount.get("name"));
		updateFS2.put("billingAddressStreet", testName);
		updateFS2.put("billingAddressCity", testName);
		updateFS2.put("billingAddressState", testName);
		updateFS2.put("billingAddressPostalCode", testName);
		updateFS2.put("billingAddressCountry", testName);
		
		// Edit the account using the UI.
		userAccount.edit(updateFS2);
		
		// Verify the account was edited.
		userAccount.verify(updateFS2);
		
		// Repeat for Non-owned record
		updateFS1.put("name", adminAccount.get("name"));
		updateFS2.put("name", adminAccount.get("name"));
		
		// Inline Edit the record and save
		sugar().accounts.navToListView();
		sugar().accounts.listView.updateRecord(2, updateFS1);
		
		// Verify that modifications are saved
		// TODO: VOOD-2120 Need support for Module.listView.verify(<row>, FieldSet). OK in subpanel
		sugar().accounts.listView.verifyField(2, "billingAddressCity", updateFS1.get("billingAddressCity"));
		sugar().accounts.listView.verifyField(2, "billingAddressCountry", updateFS1.get("billingAddressCountry"));
		
		// Edit the account using the UI.
		adminAccount.edit(updateFS2);
		
		// Verify the account was edited.
		adminAccount.verify(updateFS2);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}