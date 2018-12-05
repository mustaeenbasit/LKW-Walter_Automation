package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Roles_21258 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		FieldSet roleRecord = testData.get("env_role_setup").get(0);

		// Login as a valid user
		sugar().login();

		// Create a new role in Sugar and select a module -> Set Edit Role to be All
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, Set the Contacts Edit cell to All
		// TODO: VOOD-580
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_edit div select").set(roleRecord.get("roleAll"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Select a user for this role
		AdminModule.assignUserToRole(roleRecord);

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify edit and related actions are shown in the list view and detail view while the Edit role is set to All or Not Set
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21258_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to the select module -> Click on "Create" link of module from top navigation bar
		sugar().navbar.selectMenuItem(sugar().contacts, "createContact");

		// user should be able to create
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify that the Contact record is created successfully
		sugar().contacts.listView.getDetailField(1, "fullName").assertContains(testName, true);

		// Open the row action drop down
		sugar().contacts.listView.openRowActionDropdown(1);

		FieldSet contactsData = testData.get(testName).get(0);

		// Verify that for Sidecar modules: "Edit" should be available on the row level action drop down to in-line edit the records in list view
		sugar().contacts.listView.getControl("edit01").assertEquals(contactsData.get("edit"), true);

		// Close the row action drop down
		sugar().contacts.listView.openRowActionDropdown(1);

		// Select contacts record and open action drop down
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.listView.openActionDropdown();

		// Verify that the Mass Update and Merge actions should be shown in the list view action list
		sugar().contacts.listView.getControl("massUpdateButton").assertEquals(contactsData.get("massUpdate"), true);
		// TODO VOOD-681 and VOOD-689
		new VoodooControl("a", "css", ".fld_merge_button.list a").assertEquals(contactsData.get("merge"), true);

		// Close the action drop down and de-select all the record 
		sugar().contacts.listView.openActionDropdown();
		sugar().contacts.listView.toggleSelectAll();

		// Check the drop down action on the detail view
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.openPrimaryButtonDropdown();

		// Verify that the Edit and related actions should be shown in the detail view action list: Edit, Duplicate
		sugar().contacts.recordView.getControl("editButton").assertEquals(contactsData.get("edit"), true);
		// TODO: VOOD-738
		new VoodooControl("a", "css", ".fld_find_duplicates.detail a").assertEquals(contactsData.get("findDuplicate"), true);

		// Close the opened primary action drop down'
		sugar().contacts.recordView.openPrimaryButtonDropdown();

		// Check quick create list by clicking + sign on the top right -> user should be able to create
		sugar().navbar.quickCreateAction(sugar().contacts.moduleNamePlural);
		sugar().contacts.createDrawer.getEditField("lastName").set(contactsData.get("quickCreateContact"));
		sugar().contacts.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Navigate to contacts list view
		sugar().contacts.navToListView();

		// Verify that the Contact record is created successfully
		sugar().contacts.listView.getDetailField(1, "fullName").assertContains(contactsData.get("quickCreateContact"), true); 

		// Check related module sub-panel 
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel contactsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.expandSubpanel();
		contactsSubpanel.expandSubpanelRowActions(1);

		// Verify that the Edit button should be shown on the sub-panel for this module(i.e. Contacts)
		contactsSubpanel.getControl("editActionRow01").assertEquals(contactsData.get("edit"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}