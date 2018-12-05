package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24724 extends SugarTest {
	UserRecord chrisUser;

	public void setup() throws Exception {
		// Create an account record, contact record and leads record
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().leads.api.create();
		sugar().login();
		chrisUser = (UserRecord)sugar().users.create();
	}

	/**
	 * User-Global Team_Verify that records created in "Global" team can be accessed by any user.
	 * @throws Exception
	 */
	@Test
	public void Users_24724_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String teamGlobal = testData.get(testName).get(0).get("globalTeam");

		// Navigate to the Account record and assert that the team is Global
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("relTeam").assertContains(teamGlobal, true);

		// Navigate to the Contact record and assert that the team is Global
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getDetailField("relTeam").assertContains(teamGlobal, true);

		// Navigate to the lead record and assert that the team is Global
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.getDetailField("relTeam").assertContains(teamGlobal, true);

		// Logout from admin user
		sugar().logout();

		// Login as regular user Chris
		sugar().login(chrisUser);

		// Assert that the Records created by first user in "Global" team are displayed to the regular user.
		// Assert Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyField(1, "name", sugar().accounts.getDefaultData().get("name"));

		// Assert Contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("fullName"));

		// Assert Lead record
		sugar().leads.navToListView();
		sugar().leads.listView.verifyField(1, "fullName", sugar().leads.getDefaultData().get("fullName"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}