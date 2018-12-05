package com.sugarcrm.test.roles;

import org.junit.*;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_31085 extends SugarTest {
	FieldSet customData = new FieldSet();
	DataSource kbData = new DataSource();

	public void setup() throws Exception {
		FieldSet roleRecord = testData.get("env_role_setup").get(0);
		kbData = testData.get(testName + "_kbData");
		customData = testData.get(testName).get(0);

		// Create 3 KB record
		sugar().knowledgeBase.api.create(kbData);

		// Login as Admin
		sugar().login();

		// Enable KB module from Admin -> Enable Disable module and subpanel
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		
		// Assign one kb record to qauser
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.showMore();
		sugar().knowledgeBase.recordView.getEditField("relAssignedTo").set(customData.get("relAssignedTo"));
		sugar().knowledgeBase.recordView.save();

		// Create role
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580, VOOD-856
		// Role => KB => Delete=Owner
		new VoodooControl("td", "css", "#ACLEditView_Access_KBContents_delete").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_KBContents_delete div select").set(roleRecord.get("roleOwner"));

		// Role => KB => Edit=Owner
		new VoodooControl("td", "css", "#ACLEditView_Access_KBContents_edit").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_KBContents_edit div select").set(roleRecord.get("roleOwner"));
		
		// Save the role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify that confirmation message should not appear continuously while click on create button in KB
	 * @throws Exception
	 */
	@Test
	public void Roles_31085_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Navigate KB module
		sugar().knowledgeBase.navToListView();
		
		// Verifying 3 records are appearing before deletion
		Assert.assertTrue(sugar().knowledgeBase.listView.countRows() == kbData.size());

		// Selecting all record in list view and click on delete
		sugar().knowledgeBase.listView.toggleSelectAll();
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.delete();

		// Verifying warning message is appearing
		sugar().alerts.getWarning().assertVisible(true);

		// Confirm the warning
		sugar().alerts.getWarning().confirmAlert();

		// Verifying permission warning message is appearing when trying to delete KB record
		sugar().alerts.getWarning().assertEquals(customData.get("warningMessage"), true);
		sugar().alerts.getWarning().closeAlert();
		
		// Verifying only one record is deleted due to permission and 2 records are showing
		Assert.assertTrue(sugar().knowledgeBase.listView.countRows() == kbData.size()-1);

		// Click on create button in list view
		sugar().knowledgeBase.listView.create();

		// Verifying No warning appears and create drawer opens
		sugar().alerts.getWarning().assertVisible(false);
		sugar().knowledgeBase.createDrawer.assertVisible(true);

		// Cancel the create drawer
		sugar().knowledgeBase.createDrawer.cancel();

		// Selecting all record in list view and click on delete
		sugar().knowledgeBase.listView.toggleSelectAll();
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.delete();

		// Verifying warning message is appearing
		sugar().alerts.getWarning().assertVisible(true);

		// Click on Cancel
		sugar().alerts.getWarning().cancelAlert();

		// Verifying record is not deleted
		Assert.assertTrue(sugar().knowledgeBase.listView.countRows() == kbData.size()-1);

		// Click on create button in list view
		sugar().knowledgeBase.listView.create();

		// Verifying No warning appears and create drawer opens
		sugar().alerts.getWarning().assertVisible(false);
		sugar().knowledgeBase.createDrawer.assertVisible(true);
		
		// Closing the drawer
		sugar().knowledgeBase.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}