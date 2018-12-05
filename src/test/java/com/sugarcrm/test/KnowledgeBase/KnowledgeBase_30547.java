package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30547 extends SugarTest {
	DataSource kbData = new DataSource();
	UserRecord chrisUser;
	FieldSet roleRecord = new FieldSet();
	KBRecord kb1, kb2;

	public void setup() throws Exception {
		kbData = testData.get(testName);

		// Login
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		
		// Create chrisUser, other user will be qauser
		chrisUser = (UserRecord) sugar().users.create();

		// Create 2 published KB with several minutes apart
		kbData.get(0).put("relAssignedTo", chrisUser.getRecordIdentifier());
		kbData.get(1).put("relAssignedTo", "qauser");
		kb1 = (KBRecord) sugar().knowledgeBase.create(kbData.get(0));
		kb2 = (KBRecord) sugar().knowledgeBase.create(kbData.get(1));
		sugar().alerts.getSuccess().closeAlert();

		roleRecord = testData.get("env_role_setup").get(0);
		roleRecord.put("userName", chrisUser.get("userName"));
		
		// Create role => Accounts => Access=Disabled
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580, VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_KBContents_delete").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_KBContents_delete div select").set("Owner");
		new VoodooControl("td", "css", "#ACLEditView_Access_KBContents_edit").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_KBContents_edit div select").set("Owner");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign chrisUser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		
		// Logout from admin user and login as custom user
		sugar().logout();
		chrisUser.login();
	}

	/**
	 * Verify that "Create Localization" and "Create Revision" are unavailable when Role has set to Owner
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30547_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Owned record 
		// Check Chris has 5 actions on Listview record dropdown
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.openRowActionDropdown(2);
		new VoodooControl("a", "css", ".list.fld_edit_button a").assertVisible(true);
		new VoodooControl("a", "css", ".list.fld_create_localization_button a").assertVisible(true);
		new VoodooControl("a", "css", ".list.fld_create_revision_button a").assertVisible(true);
		new VoodooControl("a", "css", ".list.fld_follow_button a").assertVisible(true);
		new VoodooControl("a", "css", ".list.fld_delete_button a").assertVisible(true);
		
		
		kb1.navToRecord();
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();
		
		// Check Chris has 7 actions on recordview dropdown
		new VoodooControl("a", "css", ".detail.fld_edit_button a").assertVisible(true);
		new VoodooControl("a", "css", ".detail.fld_create_localization_button a").assertVisible(true);
		new VoodooControl("a", "css", ".detail.fld_create_revision_button a").assertVisible(true);
		new VoodooControl("a", "css", ".detail.fld_share a").assertVisible(true);
		new VoodooControl("a", "css", ".detail.fld_duplicate_button a").assertVisible(true);
		new VoodooControl("a", "css", ".detail.fld_audit_button a").assertVisible(true);
		new VoodooControl("a", "css", ".detail.fld_delete_button a").assertVisible(true);
		
		
		// Check Localizations and revisions subpanel add record is working fine
		new VoodooControl("a", "css", ".panel-top-for-localizations .fld_create_button a").click();
		new VoodooControl("a", "css", ".panel-top-for-revisions .fld_create_button a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#drawers .layout_KBContents.drawer.active").assertVisible(true);
		sugar().knowledgeBase.createDrawer.cancel();
		if (sugar().alerts.getAlert().queryVisible())
			sugar().alerts.confirmAllAlerts();
		
		// Non-owned record
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.openRowActionDropdown(1);
		// Check Chris has 1 action on Listview record dropdown
		new VoodooControl("a", "css", ".list.fld_edit_button a").assertExists(false);
		new VoodooControl("a", "css", ".list.fld_create_localization_button a").assertExists(false);
		new VoodooControl("a", "css", ".list.fld_create_revision_button a").assertExists(false);
		new VoodooControl("a", "css", ".list.fld_follow_button a").assertVisible(true);
		new VoodooControl("a", "css", ".list.fld_delete_button a").assertExists(false);
		
		kb2.navToRecord();
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();
		
		// Check Chris has 3 actions on recordview dropdown
		new VoodooControl("a", "css", ".detail.fld_edit_button a").assertExists(false);
		new VoodooControl("a", "css", ".detail.fld_create_localization_button a").assertExists(false);
		new VoodooControl("a", "css", ".detail.fld_create_revision_button a").assertExists(false);
		new VoodooControl("a", "css", ".detail.fld_share a").assertVisible(true);
		new VoodooControl("a", "css", ".detail.fld_duplicate_button a").assertVisible(true);
		new VoodooControl("a", "css", ".detail.fld_audit_button a").assertVisible(true);
		new VoodooControl("a", "css", ".detail.fld_delete_button a").assertExists(false);
		
		// Check Localizations and revisions subpanel add record is not available for non-owned record
		new VoodooControl("a", "css", ".panel-top-for-localizations .fld_create_button a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#drawers .layout_KBContents.drawer.active").assertVisible(false);

		new VoodooControl("a", "css", ".panel-top-for-revisions .fld_create_button a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#drawers .layout_KBContents.drawer.active").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}