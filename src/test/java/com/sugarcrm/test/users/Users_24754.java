package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24754 extends SugarTest {
	UserRecord chris;

	public void setup() throws Exception {
		// Login as admin and creating chris user
		sugar().login();
		chris = (UserRecord) sugar().users.create();
		sugar().logout();
		
		// Login as chris
		sugar().login(chris);
	}

	/**
	 * Create new record using new user.
	 * @throws Exception
	 */
	@Test
	public void Users_24754_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet contactRecord = testData.get(testName).get(0);
		
		// Creating contact record
		sugar().navbar.selectMenuItem(sugar().contacts, "createContact");
		sugar().contacts.createDrawer.getEditField("lastName").set(contactRecord.get("lastName"));
		sugar().contacts.createDrawer.save();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.showMore();
		
		// Verifying default team of contact is set to global created by chris user
		sugar().contacts.recordView.getDetailField("relTeam").assertEquals(contactRecord.get("team"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
