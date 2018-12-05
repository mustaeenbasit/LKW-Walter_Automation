package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21206 extends SugarTest {
	FieldSet roleRecord; 
	DataSource accountDataSet;
	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		accountDataSet = testData.get(testName);
		// create account record
		sugar().accounts.api.create(accountDataSet);

		sugar().login();
		// Create a new Role 
		AdminModule.createRole(roleRecord);

		VoodooUtils.focusFrame("bwc-frame");
		// TODO VOOD-856
		//  Admin -> Role management -> Select account module and set its "Delete" role to be "Owner"
		new VoodooControl("td", "id", "ACLEditView_Access_Accounts_delete").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_delete select").set("Owner");
		VoodooUtils.waitForReady();
		// Click Save
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// assign role to "qauser"
		AdminModule.assignUserToRole(roleRecord);

		// navigate to accounts listview
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(2);
		sugar().accounts.listView.checkRecord(3);
		FieldSet fs = new FieldSet();
		fs.put("Assigned to", sugar().users.getQAUser().get("userName"));
		// update "Assigned to" to "qauser" for few records
		sugar().accounts.massUpdate.performMassUpdate(fs);

		sugar().logout();
	}

	/**
	 * Verify user can only delete own records while the module delete role is set to be Owner
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21206_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// login as "qauser"
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();
		sugar().accounts.listView.sortBy("headerName", true);

		// Select a record assigned to this user on the list view and click delete 
		sugar().accounts.listView.deleteRecord(2);
		sugar().alerts.getWarning().confirmAlert();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that the record assigned to the selected user is deleted and removed from the list view. 
		for (int i = 1; i < accountDataSet.size(); i++) {
			sugar().accounts.listView.getDetailField(i, "name").assertEquals(accountDataSet.get(1).get("name"), false);
		}

		// Select two records, one is assigned to this user and the other is not owned by this user
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(2);

		sugar().accounts.listView.openActionDropdown();
		// Click Delete button from drop down action on the list view 
		sugar().accounts.listView.delete();
		sugar().alerts.getWarning().confirmAlert();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that the user-owned record should be deleted from the list view.
		for (int i = 1; i < accountDataSet.size()-2; i++) {
			sugar().accounts.listView.getDetailField(i, "name").assertEquals(accountDataSet.get(2).get("name"), false);
		}

		// Verify that The non-owned record should not be deleted and should be kept in the list view. 
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(accountDataSet.get(0).get("name"), true);

		// Select another record assigned to this user and go to the detail view 
		sugar().accounts.listView.clickRecord(2);
		// Click Delete button from drop down action
		sugar().accounts.recordView.delete();
		sugar().alerts.getWarning().confirmAlert();
		sugar().alerts.waitForLoadingExpiration();

		// Verify that  The record assigned to the selected user should be deleted from the detail view.   
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(accountDataSet.get(3).get("name"), false);

		// Select a record not assigned to this user and go to detail view
		sugar().accounts.listView.clickRecord(1);
		//  Click action drop down list
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		// Verify that Delete is not shown.  
		sugar().accounts.recordView.getControl("deleteButton").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}