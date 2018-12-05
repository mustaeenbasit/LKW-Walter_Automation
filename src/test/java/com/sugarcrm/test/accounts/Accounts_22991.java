package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_22991 extends SugarTest {
	UserRecord myUser;

	public void setup() throws Exception {
		// Login
		sugar().login();

		// Create a Regular User
		// TODO: VOOD-1200
		myUser = (UserRecord) sugar().users.create();

		// Logout
		sugar().logout();
	}

	/**
	 * Create Account _Verify that only the records belong to the current user's team are displayed when creating a potentially duplicated record.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22991_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login to SugarCRM as a valid regulat user (QAUser)
		sugar().login(sugar().users.getQAUser());

		// Go to "Accounts" module
		sugar().accounts.navToListView();

		// Define controls for Accounts create drawer
		VoodooControl nameEditFieldCtrl = sugar().accounts.createDrawer.getEditField("name");
		VoodooControl teamEditFieldCtrl = sugar().accounts.createDrawer.getEditField("relTeam");
		VoodooControl teamDetailFieldCtrl = sugar().accounts.recordView.getDetailField("relTeam");

		// Create an account
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();
		nameEditFieldCtrl.set(testName);
		teamEditFieldCtrl.set(sugar().users.getQAUser().get("userName"));
		sugar().accounts.createDrawer.save();

		// Logout from QAUser and Login to SugarCRM as another user (myUser)
		sugar().logout();
		myUser.login();

		// Go to "Accounts" module
		sugar().accounts.navToListView();

		// Create an account, make sure the newly created account will cause potentially duplicate
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();
		nameEditFieldCtrl.set(testName);
		teamEditFieldCtrl.set(myUser.getRecordIdentifier());

		// Save the account
		sugar().accounts.createDrawer.save();

		// Verify that other team's record are not displayed in the potentially duplicated records list
		Assert.assertTrue("Other team's record are appearing in the list view", sugar().accounts.listView.countRows() == 1);
		sugar().accounts.listView.getDetailField(1,"name").assertEquals(testName, true);
		sugar().accounts.listView.getDetailField(1,"relAssignedTo").assertContains(myUser.getRecordIdentifier(), true);

		// Go to record view of the Account
		sugar().accounts.listView.clickRecord(1);

		// Verify the Team field
		teamDetailFieldCtrl.assertContains(myUser.getRecordIdentifier(), true);
		teamDetailFieldCtrl.assertContains(sugar().users.getQAUser().get("userName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}