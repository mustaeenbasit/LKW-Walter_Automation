package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24713 extends SugarTest {
	DataSource usersData = new DataSource(), appendText = new DataSource();;
	UserRecord myFirstUser, mySecondUser, myManager;

	public void setup() throws Exception {
		usersData = testData.get(testName);
        appendText = testData.get(testName+ "_1");
		// Create 3 non-admin users. Such as <manager> and <member1>,<member2>. 
		// Make sure <member1>,<member2> report to <manager>.
		sugar().login();

		// TODO: VOOD-444 - API Creating relationships
		myManager = (UserRecord) sugar().users.create(usersData.get(0));
		myFirstUser = (UserRecord) sugar().users.create(usersData.get(1));
		mySecondUser = (UserRecord) sugar().users.create(usersData.get(2));		
	}

	/**
	 * User-Verify that team security between team manager and members
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24713_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = sugar().accounts.getDefaultData();

		// Choose module and create at least three records. Make sure at least three records's team field is 
		// the three user's private team
		for(int c=0;c<usersData.size();c++){
			fs.put("name", usersData.get(c).get("userName"));
			fs.put("emailAddress", usersData.get(c).get("emailAddress"));
			fs.put("relTeam", usersData.get(c).get("userName"));
			fs.put("relAssignedTo",usersData.get(c).get("userName"));
			sugar().accounts.create(fs);
		}
		sugar().logout();

		// Login to SugarCRM as <member1>
		// Verify only records created by <member1> and in (member1) private team are displayed
		sugar().login(myFirstUser);
		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyField(1, "name", usersData.get(1).get("userName"));
		sugar().accounts.listView.assertContains(usersData.get(0).get("userName"), false);
		sugar().accounts.listView.assertContains(usersData.get(2).get("userName"), false);
		sugar().accounts.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("relTeam").assertEquals(usersData.get(1).get("userName")+appendText.get(0).get("primary"), true);
		sugar().logout();

		// Login to SugarCRM as <member2>
		// Verify only records created by <member2> and in (member2) private team are displayed.
		sugar().login(mySecondUser);
		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyField(1, "name", usersData.get(2).get("userName"));
		sugar().accounts.listView.assertContains(usersData.get(1).get("userName"), false);
		sugar().accounts.listView.assertContains(usersData.get(0).get("userName"), false);
		sugar().accounts.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("relTeam").assertEquals(usersData.get(2).get("userName")+appendText.get(0).get("primary"), true);
		sugar().logout();

		// Login to SugarCRM as <manager>
		// Verify only records created by <manager>,<member1>,<member2> and in (manager),(member1),(member2) 
		// private team are displayed.
		sugar().login(myManager);
		sugar().accounts.navToListView();
		sugar().accounts.listView.assertContains(usersData.get(2).get("userName"), true);
		sugar().accounts.listView.assertContains(usersData.get(1).get("userName"), true);
		sugar().accounts.listView.assertContains(usersData.get(0).get("userName"), true);
		sugar().accounts.listView.clickRecord(3);
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("relTeam").assertEquals(usersData.get(0).get("userName")+appendText.get(0).get("primary"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}