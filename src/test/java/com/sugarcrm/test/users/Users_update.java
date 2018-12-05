package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_update extends SugarTest {
	UserRecord myUser;

	public void setup() throws Exception {
		myUser = (UserRecord)sugar().users.api.create();
		sugar().login();
	}

	@Test
	public void Users_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("firstName", "Demo");
		newData.put("lastName", "User");
		newData.put("emailAddress", "qa.sugar.qa.79@gmail.com");
		newData.put("userName", "Demo");

		// Edit the user using the UI.
		myUser.edit(newData);

		// Verify the user was edited.
		myUser.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}