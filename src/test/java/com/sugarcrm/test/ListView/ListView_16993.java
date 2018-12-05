package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class ListView_16993 extends SugarTest {
	FieldSet roleRecord, roleData;
	DataSource accData;
	UserRecord qauser;

	public void setup() throws Exception {
		accData = testData.get(testName);
		roleData = testData.get(testName+"_roleData").get(0);
		sugar.accounts.api.create(accData);
		roleRecord = testData.get("env_role_setup").get(0);
		sugar.login();

		// Create and Save the Role 
		AdminModule.createRole(roleRecord);
		// Account module > Edit field permissions > set field permissions to "Read/Owner Write".
		// TODO: VOOD-856
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".edit.view tr:nth-of-type(2) a").click();
		new VoodooControl("div", "id", "email1link").click();
		new VoodooControl("select", "id", "flc_guidemail1").set(roleData.get("role")); 
		new VoodooControl("div", "id", "namelink").click();
		new VoodooControl("select", "id", "flc_guidname").set(roleData.get("role"));
		new VoodooControl("div", "id", "assigned_user_namelink").click();
		new VoodooControl("select", "id", "flc_guidassigned_user_name").set(roleData.get("role"));
		new VoodooControl("div", "id", "phone_officelink").click();
		if (!(new VoodooControl("select", "id", "flc_guidphone_office").queryVisible()))
			new VoodooControl("div", "id", "phone_officelink").click();
		new VoodooControl("select", "id", "flc_guidphone_office").set(roleData.get("role"));
		new VoodooControl("div", "id", "billing_addresslink").click();
		if (!(new VoodooControl("select", "id", "flc_guidbilling_address").queryVisible()))
			new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address").set(roleData.get("role"));
		// Save the Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.focusDefault();

		// Assign qauser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar.accounts.navToListView();
		sugar.accounts.listView.sortBy("headerName", true);
		// Change "Assigned To" qauser for 1 record to create "owned" records
		sugar.accounts.listView.editRecord(1);
		qauser = new UserRecord(sugar.users.getQAUser());
		sugar.accounts.listView.getEditField(1, "relAssignedTo").set(qauser.get("userName"));
		sugar.accounts.listView.saveRecord(1);
	}

	/**
	 * Edit inline record - All field permission set to "Read/Owner Write"
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_16993_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.logout();
		sugar.login(qauser);
		sugar.accounts.navToListView();
		// Verify edit button is available 
		sugar.accounts.listView.openRowActionDropdown(1);
		sugar.accounts.listView.getControl("edit01").assertVisible(true);
		// Click edit button on owned record.
		sugar.accounts.listView.getControl("edit01").click();
		sugar.accounts.listView.getEditField(1, "name").set(testName);
		sugar.accounts.listView.getEditField(1, "billingAddressCity").set(testName);
		sugar.accounts.listView.getEditField(1, "workPhone").set(testName);
		sugar.accounts.listView.getEditField(1, "relAssignedTo").set(roleData.get("user"));
		sugar.accounts.listView.saveRecord(1);
		// Verify inline edit works on owned records,
		VoodooControl nameFieldCtrl = sugar.accounts.listView.getDetailField(1, "name");
		nameFieldCtrl.hover();
		nameFieldCtrl.assertContains(testName, true);
		sugar.accounts.listView.getDetailField(1, "workPhone").assertContains(testName, true);
		sugar.accounts.listView.getDetailField(1, "billingAddressCity").assertContains(testName, true);
		sugar.accounts.listView.getDetailField(1, "relAssignedTo").assertContains(roleData.get("user"), true);

		// Verify User can still click to open the action menu to access edit action 
		sugar.accounts.listView.openRowActionDropdown(2);
		sugar.accounts.listView.getControl("edit02").assertVisible(true);
		// Click edit button on non-owned record.
		sugar.accounts.listView.getControl("edit02").click();
		// Verify fields should stay in read-only mode
		// TODO: VOOD-1430 Need library support to verify "Read only" fields if there is no "disable" class applied to the field
		new VoodooControl("span", "css", ".fld_name.detail").assertAttribute("class", "detail");
		new VoodooControl("span", "css", ".fld_phone_office.detail").assertAttribute("class", "detail");
		new VoodooControl("span", "css", ".fld_billing_address_city.detail").assertAttribute("class", "detail");
		new VoodooControl("span", "css", ".fld_assigned_user_name.detail").assertAttribute("class", "detail");
		// Cancel
		sugar.accounts.listView.cancelRecord(2);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}