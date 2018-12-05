package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Accounts_17254 extends SugarTest {
	FieldSet roleRecord; 

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().accounts.api.create();
		
		sugar().login();

		FieldSet accountRecord = new FieldSet();
		accountRecord.put("name", sugar().users.getDefaultData().get("lastName"));
		accountRecord.put("relAssignedTo", sugar().users.getQAUser().get("userName"));
		// create one record via UI to select qauser in AssignedTo field
		sugar().accounts.create(accountRecord);

		// Create a role 
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "#contentTable table tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "assigned_user_namelink").click();
		new VoodooControl("select", "id", "flc_guidassigned_user_name").click();
		new VoodooControl("option", "css", "#flc_guidassigned_user_name [label='Read/Owner Write']").click();
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}


	/**
	 *  check field's read/owner write ACL control in listview inline edit
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17254_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		// Verify that if the record is assigned to qauser, then the field is shown up and editable
		sugar().accounts.listView.getEditField(1, "relAssignedTo").assertAttribute("class","select2-choice");

		sugar().accounts.listView.editRecord(2);
		// TODO: VOOD-1430 
		//  Verify that if the record is not assigned to qauser, then the field is read only
		new VoodooControl("div", "css", ".flex-list-view-content tr:nth-child(2) .fld_assigned_user_name").assertAttribute("class", "detail");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}