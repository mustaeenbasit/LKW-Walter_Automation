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
public class Accounts_17072 extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		
		// Update accounts records AssignedTo field to qauser
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.getEditField(1,"relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().accounts.listView.saveRecord(1);

		// Create a role 
		roleRecord = testData.get("env_role_setup").get(0);
		roleRecord.put("roleName",testName);
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		
		// For this role, set Account Module's Edit access to Owner 
		// TODO: VOOD-856 VOOD-580 -Create a Roles (ACL) Module LIB,
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_edit div select").set(roleRecord.get("roleOwner"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		sugar().logout();
	}

	/**
	 * 17072: Verify user can inline edit the own records if has Edit permission
	 * @throws Exception
	 */
	@Test
	public void Accounts_17072_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser and open owned record
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Click pencil icon to inline edit
		sugar().accounts.recordView.getDetailField("name").hover();
		// TODO: VOOD-854 - Need lib support for inline edit on Record view
		new VoodooControl ("i", "css", "span[data-name='name'] .fa.fa-pencil").click();
		sugar().accounts.recordView.getEditField("name").set(testName);
		
		// Save and Verify that the record is updated
		sugar().accounts.recordView.save();
		sugar().accounts.recordView.getDetailField("name").assertEquals(testName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}