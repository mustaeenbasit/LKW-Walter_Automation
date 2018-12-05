package com.sugarcrm.test.users;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_delete extends SugarTest {
	UserRecord myUser;

	public void setup() throws Exception {
		myUser = (UserRecord)sugar().users.api.create();
		sugar().login();
	}

	@Test
	public void Users_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete user using the UI.
		myUser.delete();

		// Verify the user was deleted.
		assertEquals(VoodooUtils.contains(myUser.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}