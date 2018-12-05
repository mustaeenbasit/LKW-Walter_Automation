package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Accounts_17007 extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		// create records via API (assigned to Admin), assign one to qauser after login via UI
		sugar().accounts.api.create();
		sugar().accounts.api.create();
		sugar().login();

		// Update one records AssignedTo field to qauser
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.getEditField(1,"relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().accounts.listView.saveRecord(1);

		// Create a role 
		roleRecord = testData.get("env_role_setup").get(0);
		roleRecord.remove("roleName");
		roleRecord.put("roleName",testName);
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Set Phone field's access to Read/Owner Write
		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "#contentTable table tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "phone_officelink").click();
		new VoodooControl("select", "id", "flc_guidphone_office").click();
		new VoodooControl("option", "css", "#flc_guidphone_office [label='Read/Owner Write']").click();
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		sugar().logout();
	}

	/**
	 * ACL control for phone type field on list view inline edit
	 * @throws Exception
	 */
	@Test
	public void Accounts_17007_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);

		// Verify that if the record is assigned to qauser, then the field is shown up and editable
		sugar().accounts.listView.getEditField(1,"workPhone").set(testName);
		sugar().accounts.listView.saveRecord(1);
		sugar().accounts.listView.getDetailField(1,"workPhone").assertEquals(testName, true);

		// Verify that if the record is not assigned to qauser, then the field is read only
		sugar().accounts.listView.editRecord(2);
		// TODO: VOOD-1430  verify "Read only" fields if there is no "disable" class applied to the field
		new VoodooControl("span", "css", ".flex-list-view-content tr:nth-child(2) .fld_phone_office").assertAttribute("class", "detail");
		sugar().accounts.listView.cancelRecord(2);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}