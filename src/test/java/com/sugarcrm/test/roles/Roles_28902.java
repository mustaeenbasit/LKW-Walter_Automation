package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_28902 extends SugarTest {
	DataSource roleRecords = new DataSource();
	UserRecord customUser;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		roleRecords = testData.get(testName);
		sugar().login();

		// Create a user record
		customUser = (UserRecord) sugar().users.create();
	}

	/**
	 * Verify warning message is displayed when user tries to mass update the non-owned records. 
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_28902_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create firstRole : Set 'Account Name' field permission in contacts module to Read/Owner Write
		// Create secondRole : Set 'Account Name' field permission in contacts module to Read/Write
		// TODO: VOOD-580, VOOD-856
		VoodooControl saveRole, contactsLink;
		saveRole = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		contactsLink = new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().contacts.moduleNamePlural+"')]");
		VoodooControl fieldCtrl = new VoodooControl("div", "id", "account_namelink");
		VoodooControl permissionsCtrl = new VoodooControl("select", "id", "flc_guidaccount_name");
		for(int i = 0; i < roleRecords.size(); i++) {
			AdminModule.createRole(roleRecords.get(i));
			VoodooUtils.focusFrame("bwc-frame");
			contactsLink.click();
			VoodooUtils.waitForReady();
			fieldCtrl.click();
			if(!permissionsCtrl.queryVisible())
				fieldCtrl.click();
			permissionsCtrl.set(roleRecords.get(0).get("set_access"));

			// Click Save button to save the roleNone
			saveRole.click();
			VoodooUtils.waitForReady();
			VoodooUtils.focusDefault();

			// Assign role to QAUser and userA
			AdminModule.assignUserToRole(roleRecords.get(0));
			VoodooUtils.waitForReady();
			AdminModule.assignUserToRole(roleRecords.get(1));
			VoodooUtils.waitForReady();
		}

		// Create 2 contact records and assign the records to user 'QAUser'
		sugar().contacts.navToListView();
		for(int i = 0; i < roleRecords.size(); i++) {
			sugar().contacts.listView.create();
			sugar().contacts.createDrawer.showMore();
			sugar().contacts.createDrawer.getEditField("lastName").set(testName + "_" + i);
			sugar().contacts.createDrawer.getEditField("relAssignedTo").set(roleRecords.get(1).get("userName"));
			sugar().contacts.createDrawer.save();
		}

		// Logout from admin user and Login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Do mass update for records created above for Contacts' module
		sugar().contacts.navToListView();
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.listView.openActionDropdown();
		sugar().contacts.listView.massUpdate();
		sugar().contacts.massUpdate.getControl("massUpdateField02").set(roleRecords.get(0).get("massUpdateField"));
		sugar().contacts.massUpdate.getControl("massUpdateValue02").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.massUpdate.update();

		// Verify that the following warning message is shown if trying to mass update on non-owner's record:'Warning Mass Update incomplete. 2 record(s) remain unchanged'.
		sugar().alerts.getWarning().assertContains(roleRecords.get(0).get("warningMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}