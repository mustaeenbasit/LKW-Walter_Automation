package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Roles_21216 extends SugarTest {
	FieldSet roleRecord = new FieldSet(); 

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		DataSource accountDataSet = testData.get(testName + "_records");

		// crate case record
		sugar().cases.api.create();
		// create account records
		sugar().accounts.api.create(accountDataSet);

		sugar().login();
		// Create a new Role 
		AdminModule.createRole(roleRecord);

		VoodooUtils.focusFrame("bwc-frame");
		// TODO VOOD-856
		//  Admin -> Role management -> Select account module and set its "Delete" role to be "Owner"
		new VoodooControl("td", "id", "ACLEditView_Access_Accounts_edit").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_edit select").set(roleRecord.get("accessType"));
		VoodooUtils.waitForReady();
		// Click Save
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// assign role to "qauser"
		AdminModule.assignUserToRole(roleRecord);

		// navigate to accounts listview
		sugar().accounts.navToListView();
		sugar().accounts.listView.sortBy("headerName", true);
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(2);
		FieldSet fs = new FieldSet();
		fs.put("Assigned to", sugar().users.getQAUser().get("userName"));
		// update "Assigned to" to "qauser" for few records
		sugar().accounts.massUpdate.performMassUpdate(fs);

		// navigate to cases listview
		sugar().cases.navToListView();
		sugar().cases.listView.sortBy("headerName", true);
		sugar().cases.listView.checkRecord(1);
		fs.put("Account Name", accountDataSet.get(0).get("name"));
		// link account record with case record
		sugar().cases.massUpdate.performMassUpdate(fs);
		sugar().logout();
	}

	/**
	 * Verify user can only edit own records while the module edit role is set to Owner
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21216_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// login as "qauser"
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		// Click account module from navigation bar
		sugar().navbar.clickModuleDropdown(sugar().accounts);

		// Verify that account create action available inside menu
		sugar().accounts.menu.getControl("createAccount").assertVisible(true);

		// close the dropdown
		sugar().navbar.clickModuleDropdown(sugar().accounts);

		sugar().accounts.listView.sortBy("headerName", true);

		// open action dropdown of a record that is assigned to qauser
		sugar().accounts.listView.openRowActionDropdown(1);
		// verify edit action available inside menu
		sugar().accounts.listView.getControl("edit01").assertExists(true);

		// close already opened action menu
		sugar().accounts.listView.openRowActionDropdown(1);

		// open action dropdown of second record that is assigned to qauser
		sugar().accounts.listView.openRowActionDropdown(2);
		// verify edit action available inside menu
		sugar().accounts.listView.getControl("edit02").assertExists(true);

		// close already opened action menu 
		sugar().accounts.listView.openRowActionDropdown(2);

		// open action dropdown of a record that is not assigned to qauser
		sugar().accounts.listView.openRowActionDropdown(3);
		// verify edit action is not available inside menu
		sugar().accounts.listView.getControl("edit03").assertExists(false);

		// Navigate to recodview of a record that is assigned to qauser
		sugar().accounts.listView.clickRecord(1);
		// verify that edit button available on record view
		sugar().accounts.recordView.getControl("editButton").assertVisible(true);

		// navigate to cases subpanel inside account record
		StandardSubpanel casesSubPanel = sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		casesSubPanel.expandSubpanel();
		casesSubPanel.expandSubpanelRowActions(1);
		// verify that edit option is available in the action drop down for the record in the sub-panel 	
		casesSubPanel.getControl("editActionRow01").assertVisible(true);

		// edit the record and made some changes on record view
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("name").set(testName);
		// save the record
		sugar().accounts.recordView.save();
		// verify that changes in the record saved successfully
		sugar().accounts.recordView.getDetailField("name").assertEquals(testName, true);

		// open quick create menu
		sugar().accounts.navToListView();
		sugar().navbar.openQuickCreateMenu();
		// verify that create account action available there 
		sugar().navbar.quickCreate.getControl("Accounts").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}