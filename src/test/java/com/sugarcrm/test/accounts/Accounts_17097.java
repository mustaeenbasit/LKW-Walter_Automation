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
public class Accounts_17097 extends SugarTest {
	FieldSet roleRecord = new FieldSet();
	FieldSet myFS = new FieldSet();
	UserRecord userChris;
	
	public void setup() throws Exception {
		// create records via API (assigned to Admin), assign to qauser, chris after login via UI
		sugar().accounts.api.create();
		sugar().accounts.api.create();
		userChris = (UserRecord) sugar().users.api.create();
		sugar().login();
		
		// Update records Description field, AssignedTo field to qauser
		myFS = testData.get(testName).get(0);
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("description").set(myFS.get("description"));
		sugar().accounts.recordView.save();
		
		// Update records Description, AssignedTo field to chris
		sugar().accounts.recordView.gotoNextRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("relAssignedTo").set(userChris.get("userName"));
		sugar().accounts.recordView.getEditField("description").set(myFS.get("description"));
		sugar().accounts.recordView.save();

		// Create a role 
		roleRecord = testData.get("env_role_setup").get(0);
		roleRecord.put("roleName",testName);
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Set Description field's access to Owner Read/Owner Write
		// TODO: VOOD-856, VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "#contentTable table tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "descriptionlink").click();
		new VoodooControl("select", "id", "flc_guiddescription").click();
		new VoodooControl("option", "css", "#flc_guiddescription [label='Owner Read/Owner Write']").click();
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		roleRecord.put("userName",userChris.get("userName"));
		AdminModule.assignUserToRole(roleRecord);
		sugar().logout();
	}

	/**
	 * 17097: Verify user can (pre)view the textarea field of the owned record if field permission = owner read/owner write
	 * @throws Exception
	 */
	@Test
	public void Accounts_17097_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser and goto accounts module
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();
		
		// Click the preview button for the new created account assigned to qauser
		sugar().accounts.listView.previewRecord(2);
		sugar().previewPane.showMore();
		sugar().previewPane.getPreviewPaneField("description").assertVisible(true);
		// Click the preview button for the new created account assigned to chrisUser
		sugar().accounts.listView.previewRecord(1);
		sugar().previewPane.getPreviewPaneField("description").assertVisible(false);
		// TODO: VOOD-1445
		new VoodooControl("span", "css", ".preview-data .noaccess.fld_description span").assertContains(myFS.get("NoAccess"), true);
		
		// Click account record assigned to qauser and check Description field is visible on the record view
		sugar().accounts.listView.clickRecord(2);
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("description").assertVisible(true);
		
		// Click account record assigned to chrisUser and check Description field is Not visible on the record view
		sugar().accounts.recordView.gotoPreviousRecord();
		// TODO: VOOD-1445
		new VoodooControl("span", "css", ".record-cell .noaccess.fld_description span").assertContains(myFS.get("NoAccess"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}