package com.sugarcrm.test.users;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24718  extends SugarTest {
	UserRecord Chris;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		// Log in as valid admin user
		sugar().login();
	}

	/**
	 *  User-Normal_Verify that non-admin user cannot access other team's record.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24718_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create non-admin user
		// TODO: VOOD-1200, Once resolved non-admin user can be created via api
		Chris = (UserRecord)sugar().users.create();

		FieldSet customData = testData.get(testName).get(0);		
		// Change the team field of account record to private team of admin user.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("relTeam").set(customData.get("privateTeam"));
		sugar().accounts.recordView.save();

		// Change the team field of contact record to private team of admin user.
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("relTeam").set(customData.get("privateTeam"));
		sugar().contacts.recordView.save();

		sugar().logout();
		// Log in with non-admin user
		Chris.login();

		// Navigate to accounts module
		sugar().accounts.navToListView();

		// Verify that non admin user cannot access other team's account record 
		Assert.assertTrue("Account list view has non zero account records", sugar().accounts.listView.countRows() == 0);

		sugar().contacts.navToListView();

		// Verify that non admin user cannot access other team's contact record 
		Assert.assertTrue("Contact list view has non zero contact records", sugar().contacts.listView.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}