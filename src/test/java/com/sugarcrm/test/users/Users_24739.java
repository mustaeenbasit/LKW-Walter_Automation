package com.sugarcrm.test.users;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24739 extends SugarTest {
	UserRecord chris;

	public void setup() throws Exception {
		chris = (UserRecord) sugar().users.api.create();
		sugar().login();
	}

	/**
	 * Verify users search and select drawer search filters
	 * @throws Exception
	 */
	@Test
	public void Users_24739_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts create Drawer
		// TODO: VOOD-795
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		sugar().accounts.createDrawer.getEditField("relAssignedTo").click();
		new VoodooControl("div", "css", "#select2-drop ul:nth-child(3) div").click();
		VoodooUtils.waitForReady();

		// Asserting the column labels in users search and select drawer
		// TODO: VOOD-1671
		FieldSet customData = testData.get(testName).get(0);
		new VoodooControl("sapn", "css", ".search-and-select th[data-fieldname='name'] span").assertEquals(customData.get("fullName"), true);
		new VoodooControl("sapn", "css", ".search-and-select th[data-fieldname='user_name'] span").assertEquals(customData.get("userName"), true);

		// Searching with "First Name"
		sugar().users.searchSelect.search(sugar().users.getDefaultData().get("firstName"));
		String customUserName = sugar().users.getDefaultData().get("userName");
		int rowCount = sugar().users.searchSelect.countRows();
		Assert.assertTrue("Row count is not equal to 1", rowCount == 1);
		sugar().users.searchSelect.assertContains(customUserName, true);

		// Searching with "Last Name"
		sugar().users.searchSelect.search(sugar().users.getDefaultData().get("lastName"));
		Assert.assertTrue("Row count is not equal to 1", rowCount == 1);
		sugar().users.searchSelect.assertContains(customUserName, true);

		// Searching with "Full Name"
		sugar().users.searchSelect.search(sugar().users.getDefaultData().get("fullName"));
		Assert.assertTrue("Row count is not equal to 1", rowCount == 1);
		sugar().users.searchSelect.assertContains(customUserName, true);
		sugar().users.searchSelect.cancel();
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}