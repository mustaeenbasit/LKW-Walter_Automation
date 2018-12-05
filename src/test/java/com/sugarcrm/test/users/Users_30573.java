package com.sugarcrm.test.users;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_30573 extends SugarTest{
	UserRecord customUser;

	public void setup() throws Exception {
		// Create a custom user
		customUser = (UserRecord) sugar().users.api.create();

		// Login as an Admin user
		sugar().login();
	}

	/**
	 * Verify that blank row should not be added in User Management while deleting the existing user
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_30573_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the above created custom user
		customUser.navToRecord();

		// Not using method to delete user, as test case specifically asking to click on OK button twice
		// Select Edit drop down and click on Delete
		sugar().users.detailView.delete();

		// Click on "Ok" button more than 2 time
		// TODO: VOOD-1871 and VOOD-1045
		new VoodooControl("button", "css", ".first-child button").doubleClick();
		VoodooUtils.waitForReady();
		// Click on Cancel button
		new VoodooControl("button", "css", ".button[value='Cancel']").click();
		VoodooUtils.focusDefault();
		sugar().users.listView.clearSearchForm();
		sugar().users.listView.submitSearchForm();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the Blank row should not be added in User Management while deleting the existing user
		// Asserting first row contains link and total three rows are available
		sugar().users.listView.getControl("link01").assertExists(true);
		VoodooUtils.focusDefault();
		Assert.assertTrue("Blank row is added in User Management while deleting the existing user", sugar().users.listView.countRows() == 3);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}