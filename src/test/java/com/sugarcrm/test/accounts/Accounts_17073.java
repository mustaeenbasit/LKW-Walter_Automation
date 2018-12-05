package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Accounts_17073 extends SugarTest {
	FieldSet roleRecord = new FieldSet();
	UserRecord userChris;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		userChris = (UserRecord) sugar().users.api.create();
		sugar().login();
		
		// Update accounts records AssignedTo field to chris user
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		String userChrisName = sugar().users.getDefaultData().get("userName");
		sugar().accounts.listView.getEditField(1,"relAssignedTo").set(userChrisName);
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

		// Assign QAuser, chris to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		roleRecord.put("userName",userChrisName);
		AdminModule.assignUserToRole(roleRecord);
		sugar().logout();
	}

	/**
	 * 17073: Verify user can not inline edit the not-owned records if the user doesn't have Edit permission 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17073_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as user qauser
		sugar().login(sugar().users.getQAUser());
		
		// Assert 'Edit' isn't visible in list/detail view
		sugar().accounts.navToListView();
		sugar().accounts.listView.openRowActionDropdown(1);
		sugar().accounts.listView.getControl("edit01").assertVisible(false);
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.getControl("editButton").assertVisible(false);
		
		// Assert pencil icon is not visible for inline edit
		sugar().accounts.recordView.getDetailField("name").hover();
		// TODO: VOOD-854 - Need lib support for inline edit on Record view
		new VoodooControl ("i", "css", "span[data-name='name'] .fa.fa-pencil").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}